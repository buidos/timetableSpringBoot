package bel.dmitrui98.timetable.util.gridpane;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridPaneUtil {

    /**
     * Возвращает ячейку грида по строке и колонке
     */
    public static Node getCell(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
