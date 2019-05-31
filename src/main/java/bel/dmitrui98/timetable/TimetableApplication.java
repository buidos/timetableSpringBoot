package bel.dmitrui98.timetable;

import bel.dmitrui98.timetable.controller.MainController;
import bel.dmitrui98.timetable.service.timetable.TimetableService;
import bel.dmitrui98.timetable.service.timetable.save.TimetableSaveService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.exception.AppsExceptionHandler;
import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import java.io.File;

@Lazy
@SpringBootApplication
@Log4j2
public class TimetableApplication extends AbstractJavaFxApplicationSupport {

    private String windowTitle = "Помощник составления расписания";
    private static final String IMAGE_URL = "images/icon.png";
    public static Image applicationIcon;


    @Autowired
    @Qualifier("mainView")
    private AppsView mainView;

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private TimetableSaveService timetableSaveService;

    @Override
    public void init() throws Exception {
        // инициализация иконки приложения
        try {
            applicationIcon = new Image(IMAGE_URL);
        } catch (IllegalArgumentException ex) {
            log.error("image with url " + IMAGE_URL + " not found");
            applicationIcon = null;
        }
        super.init();
    }

    @Override
    public void start(Stage stage) {
        // сквозной перехватчик исключений
        Thread.currentThread().setUncaughtExceptionHandler(new AppsExceptionHandler(stage));

        stage.setTitle(windowTitle);
        stage.setScene(mainView.getScene());
        if (mainView.getIcon() != null) {
            stage.getIcons().add(mainView.getIcon());
        }
        MainController controller = (MainController) mainView.getController();
        controller.setRootStage(stage);

        stage.setResizable(true);

        stage.setOnCloseRequest(e -> {
            if (timetableService.isEdit()) {
                Boolean isConfirm = AlertsUtil.showCustomConfirmAlert("Сохранить внесенные изменения?", null);
                if (isConfirm == null) {
                    e.consume();
                } else if (isConfirm) {
                    controller.save();
                }
            }
        });

        // на полный экран
        stage.setMaximized(true);

        stage.show();

        // Пытается загрузить последний открытый файл с расписанием
        File file = timetableSaveService.getTimetableFilePath();
        if (file != null) {
            timetableSaveService.loadTimetableFromFile(file);
        }
    }

    public static void main(String[] args) {
        launchApp(TimetableApplication.class, args);
    }
}
