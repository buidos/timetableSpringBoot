package bel.dmitrui98.timetable.config.controller.database;

import bel.dmitrui98.timetable.controller.database.EditDBController;
import bel.dmitrui98.timetable.controller.database.dictionary.SettingController;
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

    @Bean
    public AppsView settingView() throws IOException {
        return loadView(ViewUtil.getModifyName(PREFIX + "setting"));
    }

    @Bean
    public SettingController settingController() throws IOException {
        return (SettingController) settingView().getController();
    }
}
