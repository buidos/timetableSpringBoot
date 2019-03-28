package bel.dmitrui98.timetable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * форма обучения
 */
@Getter
@AllArgsConstructor
public enum StudyFormEnum {
    INTRAMURAL("очно"),
    IN_ABSENTIA("заочно"),
    ;
    private String name;
}
