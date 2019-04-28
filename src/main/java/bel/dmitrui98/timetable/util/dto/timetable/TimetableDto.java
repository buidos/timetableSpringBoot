package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class TimetableDto {
    private StudyGroup group;

    public TimetableDto(StudyGroup group) {
        this.group = group;
    }
}
