package bel.dmitrui98.timetable.control;

import bel.dmitrui98.timetable.util.dto.timetable.TimetableListDto;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка расписания
 */
@Getter
@Setter
public class TimetableLabel extends Label {

    private TimetableListDto timetableListDto;

    /**
     * Индекс ячейки расписания по вертикали (учитывает день, для которого устанавливалось расписание и пару)
     */
    private int verticalCellIndex;

    public TimetableLabel(double width, double height, int verticalCellIndex) {
        this.setMaxSize(width, height);
        this.setMinSize(width, height);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");

        this.verticalCellIndex = verticalCellIndex;
    }
}
