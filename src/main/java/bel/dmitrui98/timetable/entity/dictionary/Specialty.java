package bel.dmitrui98.timetable.entity.dictionary;

import javafx.beans.property.*;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Специальность группы
 */
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "spec_name_constr")
})
public class Specialty {

    private IntegerProperty specialtyId = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<Department> department = new SimpleObjectProperty<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getSpecialtyId() {
        return specialtyId.get();
    }

    @Column(nullable = false)
    public String getName() {
        return name.get();
    }

    @ManyToOne
    @JoinColumn(name = "departmentId")
    public Department getDepartment() {
        return department.get();
    }

    public Specialty(String name, Department department) {
        this.name.setValue(name);
        this.department.setValue(department);
    }

    public void setDepartment(Department department) {
        this.department.set(department);
    }

    public IntegerProperty specialtyIdProperty() {
        return specialtyId;
    }

    public ObjectProperty<Department> departmentProperty() {
        return department;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId.set(specialtyId);
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
