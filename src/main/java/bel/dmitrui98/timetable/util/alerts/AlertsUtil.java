package bel.dmitrui98.timetable.util.alerts;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class AlertsUtil {

    public static void showErrorAlert(String headerText, String contentText, Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initStyle(StageStyle.UTILITY);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Стек вызовов методов:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        GridPane expContent = new GridPane();
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public static void showErrorAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    public static void showInfoAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    public static boolean showConfirmAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initStyle(StageStyle.UTILITY);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }


    /**
     * confirm dialog да, нет, отмена
     * @return true - да, false - нет, null - отмена
     */
    public static Boolean showCustomConfirmAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет");
        ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // да
            return true;
        } else if (result.get() == buttonTypeTwo) {
            // нет
            return false;
        } else {
            // отмена
            return null;
        }
    }
}
