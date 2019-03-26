package bel.dmitrui98.timetable.entity;

import bel.dmitrui98.timetable.entity.dictionary.SubjectType;
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

    @ManyToOne
    @JoinColumn(name = "subjectTypeId", nullable = false)
    private SubjectType subjectType;
}
