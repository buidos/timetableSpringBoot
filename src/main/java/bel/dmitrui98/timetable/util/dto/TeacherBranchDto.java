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

    private Long teacherBranchId;
    /**
     * Связка учителей
     */
    private List<Teacher> teacherBranch;
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
     * Преобразование {@link TeachersBranch} в {@link TeacherBranchDto}. Входной список должен быть отсортирован по id
     */
    public static List<TeacherBranchDto> convert(List<TeachersBranch> teachersBranches) {
        List<TeacherBranchDto> dtoList = new ArrayList<>();
        int size = teachersBranches.size();
        // берем ту связку, где больше всего совпадений
        for (int i = 0; i < size; i++) {
            TeachersBranch tb = teachersBranches.get(i);
            Long teacherBranchId = tb.getTeacherBranchId();
            List<Teacher> teachers = new ArrayList<>();

            TeachersBranch previous = tb;
            while (i < size && tb.getTeacherBranchId().equals(teacherBranchId)) {
                teachers.add(tb.getTeacher());
                previous = tb;

                i++;
                if (i < size) {
                    tb = teachersBranches.get(i);
                }
            }
            i--;

            int hour = previous.getStudyLoad().getCountMinutesInTwoWeek() / (AppsSettingsHolder.getHourTime() * 2);
            dtoList.add(new TeacherBranchDto(previous.getTeacherBranchId(), teachers, previous.getStudyLoad().getSubject(),
                    hour, previous.getStudyGroup()));
        }
        return dtoList;
    }
}
