package bel.dmitrui98.timetable.util.exception;

import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.REC_NOT_DELETED;
import static bel.dmitrui98.timetable.util.exception.ExceptionType.REC_NOT_SAVED;

@Log4j2
public class AppsExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Stage ownerStage;

    public AppsExceptionHandler(Stage stage) {
        this.ownerStage = stage;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        // хз как обработать ошибки в критериях (фильтры на главной страницы)
        if (throwable instanceof IndexOutOfBoundsException || throwable instanceof NullPointerException) {
            return;
        }

        // логируем все исключения
        log.error("error", throwable);

        // преобразуем throwable к AppsException
        boolean isAppsException = false;
        String headerText = "Произошла ошибка в процессе работы программы";
        if (throwable instanceof RuntimeException) {
            RuntimeException runtimeEx = (RuntimeException) throwable;
            if (runtimeEx.getCause() instanceof InvocationTargetException) {
                InvocationTargetException invocationTargetEx = (InvocationTargetException) runtimeEx.getCause();
                if (invocationTargetEx.getTargetException() instanceof AppsException) {
                    isAppsException = true;
                    AppsException ex = (AppsException) invocationTargetEx.getTargetException();

                    if (ex.getExceptionType().equals(REC_NOT_SAVED)) {
                        AlertsUtil.showErrorAlert(headerText, "Не удалось сохранить или изменить запись", ex);
                    } else if (ex.getExceptionType().equals(REC_NOT_DELETED)) {
                        AlertsUtil.showErrorAlert(headerText, "Не удалось удалить запись", ex);
                    } else {
                        // показываем общую ошибку
                        isAppsException = false;
                    }
                }
            }
        }


        if (!isAppsException) {
            AlertsUtil.showErrorAlert(headerText,"Неожиданная ошибка", throwable);
        }
    }


}
