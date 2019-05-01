package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Объект ячейки расписания. Содержит в себе список объектов {@link TimetableDto}
 */
@Getter
@Setter
@NoArgsConstructor
public class TimetableListDto {

    /**
     * Список установленных типов часов со связками преподавателей
     */
    List<TimetableDto> timetableDtoList = new ArrayList<>();

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

    public TimetableListDto(int verticalCellIndex, StudyGroup group) {
        this.verticalCellIndex = verticalCellIndex;
        this.group = group;
    }

    public void addDto(TimetableDto dto) {
        timetableDtoList.add(dto);
    }

    public void removeDto(TimetableDto dto) {
        timetableDtoList.remove(dto);
    }
}
