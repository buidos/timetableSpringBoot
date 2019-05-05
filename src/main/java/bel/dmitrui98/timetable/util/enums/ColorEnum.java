package bel.dmitrui98.timetable.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ColorEnum {
    CELL_BORDER("#BABABA"),
    GREEN("#2CFF5A"),
    YELLOW("#FFFF00"),
    RED("#FF1A22"),
    LOAD_SELECTED("#9D92FF"),
    WHITE("#FFFFFF")
    ;

    private String color;
}
