package bel.dmitrui98.timetable.config.controller.database.dictionary;

import bel.dmitrui98.timetable.controller.database.dictionary.DepartmentController;
import bel.dmitrui98.timetable.controller.database.dictionary.SpecialtyController;
import bel.dmitrui98.timetable.util.view.AppsView;
import bel.dmitrui98.timetable.util.view.ViewUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static bel.dmitrui98.timetable.util.view.ViewUtil.loadView;

@Configuration
public class DictionaryControllerConfig {

    private static final String PREFIX = "database/dictionary/";

    // DEPARTMENT
    @Bean
    public AppsView departmentView() throws IOException {
        return loadView(ViewUtil.getModifyName(PREFIX + "department"));
    }

    @Bean
    public DepartmentController departmentController() throws IOException {
        return (DepartmentController) departmentView().getController();
    }

    // SPECIALTY
    @Bean
    public AppsView specialtyView() throws IOException {
        return loadView(ViewUtil.getModifyName(PREFIX + "specialty"));
    }

    @Bean
    public SpecialtyController specialtyController() throws IOException {
        return (SpecialtyController) specialtyView().getController();
    }
}
