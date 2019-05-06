package bel.dmitrui98.timetable.control;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableListDto;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Ячейка расписания
 */
@Getter
@Setter
public class TimetableLabel extends Label {

    private TimetableListDto timetableListDto;

    private int verticalCellIndex;

    public TimetableLabel(double width, double height, int verticalCellIndex, StudyGroup group) {
        this.setMaxSize(width, height);
        this.setMinSize(width, height);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");

        timetableListDto = new TimetableListDto(verticalCellIndex, group);
        this.verticalCellIndex = verticalCellIndex;
    }

    public void refresh() {
        List<TimetableDto> timetableDtoList = timetableListDto.getTimetableDtoList();
        if (timetableDtoList.isEmpty()) {
            setText("");
            setTooltip(null);
            return;
        }

        StringBuilder teacherSb = new StringBuilder();
        StringBuilder subjectSb = new StringBuilder();
        StringBuilder typeHourSb = new StringBuilder();
        for (TimetableDto dto : timetableDtoList) {
            teacherSb.append(dto.getBranch().getTeacherSet().toString()).append("|");
            subjectSb.append(dto.getBranch().getStudyLoad().getSubject().getName()).append("|");
            typeHourSb.append(dto.getHourType().getShortName()).append("|");
        }
        teacherSb.replace(teacherSb.length()-1, teacherSb.length(), "");
        subjectSb.replace(subjectSb.length()-1, subjectSb.length(), "");
        typeHourSb.replace(typeHourSb.length()-1, typeHourSb.length(), "");

        teacherSb.append("\n").append(subjectSb.toString());
        setText(teacherSb.toString());
        teacherSb.append("\n").append(typeHourSb.toString());
        setTooltip(new Tooltip(teacherSb.toString()));
    }
}
