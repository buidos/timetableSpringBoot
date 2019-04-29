package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private HourTypeEnum hourTypeEnum;
}
