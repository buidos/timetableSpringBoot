package bel.dmitrui98.timetable.util.validation;

import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.exception.ExceptionType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Валидация входных данных, в случае ошибки выбрасывает исключение с типом ошибки ввода данных
 */
public class AppsValidation {

    /**
     * Проверяет строку по параметрам валидации
     * @param s строка, которую нужно проверить
     * @param conds параметры валидации
     * @param strings список строк, в которых проверяется дубликат, может быть пустым
     * @throws AppsException тип ошибки валидации
     */
    public static void validate(String s, ValidConditions conds, List<String> strings) throws AppsException {
        validate(s, conds, strings, -1);
    }

    public static void validate(String s, ValidConditions conds, List<String> strings, int ignoreIntex) throws AppsException {
        if (s == null || (!conds.isAllowEmpty() && s.isEmpty())) {
            throw new AppsException(ExceptionType.VALID_EMPTY_VALUE);
        }
        if (s.length() > conds.getMaxStringLength()) {
            throw new AppsException(ExceptionType.VALID_LONG_VALUE);
        }
        if (!conds.isAllowDuplicate()) {
            validateDuplicatesThrow(s, strings, ignoreIntex);
        }
    }

    private static void validateDuplicatesThrow(String s, List<String> strings, int ignoreIndex) throws AppsException {
        if (strings != null) {
            // переводим имена в нижний регистр
            List<String> lowerCaseList = strings.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            String lower = s.toLowerCase();
            for (int i = 0; i < lowerCaseList.size(); i++) {
                if (i == ignoreIndex) {
                    continue;
                }
                if (lower.equals(lowerCaseList.get(i))) {
                    throw new AppsException(ExceptionType.VALID_DUPLICATE_VALUE);
                }
            }
        }
    }
}
