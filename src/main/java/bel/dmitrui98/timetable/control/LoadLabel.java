package bel.dmitrui98.timetable.control;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.util.dto.timetable.LoadDto;
import bel.dmitrui98.timetable.util.time.TimeUtil;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка нагрузки (связка преподавателей или часы преподавателей)
 */
@Getter
@Setter
public class LoadLabel extends Label {

    /**
     * Связка учителей и нагрузка данной ячейки выбранной колонки (группа)
     */
    private LoadDto loadDto;

    /**
     * Ячейка с часами, которые отображаются пользователю. Если была выделена ячейка с часами, то ссылка на саму себя
     */
    private LoadLabel hourCell;

    /**
     * Ячейка с общим количеством часов данной группы
     */
    private LoadLabel commonHourCell;

    /**
     * Вычисляется только для commonHourCell, для остальных ячеек 0
     */
    private int commonMinutes;

    /**
     * Колонка расписания
     */
    private int col;

    private int rowIndex = -1;

    private boolean isHourLabel;

    public LoadLabel(double width, double height, TeachersBranch branch, StudyGroup group, String text) {
        this(width, height, text);
        loadDto = new LoadDto(branch, group, branch.getStudyLoad().getCountMinutesInTwoWeek());
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

    /**
     * Обновляет ячейку нагрузки
     */
    public void refresh() {
        getHourCell().setText(String.valueOf(TimeUtil.convertMinuteToHour(getLoadDto().getCountMinutesInTwoWeek())));
    }

    /**
     * Обновляет общую ячейку с часами
     * @param minutes количество минут, которое добавляется/удаляется к общей ячейке расписания
     * @param isAdd добавляются ли минуты
     */
    public void refreshCommonHourCell(int minutes, boolean isAdd) {
        int commonMinutes = getCommonHourCell().getCommonMinutes();
        if (isAdd) {
            commonMinutes += minutes;
        } else {
            commonMinutes -= minutes;
        }
        getCommonHourCell().setCommonMinutes(commonMinutes);
        refreshCommonHourCell();
    }

    /**
     * Обновляет текст общей ячейки с часами
     */
    public void refreshCommonHourCell() {
        int commonMinutes = getCommonHourCell().getCommonMinutes();
        getCommonHourCell().setText(String.valueOf(TimeUtil.convertMinuteToHour(commonMinutes)));
    }
}
