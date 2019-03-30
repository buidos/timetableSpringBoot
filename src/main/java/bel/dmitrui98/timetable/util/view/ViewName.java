package bel.dmitrui98.timetable.util.view;

public class ViewName {
    private static String prefix = "view/";
    private static String postfix = ".fxml";

    /**
     * Добавляет префикс(путь по умолчанию к файлу) и постфикс(расширение) к переданной строке
     * @param name - путь с именем fxml файла
     * @return строка с префиксом и постфиксом
     */
    public static String getName(String name) {
        return prefix + name + postfix;
    }
}
