package bel.dmitrui98.timetable.entity;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class Teacher {

    private LongProperty teacherId = new SimpleLongProperty();
    private StringProperty surname = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty patronymic = new SimpleStringProperty();
    private StringProperty telephone = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();

    public Teacher (String surname, String name, String patronymic, String telephone, String email) {
        this.surname.setValue(surname);
        this.name.setValue(name);
        this.patronymic.setValue(patronymic);
        this.telephone.setValue(telephone);
        this.email.setValue(email);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher_gen")
    @SequenceGenerator(name="teacher_gen", sequenceName = "teacher_seq", allocationSize=1)
    public long getTeacherId() {
        return teacherId.get();
    }

    public LongProperty teacherIdProperty() {
        return teacherId;
    }

    @Column(nullable = false)
    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getPatronymic() {
        return patronymic.get();
    }

    public StringProperty patronymicProperty() {
        return patronymic;
    }

    public String getTelephone() {
        return telephone.get();
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId.set(teacherId);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPatronymic(String patronymic) {
        this.patronymic.set(patronymic);
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    @Override
    public String toString() {
        // отображаем Иванов Иван в виде И.Иванов
        String name, surname;
        if (getName().isEmpty()) {
            name = getName();
        } else {
            name = getName().substring(0, 1).toUpperCase() + ".";
        }
        if (getSurname().isEmpty()) {
            surname = getSurname();
        } else {
            String firstSurnameLetter = getSurname().substring(0, 1).toUpperCase();
            surname = firstSurnameLetter + getSurname().substring(1);
        }
        return name + surname;
    }
}
