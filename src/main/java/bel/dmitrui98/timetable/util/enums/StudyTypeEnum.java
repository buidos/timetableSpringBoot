package bel.dmitrui98.timetable.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * тип обучения
 */
@Getter
@AllArgsConstructor
public enum StudyTypeEnum {
    BUDGET("бюджет"),
    PAID("платно"),
    ;
    private String name;
}
