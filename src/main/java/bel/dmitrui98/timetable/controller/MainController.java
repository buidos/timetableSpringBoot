package bel.dmitrui98.timetable.controller;

import bel.dmitrui98.timetable.controller.database.EditDBController;
import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.service.timetable.CriteriaService;
import bel.dmitrui98.timetable.util.dto.timetable.CriteriaCheckComboBoxesDto;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.Arrays;

public class MainController {



    @Autowired
    @Qualifier("editDatabaseView")
    private AppsView editDatabaseView;

    @FXML
    private HBox criteriaHBox;
    private CheckComboBox<Department> depCheckComboBox = new CheckComboBox<>();
    private CheckComboBox<Specialty> specialtyCheckComboBox = new CheckComboBox<>();
    private CheckComboBox<StudyGroup> groupCheckComboBox = new CheckComboBox<>();
    private CheckComboBox<DayEnum> dayCheckComboBox = new CheckComboBox<>();

    @Autowired
    private BaseService<Department, Integer> departmentService;

    @Autowired
    private BaseService<Specialty, Integer> specialtyService;

    @Autowired
    private BaseService<StudyGroup, Long> studyGroupService;

    @Autowired
    private CriteriaService criteriaService;


    @FXML
    private void showEditDatabase() {
        Stage stage = new Stage();
        stage.setTitle("Редактирование базы данных");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(editDatabaseView.getIcon());

        EditDBController editDBController = (EditDBController) editDatabaseView.getController();
        // заполняем табы содержимым
        Scene editDatabaseScene = editDBController.initTabs();

        stage.setScene(editDatabaseScene);
        stage.show();
    }

    @PostConstruct
    public void init() {
        depCheckComboBox.getItems().add(null);
        depCheckComboBox.getItems().addAll(departmentService.findAll());
        criteriaHBox.getChildren().add(depCheckComboBox);

        specialtyCheckComboBox.getItems().add(null);
        specialtyCheckComboBox.getItems().addAll(specialtyService.findAll());
        criteriaHBox.getChildren().add(specialtyCheckComboBox);

        groupCheckComboBox.getItems().add(null);
        groupCheckComboBox.getItems().addAll(studyGroupService.findAll());
        criteriaHBox.getChildren().add(groupCheckComboBox);

        dayCheckComboBox.getItems().add(null);
        dayCheckComboBox.getItems().addAll(Arrays.asList(DayEnum.values()));
        criteriaHBox.getChildren().add(dayCheckComboBox);

        tuningCheckBoxes();

        Button showTimeTableButton = new Button("Показать");
        criteriaHBox.getChildren().add(showTimeTableButton);
        showTimeTableButton.setOnAction((e) -> {
            System.out.println("show");
        });
    }

    private void tuningCheckBoxes() {
        CriteriaCheckComboBoxesDto dto = new CriteriaCheckComboBoxesDto(depCheckComboBox, specialtyCheckComboBox,
                groupCheckComboBox, dayCheckComboBox);
        criteriaService.enableSelectAll(dto);
    }
}
