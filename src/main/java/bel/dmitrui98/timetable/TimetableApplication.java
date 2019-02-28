package bel.dmitrui98.timetable;

import bel.dmitrui98.timetable.config.ControllerConfig;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class TimetableApplication extends AbstractJavaFxApplicationSupport {

    private String windowTitle = "Помощник составления расписания";

    @Autowired
    private ControllerConfig.View view;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(view.getView()));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launchApp(TimetableApplication.class, args);
    }

}
