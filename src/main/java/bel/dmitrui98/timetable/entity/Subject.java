package bel.dmitrui98.timetable.entity;

import bel.dmitrui98.timetable.util.enums.SubjectTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Дисциплина
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subjectId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubjectTypeEnum subjectType;
}
