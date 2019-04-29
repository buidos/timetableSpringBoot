package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Объект ячейки расписания. Содержит в себе список объектов {@link TimetableDto}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimetableListDto {

    /**
     * Список установленных типов часов со связками преподавателей
     */
    List<TimetableDto> timetableDtoList;

    /**
     * Индекс ячейки расписания по вертикали (учитывает день, для которого устанавливалось расписание и пару)
     * (строка)
     */
    private int verticalCellIndex;

    /**
     * Группа, для которой устанавливается ячейка расписания
     * (колонка)
     */
    private StudyGroup group;
}
