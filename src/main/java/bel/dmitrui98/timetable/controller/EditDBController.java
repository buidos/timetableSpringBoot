package bel.dmitrui98.timetable.controller;

import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.exception.AppsException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

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
        String name = depNameTextField.getText();

        Department department = new Department(name);
        if (departmentService.save(department)) {
            departments.add(department);
        }
    }

    @FXML
    private void editDepartment() {
        System.out.println("edit");
    }

    @FXML
    private void deleteDepartment() {
        System.out.println("delete");
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
        VBox.setVgrow(departmentTableView, Priority.ALWAYS);
    }
}
