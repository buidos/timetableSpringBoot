package bel.dmitrui98.timetable.controller.database;

import bel.dmitrui98.timetable.entity.*;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.service.TeachersBranchService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.dto.TeacherBranchDto;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.validation.AppsValidation;
import bel.dmitrui98.timetable.util.validation.ValidConditions;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.VALID_EMPTY_VALUE;
import static bel.dmitrui98.timetable.util.exception.ExceptionType.VALID_LONG_VALUE;

public class LoadController {

    private static final String DELETE_LABEL_NAME = "Номера для удаления: ";
    private static final String DELETE_LABEL_INIT = "не установлены";
    private static final String LOAD_FOR_GROUP_LABEL = "Нагрузка для группы";
    private static final String BUTTON_GROUP_TEXT = "Выбрать группу";

    @FXML
    private Button defaultButton;

    @FXML
    private ListView<StudyGroup> groupListView;
    @FXML
    private TextField filterGroupField;

    @Autowired
    private BaseService<StudyGroup, Long> studyGroupService;
    private ObservableList<StudyGroup> groups;
    private FilteredList<StudyGroup> filteredGroups;

    @FXML
    private ListView<Teacher> teacherListView;
    @FXML
    private TextField filterTeacherField;

    @Autowired
    private BaseService<Teacher, Long> teacherService;
    private ObservableList<Teacher> teachers;
    private FilteredList<Teacher> filteredTeachers;

    @FXML
    private TableView<Teacher> branchTableView;
    @FXML
    private TableColumn<Teacher, Void> teacherIndexCol;
    @FXML
    private TableColumn<Teacher, String> teacherCol;

    @FXML
    private ComboBox<Subject> subjectComboBox;

    @Autowired
    private BaseService<Subject, Long> subjectService;
    private ObservableList<Subject> subjects;
    private FilteredList<Subject> filteredSubjects;

    @FXML
    private TextField hourField;

    @FXML
    private TableView<TeacherBranchDto> loadTableView;
    @FXML
    private TableColumn<TeacherBranchDto, Void> indexCol;
    @FXML
    private TableColumn<TeacherBranchDto, String> branchCol;
    @FXML
    private TableColumn<TeacherBranchDto, String> subjectCol;
    @FXML
    private TableColumn<TeacherBranchDto, Integer> hourCol;

    @Autowired
    private TeachersBranchService teachersBranchService;
    @Autowired
    private TeachersBranchRepository teachersBranchRepository;
    private ObservableList<TeacherBranchDto> teachersBranches = FXCollections.observableArrayList();

    @FXML
    private Label deleteIndexesLabel;

    @FXML
    private Label loadLabel;
    @FXML
    private Button selectGroupButton;
    private StudyGroup selectedGroup;

    @FXML
    private void addTeacherToBranch() {
        List<Teacher> selectedItems = teacherListView.getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбран преподаватель", "Выберите хотя бы одного преподавателя для добавления в связку");
            return;
        }
        int size = branchTableView.getItems().size() + selectedItems.size();
        if (size > AppsSettingsHolder.getMaxTeachersInBranch()) {
            AlertsUtil.showInfoAlert(String.format("Связка преподавателей достигла максимального значения (%d). Текущее значение: %d",
                    AppsSettingsHolder.getMaxTeachersInBranch(), branchTableView.getItems().size()),
                    "Если вам нужно добавить преподавателей в связку, увеличьте максимальное значение в настройках приложения до "
                            + size);
            return;
        }
        List<Teacher> branchTeachers = branchTableView.getItems();
        for (Teacher t : selectedItems) {
            if (branchTeachers.contains(t)) {
                AlertsUtil.showInfoAlert("Преподаватель " + Teacher.getTeacherName(t) + " уже есть в связке", null);
                return;
            }
        }
        branchTableView.getItems().addAll(selectedItems);
    }

    @FXML
    private void deleteTeacherFromBranch() {
        List<Teacher> selectedItems = branchTableView.getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбран преподаватель", "Выберите хотя бы одного преподавателя для удаления из связки");
            return;
        }
        branchTableView.getItems().removeAll(selectedItems);
    }

    @FXML
    @Transactional
    public void add() throws AppsException {
        // проверяем группу
        if (selectedGroup == null) {
            AlertsUtil.showInfoAlert("Не выбрана группа", "Выделите группу из списка, затем нажмите на кнопку " +
                    "\"Выбрать группу\", чтобы редактировать нагрузку этой группы");
            return;
        }

        // проверяем связку преподавателей
        List<Teacher> branchTeachers = branchTableView.getItems();
        if (branchTeachers.isEmpty()) {
            AlertsUtil.showInfoAlert("В связке должен быть хотя бы один преподаватель",
                    "Выделите преподавателя, затем нажмите \"Добавить в связку\"");
            return;
        }
        Subject subject = subjectComboBox.getSelectionModel().getSelectedItem();
        if (subject == null) {
            AlertsUtil.showInfoAlert("Дисциплина должна быть установлена", null);
            return;
        }

        if (!isValid(hourField.getText())) {
            return;
        }

        // общее количество часов в две недели не должно превышать максимальное значение, которое возможно установить в нагрузку
        Integer hours = Integer.valueOf(hourField.getText());
        Integer minutesInTwoWeeks = hours * (AppsSettingsHolder.getHourTime() * 2);
        int maxLoadMinutes = AppsSettingsHolder.getPairsPerDay() * (AppsSettingsHolder.getHourTime() * 2) *
                AppsSettingsHolder.COUNT_WEEK_DAYS * 2;
        int sum = 0;
        for (TeacherBranchDto dto : teachersBranches) {
            Integer countMinutesInTwoWeek = dto.getHour() * (AppsSettingsHolder.getHourTime() * 2);
            sum += countMinutesInTwoWeek;
        }
        sum += minutesInTwoWeeks;
        if (sum > maxLoadMinutes) {
            AlertsUtil.showInfoAlert("Слишком много часов для нагрузки. Максимальное количество часов для двух недель: " +
                    (maxLoadMinutes / (AppsSettingsHolder.getHourTime() * 2) + ". Текущее значение: " + (sum / (AppsSettingsHolder.getHourTime() * 2))), "Формула вычисления: pairsPerDay * (hourTime * 2) *" +
                    " COUNT_WEEK_DAYS * 2");
            return;
        }

        List<TeachersBranch> groupTeachersBranches = teachersBranchService.getTeachersBranches(branchTeachers, selectedGroup);
        if (groupTeachersBranches.isEmpty()) {
            // связка не найдена, создаем новую
            Long maxId = teachersBranchRepository.getMaxId();
            long nextId;
            if (maxId == null) {
                nextId = 1;
            } else {
                nextId = maxId + 1;
            }
            for (Teacher t : branchTeachers) {
                StudyLoad load = new StudyLoad(minutesInTwoWeeks, subject);
                TeachersBranch teachersBranch = new TeachersBranch(nextId, t, load, selectedGroup);
                teachersBranchService.save(teachersBranch);
            }
            teachersBranches.add(new TeacherBranchDto(nextId, branchTeachers, subject, hours, selectedGroup));
        } else {
            // связка для данной группы уже существует, предлагаем отредактировать нагрузку
            boolean isConfirm = AlertsUtil.showConfirmAlert("Данная связка у выделенной группы уже существует",
                    "Отредактировать данную связку?");
            if (isConfirm) {
                for (TeachersBranch tb : groupTeachersBranches) {
                    tb.getStudyLoad().setCountMinutesInTwoWeek(minutesInTwoWeeks);
                    tb.getStudyLoad().setSubject(subject);
                    teachersBranchService.save(tb);
                    teachersBranches.clear();
                    teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup)));
                }
            }
        }

    }

    @FXML
    private void delete() throws AppsException {
        List<TeacherBranchDto> selectedItems = loadTableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбраны часы связки преподавателей", null);
            return;
        }
        List<Integer> selectedNumbers = loadTableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        boolean isConfirmed = AlertsUtil.showConfirmAlert("Вы точно хотите удалить выделенные часы связок преподавателей?",
                "Часы связок преподавателей с номерами " + selectedNumbers + " будут удалены");

        if (isConfirmed) {
            teachersBranchService.deleteAllDto(selectedItems);

            teachersBranches.clear();
            teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup)));
            refreshLabels();
        }
    }

    private boolean isValid(String hour) {
        ValidConditions cond = new ValidConditions();
        cond.setMaxStringLength(8);
        try {
            AppsValidation.validate(hour, cond);
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(VALID_EMPTY_VALUE)) {
                contentText = "Часы в две недели должны быть установлены";
            } else if (ex.getExceptionType().equals(VALID_LONG_VALUE)) {
                contentText = "Строка не должна превышать длину в " + cond.getMaxStringLength() + " символов";
            }
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, contentText);
            return false;
        }
        return true;
    }

    @FXML
    private void selectGroup() {
        StudyGroup selectedItem = groupListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            AlertsUtil.showInfoAlert("Группа не выбрана", "Выберите группу для редактирования" +
                    " её нагрузки");
            return;
        }
        selectedGroup = selectedItem;

        teachersBranches.clear();
        teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup)));

        refreshLabels();
    }

    @PostConstruct
    public void init() {

//        indexCol.setCellFactory(col -> {
//            TableCell<Teacher, Void> cell = new TableCell<>();
//            cell.textProperty().bind(Bindings.createStringBinding(() -> {
//                if (cell.isEmpty()) {
//                    return null ;
//                } else {
//                    return Integer.toString(cell.getIndex() + 1);
//                }
//            }, cell.emptyProperty(), cell.indexProperty()));
//
//            return cell ;
//        });
//        surnameCol.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
//        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//        patronymicCol.setCellValueFactory(cellData -> cellData.getValue().patronymicProperty());
//        telephoneCol.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());
//        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
//
//        teachers = FXCollections.observableArrayList(teacherService.findAll());
//        loadTableView.setItems(teachers);
//
//        loadTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        loadTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                surnameField.setText(newValue.getSurname());
//                nameField.setText(newValue.getName());
//                patronymicField.setText(newValue.getPatronymic());
//                telephoneField.setText(newValue.getTelephone());
//                emailField.setText(newValue.getEmail());
//                refreshLabels();
//            }
//        });
//        VBox.setVgrow(loadTableView, Priority.ALWAYS);
//
//        refreshLabels();
        tuningListViews();
        tuningTableViews();

        // дисциплыны
        tuningComboBoxes();

        setNumberFormatter(hourField);
    }

    private void tuningComboBoxes() {
        subjectComboBox.setCellFactory((listView) -> {
            return new ListCell<Subject>() {
                @Override
                protected void updateItem(Subject item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            };
        });

        subjectComboBox.setConverter(new StringConverter<Subject>() {
            @Override
            public String toString(Subject object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getName();
                }
            }

            @Override
            public Subject fromString(String string) {
                return null;
            }
        });

        subjectComboBox.valueProperty().addListener((o, oldValue, newValue) -> {
            if (newValue == null) {
                subjectComboBox.getSelectionModel().select(oldValue);
            }
        });
        subjectComboBox.getEditor().textProperty().addListener((o, oldValue, newValue) -> {
            Subject selected = subjectComboBox.getSelectionModel().getSelectedItem();
            // Platform нужен из-за бага в textField listener
            Platform.runLater(() -> {
                if (selected == null || !selected.getName().equals(newValue)) {
                    filteredSubjects.setPredicate(item ->
                            item.getName().toLowerCase().startsWith(newValue.toLowerCase())
                    );
                }
            });
        });
    }


    private void tuningTableViews() {
        teacherIndexCol.setCellFactory(col -> {
            TableCell<Teacher, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));

            return cell ;
        });
        teacherCol.setCellValueFactory(cellData -> new SimpleStringProperty(Teacher.getTeacherName(cellData.getValue())));
        branchTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);



        indexCol.setCellFactory(col -> {
            TableCell<TeacherBranchDto, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));

            return cell ;
        });
        branchCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeacherBranch().toString()));
        subjectCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject().getName()));
        hourCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHour()).asObject());

        loadTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                refreshLabels();
            }
        });
        VBox.setVgrow(loadTableView, Priority.ALWAYS);

    }

    private void tuningListViews() {
        // группы
        groupListView.setCellFactory((list) -> {
            return new ListCell<StudyGroup>() {
                @Override
                protected void updateItem(StudyGroup item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });
        groupListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectGroupButton.setText(BUTTON_GROUP_TEXT + " " + newValue.getName());
            } else {
                selectGroupButton.setText(BUTTON_GROUP_TEXT);
            }
        }));


        filterGroupField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredGroups.setPredicate(group -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();
                return group.getName().toLowerCase().startsWith(lowerCase);
            });
        }));

        // учителя
        teacherListView.setCellFactory((list) -> {
            return new ListCell<Teacher>() {
                @Override
                protected void updateItem(Teacher item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(Teacher.getTeacherName(item));
                    }
                }
            };
        });

        teacherListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        filterTeacherField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredTeachers.setPredicate(teacher -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();
                return teacher.getSurname().toLowerCase().startsWith(lowerCase);
            });
        }));
    }

    public void refresh() {
        if (selectedGroup != null) {
            teachersBranches.clear();
            teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup)));
        }
        loadTableView.setItems(teachersBranches);

        // обновляем группы
        groups = FXCollections.observableArrayList(studyGroupService.findAll());
        // оборачиваем observableList в FilteredList (по умолчанию отображаем все значения)
        filteredGroups = new FilteredList<>(groups, g -> true);
        groupListView.setItems(filteredGroups);

        // обновляем учителей
        teachers = FXCollections.observableArrayList(teacherService.findAll());
        filteredTeachers = new FilteredList<>(teachers, t -> true);
        teacherListView.setItems(filteredTeachers);

        // дисциплины
        subjects = FXCollections.observableArrayList(subjectService.findAll());
        filteredSubjects = new FilteredList<>(subjects, s -> true);
        subjectComboBox.setItems(filteredSubjects);
        subjectComboBox.getSelectionModel().selectFirst();

        refreshLabels();
    }

    private void refreshLabels() {
        List<Integer> selectedNumbers = loadTableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        if (!selectedNumbers.isEmpty()) {
            deleteIndexesLabel.setText(DELETE_LABEL_NAME + selectedNumbers);
        } else {
            deleteIndexesLabel.setText(DELETE_LABEL_NAME + DELETE_LABEL_INIT);
        }
        if (selectedGroup == null) {
            loadLabel.setText(LOAD_FOR_GROUP_LABEL);
        } else {
            loadLabel.setText(LOAD_FOR_GROUP_LABEL + " " + selectedGroup.getName());
        }
    }

    public Button getDefaultButton() {
        return defaultButton;
    }

    private void setNumberFormatter(TextField numberField){
        TextFormatter<String> numberFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }
            String newValue = change.getControlNewText();

            // если не цифра, пропускаем
            if (!newValue.matches("\\d+") && !newValue.equals("")) {
                return null;
            }
            return change;
        });
        numberField.setTextFormatter(numberFormatter);
    }

}
