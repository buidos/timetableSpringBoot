package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.control.TimetableContextMenu;
import bel.dmitrui98.timetable.entity.StudyGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Объект ячейки расписания. Содержит в себе список объектов {@link TimetableDto}
 */
@Getter
@Setter
@NoArgsConstructor
@Log4j2
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

    /**
     * Колонка группы
     */
    private int col;

    /**
     * Контекстное меню
     */
    private TimetableContextMenu contextMenu;

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

    public boolean isEmpty() {
        return timetableDtoList.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimetableListDto that = (TimetableListDto) o;
        return verticalCellIndex == that.verticalCellIndex &&
                Objects.equals(group, that.group) &&
                CollectionUtils.isEqualCollection(timetableDtoList, that.timetableDtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verticalCellIndex, group, timetableDtoList);
    }
}
