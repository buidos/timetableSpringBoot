package bel.dmitrui98.timetable.controller;

import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MainController {

    private static final String SETTINGS = "Настройки";
    private static final String DICTIONARIES = "Словари";
    private static final String TEACHERS = "Учителя";
    private static final String GROUPS = "Группы";
    private static final String PAIRS = "Пары";
    private static final String LOAD = "Нагрузка";
    private static final String DEPARTMENT = "Отделение";
    private static final String SPECIALTY = "Специальность";
    private static final String SUBJECT_TYPE = "Тип дисциплины";
    private static final String SUBJECT = "Дисциплина";
    private static final String STUDY_FORM = "Форма обучения";
    private static final String STUDY_TYPE = "Тип обучения";
    private static final String STUDY_SHIFT = "Смена";

    @Autowired
    @Qualifier("editDatabaseView")
    private AppsView editDatabaseView;

    @Autowired
    @Qualifier("departmentView")
    private AppsView departmentView;

    @Autowired
    @Qualifier("specialtyView")
    private AppsView specialtyView;

    @Autowired
    @Qualifier("subjectTypeView")
    private AppsView subjectTypeView;

    @Autowired
    @Qualifier("subjectView")
    private AppsView subjectView;

    @Autowired
    @Qualifier("studyTypeView")
    private AppsView studyTypeView;

    @Autowired
    @Qualifier("studyFormView")
    private AppsView studyFormView;

    @Autowired
    @Qualifier("studyShiftView")
    private AppsView studyShiftView;

    @Autowired
    @Qualifier("settingView")
    private AppsView settingView;


    @FXML
    private void showEditDatabase() {
        Stage stage = new Stage();
        stage.setTitle("Редактирование базы данных");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(editDatabaseView.getIcon());

        Scene editDatabaseScene = editDatabaseView.getScene();

        BorderPane borderPane = (BorderPane) editDatabaseScene.getRoot();
        TabPane rootTabPane = (TabPane) borderPane.getCenter();
        for (Tab tab : rootTabPane.getTabs()) {
            switch (tab.getText()) {
                case SETTINGS:
                    tab.setContent(settingView.getScene().getRoot());
                    break;
                case DICTIONARIES:
                    initDictionariesTabPane(tab);
                    break;
                case TEACHERS:
                    System.out.println(TEACHERS);
                    break;
                case GROUPS:
                    System.out.println(GROUPS);
                    break;
                case PAIRS:
                    System.out.println(PAIRS);
                    break;
                case LOAD:
                    System.out.println(LOAD);
                    break;
            }
        }

        stage.setScene(editDatabaseScene);

        stage.show();
    }

    private void initDictionariesTabPane(Tab rootTab) {
        TabPane dictionariesTabPane = (TabPane) rootTab.getContent();
        for (Tab tab : dictionariesTabPane.getTabs()) {
            switch (tab.getText()) {
                case DEPARTMENT:
                    tab.setContent(departmentView.getScene().getRoot());
                    break;
                case SPECIALTY:
                    tab.setContent(specialtyView.getScene().getRoot());
                    break;
                case SUBJECT_TYPE:
                    tab.setContent(subjectTypeView.getScene().getRoot());
                    break;
                case SUBJECT:
                    tab.setContent(subjectView.getScene().getRoot());
                    break;
                case STUDY_FORM:
                    tab.setContent(studyFormView.getScene().getRoot());
                    break;
                case STUDY_TYPE:
                    tab.setContent(studyTypeView.getScene().getRoot());
                    break;
                case STUDY_SHIFT:
                    tab.setContent(studyShiftView.getScene().getRoot());
                    break;
            }
        }
    }
}
