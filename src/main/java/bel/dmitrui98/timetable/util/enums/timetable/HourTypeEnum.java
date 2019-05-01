package bel.dmitrui98.timetable.util.enums.timetable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Тип часа в расписании (две недели, числитель, знаменатель, половина пары...)
 * Порядок следования должен совпадать с контекстным меню {@link bel.dmitrui98.timetable.control.TimetableContextMenu}
 */
@Getter
@AllArgsConstructor
public enum HourTypeEnum {

    /**
     * Две недели
     */
    TWO_WEEKS(2, "две недели", "2 нед"),
    /**
     * Числитель
     */
    NUMERATOR(1, "числитель", "числ"),
    /**
     * Знаменатель
     */
    DENOMINATOR(1, "знаменатель", "знам"),

    /**
     * Две недели первая половина пары
     */
    WEEK_HALF_BEGIN(1, "2 недели полпары", "2 нед 1/2 нач"),
    /**
     * Две недели вторая половина пары
     */
    WEEK_HALF_END(1, "2 недели полпары", "2 нед 1/2 кон"),

    /**
     * Числитель первая половина пары
     */
    NUM_HALF_BEGIN(0.5, "числитель полпары", "числ 1/2 нач"),
    /**
     * Числитель вторая половина пары
     */
    NUM_HALF_END(0.5, "числитель полпары", "числ 1/2 кон"),

    /**
     * Знаменатель первая половина пары
     */
    DEN_HALF_BEGIN(0.5, "знаменатель полпары", "знам 1/2 нач"),
    /**
     * Знаменатель вторая половина пары
     */
    DEN_HALF_END(0.5, "знаменатель полпары", "знам 1/2 кон"),
    ;

    /**
     * Количество часов, которое будет добавляться или вычитаться из нагрузки
     */
    private double hour;
    private String name;
    private String shortName;

    HourTypeEnum(double hour, String name) {
        this.hour = hour;
        this.name = name;
    }
}
