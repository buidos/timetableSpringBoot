package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class LoadDto {
    private StudyGroup group;
    private TeachersBranch branch;

    public LoadDto(TeachersBranch tb, StudyGroup group) {
        this.branch = tb;
        this.group = group;
    }
}
