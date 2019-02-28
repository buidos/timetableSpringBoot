package bel.dmitrui98.timetable.util.view;

public class ViewName {
    private static String prefix = "view/";
    private static String postfix = ".fxml";

    public static String getName(String name) {
        return prefix + name + postfix;
    }
}
