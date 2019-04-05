package bel.dmitrui98.timetable.entity;

import bel.dmitrui98.timetable.entity.dictionary.StudyShift;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * учебная пара (занятие)
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class StudyPair {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pair_gen")
    @SequenceGenerator(name="pair_gen", sequenceName = "pair_seq", allocationSize=1)
    private Long studyPairId;

    /**
     * номер пары
     */
    @Column(nullable = false)
    private Integer pairNumber;

    /**
     * Время начала первой половины пары
     */
    private LocalTime beginTimeFirstHalf;

    /**
     * Время окончания первой половины пары
     */
    private LocalTime endTimeFirstHalf;

    /**
     * Время начала второй половины пары
     */
    private LocalTime beginTimeSecondHalf;

    /**
     * Время окончания второй половины пары
     */
    private LocalTime endTimeSecondHalf;

    @ManyToOne
    @JoinColumn(name = "studyShiftId", nullable = false)
    private StudyShift studyShift;
}
