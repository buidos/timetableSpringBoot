package bel.dmitrui98.timetable.entity.dictionary;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Отделение, к которому принадлежит специальность
 */
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "dep_name_constr")
})
public class Department {

    private IntegerProperty departmentId = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getDepartmentId() {
        return departmentId.get();
    }

    @Column(nullable = false)
    public String getName() {
        return name.get();
    }

    public IntegerProperty departmentIdProperty() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId.set(departmentId);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Department(String name) {
        this.name.setValue(name);
    }
}
