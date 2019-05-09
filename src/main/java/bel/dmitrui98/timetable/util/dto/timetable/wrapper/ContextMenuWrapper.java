package bel.dmitrui98.timetable.util.dto.timetable.wrapper;

import bel.dmitrui98.timetable.control.TimetableContextMenu;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Сохраняет состояние контекстного меню
 */
@Getter
@Setter
public class ContextMenuWrapper implements Serializable {
    private List<Boolean> isSelectedList = new ArrayList<>();

    public ContextMenuWrapper(TimetableContextMenu contextMenu) {
        for (MenuItem item : contextMenu.getItems()) {
            if (item instanceof SeparatorMenuItem) {
                continue;
            }
            CheckMenuItem checkItem;
            if (item instanceof Menu) {
                Menu menu = (Menu) item;
                for (MenuItem menuItem : menu.getItems()) {
                    checkItem = (CheckMenuItem) menuItem;
                    isSelectedList.add(checkItem.isSelected());
                }
            } else {
                checkItem = (CheckMenuItem) item;
                isSelectedList.add(checkItem.isSelected());
            }
        }
    }
}
