package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.control.LoadLabel;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Dto для ячейки расписания (может быть несколько объектов в одной ячейке)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimetableDto {

    /**
     * Связка учителей
     */
    private TeachersBranch branch;
    /**
     * Тип часа
     */
    private HourTypeEnum hourType;

    /**
     * Ячейка, от куда бралась нагрузка
     */
    private LoadLabel loadCell;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimetableDto that = (TimetableDto) o;
        return branch.equals(that.branch) &&
                hourType == that.hourType &&
                loadCell.getLoadDto().equals(that.loadCell.getLoadDto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(branch, hourType, loadCell);
    }
}
