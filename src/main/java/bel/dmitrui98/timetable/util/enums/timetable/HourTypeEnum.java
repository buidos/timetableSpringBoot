package bel.dmitrui98.timetable.util.enums.timetable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Тип часа в расписании (две недели, числитель, знаменатель, половина пары...)
 */
@Getter
@AllArgsConstructor
public enum HourTypeEnum {

    /**
     * Две недели
     */
    TWO_WEEKS(2),
    /**
     * Числитель
     */
    NUMERATOR(1),
    /**
     * Знаменатель
     */
    DENOMINATOR(1),

    /**
     * Две недели первая половина пары
     */
    WEEK_HALF_BEGIN(1),
    /**
     * Две недели вторая половина пары
     */
    WEEK_HALF_END(1),

    /**
     * Числитель первая половина пары
     */
    NUM_HALF_BEGIN(0.5),
    /**
     * Числитель вторая половина пары
     */
    NUM_HALF_END(0.5),

    /**
     * Знаменатель первая половина пары
     */
    DEN_HALF_BEGIN(0.5),
    /**
     * Знаменатель вторая половина пары
     */
    DEN_HALF_END(0.5),
    ;

    /**
     * Количество часов, которое будет добавляться или вычитаться из нагрузки
     */
    private double hour;
}
