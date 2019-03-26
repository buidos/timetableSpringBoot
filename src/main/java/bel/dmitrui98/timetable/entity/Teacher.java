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

    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String email;

    public Teacher(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
