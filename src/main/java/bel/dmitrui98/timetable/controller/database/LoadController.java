package bel.dmitrui98.timetable.controller.database;

import bel.dmitrui98.timetable.entity.*;
import bel.dmitrui98.timetable.repository.StudyGroupRepository;
import bel.dmitrui98.timetable.repository.TeacherRepository;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.service.TeachersBranchService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.dto.TeacherBranchDto;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.time.TimeUtil;
import bel.dmitrui98.timetable.util.validation.AppsValidation;
import bel.dmitrui98.timetable.util.validation.ValidConditions;
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
    private static final String GROUP_LABEL = "Группа";
    private static final String BUTTON_GROUP_TEXT = "Выбрать группу";

    @FXML
    private Button defaultButton;

    @FXML
    private Label groupLabel;

    @FXML
    private ListView<StudyGroup> groupListView;
    @FXML
    private TextField filterGroupField;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private BaseService<StudyGroup, Long> studyGroupService;
    private ObservableList<StudyGroup> groups;
    private FilteredList<StudyGroup> filteredGroups;

    @FXML
    private ListView<Teacher> teacherListView;
    @FXML
    private TextField filterTeacherField;

    @FXML
    private ListView<Subject> subjectListView;
    @FXML
    private TextField filterSubjectField;

    @Autowired
    private TeacherRepository teacherRepository;

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

    @Autowired
    private BaseService<Subject, Long> subjectService;
    private ObservableList<Subject> subjects;
    private FilteredList<Subject> filteredSubjects;

    @FXML
    private TextField hourField;

    @FXML
    private CheckBox halfPairCheckBox;

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
    @FXML
    private TableColumn<TeacherBranchDto, Integer> minuteCol;

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
    @Transactional(rollbackFor = AppsException.class)
    public void add() throws AppsException {
        ValidationInnerClass innerClass = new ValidationInnerClass().validate(false);
        if (innerClass.isNotValid()) return;
        List<Teacher> branchTeachers = innerClass.getBranchTeachers();
        Subject subject = innerClass.getSubject();
        Integer hours = innerClass.getHours();
        Integer minutesInTwoWeeks = innerClass.getMinutesInTwoWeeks();

        TeachersBranch tb = teachersBranchService.findByTeachersAndGroup(branchTeachers, selectedGroup);
        if (tb == null) {
            StudyLoad load = new StudyLoad(minutesInTwoWeeks, subject);
            tb = new TeachersBranch(load);
            tb.addGroup(selectedGroup);

            studyGroupRepository.saveAndFlush(selectedGroup);
            TeachersBranch last = teachersBranchRepository.findTopByOrderByTeacherBranchIdDesc();

            branchTeachers = teacherRepository.branchFetch(branchTeachers);

            for (Teacher t : branchTeachers) {
                t.addTeachersBranch(last);
                teacherService.save(t);
            }
            boolean isHalfPair = halfPairCheckBox.isSelected();
            teachersBranches.add(new TeacherBranchDto(last, branchTeachers, subject, hours, isHalfPair, selectedGroup));
            clear();
        } else {
            // связка для данной группы уже существует, предлагаем отредактировать нагрузку
            boolean isConfirm = AlertsUtil.showConfirmAlert("Данная связка у выделенной группы уже существует",
                    "Отредактировать данную связку?");
            if (isConfirm) {
                tb.getStudyLoad().setCountMinutesInTwoWeek(minutesInTwoWeeks);
                tb.getStudyLoad().setSubject(subject);
                teachersBranchService.save(tb);
                teachersBranches.clear();
                teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup), selectedGroup));
            }
        }

    }

    private void clear() {
        branchTableView.getItems().clear();
        hourField.setText("");
        halfPairCheckBox.setSelected(false);
        filterSubjectField.setText("");
    }

    @FXML
    private void edit() throws AppsException {
        ValidationInnerClass innerClass = new ValidationInnerClass().validate(true);
        if (innerClass.isNotValid()) return;
        List<Teacher> branchTeachers = innerClass.getBranchTeachers();
        Subject subject = innerClass.getSubject();
        Integer minutesInTwoWeeks = innerClass.getMinutesInTwoWeeks();

        TeachersBranch tb = teachersBranchService.findByTeachersAndGroup(branchTeachers, selectedGroup);
        if (tb == null) {
            AlertsUtil.showInfoAlert("Связка для данной группы не найдена",
                    "Выберите существующую связку из списка для редактирования");
        } else {
            tb.getStudyLoad().setCountMinutesInTwoWeek(minutesInTwoWeeks);
            tb.getStudyLoad().setSubject(subject);
            teachersBranchService.save(tb);
            teachersBranches.clear();
            teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup), selectedGroup));

        }
    }

    @FXML
    @Transactional(rollbackFor = AppsException.class)
    public void delete() throws AppsException {
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
            teachersBranchService.deleteAllByDto(selectedItems);

            teachersBranches.clear();
            teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup), selectedGroup));
            refreshLabels();
        }
    }

    private boolean isValid(String hour, boolean isShowAlert) {
        ValidConditions cond = new ValidConditions();
        cond.setMaxStringLength(8);
        try {
            AppsValidation.validate(hour, cond);
        } catch (AppsException ex) {
            if (!isShowAlert) {
                // сообщение не показываем
                return false;
            }
            String contentText = "";
            if (ex.getExceptionType().equals(VALID_EMPTY_VALUE)) {
                contentText = "Часы (минуты) в две недели должны быть установлены";
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
        selectedGroup = studyGroupRepository.branchFetch(selectedItem);

        teachersBranches.clear();
        teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup), selectedGroup));
        clear();

        refreshLabels();
    }

    @PostConstruct
    public void init() {
        tuningListViews();

        tuningTableViews();

        setNumberFormatter(hourField);
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
        branchCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeachers().toString()));
        subjectCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject().getName()));
        hourCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHour()).asObject());
        minuteCol.setCellValueFactory(cellData -> {
            int minute = TimeUtil.convertHourToMinute(cellData.getValue().getHour());
            if (cellData.getValue().isHalfPair()) {
                // вычитаем лишнюю половину пары
                minute -= AppsSettingsHolder.getHourTime();
            }
            return new SimpleIntegerProperty(minute).asObject();
        });

        loadTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                refreshLabels();
                branchTableView.setItems(FXCollections.observableArrayList(newValue.getTeachers()));
                subjectListView.getSelectionModel().select(newValue.getSubject());
                filterSubjectField.setText(newValue.getSubject().getName());
                int hour = newValue.getHour();
                if (newValue.isHalfPair()) {
                    hour--;
                    halfPairCheckBox.setSelected(true);
                } else {
                    halfPairCheckBox.setSelected(false);
                }
                hourField.setText(String.valueOf(hour));
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

        // дисциплины
        subjectListView.setCellFactory((list) -> {
            return new ListCell<Subject>() {
                @Override
                protected void updateItem(Subject item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });

        filterSubjectField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredSubjects.setPredicate(teacher -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();
                return teacher.getName().toLowerCase().startsWith(lowerCase);
            });
        }));
    }

    public void refresh() {
        if (selectedGroup != null) {
            teachersBranches.clear();
            teachersBranches.addAll(TeacherBranchDto.convert(teachersBranchRepository.findByGroup(selectedGroup), selectedGroup));
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
        subjectListView.setItems(filteredSubjects);

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
            groupLabel.setText(GROUP_LABEL);
        } else {
            loadLabel.setText(LOAD_FOR_GROUP_LABEL + " " + selectedGroup.getName());
            groupLabel.setText(GROUP_LABEL + " " + selectedGroup.getName());
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

    private class ValidationInnerClass {
        private boolean isNotValid;
        private List<Teacher> branchTeachers;
        private Subject subject;
        private Integer hours;
        private Integer minutesInTwoWeeks;

        boolean isNotValid() {
            return isNotValid;
        }

        public List<Teacher> getBranchTeachers() {
            return branchTeachers;
        }

        public Subject getSubject() {
            return subject;
        }

        public Integer getHours() {
            return hours;
        }

        public Integer getMinutesInTwoWeeks() {
            return minutesInTwoWeeks;
        }

        public ValidationInnerClass validate(boolean isEdit) {
            // проверяем группу
            if (selectedGroup == null) {
                AlertsUtil.showInfoAlert("Не выбрана группа", "Выделите группу из списка, затем нажмите на кнопку " +
                        "\"Выбрать группу\", чтобы редактировать нагрузку этой группы");
                isNotValid = true;
                return this;
            }

            // проверяем связку преподавателей
            branchTeachers = branchTableView.getItems();
            if (branchTeachers.isEmpty()) {
                AlertsUtil.showInfoAlert("В связке должен быть хотя бы один преподаватель",
                        "Выделите преподавателя, затем нажмите \"Добавить в связку\"");
                isNotValid = true;
                return this;
            }
            subject = subjectListView.getSelectionModel().getSelectedItem();
            if (subject == null) {
                AlertsUtil.showInfoAlert("Дисциплина должна быть установлена", null);
                isNotValid = true;
                return this;
            }

            if (!halfPairCheckBox.isSelected()) {
                if (!isValid(hourField.getText(), true)) {
                    isNotValid = true;
                    return this;
                }
            } else {
                if (!isValid(hourField.getText(), false)) {
                    hourField.setText("0");
                }
            }

            // общее количество часов в две недели не должно превышать максимальное значение, которое возможно установить в нагрузку
            hours = Integer.valueOf(hourField.getText());
            minutesInTwoWeeks = TimeUtil.convertHourToMinute(hours);
            if (halfPairCheckBox.isSelected()) {
                minutesInTwoWeeks += AppsSettingsHolder.getHourTime();

                // если есть половина пары, то добавляем целый час
                hours++;
            }
            int maxLoadMinutes = AppsSettingsHolder.getPairsPerDay() * (AppsSettingsHolder.getHourTime() * 2) *
                    AppsSettingsHolder.COUNT_WEEK_DAYS * 2;
            int sum = 0;
            for (TeacherBranchDto dto : teachersBranches) {
                Integer countMinutesInTwoWeek = dto.getHour() * (AppsSettingsHolder.getHourTime() * 2);
                sum += countMinutesInTwoWeek;
            }

            int editMinutes = 0;
            if (isEdit) {
                if (loadTableView.getSelectionModel().getSelectedItem() != null) {
                    TeacherBranchDto dto = loadTableView.getSelectionModel().getSelectedItem();
                    editMinutes = dto.getHour() * (AppsSettingsHolder.getHourTime() * 2);
                    if (dto.isHalfPair()) {
                        // отнимаем, потому что в dto для половины пары усатанавливается целый час
                        editMinutes -= AppsSettingsHolder.getHourTime();
                    }
                }
            }

            sum += minutesInTwoWeeks - editMinutes;


            if (sum > maxLoadMinutes) {
                AlertsUtil.showInfoAlert("Слишком много часов для нагрузки. Максимальное количество часов для двух недель: " +
                        (maxLoadMinutes / (AppsSettingsHolder.getHourTime() * 2) + ". Текущее значение: " + (sum / (AppsSettingsHolder.getHourTime() * 2))), "Формула вычисления: pairsPerDay * (hourTime * 2) *" +
                        " COUNT_WEEK_DAYS * 2");
                isNotValid = true;
                return this;
            }
            isNotValid = false;
            return this;
        }
    }
}
