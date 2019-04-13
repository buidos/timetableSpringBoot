package bel.dmitrui98.timetable.entity;

import bel.dmitrui98.timetable.util.enums.StudyShiftEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * Учебная пара (занятие)
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class StudyPair {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pair_gen")
    @SequenceGenerator(name="pair_gen", sequenceName = "pair_seq", allocationSize=1)
    private Integer studyPairId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyShiftEnum studyShift;

    /**
     * Номер пары
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

    public StudyPair(StudyShiftEnum studyShift, Integer pairNumber, LocalTime beginTimeFirstHalf, LocalTime endTimeFirstHalf, LocalTime beginTimeSecondHalf, LocalTime endTimeSecondHalf) {
        this.studyShift = studyShift;
        this.pairNumber = pairNumber;
        this.beginTimeFirstHalf = beginTimeFirstHalf;
        this.endTimeFirstHalf = endTimeFirstHalf;
        this.beginTimeSecondHalf = beginTimeSecondHalf;
        this.endTimeSecondHalf = endTimeSecondHalf;
    }
}
