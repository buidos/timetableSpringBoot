package bel.dmitrui98.timetable.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Смена обучения
 */
@Getter
@AllArgsConstructor
public enum StudyShiftEnum {

    FIRST("первая"),
    SECOND("вторая"),
    THIRD("третия"),
    ;

    private String name;
}
