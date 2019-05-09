package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Dto нагрузки. Часы сохраняются в рамках расписания в отдельный xml файл.
 * При составлении расписания из базы часы не отнимаются
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoadDto {
    private TeachersBranch branch;
    private StudyGroup group;

    private Integer countMinutesInTwoWeek;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadDto loadDto = (LoadDto) o;
        return branch.equals(loadDto.branch) &&
                group.equals(loadDto.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branch, group);
    }
}
