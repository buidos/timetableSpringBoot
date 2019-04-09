package bel.dmitrui98.timetable.controller.database;

import bel.dmitrui98.timetable.controller.database.dictionary.DepartmentController;
import bel.dmitrui98.timetable.controller.database.dictionary.SpecialtyController;
import bel.dmitrui98.timetable.controller.database.dictionary.SubjectController;
import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class EditDBController {

    @Autowired
    @Qualifier("specialtyView")
    private AppsView specialtyView;

    @Autowired
    @Qualifier("departmentView")
    private AppsView departmentView;

    @Autowired
    @Qualifier("subjectView")
    private AppsView subjectView;

    @Autowired
    @Qualifier("teacherView")
    private AppsView teacherView;

    @Autowired
    @Qualifier("studyGroupView")
    private AppsView studyGroupView;

    @Autowired
    @Qualifier("pairView")
    private AppsView pairView;

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
}
