package bel.dmitrui98.timetable.controller.database;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.Subject;
import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.validation.AppsValidation;
import bel.dmitrui98.timetable.util.validation.ValidConditions;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

public class LoadController {

    private static final String DELETE_LABEL_NAME = "Номера для удаления: ";
    private static final String DELETE_LABEL_INIT = "не установлены";
    private static final String LOAD_FOR_GROUP_LABEL = "Нагрузка для группы";
    private static final String BUTTON_GROUP_TEXT = "Выбрать группу";

    private static final String VALIDATION_ERROR = "Ошибка валидации";

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
    private TableView<Teacher> tableView;
    @FXML
    private TableColumn<Teacher, Void> indexCol;
    @FXML
    private TableColumn<Teacher, String> surnameCol;
    @FXML
    private TableColumn<Teacher, String> nameCol;
    @FXML
    private TableColumn<Teacher, String> patronymicCol;
    @FXML
    private TableColumn<Teacher, String> telephoneCol;
    @FXML
    private TableColumn<Teacher, String> emailCol;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField patronymicField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField emailField;

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
    private void add() throws AppsException {
        String surname = surnameField.getText().toLowerCase();
        String name = nameField.getText().toLowerCase();
        String patronymic = patronymicField.getText().toLowerCase();
        String telephone = telephoneField.getText().toLowerCase();
        String email = emailField.getText().toLowerCase();

        if (isValid(name) && isValid(surname, false) && isValid(patronymic) && isValid(email) && isValid(telephone)) {
            Teacher teacher = new Teacher(surname, name, patronymic, telephone, email);
            teacherService.save(teacher);
            teachers.add(teacher);
        }
    }

    @FXML
    private void edit() throws AppsException {
        Teacher entityForEdit  = tableView.getSelectionModel().getSelectedItem();
        if (entityForEdit == null) {
            AlertsUtil.showInfoAlert("Не выбран преподаватель", "Выберите хотя бы одного преподавателя для редактирования");
            return;
        }
        String surname = surnameField.getText().toLowerCase();
        String name = nameField.getText().toLowerCase();
        String patronymic = patronymicField.getText().toLowerCase();
        String telephone = telephoneField.getText().toLowerCase();
        String email = emailField.getText().toLowerCase();

        // если изменений не было
        if (entityForEdit.getSurname().equals(surname) && entityForEdit.getName().equals(name)
                && entityForEdit.getPatronymic().equals(patronymic) && entityForEdit.getTelephone().equals(telephone)
                && entityForEdit.getEmail().equals(email)) {
            AlertsUtil.showInfoAlert("Изменений не зафиксировано",
                    "Измените хотя бы одно поле");
            return;
        }

        if (isValid(name) && isValid(surname, false) && isValid(patronymic) && isValid(email) && isValid(telephone)) {
            entityForEdit.setSurname(surname);
            entityForEdit.setName(name);
            entityForEdit.setPatronymic(patronymic);
            entityForEdit.setTelephone(telephone);
            entityForEdit.setEmail(email);
            teacherService.save(entityForEdit);
        }
    }

    @FXML
    private void delete() throws AppsException {
        ObservableList<Teacher> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбран преподаватель", "Выберите хотя бы одного преподавателя для удаления");
            return;
        }
        List<Integer> selectedNumbers = tableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        boolean isConfirmed = AlertsUtil.showConfirmAlert("Вы точно хотите удалить выделенных преподавателей?",
                "Преподаватели с номерами " + selectedNumbers + " будут удалены");

        if (isConfirmed) {
            try {
                teacherService.deleteAll(selectedItems);
            } catch (Exception ex) {
                AlertsUtil.showErrorAlert("Ошибка удаления", "Преподаватели не удалены. Возможно, " +
                        "выделенные преподаватели учавствуют в нагрузке. Сначала необходимо удалить нагрузку данных преподавателей", ex);
                return;
            }

            teachers.clear();
            teachers.setAll(teacherService.findAll());
            refreshLabels();
        }
    }

    private boolean isValid(String name) {
        return isValid(name, true);
    }

    private boolean isValid(String name, boolean allowEmpty) {
        try {
            AppsValidation.validate(name, new ValidConditions(allowEmpty, true));
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(VALID_EMPTY_VALUE)) {
                contentText = "Фамилия не должна быть пустой";
            } else if (ex.getExceptionType().equals(VALID_LONG_VALUE)) {
                contentText = "Строка не должна превышать длину в " + ValidConditions.MAX_STRING_LENGTH + " символов";
            } else if (ex.getExceptionType().equals(VALID_DUPLICATE_VALUE)) {
                contentText = "Запись со строкой \"" + name + "\" уже существует";
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
//        tableView.setItems(teachers);
//
//        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                surnameField.setText(newValue.getSurname());
//                nameField.setText(newValue.getName());
//                patronymicField.setText(newValue.getPatronymic());
//                telephoneField.setText(newValue.getTelephone());
//                emailField.setText(newValue.getEmail());
//                refreshLabels();
//            }
//        });
//        VBox.setVgrow(tableView, Priority.ALWAYS);
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

        subjectComboBox.getEditor().textProperty().addListener((o, oldValue, newValue) -> {
            TextField editor = subjectComboBox.getEditor();
            Subject selected = subjectComboBox.getSelectionModel().getSelectedItem();

            // Platform нужен из-за бага в textField listener
            Platform.runLater(() -> {
                // если выделенный элемент не равен введенному или выделенного элемента нет, то фильтруем список
                if (selected == null || !selected.getName().equals(editor.getText())) {
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
        refreshLabels();

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
    }

    private void refreshLabels() {
        List<Integer> selectedNumbers = tableView.getSelectionModel().getSelectedIndices().stream()
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
