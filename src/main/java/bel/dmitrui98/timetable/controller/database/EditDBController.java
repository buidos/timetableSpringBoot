package bel.dmitrui98.timetable.controller.database;

import bel.dmitrui98.timetable.controller.database.dictionary.DepartmentController;
import bel.dmitrui98.timetable.controller.database.dictionary.SpecialtyController;
import bel.dmitrui98.timetable.controller.database.dictionary.SubjectController;
import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class EditDBController {

    private static final String SETTINGS = "Настройки";
    private static final String DICTIONARIES = "Словари";
    private static final String TEACHERS = "Преподаватели";
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

    @Autowired
    @Qualifier("teacherView")
    private AppsView teacherView;

    @Autowired
    @Qualifier("studyGroupView")
    private AppsView studyGroupView;

    @Autowired
    @Qualifier("pairView")
    private AppsView pairView;

    @Autowired
    @Qualifier("loadView")
    private AppsView loadView;

    /**
     * Обновляет вкладку отделений
     */
    @FXML
    private void departmentTabSelectionChange(Event event) {
        if (departmentView == null) {
            return;
        }
        Tab tab = (Tab) event.getSource();
        DepartmentController controller = (DepartmentController) departmentView.getController();
        if (tab.isSelected()) {
            controller.getDefaultButton().setDefaultButton(true);
        } else {
            controller.getDefaultButton().setDefaultButton(false);
        }
    }

    @FXML
    private void dictionaryTabSelectionChange(Event event) {
        departmentTabSelectionChange(event);
    }

    /**
     * Обновляет вкладку специальностей
     */
    @FXML
    private void specialtyTabSelectionChange(Event event) {
        if (specialtyView == null) {
            return;
        }
        Tab tab = (Tab) event.getSource();
        SpecialtyController controller = (SpecialtyController) specialtyView.getController();
        if (tab.isSelected()) {
            controller.getDefaultButton().setDefaultButton(true);
            controller.refresh();
        } else {
            controller.getDefaultButton().setDefaultButton(false);
        }
    }

    /**
     * Обновляет вкладку дисциплин
     */
    @FXML
    private void subjectTabSelectionChange(Event event) {
        if (subjectView == null) {
            return;
        }
        Tab tab = (Tab) event.getSource();
        SubjectController controller = (SubjectController) subjectView.getController();
        if (tab.isSelected()) {
            controller.getDefaultButton().setDefaultButton(true);
        } else {
            controller.getDefaultButton().setDefaultButton(false);
        }
    }

    /**
     * Обновляет вкладку учителей
     */
    @FXML
    private void teacherTabSelectionChange(Event event) {
        if (teacherView == null) {
            return;
        }
        Tab tab = (Tab) event.getSource();
        TeacherController controller = (TeacherController) teacherView.getController();
        if (tab.isSelected()) {
            controller.getDefaultButton().setDefaultButton(true);
        } else {
            controller.getDefaultButton().setDefaultButton(false);
        }
    }

    /**
     * Обновляет вкладку учебных групп
     */
    @FXML
    private void studyGroupTabSelectionChange(Event event) {
        if (studyGroupView == null) {
            return;
        }
        Tab tab = (Tab) event.getSource();
        StudyGroupController controller = (StudyGroupController) studyGroupView.getController();
        if (tab.isSelected()) {
            controller.getDefaultButton().setDefaultButton(true);
            controller.refresh();
        } else {
            controller.getDefaultButton().setDefaultButton(false);
        }
    }

    /**
     * Обновляет вкладку пар
     */
    @FXML
    private void pairTabSelectionChange(Event event) {
        if (pairView == null) {
            return;
        }
        Tab tab = (Tab) event.getSource();
        StudyPairController controller = (StudyPairController) pairView.getController();
        if (tab.isSelected()) {
            controller.getDefaultButton().setDefaultButton(true);
        } else {
            controller.getDefaultButton().setDefaultButton(false);
        }
    }

    /**
     * Обновляет вкладку нагрузки
     */
    @FXML
    private void loadTabSelectionChange(Event event) {
        if (loadView == null) {
            return;
        }
        Tab tab = (Tab) event.getSource();
        LoadController controller = (LoadController) loadView.getController();
        if (tab.isSelected()) {
            controller.getDefaultButton().setDefaultButton(true);
            controller.refresh();
        } else {
            controller.getDefaultButton().setDefaultButton(false);
        }
    }

    public Scene initTabs() {
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
                    tab.setContent(teacherView.getScene().getRoot());
                    break;
                case GROUPS:
                    tab.setContent(studyGroupView.getScene().getRoot());
                    break;
                case PAIRS:
                    tab.setContent(pairView.getScene().getRoot());
                    break;
                case LOAD:
                    tab.setContent(loadView.getScene().getRoot());
                    break;
            }
        }
        return editDatabaseScene;
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
