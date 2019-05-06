package bel.dmitrui98.timetable.util.gridpane;

import bel.dmitrui98.timetable.control.TimetableLabel;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridPaneUtil {

    /**
     * Возвращает ячейку расписания по строке и колонке
     */
    public static TimetableLabel getTimetableCell(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (TimetableLabel) node;
            }
        }
        return null;
    }
}
