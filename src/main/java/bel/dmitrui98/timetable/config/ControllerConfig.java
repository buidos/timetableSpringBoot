package bel.dmitrui98.timetable.config;

import bel.dmitrui98.timetable.controller.MainController;
import bel.dmitrui98.timetable.controller.database.DepartmentController;
import bel.dmitrui98.timetable.controller.database.EditDBController;
import bel.dmitrui98.timetable.util.view.AppsView;
import bel.dmitrui98.timetable.util.view.ViewName;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ControllerConfig {

    private Image icon;

    //*******BEGIN MAIN VIEW*******
    @Bean
    public AppsView mainView() throws IOException {
        return loadView(ViewName.getName("main"));
    }

    @Bean
    public MainController mainController() throws IOException {
        return (MainController) mainView().getController();
    }
    //*******END MAIN VIEW*******

    //*******BEGIN EDIT DATABASE VIEW*******
    @Bean
    public AppsView editDatabaseView() throws IOException {
        return loadView(ViewName.getName("database/editDatabase"));
    }

    @Bean
    public EditDBController editDBController() throws IOException {
        return (EditDBController) editDatabaseView().getController();
    }
    //*******END EDIT DATABASE VIEW*******

    //*******BEGIN DEPARTMENT VIEW*******
    @Bean
    public AppsView departmentView() throws IOException {
        return loadView(ViewName.getName("database/department"));
    }

    @Bean
    public DepartmentController departmentController() throws IOException {
        return (DepartmentController) departmentView().getController();
    }
    //*******END DEPARTMENT VIEW*******

    private AppsView loadView(String url) throws IOException {
        if (icon == null) {
            icon = new Image("images/icon.png");
        }

        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            return new AppsView(new Scene(loader.getRoot()), loader.getController(), icon);
        } finally {
            if (fxmlStream != null) {
                fxmlStream.close();
            }
        }
    }
}
