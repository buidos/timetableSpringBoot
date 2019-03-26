package bel.dmitrui98.timetable.entity.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Смена обучения(первая смена, вторая смена...)
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class StudyShift {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studyShiftId;

    @Column(nullable = false)
    private Integer value;

    @Column(nullable = false)
    private String name;
}
