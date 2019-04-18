package bel.dmitrui98.timetable.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Учебная нагрузка
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class StudyLoad {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "load_gen")
    @SequenceGenerator(name="load_gen", sequenceName = "load_seq", allocationSize=1)
    private Long studyLoadId;

    /**
     * Количество минут в неделю (при отображении переводится в часы)
     * Количество минут в часе берется из таблицы StudyPair (endTimeFirstHalf - beginTimeFirstHalf)
     */
    @Column(nullable = false)
    private Integer countMinutesInTwoWeek;

    @ManyToOne
    @JoinColumn(name = "subjectId", nullable = false)
    private Subject subject;

    public StudyLoad(Integer countMinutesInTwoWeek, Subject subject) {
        this.countMinutesInTwoWeek = countMinutesInTwoWeek;
        this.subject = subject;
    }
}
