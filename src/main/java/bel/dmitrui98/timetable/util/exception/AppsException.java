package bel.dmitrui98.timetable.util.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppsException extends Exception {

    public static final String VALIDATION_ERROR = "Ошибка валидации";

    private ExceptionType exceptionType;
    private String message;

    public AppsException(ExceptionType exceptionType, Exception e) {
        super(exceptionType.name(), e);
        this.exceptionType = exceptionType;
    }

    public AppsException(ExceptionType exceptionType) {
        super(exceptionType.name());
        this.exceptionType = exceptionType;
    }

    public AppsException(ExceptionType exceptionType, String message) {
        super(message);
        this.message = message;
        this.exceptionType = exceptionType;
    }
}
