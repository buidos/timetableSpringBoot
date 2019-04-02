package bel.dmitrui98.timetable.util.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidConditions {
    public static final int MAX_STRING_LENGTH = 150;

    private boolean allowEmpty = false;
    private boolean allowDuplicate = true;
    private int maxStringLength = MAX_STRING_LENGTH;

    public ValidConditions(boolean allowEmpty, boolean allowDuplicate) {
        this.allowEmpty = allowEmpty;
        this.allowDuplicate = allowDuplicate;
    }
}
