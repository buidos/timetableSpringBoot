package bel.dmitrui98.timetable.util.color;

import javafx.scene.Node;

public class ColorUtil {

    public static void setBackgroundColor(Node node, String color) {
        String style = node.getStyle();
        style = style.replaceAll("-fx-background-color: #[^;]+;", String.format("-fx-background-color: %s;", color));
        node.setStyle(style);
    }
}
