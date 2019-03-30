package bel.dmitrui98.timetable.util.validation;

import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.exception.ExceptionType;

/**
 * Валидация входных данных, в случае ошибки выбрасывает исключение с типом ошибки ввода данных
 */
public class AppsValidation {

    public static final int MAX_STRING_LENGTH = 150;

    /**
     * Проверяет входную строку, которая может быть пустой
     */
    public static void validateStringMayBeEmpty(String s) throws AppsException {
        if (s == null) {
            throw new AppsException(ExceptionType.VALID_EMPTY_VALUE);
        }
        boolean isValid = true;
        if (s.length() > MAX_STRING_LENGTH) {
            throw new AppsException(ExceptionType.VALID_LONG_VALUE);
        }
    }

    public static void validateString(String s) throws AppsException {
        validateStringMayBeEmpty(s);
        if (s.isEmpty()) {
            throw new AppsException(ExceptionType.VALID_EMPTY_VALUE);
        }
    }
}
