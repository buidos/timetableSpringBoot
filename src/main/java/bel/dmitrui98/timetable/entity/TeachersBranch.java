package bel.dmitrui98.timetable.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * связка учителей, которые будут преподавать в рамках одной нагрузки
 */
@NoArgsConstructor
@Entity
public class TeachersBranch {

    private Long teacherBranchId;
    private StudyLoad studyLoad;
    private Set<StudyGroup> studyGroupSet = new HashSet<>();
    private Set<Teacher> teacherSet = new HashSet<>();

    public TeachersBranch(StudyLoad studyLoad) {
        this.studyLoad = studyLoad;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher_br_gen")
    @SequenceGenerator(name="teacher_br_gen", sequenceName = "teacher_br_seq", allocationSize=1)
    public Long getTeacherBranchId() {
        return teacherBranchId;
    }

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(nullable = false, name = "studyLoadId")
    public StudyLoad getStudyLoad() {
        return studyLoad;
    }

    @ManyToMany(mappedBy = "teachersBranchSet")
    public Set<StudyGroup> getStudyGroupSet() {
        return studyGroupSet;
    }

    @ManyToMany(mappedBy = "teachersBranchSet", fetch = FetchType.EAGER)
    public Set<Teacher> getTeacherSet() {
        return teacherSet;
    }

    public void addGroup(StudyGroup group) {
        if (studyGroupSet.contains(group)) {
            return;
        }
        studyGroupSet.add(group);
        group.addTeacherBranch(this);
    }

    public void removeGroup(StudyGroup group) {
        if (!studyGroupSet.contains(group)) {
            return;
        }
        studyGroupSet.remove(group);
        group.removeTeacherBranch(this);
    }

    public void addTeacher(Teacher teacher) {
        if (teacherSet.contains(teacher)) {
            return;
        }
        teacherSet.add(teacher);
        teacher.addTeachersBranch(this);
    }

    public void removeTeacher(Teacher teacher) {
        if (!teacherSet.contains(teacher)) {
            return;
        }
        teacherSet.remove(teacher);
        teacher.removeTeachersBranch(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeachersBranch that = (TeachersBranch) o;
        return Objects.equals(teacherBranchId, that.teacherBranchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherBranchId);
    }

    public void setTeacherBranchId(Long teacherBranchId) {
        this.teacherBranchId = teacherBranchId;
    }

    public void setStudyLoad(StudyLoad studyLoad) {
        this.studyLoad = studyLoad;
    }

    public void setStudyGroupSet(Set<StudyGroup> studyGroupSet) {
        this.studyGroupSet = studyGroupSet;
    }

    public void setTeacherSet(Set<Teacher> teacherSet) {
        this.teacherSet = teacherSet;
    }
}
