package bel.dmitrui98.timetable.config.controller;

import bel.dmitrui98.timetable.controller.MainController;
import bel.dmitrui98.timetable.util.view.AppsView;
import bel.dmitrui98.timetable.util.view.ViewUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static bel.dmitrui98.timetable.util.view.ViewUtil.getView;

@Configuration
public class ControllerConfig {

    @Bean
    public AppsView mainView() throws IOException {
        return getView(ViewUtil.getModifyName("main"));
    }

    @Bean
    public MainController mainController() throws IOException {
        return (MainController) mainView().getController();
    }
}
