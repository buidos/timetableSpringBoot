package bel.dmitrui98.timetable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * коды настроек, соответствуют кодам настроек в базе
 */
@Getter
@AllArgsConstructor
public enum SettingCodeEnum {

    /**
     * учебный час
     */
    HOUR_TIME(10),
    /**
     * максимальный курс
     */
    MAX_COURSE(20),
    /**
     * количество пар в день
     */
    PAIRS_PER_DAY(30),
    /**
     * предпочтительный тип дисциплины в субботу
     */
    SATURDAY_SUBJECT_ID(40)
    ;

    private Integer code;
}
