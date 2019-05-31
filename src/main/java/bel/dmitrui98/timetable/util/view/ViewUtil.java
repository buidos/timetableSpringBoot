package bel.dmitrui98.timetable.util.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.InputStream;

import static bel.dmitrui98.timetable.TimetableApplication.applicationIcon;

public class ViewUtil {
    private static String prefix = "view/";
    private static String postfix = ".fxml";

    /**
     * Добавляет префикс(путь по умолчанию к файлу) и постфикс(расширение) к переданной строке
     * @param name - путь с именем fxml файла
     * @return строка с префиксом и постфиксом
     */
    public static String getModifyName(String name) {
        return prefix + name + postfix;
    }

    /**
     * Загружает view (fxml файл)
     * @param url путь к fxml
     * @return {@link AppsView}
     * @throws IOException
     */
    public static AppsView getView(String url) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = ViewUtil.class.getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            return new AppsView(new Scene(loader.getRoot()), loader.getController(), applicationIcon);
        } finally {
            if (fxmlStream != null) {
                fxmlStream.close();
            }
        }
    }
}
