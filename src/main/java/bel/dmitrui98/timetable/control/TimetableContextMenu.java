package bel.dmitrui98.timetable.control;

import bel.dmitrui98.timetable.service.timetable.LoadService;
import bel.dmitrui98.timetable.service.timetable.TimetableService;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Контекстное меню ячеейки расписания
 */
@Component
@Scope("prototype")
public class TimetableContextMenu extends ContextMenu {

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private LoadService loadService;

    private TimetableLabel timetableLabel;

    public TimetableContextMenu() {
        CheckMenuItem check = new CheckMenuItem("две недели");
        check.setOnAction(this::onTwoWeeksClick);
        this.getItems().add(check);

        check = new CheckMenuItem("числитель");
        this.getItems().add(check);
        check.setOnAction(this::onNumClick);

        check = new CheckMenuItem("знаменатель");
        this.getItems().add(check);
        check.setOnAction(this::onDenClick);

        this.getItems().add(new SeparatorMenuItem());

        Menu parentMenu = new Menu("2 недели полпары");
        check = new CheckMenuItem("начало");
        check.setOnAction(this::onWeekHalfBeginClick);
        parentMenu.getItems().add(check);

        check = new CheckMenuItem("конец");
        check.setOnAction(this::onWeekHalfEndClick);
        parentMenu.getItems().add(check);
        this.getItems().add(parentMenu);

        parentMenu = new Menu("числитель полпары");
        check = new CheckMenuItem("начало");
        check.setOnAction(this::onNumHalfBeginClick);
        parentMenu.getItems().add(check);

        check = new CheckMenuItem("конец");
        parentMenu.getItems().add(check);
        check.setOnAction(this::onNumHalfEndClick);
        this.getItems().add(parentMenu);

        parentMenu = new Menu("знаменатель полпары");
        check = new CheckMenuItem("начало");
        check.setOnAction(this::onDenHalfBeginClick);
        parentMenu.getItems().add(check);

        check = new CheckMenuItem("конец");
        check.setOnAction(this::onDenHalfEndClick);
        parentMenu.getItems().add(check);
        this.getItems().add(parentMenu);
    }

    private void onTwoWeeksClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        TimetableContextMenu contextMenu = (TimetableContextMenu) checkMenuItem.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.TWO_WEEKS, isDelete);
    }

    private void onNumClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        TimetableContextMenu contextMenu = (TimetableContextMenu) checkMenuItem.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.NUMERATOR, isDelete);
    }

    private void onDenClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        TimetableContextMenu contextMenu = (TimetableContextMenu) checkMenuItem.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.DENOMINATOR, isDelete);
    }

    private void onWeekHalfBeginClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.WEEK_HALF_BEGIN, isDelete);
    }

    private void onWeekHalfEndClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.WEEK_HALF_END, isDelete);
    }

    private void onNumHalfBeginClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.NUM_HALF_BEGIN, isDelete);
    }

    private void onNumHalfEndClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.NUM_HALF_END, isDelete);
    }

    private void onDenHalfBeginClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.DEN_HALF_BEGIN, isDelete);
    }

    private void onDenHalfEndClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, HourTypeEnum.DEN_HALF_END, isDelete);
    }

    @Override
    public void show(Node anchor, double screenX, double screenY) {
        disableItems(anchor);
        super.show(anchor, screenX, screenY);
    }

    private void disableItems(Node anchor) {
        TimetableLabel cell = (TimetableLabel) anchor;
//        System.out.println("disabling items for " + cell);
        LoadLabel selectedLoadLabel = timetableService.getSelectedLoadLabel();
        if (selectedLoadLabel == null) {
            getItems().forEach(item -> item.setDisable(true));
        } else {
            getItems().forEach(item -> item.setDisable(false));
        }
    }

    public TimetableLabel getTimetableLabel() {
        return timetableLabel;
    }

    public void setTimetableLabel(TimetableLabel timetableLabel) {
        this.timetableLabel = timetableLabel;
    }
}
