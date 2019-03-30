package bel.dmitrui98.timetable;

import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class TimetableApplication extends AbstractJavaFxApplicationSupport {

    private String windowTitle = "Помощник составления расписания";

    @Autowired
    @Qualifier("mainView")
    private AppsView mainView;

    @Override
    public void start(Stage stage) {
        stage.setTitle(windowTitle);
        stage.setScene(mainView.getScene());
        stage.getIcons().add(mainView.getIcon());
        stage.setResizable(true);

        // на полный экран
//        stage.setMaximized(true);



        stage.show();
    }

    public static void main(String[] args) {
        launchApp(TimetableApplication.class, args);
    }

}
