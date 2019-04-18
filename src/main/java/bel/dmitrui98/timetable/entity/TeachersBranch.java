package bel.dmitrui98.timetable.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * связка учителей, которые будут преподавать в рамках одной нагрузки
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class TeachersBranch {

    @EmbeddedId
    private TeacherBranchPK teacherBranchPK;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(nullable = false, name = "studyLoadId")
    private StudyLoad studyLoad;

    @ManyToOne
    @JoinColumn(nullable = false, name = "studyGroupId")
    private StudyGroup studyGroup;

    public TeachersBranch(Long teacherBranchId, Teacher teacher, StudyLoad studyLoad, StudyGroup studyGroup) {
        teacherBranchPK = new TeacherBranchPK(teacherBranchId, teacher);
        this.studyLoad = studyLoad;
        this.studyGroup = studyGroup;
    }

    public Teacher getTeacher() {
        return teacherBranchPK.getTeacher();
    }

    public Long getTeacherBranchId() {
        return teacherBranchPK.getTeacherBranchId();
    }

    public void setTeacher(Teacher teacher) {
        teacherBranchPK.setTeacher(teacher);
    }

    public void setTeacherBranchId(Long teacherBranchId) {
        teacherBranchPK.setTeacherBranchId(teacherBranchId);
    }

}
