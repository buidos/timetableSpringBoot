package bel.dmitrui98.timetable.entity.dictionary;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Смена обучения(первая смена, вторая смена...)
 */
@NoArgsConstructor
@Entity
@Table(indexes =
        {@Index(columnList = "name", unique = true, name = "shift_name_ind_unique")})
public class StudyShift {

    private IntegerProperty studyShiftId = new SimpleIntegerProperty();
    private IntegerProperty value = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();

    public StudyShift(Integer value, String name) {
        this.value.setValue(value);
        this.name.setValue(name);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shift_gen")
    @SequenceGenerator(name="shift_gen", sequenceName = "shift_seq", allocationSize=1)
    public int getStudyShiftId() {
        return studyShiftId.get();
    }

    public IntegerProperty studyShiftIdProperty() {
        return studyShiftId;
    }

    @Column(nullable = false)
    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    @Column(nullable = false)
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setStudyShiftId(int studyShiftId) {
        this.studyShiftId.set(studyShiftId);
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
