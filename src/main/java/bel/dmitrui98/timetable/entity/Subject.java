package bel.dmitrui98.timetable.entity;

import bel.dmitrui98.timetable.util.enums.SubjectTypeEnum;
import javafx.beans.property.*;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Дисциплина
 */
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "subj_name_constr")
})
public class Subject {

    private LongProperty subjectId = new SimpleLongProperty();
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<SubjectTypeEnum> subjectType = new SimpleObjectProperty<>();

    public Subject(String name, SubjectTypeEnum subjectType) {
        this.name.setValue(name);
        this.subjectType.setValue(subjectType);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_gen")
    @SequenceGenerator(name="subject_gen", sequenceName = "subject_seq", allocationSize=1)
    public long getSubjectId() {
        return subjectId.get();
    }

    @Column(nullable = false)
    public String getName() {
        return name.get();
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public SubjectTypeEnum getSubjectType() {
        return subjectType.get();
    }

    public ObjectProperty<SubjectTypeEnum> subjectTypeProperty() {
        return subjectType;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public LongProperty subjectIdProperty() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId.set(subjectId);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSubjectType(SubjectTypeEnum subjectType) {
        this.subjectType.set(subjectType);
    }
}
