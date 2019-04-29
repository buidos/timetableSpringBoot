package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.control.LoadLabel;
import bel.dmitrui98.timetable.control.TimetableContextMenu;
import bel.dmitrui98.timetable.control.TimetableLabel;
import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import bel.dmitrui98.timetable.util.time.TimeUtil;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Вспомогательный сервис для работы с расписанием
 */
@Service
class TimetableUtil {

    private static final double CELL_WIDTH = 56;
    private static final double CELL_WIDTH_CONTENT = 200;
    private static final double CELL_WIDTH_HOUR = 25;
    private static final double CELL_HEIGHT = 50;
    private static final int DAY_ROTATE = -90;
    /**
     * Смещение после вращения поля с днем (вращается относительно центра)
     */
    private static final double SHIFT;
    private static final double FIRST_MARGIN_TOP_DAY;
    public static final double MARGIN_CONTENT;
    static {
        SHIFT = Math.abs(CELL_WIDTH - CELL_HEIGHT * AppsSettingsHolder.getPairsPerDay());
        FIRST_MARGIN_TOP_DAY = CELL_HEIGHT * 3 - 3.5;
        MARGIN_CONTENT = CELL_HEIGHT / 2;
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TeachersBranchRepository teachersBranchRepository;

    @Autowired
    private TimetableService timetableService;

    private LoadLabel selectedLoadLabel;

    HBox getHeaderHBox(List<StudyGroup> groups) {
        HBox headerHBox = new HBox();

        TextField cell = new TextField("Дни");
        cell.setEditable(false);
        cell.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
        cell.setMinSize(CELL_WIDTH, CELL_HEIGHT);
        headerHBox.getChildren().add(cell);

        cell = new TextField("Пары");
        cell.setEditable(false);
        cell.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
        cell.setMinSize(CELL_WIDTH, CELL_HEIGHT);
        headerHBox.getChildren().add(cell);

        // TODO отсортировать по отделениям, специальностям, именам при загрузке из базы
        for (StudyGroup group : groups) {
            cell = new TextField(group.getName());
            cell.setEditable(false);
            cell.setMaxSize(CELL_WIDTH_CONTENT, CELL_HEIGHT);
            cell.setMinSize(CELL_WIDTH_CONTENT, CELL_HEIGHT);
            headerHBox.getChildren().add(cell);
        }

        return headerHBox;
    }

    VBox getDayVBox(List<DayEnum> days) {
        VBox dayVBox = new VBox();

        TextField cell;
        int i = 0;
        double marginTop = FIRST_MARGIN_TOP_DAY;
        for (DayEnum day : days) {
            cell = new TextField(day.getName());
            cell.setEditable(false);
            double height = CELL_WIDTH;
            double width = AppsSettingsHolder.getPairsPerDay() * CELL_HEIGHT;
            cell.setMaxSize(width, height);
            cell.setMinSize(width, height);
            cell.setRotate(DAY_ROTATE);
            cell.setStyle("-fx-alignment: center");
            dayVBox.getChildren().add(cell);
            cell.setTranslateX(cell.getTranslateX() - SHIFT / 2);

            if (i++ == 1) {
                marginTop = SHIFT;
            }
            VBox.setMargin(cell, new Insets(marginTop, 0, 0, 0));
        }
        return dayVBox;
    }

    VBox getPairVBox(int countDays) {
        VBox pairVBox = new VBox();
        TextField cell;
        for (int i = 0; i < countDays; i++) {
            for (int j = 1; j <= AppsSettingsHolder.getPairsPerDay(); j++) {
                cell = new TextField(String.valueOf(j));
                cell.setEditable(false);
                cell.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setMinSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                pairVBox.getChildren().add(cell);
            }
        }
        return pairVBox;
    }

    VBox getContentVBox(List<StudyGroup> groups, List<DayEnum> days, BorderPane borderPane) {
        VBox contentVBox = new VBox();
        Label cell;

        // расписание
        GridPane timetableGridPane = getTimetableGridPane(groups, days);
        contentVBox.getChildren().add(timetableGridPane);

        // нагрузка
        GridPane loadGridPane = getLoadGridPane(groups, borderPane);
        contentVBox.getChildren().add(loadGridPane);

        return contentVBox;
    }

    private GridPane getLoadGridPane(List<StudyGroup> groups, BorderPane borderPane) {
        LoadLabel cell;
        GridPane loadGridPane = new GridPane();
        int groupIndex = 0;
        for (int i = 0; i < groups.size() * 2; i += 2) {
            StudyGroup group = groups.get(groupIndex++);
            List<TeachersBranch> branches = teachersBranchRepository.findByGroupOrderByHour(group);

            // общее количество часов
            LoadLabel commonHourCell = new LoadLabel(CELL_WIDTH_HOUR, CELL_HEIGHT, "0");
            commonHourCell.setHourCell(commonHourCell);
            commonHourCell.setTranslateX(commonHourCell.getTranslateX() - SHIFT);

            LoadLabel commonCell = new LoadLabel(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT, "Количество часов");
            commonCell.setHourCell(commonHourCell);
            commonCell.setTranslateX(commonCell.getTranslateX() - SHIFT);

            int j = 0, sumMinute = 0;
            for (j = 0; j < branches.size(); j++) {
                TeachersBranch tb = branches.get(j);

                // часы
                int minute = tb.getStudyLoad().getCountMinutesInTwoWeek();
                int hour = TimeUtil.convertMinuteToHour(minute);
                sumMinute += minute;
                String text = String.valueOf(hour);

                cell = new LoadLabel(CELL_WIDTH_HOUR, CELL_HEIGHT, tb, group, text);
                LoadLabel hourCell = cell;
                cell.setHourCell(hourCell);
                cell.setCommonHourCell(commonHourCell);
                if (cell.getText().length() > 2) {
                    cell.setTooltip(new Tooltip(cell.getText()));
                }
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                cell.setOnMouseClicked(e -> onLoadClicked(e, borderPane));
                loadGridPane.add(cell, i + 1, j);

                // связка
                text = tb.getTeacherSet().toString() + "\n" + tb.getStudyLoad().getSubject().getName();
                cell = new LoadLabel(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT, tb, group, text);
                cell.setHourCell(hourCell);
                cell.setCommonHourCell(commonHourCell);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                cell.setOnMouseClicked(e -> onLoadClicked(e, borderPane));
                loadGridPane.add(cell, i, j);
            }

            commonHourCell.setText(String.valueOf(TimeUtil.convertMinuteToHour(sumMinute)));
            loadGridPane.add(commonCell, i, j);
            loadGridPane.add(commonHourCell, i + 1, j);
        }
        return loadGridPane;
    }

    private GridPane getTimetableGridPane(List<StudyGroup> groups, List<DayEnum> days) {
        TimetableLabel cell;
        TimetableContextMenu contextMenu;
        GridPane timetableGridPane = new GridPane();
        for (int i = 0; i < groups.size(); i++) {
            StudyGroup group = groups.get(i);
            int dayIndex = -1;
            int pairIndex;
            int verticalCellIndex;
            for (int j = 0; j < days.size() * AppsSettingsHolder.getPairsPerDay(); j++) {

                if (j % AppsSettingsHolder.getPairsPerDay() == 0) {
                    dayIndex++;
                }
                pairIndex = j % AppsSettingsHolder.getPairsPerDay();
                verticalCellIndex = (days.get(dayIndex).ordinal() * AppsSettingsHolder.getPairsPerDay()) + pairIndex;

                cell = new TimetableLabel(CELL_WIDTH_CONTENT, CELL_HEIGHT, verticalCellIndex, group);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);

                // контекстное меню
                contextMenu = applicationContext.getBean(TimetableContextMenu.class);
                contextMenu.setTimetableLabel(cell);
                cell.setOnContextMenuRequested(new TimetableContextMenuEvent(contextMenu));

                timetableGridPane.add(cell, i, j);
            }
        }
        return timetableGridPane;
    }

    void createInfoPanel(BorderPane borderPane) {
        VBox currentLoad = new VBox();
        VBox currentCellInfo = new VBox();
        VBox infoPanel = new VBox(new Label("Статус:"), currentLoad, currentCellInfo);
        infoPanel.setMinWidth(100);
        borderPane.setRight(infoPanel);
    }

    private void refreshInfoPanel(Node node) {
        if (node == null) {
            return;
        }
        VBox infoPanel = (VBox) node;
//        if (selectedBranch == null || selectedGroup == null) {
//            currentLoad.getChildren().add(new Label("Нагрузка не выбрана"));
//        } else {
//            currentLoad.getChildren().add(new Label(selectedBranch.getTeacherSet().toString()));
//        }
//
//        if (selectedCell == null) {
//            currentCellInfo.getChildren().add(new Label("Ячейка расписания не выбрана"));
//        }
    }

    @AllArgsConstructor
    private class TimetableContextMenuEvent implements EventHandler<ContextMenuEvent> {

        private TimetableContextMenu contextMenu;

        @Override
        public void handle(ContextMenuEvent e) {
            Node source = (Node) e.getSource();
            contextMenu.show(source, e.getScreenX(), e.getScreenY());
        }
    }

    private void onLoadClicked(MouseEvent e, BorderPane borderPane) {
        selectedLoadLabel = (LoadLabel) e.getSource();
        refreshInfoPanel(borderPane.getRight());
    }

    LoadLabel getSelectedLoadLabel() {
        return selectedLoadLabel;
    }
}
