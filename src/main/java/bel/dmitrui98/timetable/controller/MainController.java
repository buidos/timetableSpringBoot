package bel.dmitrui98.timetable.controller;

import bel.dmitrui98.timetable.controller.database.EditDBController;
import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.service.timetable.CriteriaService;
import bel.dmitrui98.timetable.service.timetable.TimetableService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.dto.timetable.CriteriaCheckComboBoxesDto;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainController {


    private static final int SPACING = 5;
    private static final String SHOW_INFO_PANEL_TEXT = "Показать инфо-панель";
    private static final String HIDE_INFO_PANEL_TEXT = "Скрыть инфо-панель";

    @Autowired
    @Qualifier("editDatabaseView")
    private AppsView editDatabaseView;

    @FXML
    private BorderPane borderPane;

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

    @Autowired
    private TimetableService timetableService;


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
        stage.showAndWait();
        refresh();
    }

    @PostConstruct
    public void init() {
        VBox vBox = new VBox(SPACING);
        vBox.getChildren().add(new Label("Отделение:"));
        vBox.getChildren().add(depCheckComboBox);
        criteriaHBox.getChildren().add(vBox);

        vBox = new VBox(SPACING);
        vBox.getChildren().add(new Label("Специальность:"));
        vBox.getChildren().add(specialtyCheckComboBox);
        criteriaHBox.getChildren().add(vBox);

        vBox = new VBox(SPACING);
        vBox.getChildren().add(new Label("Группа:"));
        vBox.getChildren().add(groupCheckComboBox);
        criteriaHBox.getChildren().add(vBox);

        vBox = new VBox(SPACING);
        vBox.getChildren().add(new Label("День:"));
        vBox.getChildren().add(dayCheckComboBox);
        criteriaHBox.getChildren().add(vBox);

        tuningCheckBoxes();

        final double marginTop = 26;
        final double minWidth = 180;
        Button showTimeTableButton = new Button("Показать расписание");
        showTimeTableButton.setMinWidth(minWidth);
        HBox.setMargin(showTimeTableButton, new Insets(marginTop, 0, 0, 0));
        criteriaHBox.getChildren().add(showTimeTableButton);
        showTimeTableButton.setOnAction(e -> {
            showTable();
        });

        showInfoPanelButton = new Button(SHOW_INFO_PANEL_TEXT);
        showInfoPanelButton.setMinWidth(minWidth);
        HBox.setMargin(showInfoPanelButton, new Insets(marginTop, 0, 0, 0));
        criteriaHBox.getChildren().add(showInfoPanelButton);
        showInfoPanelButton.setOnAction(this::showInfoPanel);

        refresh();
    }

    private Button showInfoPanelButton;
    private boolean isInfoPanelShown = true;
    private void showInfoPanel(ActionEvent e) {
        Button button = (Button) e.getSource();
        if (timetableService.changeVisibleInfoPanel()) {
            if (isInfoPanelShown) {
                button.setText(SHOW_INFO_PANEL_TEXT);
            } else {
                button.setText(HIDE_INFO_PANEL_TEXT);
            }
            isInfoPanelShown = !isInfoPanelShown;
        }
    }

    private void showTable() {
        List<StudyGroup> checkedGroups = groupCheckComboBox.getCheckModel().getCheckedItems().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (checkedGroups.isEmpty()) {
            if (borderPane.getCenter() == null) {
                AlertsUtil.showInfoAlert("Не выбрана ни одна группа", "Выберите хотя бы одну группу, чтобы" +
                        " отобразить расписание");
            } else {
                hideTimetable();
            }
            return;
        }

        List<DayEnum> checkedDays = dayCheckComboBox.getCheckModel().getCheckedItems().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (checkedDays.isEmpty()) {
            if (borderPane.getCenter() == null) {
                AlertsUtil.showInfoAlert("Не выбран ни один день", "Выберите хотя бы один день, чтобы" +
                        " отобразить расписание");
            } else {
                hideTimetable();
            }
            return;
        }

        timetableService.showTable(checkedGroups, checkedDays, borderPane);
        timetableService.createInfoPanel(borderPane);
        showInfoPanelButton.setText(HIDE_INFO_PANEL_TEXT);
    }

    private void hideTimetable() {
        borderPane.setCenter(null);
        borderPane.setRight(null);
        showInfoPanelButton.setText(SHOW_INFO_PANEL_TEXT);
    }

    private void tuningCheckBoxes() {
        CriteriaCheckComboBoxesDto dto = new CriteriaCheckComboBoxesDto(depCheckComboBox, specialtyCheckComboBox,
                groupCheckComboBox, dayCheckComboBox);
        criteriaService.enableSelectAll(dto);
        criteriaService.tuningDataModel(dto);
        criteriaService.addOnHiddenListener(dto);
    }

    public void refresh() {
        depCheckComboBox.getItems().clear();
        depCheckComboBox.getItems().add(null);
        depCheckComboBox.getItems().addAll(departmentService.findAll());
        depCheckComboBox.getCheckModel().checkAll();

        specialtyCheckComboBox.getItems().clear();
        specialtyCheckComboBox.getItems().add(null);
        specialtyCheckComboBox.getItems().addAll(specialtyService.findAll());
        specialtyCheckComboBox.getCheckModel().checkAll();

        groupCheckComboBox.getItems().clear();
        groupCheckComboBox.getItems().add(null);
        groupCheckComboBox.getItems().addAll(studyGroupService.findAll());
        groupCheckComboBox.getCheckModel().checkAll();

        dayCheckComboBox.getItems().clear();
        dayCheckComboBox.getItems().add(null);
        dayCheckComboBox.getItems().addAll(Arrays.asList(DayEnum.values()));
        dayCheckComboBox.getCheckModel().checkAll();

        borderPane.setCenter(null);
    }
}
