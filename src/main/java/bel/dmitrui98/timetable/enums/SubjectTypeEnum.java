package bel.dmitrui98.timetable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * тип дисциплины
 */
@Getter
@AllArgsConstructor
public enum SubjectTypeEnum {
    HUMANE("гуманитарный"),
    TECHNICAL("технический"),
    ;
    private String name;
}
