package bel.dmitrui98.timetable.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long teacherId;

    @Column(nullable = false)
    private String surname;

    private String name;
    private String patronymic;
    private String phone;
    private String email;

    public Teacher(String name, String phone, String email) {
        this.surname = name;
        this.phone = phone;
        this.email = email;
    }
}
