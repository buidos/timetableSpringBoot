package bel.dmitrui98.timetable.config.controller.database;

import bel.dmitrui98.timetable.controller.database.*;
import bel.dmitrui98.timetable.util.view.AppsView;
import bel.dmitrui98.timetable.util.view.ViewUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static bel.dmitrui98.timetable.util.view.ViewUtil.loadView;

@Configuration
public class DatabaseControllerConfig {

    private static final String PREFIX = "database/";

    @Bean
    public AppsView editDatabaseView() throws IOException {
        return loadView(ViewUtil.getModifyName(PREFIX + "editDatabase"));
    }

    @Bean
    public EditDBController editDBController() throws IOException {
        return (EditDBController) editDatabaseView().getController();
    }

    // настройки
    @Bean
    public AppsView settingView() throws IOException {
        return loadView(ViewUtil.getModifyName(PREFIX + "setting"));
    }

    @Bean
    public SettingController settingController() throws IOException {
        return (SettingController) settingView().getController();
    }

    // учителя
    @Bean
    public AppsView teacherView() throws IOException {
        return loadView(ViewUtil.getModifyName(PREFIX + "teacher"));
    }

    @Bean
    public TeacherController teacherController() throws IOException {
        return (TeacherController) teacherView().getController();
    }

    // группы
    @Bean
    public AppsView studyGroupView() throws IOException {
        return loadView(ViewUtil.getModifyName(PREFIX + "studyGroup"));
    }

    @Bean
    public StudyGroupController studyGroupController() throws IOException {
        return (StudyGroupController) studyGroupView().getController();
    }

    // пары
    @Bean
    public AppsView pairView() throws IOException {
        return loadView(ViewUtil.getModifyName(PREFIX + "pair"));
    }

    @Bean
    public StudyPairController pairController() throws IOException {
        return (StudyPairController) pairView().getController();
    }
}
