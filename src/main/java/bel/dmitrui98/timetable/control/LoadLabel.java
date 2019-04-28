package bel.dmitrui98.timetable.control;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.util.dto.timetable.LoadDto;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import lombok.Getter;

@Getter
public class LoadLabel extends Label {

    private LoadDto loadDto;

    public LoadLabel(double width, double height, TeachersBranch branch, StudyGroup group, String text) {
        this(width, height, text);
        loadDto = new LoadDto(branch, group);
        this.setTooltip(new Tooltip(branch.getTeacherSet().toString()));
    }

    /**
     * {@link LoadDto} оставляем пустым (нет нагрузки)
     */
    public LoadLabel(double width, double height, String text) {
        super(text);
        this.setMaxSize(width, height);
        this.setMinSize(width, height);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");
    }
}
