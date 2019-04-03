package bel.dmitrui98.timetable.controller.database.dictionary;

import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.exception.ExceptionType;
import bel.dmitrui98.timetable.util.validation.AppsValidation;
import bel.dmitrui98.timetable.util.validation.ValidConditions;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialtyController {

    private static final String EDIT_LABEL_NAME = "Номер для редактирования: ";
    private static final String EDIT_LABEL_INIT = "не установлен";
    private static final String DELETE_LABEL_NAME = "Номера для удаления: ";
    private static final String DELETE_LABEL_INIT = "не установлены";

    @FXML
    private Button defaultButton;

    @Autowired
    private BaseService<Department, Integer> departmentService;

    @Autowired
    private BaseService<Specialty, Integer> specialtyService;
    private ObservableList<Specialty> specialties;

    @FXML
    private TableView<Specialty> specialtyTableView;

    @FXML
    private TableColumn<Specialty, Void> specialtyIndexCol;
    @FXML
    private TableColumn<Specialty, String> specialtyNameCol;
    @FXML
    private TableColumn<Specialty, String> departmentNameCol;

    @FXML
    private TextField specialtyNameTextField;
    @FXML
    private ComboBox<Department> departmentComboBox;

    @FXML
    private Label editIndexLabel;
    @FXML
    private Label deleteIndexesLabel;

    @FXML
    private void addSpecialty() throws AppsException {
        String name = specialtyNameTextField.getText().toLowerCase();

        if (isValid(name)) {
            Department department = departmentComboBox.getSelectionModel().getSelectedItem();
            if (department == null) {
                AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, "Необходимо задать отделение для специальности");
                return;
            }
            Specialty specialty = new Specialty(name, department);
            specialtyService.save(specialty);
            specialties.add(specialty);
        }
    }

    @FXML
    private void editSpecialty() throws AppsException {
        Specialty specialtyForEdit  = specialtyTableView.getSelectionModel().getSelectedItem();
        if (specialtyForEdit == null) {
            AlertsUtil.showInfoAlert("Не выбрана специальность", "Выберите хотя бы одну специальность для редактирования");
            return;
        }
        String name = specialtyNameTextField.getText().toLowerCase();

        Department department = departmentComboBox.getSelectionModel().getSelectedItem();
        if (department == null) {
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, "Необходимо задать отделение для специальности");
            return;
        }

        boolean isValid;
        if (specialtyForEdit.getDepartment().getDepartmentId() == department.getDepartmentId()) {
            isValid = isValid(name);
        } else {
            // разрешаем дубликаты имен
            isValid = isValid(name, false, true);
        }

        if (isValid) {
            specialtyForEdit.setName(name);
            specialtyForEdit.setDepartment(department);
            specialtyService.save(specialtyForEdit);
            specialtyTableView.refresh();
        }
    }

    @FXML
    private void deleteSpecialty() throws AppsException {
        ObservableList<Specialty> selectedItems = specialtyTableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбрана специальность", "Выберите хотя бы одну специальность для удаления");
            return;
        }
        List<Integer> selectedNumbers = specialtyTableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        boolean isConfirmed = AlertsUtil.showConfirmAlert("Вы точно хотите удалить выделенные специальности?",
                "Специальности с номерами " + selectedNumbers + " будут удалены");

        if (isConfirmed) {
            specialtyService.deleteAll(selectedItems);

            specialties.clear();
            specialties.setAll(specialtyService.findAll());
            refresh();
        }
    }

    private boolean isValid(String name, boolean allowEmpty, boolean allowDuplicate) {
        try {
            List<String> names = specialtyTableView.getItems().stream()
                    .map(Specialty::getName)
                    .collect(Collectors.toList());
            AppsValidation.validate(name, new ValidConditions(allowEmpty, allowDuplicate), names);
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(ExceptionType.VALID_EMPTY_VALUE)) {
                contentText = "Имя специальности не должно быть пустым";
            } else if (ex.getExceptionType().equals(ExceptionType.VALID_LONG_VALUE)) {
                contentText = "Имя специальности не дожно превышать длину в " + ValidConditions.MAX_STRING_LENGTH + " символов";
            } else if (ex.getExceptionType().equals(ExceptionType.VALID_DUPLICATE_VALUE)) {
                contentText = "Специальность с именем \"" + name + "\" уже существует";
            }
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, contentText);
            return false;
        }
        return true;
    }

    public boolean isValid(String name) {
        return isValid(name, false, false);
    }

    @PostConstruct
    public void init() {

        // настраиваем всплывающий список отделений
        tuningComboBoxes();

        // настраиваем колонки таблицы
        tuningColumns();

        // настраиваем таблицу
        tuningTable();
    }

    private void tuningTable() {
        specialties = FXCollections.observableArrayList(specialtyService.findAll());
        specialtyTableView.setItems(specialties);

        specialtyTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        specialtyTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                specialtyNameTextField.setText(newValue.getName());
                refreshLabels();
            }
        });
        VBox.setVgrow(specialtyTableView, Priority.ALWAYS);
    }

    private void tuningColumns() {
        specialtyIndexCol.setCellFactory(col -> {
            TableCell<Specialty, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));

            return cell ;
        });
        specialtyNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        departmentNameCol.setCellValueFactory(cellData -> cellData.getValue().getDepartment().nameProperty());
    }

    private void tuningComboBoxes() {
        departmentComboBox.setCellFactory((listView) -> {
            return new ListCell<Department>() {
                @Override
                protected void updateItem(Department item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            };
        });

        departmentComboBox.setConverter(new StringConverter<Department>() {
            @Override
            public String toString(Department object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getName();
                }
            }

            @Override
            public Department fromString(String string) {
                return null;
            }
        });
    }

    public void refresh() {
        refreshLabels();
        List<Department> departments = departmentService.findAll();
        departmentComboBox.setItems(FXCollections.observableArrayList(departments));
        departmentComboBox.getSelectionModel().selectFirst();
    }

    private void refreshLabels() {
        List<Integer> selectedNumbers = specialtyTableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        if (!selectedNumbers.isEmpty()) {
            editIndexLabel.setText(EDIT_LABEL_NAME + (specialtyTableView.getSelectionModel().getSelectedIndex() + 1));
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
