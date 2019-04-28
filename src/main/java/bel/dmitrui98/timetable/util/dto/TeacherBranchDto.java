package bel.dmitrui98.timetable.util.dto;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.Subject;
import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.util.time.TimeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto, используется в нагрузке для отображение учителей, предмета и их нагрузки
 * Если есть дополнительная половина пары в нагрузке, то добавляется целый час
 */
@Getter
@Service
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
     * Добавлена ли половина пары к нагрузке
     */
    private boolean isHalfPair;

    /**
     * Группа
     */
    private StudyGroup studyGroup;

    public TeacherBranchDto(TeachersBranch teachersBranch, List<Teacher> teachers, Subject subject, Integer hour, boolean isHalfPair, StudyGroup studyGroup) {
        this.teachersBranch = teachersBranch;
        this.teachers = teachers;
        this.subject = subject;
        this.hour = hour;
        this.isHalfPair = isHalfPair;
        this.studyGroup = studyGroup;
    }

    /**
     * Преобразование {@link TeachersBranch} в {@link TeacherBranchDto}
     */
    public static List<TeacherBranchDto> convert(List<TeachersBranch> teachersBranches, StudyGroup group) {
        List<TeacherBranchDto> dtoList = new ArrayList<>();
        for (TeachersBranch tb : teachersBranches) {
            int minutes = tb.getStudyLoad().getCountMinutesInTwoWeek();
            int hour = TimeUtil.convertMinuteToHour(minutes);
            dtoList.add(new TeacherBranchDto(tb, new ArrayList<>(tb.getTeacherSet()), tb.getStudyLoad().getSubject(),
                    hour, TimeUtil.isRemainder(minutes), group));
        }
        return dtoList;
    }
}
