package bel.dmitrui98.timetable.control;

import bel.dmitrui98.timetable.service.timetable.IntersectionService;
import bel.dmitrui98.timetable.service.timetable.LoadService;
import bel.dmitrui98.timetable.service.timetable.TimetableService;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum.*;


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

    @Autowired
    private IntersectionService intersectionService;

    private TimetableLabel timetableLabel;

    public TimetableContextMenu() {
        CheckMenuItem check = new CheckMenuItem(TWO_WEEKS.getName());
        check.setOnAction(this::onTwoWeeksClick);
        this.getItems().add(check);

        check = new CheckMenuItem(NUMERATOR.getName());
        this.getItems().add(check);
        check.setOnAction(this::onNumClick);

        check = new CheckMenuItem(DENOMINATOR.getName());
        this.getItems().add(check);
        check.setOnAction(this::onDenClick);

        this.getItems().add(new SeparatorMenuItem());

        Menu parentMenu = new Menu(WEEK_HALF_BEGIN.getName());
        check = new CheckMenuItem("начало");
        check.setOnAction(this::onWeekHalfBeginClick);
        parentMenu.getItems().add(check);

        check = new CheckMenuItem("конец");
        check.setOnAction(this::onWeekHalfEndClick);
        parentMenu.getItems().add(check);
        this.getItems().add(parentMenu);

        parentMenu = new Menu(NUM_HALF_BEGIN.getName());
        check = new CheckMenuItem("начало");
        check.setOnAction(this::onNumHalfBeginClick);
        parentMenu.getItems().add(check);

        check = new CheckMenuItem("конец");
        parentMenu.getItems().add(check);
        check.setOnAction(this::onNumHalfEndClick);
        this.getItems().add(parentMenu);

        parentMenu = new Menu(DEN_HALF_BEGIN.getName());
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
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, TWO_WEEKS, isDelete);
    }

    private void onNumClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        TimetableContextMenu contextMenu = (TimetableContextMenu) checkMenuItem.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, NUMERATOR, isDelete);
    }

    private void onDenClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        TimetableContextMenu contextMenu = (TimetableContextMenu) checkMenuItem.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, DENOMINATOR, isDelete);
    }

    private void onWeekHalfBeginClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, WEEK_HALF_BEGIN, isDelete);
    }

    private void onWeekHalfEndClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, WEEK_HALF_END, isDelete);
    }

    private void onNumHalfBeginClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, NUM_HALF_BEGIN, isDelete);
    }

    private void onNumHalfEndClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, NUM_HALF_END, isDelete);
    }

    private void onDenHalfBeginClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, DEN_HALF_BEGIN, isDelete);
    }

    private void onDenHalfEndClick(ActionEvent e) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) e.getSource();
        Menu parentMenu = checkMenuItem.getParentMenu();
        TimetableContextMenu contextMenu = (TimetableContextMenu) parentMenu.getParentPopup();
        LoadLabel loadLabel = timetableService.getSelectedLoadLabel();
        boolean isDelete = !checkMenuItem.isSelected();
        loadService.setUpLoadToTimetable(contextMenu.getTimetableLabel(), loadLabel, DEN_HALF_END, isDelete);
    }

    @Override
    public void show(Node anchor, double screenX, double screenY) {
        disableItems(anchor);
        super.show(anchor, screenX, screenY);
    }

    private void disableItems(Node anchor) {
        TimetableLabel cell = (TimetableLabel) anchor;
        LoadLabel selectedLoadLabel = timetableService.getSelectedLoadLabel();

        if (selectedLoadLabel == null || !cell.getTimetableListDto().getGroup().getStudyGroupId().equals(
                selectedLoadLabel.getLoadDto().getGroup().getStudyGroupId())) {
            // если не выделена ячейка нагрузки или выделена из другой колонки, блокируем все контекстное меню
            setDisableAll(true);
        } else {

            // блокируем пункты контекстного меню
            Integer minutes = selectedLoadLabel.getLoadDto().getCountMinutesInTwoWeek();
            HourTypeEnum[] hourTypes = values();
            boolean isDisable;
            CheckMenuItem checkItem;
            for (int i = 0; i < getItems().size() && i < hourTypes.length; i++) {
                MenuItem item = getItems().get(i);
                if (item instanceof SeparatorMenuItem) {
                    continue;
                }
                HourTypeEnum hourType = hourTypes[i];

                // блокируем в зависимости от количества часов в нагрузке
                int minusMinutes = (int) ((AppsSettingsHolder.getHourTime() * 2) * hourType.getHour());
                isDisable = minutes < minusMinutes;

                if (!isDisable) {
                    // блокируем, если в одну и ту же группу в одно и то же время пытаются поставить еще одну связку
                    // (не нужно, так как реализовано через checkMenuItem)
                    isDisable = cell.getTimetableListDto().getTimetableDtoList().stream()
                            .map(TimetableDto::getHourType)
                            .anyMatch(type -> type.equals(hourType));

                    if (!isDisable) {
                        // блокируем если есть пересечение (один и тот же преподаватель не может вести пару в одно и то же время)
                        isDisable = intersectionService.isIntersects(cell.getTimetableListDto(), selectedLoadLabel.getLoadDto(), hourType);
                    }
                }

                setIsDisable(isDisable, item);
            }
        }
    }

    private void setIsDisable(boolean isDisable, MenuItem item) {
        CheckMenuItem checkItem;
        if (item instanceof Menu) {
            Menu menu = (Menu) item;
            for (MenuItem menuItem : menu.getItems()) {
                checkItem = (CheckMenuItem) menuItem;
                if (!checkItem.isSelected()) {
                    menuItem.setDisable(isDisable);
                }
            }
        } else {
            checkItem = (CheckMenuItem) item;
            if (!checkItem.isSelected()) {
                item.setDisable(isDisable);
            }
        }
    }

    private void setDisableAll(boolean isDisable) {
        getItems().forEach(item -> {
            if (!(item instanceof SeparatorMenuItem)) {
                setIsDisable(isDisable, item);
            }
        });
    }

    public TimetableLabel getTimetableLabel() {
        return timetableLabel;
    }

    public void setTimetableLabel(TimetableLabel timetableLabel) {
        this.timetableLabel = timetableLabel;
    }
}
