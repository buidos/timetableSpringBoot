package bel.dmitrui98.timetable.util.enums.timetable;

/**
 * Тип часа в расписании (две недели, числитель, знаменатель, половина пары...)
 */
public enum HourTypeEnum {

    /**
     * Две недели
     */
    TWO_WEEKS,
    /**
     * Числитель
     */
    NUMERATOR,
    /**
     * Знаменатель
     */
    DENOMINATOR,

    /**
     * Две недели первая половина пары
     */
    WEEK_HALF_BEGIN,
    /**
     * Две недели вторая половина пары
     */
    WEEK_HALF_END,

    /**
     * Числитель первая половина пары
     */
    NUM_HALF_BEGIN,
    /**
     * Числитель вторая половина пары
     */
    NUM_HALF_END,

    /**
     * Знаменатель первая половина пары
     */
    DEN_HALF_BEGIN,
    /**
     * Знаменатель вторая половина пары
     */
    DEN_HALF_END,
}
