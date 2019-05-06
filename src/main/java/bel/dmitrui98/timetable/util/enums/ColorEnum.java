package bel.dmitrui98.timetable.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ColorEnum {
    CELL_BORDER("#BABABA"),
    GREEN("#93FF85"),
    YELLOW("#FFFF00"),
    RED("#FF5C59"),
    LOAD_SELECTED("#9D92FF"),
    WHITE("#FFFFFF")
    ;

    private String color;
}
