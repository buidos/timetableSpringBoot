package bel.dmitrui98.timetable.controller;

import bel.dmitrui98.timetable.entity.dictionary.Department;
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
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

public class EditDBController {

    @Autowired
    private BaseService<Department, Integer> departmentService;
    private ObservableList<Department> departments;

    @FXML
    private TableView<Department> departmentTableView;
    @FXML
    private TableColumn<Department, Void> departmentIndexCol;
    @FXML
    private TableColumn<Department, String> departmentNameCol;
    @FXML
    private TextField depNameTextField;

    @FXML
    private void addDepartment() throws AppsException {
        String name = depNameTextField.getText().toLowerCase();

        if (isValid(name)) {
            Department department = new Department(name);
            departmentService.save(department);
            departments.add(department);
        }
    }

    @FXML
    private void editDepartment() throws AppsException {
        ObservableList<Department> selectedItems = departmentTableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбрано отделение", "Выберите хотя бы одно отделение для редактирования");
            return;
        }
        Department departmentForEdit = selectedItems.stream().findFirst().get();
        String name = depNameTextField.getText().toLowerCase();
        if (isValid(name)) {
            departmentForEdit.setName(name);
            departmentService.save(departmentForEdit);
        }
    }

    @FXML
    private void deleteDepartment() {
        System.out.println("delete");
    }

    private boolean isValid(String name) {
        try {
            List<String> depNames = departmentTableView.getItems().stream()
                    .map(Department::getName)
                    .collect(Collectors.toList());
            AppsValidation.validate(name, new ValidConditions(false, false), depNames);
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(ExceptionType.VALID_EMPTY_VALUE)) {
                contentText = "Имя отделения не должно быть пустым";
            } else if (ex.getExceptionType().equals(ExceptionType.VALID_LONG_VALUE)) {
                contentText = "Имя отделения не дожно превышать длину в " + ValidConditions.MAX_STRING_LENGTH + " символов";
            } else if (ex.getExceptionType().equals(ExceptionType.VALID_DUPLICATE_VALUE)) {
                contentText = "Отделение с именем \"" + name + "\" уже существует";
            }
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, contentText);
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {
        departmentIndexCol.setCellFactory(col -> {
            TableCell<Department, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));

            return cell ;
        });
        departmentNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        departments = FXCollections.observableArrayList(departmentService.findAll());
        departmentTableView.setItems(departments);

        departmentTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        departmentTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                depNameTextField.setText(newValue.getName());
            }
        });
        VBox.setVgrow(departmentTableView, Priority.ALWAYS);
    }
}
