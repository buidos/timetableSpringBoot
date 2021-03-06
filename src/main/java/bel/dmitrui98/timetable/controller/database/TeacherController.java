package bel.dmitrui98.timetable.controller.database;

import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.StringUtil;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.validation.AppsValidation;
import bel.dmitrui98.timetable.util.validation.ValidConditions;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

public class TeacherController {

    private static final String EDIT_LABEL_NAME = "Номер для редактирования: ";
    private static final String EDIT_LABEL_INIT = "не установлен";
    private static final String DELETE_LABEL_NAME = "Номера для удаления: ";
    private static final String DELETE_LABEL_INIT = "не установлены";

    @FXML
    private Button defaultButton;

    @Autowired
    private BaseService<Teacher, Long> teacherService;
    private ObservableList<Teacher> teachers;

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
    private Label editIndexLabel;
    @FXML
    private Label deleteIndexesLabel;

    @FXML
    private void add() throws AppsException {
        String surname = surnameField.getText().toLowerCase();
        String name = nameField.getText().toLowerCase();
        String patronymic = patronymicField.getText().toLowerCase();
        String telephone = telephoneField.getText();
        String email = emailField.getText();
        if (isValid(name) && isValid(surname, false)
                && isValid(patronymic) && isValid(email)
                && isValid(telephone) && notDuplicate(surname, name, patronymic, telephone, email)) {
            Teacher teacher = new Teacher(surname, name, patronymic, telephone, email);
            teacherService.save(teacher);
            teachers.add(teacher);

            surnameField.setText("");
            nameField.setText("");
            patronymicField.setText("");
            telephoneField.setText("");
            emailField.setText("");
            surnameField.requestFocus();
        }
    }

    private boolean notDuplicate(String surname, String name, String patronymic, String telephone, String email, int ignoreIndex) {
        List<String> strings = teachers.stream()
                .map(t -> t.getSurname() + t.getName() + t.getPatronymic() + t.getTelephone() + t.getEmail())
                .collect(Collectors.toList());
        return isValid(surname + name + patronymic + telephone + email, false, false,
                strings, ignoreIndex);
    }

    private boolean notDuplicate(String surname, String name, String patronymic, String telephone, String email) {
        return notDuplicate(surname, name, patronymic, telephone, email, -1);
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
        String telephone = telephoneField.getText();
        String email = emailField.getText();

        // если изменений не было
        if (entityForEdit.getSurname().equals(surname) && entityForEdit.getName().equals(name)
                && entityForEdit.getPatronymic().equals(patronymic) && entityForEdit.getTelephone().equals(telephone)
                && entityForEdit.getEmail().equals(email)) {
            AlertsUtil.showInfoAlert("Изменений не зафиксировано",
                    "Измените хотя бы одно поле");
            return;
        }

        int ignoreIndex = tableView.getSelectionModel().getSelectedIndex();
        if (isValid(name) && isValid(surname, false) && isValid(patronymic) && isValid(email)
                && isValid(telephone) && notDuplicate(surname, name, patronymic, telephone, email, ignoreIndex)) {
            entityForEdit.setSurname(surname);
            entityForEdit.setName(name);
            entityForEdit.setPatronymic(patronymic);
            entityForEdit.setTelephone(telephone);
            entityForEdit.setEmail(email);
            teacherService.save(entityForEdit);
        }
    }

    @FXML
    private void delete() {
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
        return isValid(name, allowEmpty, true, null, -1);
    }

    private boolean isValid(String name, boolean allowEmpty, boolean allowDuplicate, List<String> strings, int ignoreIndex) {
        try {
            AppsValidation.validate(name, new ValidConditions(allowEmpty, allowDuplicate), strings, ignoreIndex);
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(VALID_EMPTY_VALUE)) {
                contentText = "Фамилия не должна быть пустой";
            } else if (ex.getExceptionType().equals(VALID_LONG_VALUE)) {
                contentText = "Строка не должна превышать длину в " + ValidConditions.MAX_STRING_LENGTH + " символов";
            } else if (ex.getExceptionType().equals(VALID_DUPLICATE_VALUE)) {
                contentText = "Запись уже существует";
            }
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, contentText);
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {

        indexCol.setCellFactory(col -> {
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
        surnameCol.setCellValueFactory(cellData -> StringUtil.upperFirstProperty(cellData.getValue().getSurname()));
        nameCol.setCellValueFactory(cellData -> StringUtil.upperFirstProperty(cellData.getValue().getName()));
        patronymicCol.setCellValueFactory(cellData -> StringUtil.upperFirstProperty(cellData.getValue().getPatronymic()));
        telephoneCol.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        teachers = FXCollections.observableArrayList(teacherService.findAll());
        tableView.setItems(teachers);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setFocusTraversable(false);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                surnameField.setText(StringUtil.upperFirst(newValue.getSurname()));
                nameField.setText(StringUtil.upperFirst(newValue.getName()));
                patronymicField.setText(StringUtil.upperFirst(newValue.getPatronymic()));
                telephoneField.setText(newValue.getTelephone());
                emailField.setText(newValue.getEmail());
                refreshLabels();
            }
        });
        VBox.setVgrow(tableView, Priority.ALWAYS);

        refreshLabels();
    }

    private void refreshLabels() {
        List<Integer> selectedNumbers = tableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        if (!selectedNumbers.isEmpty()) {
            editIndexLabel.setText(EDIT_LABEL_NAME + (tableView.getSelectionModel().getSelectedIndex() + 1));
            deleteIndexesLabel.setText(DELETE_LABEL_NAME + selectedNumbers);
        } else {
            deleteIndexesLabel.setText(DELETE_LABEL_NAME + DELETE_LABEL_INIT);
            editIndexLabel.setText(EDIT_LABEL_NAME + EDIT_LABEL_INIT);
        }
    }

    public Button getDefaultButton() {
        return defaultButton;
    }
}
