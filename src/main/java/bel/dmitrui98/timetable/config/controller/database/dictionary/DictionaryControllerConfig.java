package bel.dmitrui98.timetable.config.controller.database.dictionary;

import bel.dmitrui98.timetable.controller.database.dictionary.*;
import bel.dmitrui98.timetable.util.view.AppsView;
import bel.dmitrui98.timetable.util.view.ViewUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static bel.dmitrui98.timetable.util.view.ViewUtil.getView;

@Configuration
public class DictionaryControllerConfig {

    private static final String PREFIX = "database/dictionary/";

    // DEPARTMENT
    @Bean
    public AppsView departmentView() throws IOException {
        return getView(ViewUtil.getModifyName(PREFIX + "department"));
    }

    @Bean
    public DepartmentController departmentController() throws IOException {
        return (DepartmentController) departmentView().getController();
    }

    // SPECIALTY
    @Bean
    public AppsView specialtyView() throws IOException {
        return getView(ViewUtil.getModifyName(PREFIX + "specialty"));
    }

    @Bean
    public SpecialtyController specialtyController() throws IOException {
        return (SpecialtyController) specialtyView().getController();
    }

    // SUBJECT_TYPE
    @Bean
    public AppsView subjectTypeView() throws IOException {
        return getView(ViewUtil.getModifyName(PREFIX + "supject_type_enum"));
    }

    @Bean
    public SubjectTypeController subjectTypeController() throws IOException {
        return (SubjectTypeController) subjectTypeView().getController();
    }

    // SUBJECT
    @Bean
    public AppsView subjectView() throws IOException {
        return getView(ViewUtil.getModifyName(PREFIX + "subject"));
    }

    @Bean
    public SubjectController subjectController() throws IOException {
        return (SubjectController) subjectView().getController();
    }

    // STUDY FORM
    @Bean
    public AppsView studyFormView() throws IOException {
        return getView(ViewUtil.getModifyName(PREFIX + "study_form_enum"));
    }

    @Bean
    public StudyFormController studyFormController() throws IOException {
        return (StudyFormController) studyFormView().getController();
    }

    // STUDY TYPE
    @Bean
    public AppsView studyTypeView() throws IOException {
        return getView(ViewUtil.getModifyName(PREFIX + "study_type_enum"));
    }

    @Bean
    public StudyTypeController studyTypeController() throws IOException {
        return (StudyTypeController) studyTypeView().getController();
    }

    // STUDY SHIFT
    @Bean
    public AppsView studyShiftView() throws IOException {
        return getView(ViewUtil.getModifyName(PREFIX + "study_shift_enum"));
    }

    @Bean
    public StudyShiftController studyShiftController() throws IOException {
        return (StudyShiftController) studyShiftView().getController();
    }

}
