package bel.dmitrui98.timetable;

import bel.dmitrui98.timetable.util.exception.AppsExceptionHandler;
import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
@Getter
public class TimetableApplication extends AbstractJavaFxApplicationSupport {

    private String windowTitle = "Помощник составления расписания";
    public static final Image APPLICATION_ICON = new Image("images/icon.png");

    private Stage rootStage;

    @Autowired
    @Qualifier("mainView")
    private AppsView mainView;

    @Override
    public void start(Stage stage) {
        this.rootStage = stage;

        // сквозной перехватчик исключений
        Thread.currentThread().setUncaughtExceptionHandler(new AppsExceptionHandler(stage));

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
