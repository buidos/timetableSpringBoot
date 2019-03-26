package bel.dmitrui98.timetable.entity.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Тип обучения(бюджет, платно)
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class StudyType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studyTypeId;

    @Column(nullable = false)
    private String name;
}
