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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studyLoadId;

    /**
     * Количество минут в неделю (при отображении переводится в часы)
     * Количество минут в часе берется из таблицы StudyPair (endTimeFirstHalf - beginTimeFirstHalf)
     */
    @Column(nullable = false)
    private Integer countMinutesInWeek;

    @ManyToOne
    @JoinColumn(name = "subjectId", nullable = false)
    private Subject subject;
}
