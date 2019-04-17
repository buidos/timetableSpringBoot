package bel.dmitrui98.timetable.util.appssettings;

import bel.dmitrui98.timetable.util.enums.SubjectTypeEnum;

/**
 * Содержит настройки по умолчанию (и текущие) для приложения
 */
public class AppsSettingsHolder {

    /**
     * Значения по умолчанию
     */
    private static final int HOUR_TIME = 90;
    private static final int MAX_COURSE = 4;
    private static final int PAIRS_PER_DAY = 7;
    private static final int MAX_TEACHERS_IN_BRANCH = 3;

    static {
        setSettingsToDefault();
    }


    /**
     * Длительность половины пары в минутах
     */
    private static int hourTime;

    /**
     * Максимальный курс
     */
    private static int maxCourse;

    /**
     * Максимальное количество учителей в связке
     */
    private static int maxTeachersInBranch;

    /**
     * Количество пар в день
     */
    private static int pairsPerDay;

    /**
     * Тип предпочтительных дисциплин в субботу
     */
    private static SubjectTypeEnum subjectType;

    public static int getHourTime() {
        return hourTime;
    }

    public static void setHourTime(int hourTime) {
        AppsSettingsHolder.hourTime = hourTime;
    }

    public static int getMaxCourse() {
        return maxCourse;
    }

    public static void setMaxCourse(int maxCourse) {
        AppsSettingsHolder.maxCourse = maxCourse;
    }

    public static int getPairsPerDay() {
        return pairsPerDay;
    }

    public static void setPairsPerDay(int pairsPerDay) {
        AppsSettingsHolder.pairsPerDay = pairsPerDay;
    }

    public static SubjectTypeEnum getSubjectType() {
        return subjectType;
    }

    public static void setSubjectType(SubjectTypeEnum subjectType) {
        AppsSettingsHolder.subjectType = subjectType;
    }

    public static int getMaxTeachersInBranch() {
        return maxTeachersInBranch;
    }

    public static void setMaxTeachersInBranch(int maxTeachersInBranch) {
        AppsSettingsHolder.maxTeachersInBranch = maxTeachersInBranch;
    }

    public static void setSettingsToDefault() {
        hourTime = HOUR_TIME;
        maxCourse = MAX_COURSE;
        maxTeachersInBranch = MAX_TEACHERS_IN_BRANCH;
        pairsPerDay = PAIRS_PER_DAY;
        subjectType = null;
    }
}
