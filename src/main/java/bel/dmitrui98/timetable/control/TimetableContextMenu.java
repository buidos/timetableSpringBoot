package bel.dmitrui98.timetable.control;

import bel.dmitrui98.timetable.service.timetable.TimetableService;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import lombok.Getter;
import lombok.Setter;

/**
 * Контекстное меню ячеейки расписания
 */
@Getter
@Setter
public class TimetableContextMenu extends ContextMenu {

    private TimetableService timetableService;

    public TimetableContextMenu(TimetableService timetableService) {
        this.timetableService = timetableService;

        CheckMenuItem check = new CheckMenuItem("вся неделя");
        this.getItems().add(check);
        check = new CheckMenuItem("числитель");
        this.getItems().add(check);
        check = new CheckMenuItem("знаменатель");
        this.getItems().add(check);

        this.getItems().add(new SeparatorMenuItem());

        Menu parentMenu = new Menu("неделя пол пары");
        parentMenu.getItems().add(new CheckMenuItem("начало"));
        parentMenu.getItems().add(new CheckMenuItem("конец"));
        this.getItems().add(parentMenu);

        parentMenu = new Menu("числитель пол пары");
        parentMenu.getItems().add(new CheckMenuItem("начало"));
        parentMenu.getItems().add(new CheckMenuItem("конец"));
        this.getItems().add(parentMenu);

        parentMenu = new Menu("знаменатель пол пары");
        parentMenu.getItems().add(new CheckMenuItem("начало"));
        parentMenu.getItems().add(new CheckMenuItem("конец"));
        this.getItems().add(parentMenu);
    }

    @Override
    public void show(Node anchor, double screenX, double screenY) {
        disableItems(anchor);
        super.show(anchor, screenX, screenY);
    }

    private void disableItems(Node anchor) {
//        TimetableLabel rasp = (TimetableLabel) anchor;
        LoadLabel selectedLoadLabel = timetableService.getSelectedLoadLabel();
        if (selectedLoadLabel == null) {
            getItems().forEach(item -> item.setDisable(true));
        } else {
            getItems().forEach(item -> item.setDisable(false));
        }
    }
}
