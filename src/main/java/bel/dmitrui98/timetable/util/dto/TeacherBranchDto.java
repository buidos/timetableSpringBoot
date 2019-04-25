package bel.dmitrui98.timetable.util.dto;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.Subject;
import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class TeacherBranchDto {

    private TeachersBranch teachersBranch;
    /**
     * Связка учителей
     */
    private List<Teacher> teachers;
    /**
     * Дисциплина
     */
    private Subject subject;
    /**
     * Час в две недели
     */
    private Integer hour;

    /**
     * Группа
     */
    private StudyGroup studyGroup;

    /**
     * Преобразование {@link TeachersBranch} в {@link TeacherBranchDto}
     */
    public static List<TeacherBranchDto> convert(List<TeachersBranch> teachersBranches, StudyGroup group) {
        List<TeacherBranchDto> dtoList = new ArrayList<>();
        for (TeachersBranch tb : teachersBranches) {
            int hour = tb.getStudyLoad().getCountMinutesInTwoWeek() / (AppsSettingsHolder.getHourTime() * 2);
            dtoList.add(new TeacherBranchDto(tb, new ArrayList<>(tb.getTeacherSet()), tb.getStudyLoad().getSubject(),
                    hour, group));
        }
        return dtoList;
    }
}
