package bel.dmitrui98.timetable.control;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка расписания
 */
@Getter
@Setter
public class TimetableLabel extends Label {

    private TimetableDto timetableDto;

    public TimetableLabel(double width, double height, StudyGroup group) {
        this.setMaxSize(width, height);
        this.setMinSize(width, height);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");
        timetableDto = new TimetableDto(group);
    }
}
