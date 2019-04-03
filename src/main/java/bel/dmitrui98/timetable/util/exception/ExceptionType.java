package bel.dmitrui98.timetable.util.exception;

public enum ExceptionType {
    REC_NOT_SAVED,
    REC_NOT_DELETED,
    /**
     * Запись не удалена, так как есть ссылки из других таблиц
     */
    REC_NOT_DELETED_RELATION,
    REC_NOT_FOUND,
    VALID_LONG_VALUE,
    VALID_EMPTY_VALUE,
    VALID_DUPLICATE_VALUE,
    ;
}
