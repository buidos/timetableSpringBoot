package bel.dmitrui98.timetable.entity;

import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.util.enums.StudyFormEnum;
import bel.dmitrui98.timetable.util.enums.StudyShiftEnum;
import bel.dmitrui98.timetable.util.enums.StudyTypeEnum;
import javafx.beans.property.*;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * учебная группа
 */
@NoArgsConstructor
@Entity
public class StudyGroup {

    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty course = new SimpleIntegerProperty();
    private ObjectProperty<StudyShiftEnum> studyShift = new SimpleObjectProperty<>();
    private ObjectProperty<StudyTypeEnum> studyType = new SimpleObjectProperty<>();
    private ObjectProperty<StudyFormEnum> studyForm = new SimpleObjectProperty<>();
    private ObjectProperty<Specialty> specialty = new SimpleObjectProperty<>();

    public StudyGroup(String name, Integer course, StudyShiftEnum studyShift, StudyTypeEnum studyType,
                      StudyFormEnum studyForm, Specialty specialty) {
        this.name.setValue(name);
        this.course.setValue(course);
        this.studyShift.setValue(studyShift);
        this.studyType.setValue(studyType);
        this.studyForm.setValue(studyForm);
        this.specialty.setValue(specialty);
    }

    private Long studyGroupId;

    private Set<TeachersBranch> teachersBranchSet = new HashSet<>();

    @Column(nullable = false)
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Column(nullable = false)
    public int getCourse() {
        return course.get();
    }

    public IntegerProperty courseProperty() {
        return course;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public StudyShiftEnum getStudyShift() {
        return studyShift.get();
    }

    public ObjectProperty<StudyShiftEnum> studyShiftProperty() {
        return studyShift;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public StudyTypeEnum getStudyType() {
        return studyType.get();
    }

    public ObjectProperty<StudyTypeEnum> studyTypeProperty() {
        return studyType;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public StudyFormEnum getStudyForm() {
        return studyForm.get();
    }

    public ObjectProperty<StudyFormEnum> studyFormProperty() {
        return studyForm;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "specialtyId")
    public Specialty getSpecialty() {
        return specialty.get();
    }

    public ObjectProperty<Specialty> specialtyProperty() {
        return specialty;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_gen")
    @SequenceGenerator(name="group_gen", sequenceName = "group_seq", allocationSize=1)
    public Long getStudyGroupId() {
        return studyGroupId;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setCourse(int course) {
        this.course.set(course);
    }

    public void setStudyShift(StudyShiftEnum studyShift) {
        this.studyShift.set(studyShift);
    }

    public void setStudyType(StudyTypeEnum studyType) {
        this.studyType.set(studyType);
    }

    public void setStudyForm(StudyFormEnum studyForm) {
        this.studyForm.set(studyForm);
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty.set(specialty);
    }

    public void setStudyGroupId(Long studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public void setTeachersBranchSet(Set<TeachersBranch> teachersBranchSet) {
        this.teachersBranchSet = teachersBranchSet;
    }
}
