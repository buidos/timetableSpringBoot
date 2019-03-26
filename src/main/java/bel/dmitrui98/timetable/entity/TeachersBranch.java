package bel.dmitrui98.timetable.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * связка учителей, которые будут преподавать в рамках одной нагрузки
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"teacherId", "studyLoadId"}, name = "t_br_teacher_load_constr")
})
public class TeachersBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long teacherBranchId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "teacherId")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(nullable = false, name = "studyLoadId")
    private StudyLoad studyLoad;

    @ManyToMany(mappedBy = "teachersBranchSet")
    private Set<StudyGroup> studyGroupSet = new HashSet<>();
}
