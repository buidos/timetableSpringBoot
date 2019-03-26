package bel.dmitrui98.timetable.entity.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Форма обучения(очно, заочно)
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class StudyForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studyFormId;

    @Column(nullable = false)
    private String name;
}
