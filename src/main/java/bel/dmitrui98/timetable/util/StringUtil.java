package bel.dmitrui98.timetable.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringUtil {

    /**
     * Делает первый символ строки заглавным
     */
    public static String upperFirst(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        s = s.substring(0, 1).toUpperCase() + s.substring(1);
        return s;
    }

    public static StringProperty upperFirstProperty(String s) {
        s = upperFirst(s);
        if (s == null) {
            return null;
        }
        return new SimpleStringProperty(s);
    }
}
