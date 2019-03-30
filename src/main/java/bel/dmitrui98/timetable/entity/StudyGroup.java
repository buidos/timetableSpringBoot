package bel.dmitrui98.timetable.entity;

import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.entity.dictionary.StudyShift;
import bel.dmitrui98.timetable.util.enums.StudyFormEnum;
import bel.dmitrui98.timetable.util.enums.StudyTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * учебная группа
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studyGroupId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer course;

    @ManyToOne
    @JoinColumn(nullable = false, name = "studyShiftId")
    private StudyShift studyShift;

    @ManyToOne
    @JoinColumn(nullable = false, name = "specialtyId")
    private Specialty specialty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyTypeEnum studyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyFormEnum studyForm;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "studyGroup_teachersBranch", joinColumns = {
            @JoinColumn(name = "studyGroupId", nullable = false)},
        inverseJoinColumns = {@JoinColumn(name = "teachersBranchId", nullable = false)}
    )
    private Set<TeachersBranch> teachersBranchSet = new HashSet<>();
}
