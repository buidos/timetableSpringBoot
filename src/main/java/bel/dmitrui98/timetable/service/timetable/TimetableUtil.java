package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.control.LoadLabel;
import bel.dmitrui98.timetable.control.TimetableContextMenu;
import bel.dmitrui98.timetable.control.TimetableLabel;
import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.color.ColorUtil;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableListDto;
import bel.dmitrui98.timetable.util.enums.ColorEnum;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import bel.dmitrui98.timetable.util.gridpane.GridPaneUtil;
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
import lombok.Getter;
import lombok.Setter;
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
    private ColorService colorService;

    private LoadLabel selectedLoadLabel;
    private TimetableLabel selectedTimetableLabel;
    private GridPane timetableGrid;
    private GridPane loadGrid;

    HBox getHeaderHBox(List<StudyGroup> groups) {
        HBox headerHBox = new HBox();
        String c = ColorEnum.CELL_BORDER.getColor();

        TextField cell = new TextField("Дни");
        cell.setEditable(false);
        cell.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
        cell.setMinSize(CELL_WIDTH, CELL_HEIGHT);
        headerHBox.getChildren().add(cell);
        cell.setStyle(String.format("-fx-border-style: none none none solid;" +
                " -fx-border-color: %s %s black %s;", c, c, c));

        cell = new TextField("Пары");
        cell.setEditable(false);
        cell.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
        cell.setMinSize(CELL_WIDTH, CELL_HEIGHT);
        headerHBox.getChildren().add(cell);
        cell.setStyle(String.format("-fx-border-style: none none none solid;" +
                " -fx-border-color: %s %s black %s;", c, c, c));

        // TODO отсортировать по отделениям, специальностям, именам при загрузке из базы
        for (StudyGroup group : groups) {
            cell = new TextField(group.getName());
            cell.setEditable(false);
            cell.setMaxSize(CELL_WIDTH_CONTENT, CELL_HEIGHT);
            cell.setMinSize(CELL_WIDTH_CONTENT, CELL_HEIGHT);
            headerHBox.getChildren().add(cell);
            cell.setStyle(String.format("-fx-border-style: none none none solid;" +
                    " -fx-border-color: %s %s black %s;", c, c, c));
        }

        return headerHBox;
    }

    VBox getDayVBox(List<DayEnum> days) {
        VBox dayVBox = new VBox();

        TextField cell;
        int i = 0;
        double marginTop = FIRST_MARGIN_TOP_DAY;
        String c = ColorEnum.CELL_BORDER.getColor();
        for (DayEnum day : days) {
            cell = new TextField(day.getName());
            cell.setEditable(false);
            double height = CELL_WIDTH;
            double width = AppsSettingsHolder.getPairsPerDay() * CELL_HEIGHT;
            cell.setMaxSize(width, height);
            cell.setMinSize(width, height);
            cell.setRotate(DAY_ROTATE);
            cell.setStyle("-fx-alignment: center; " + String.format("-fx-border-style: none none none solid;" +
                    " -fx-border-color: %s %s %s black;", c, c, c));
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
        TextField cell = null;
        String c = ColorEnum.CELL_BORDER.getColor();
        for (int i = 0; i < countDays; i++) {
            for (int j = 1; j <= AppsSettingsHolder.getPairsPerDay(); j++) {
                cell = new TextField(String.valueOf(j));
                cell.setEditable(false);
                cell.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setMinSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                pairVBox.getChildren().add(cell);
            }
            if (cell != null) {
                cell.setStyle(String.format("-fx-border-style: none none none solid;" +
                        " -fx-border-color: %s %s black %s;", c, c, c));
            }
        }
        return pairVBox;
    }

    VBox getContentVBox(List<StudyGroup> groups, List<DayEnum> days, BorderPane borderPane) {
        VBox contentVBox = new VBox();
        Label cell;

        // расписание
        GridPane timetableGridPane = getTimetableGridPane(groups, days, borderPane);
        this.timetableGrid = timetableGridPane;
        contentVBox.getChildren().add(timetableGridPane);

        // нагрузка
        GridPane loadGridPane = getLoadGridPane(groups, borderPane);
        contentVBox.getChildren().add(loadGridPane);

        return contentVBox;
    }

    private GridPane getLoadGridPane(List<StudyGroup> groups, BorderPane borderPane) {
        LoadLabel cell;
        GridPane loadGridPane = new GridPane();
        this.loadGrid = loadGridPane;
        int groupIndex = 0;
        for (int i = 0; i < groups.size() * 2; i += 2) {
            StudyGroup group = groups.get(groupIndex);
            List<TeachersBranch> branches = teachersBranchRepository.findByGroupOrderByHour(group);

            // общее количество часов
            LoadLabel commonHourCell = new LoadLabel(CELL_WIDTH_HOUR, CELL_HEIGHT, "0");
            commonHourCell.setHourCell(commonHourCell);
            commonHourCell.setTranslateX(commonHourCell.getTranslateX() - SHIFT);

            LoadLabel commonCell = new LoadLabel(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT, "Количество часов");
            commonCell.setHourCell(commonHourCell);
            commonCell.setTranslateX(commonCell.getTranslateX() - SHIFT);

            int j, sumMinute = 0;
            for (j = 0; j < branches.size(); j++) {
                TeachersBranch tb = branches.get(j);

                // часы
                int minute = tb.getStudyLoad().getCountMinutesInTwoWeek();
                int hour = TimeUtil.convertMinuteToHour(minute);
                sumMinute += minute;
                String text = String.valueOf(hour);

                cell = new LoadLabel(CELL_WIDTH_HOUR, CELL_HEIGHT, tb, group, text);
                cell.setHourLabel(true);
                cell.setCol(groupIndex);
                cell.setRowIndex(j);
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
                cell.setCol(groupIndex);
                cell.setRowIndex(j);
                cell.setHourCell(hourCell);
                cell.setCommonHourCell(commonHourCell);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                cell.setOnMouseClicked(e -> onLoadClicked(e, borderPane));
                loadGridPane.add(cell, i, j);
            }

            commonHourCell.setText(String.valueOf(TimeUtil.convertMinuteToHour(sumMinute)));
            commonHourCell.setCommonMinutes(sumMinute);
            loadGridPane.add(commonCell, i, j);
            loadGridPane.add(commonHourCell, i + 1, j);
            groupIndex++;
        }
        return loadGridPane;
    }

    private GridPane getTimetableGridPane(List<StudyGroup> groups, List<DayEnum> days, BorderPane borderPane) {
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
                cell.getTimetableListDto().setCol(i);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);

                // нижнее подчеркивание
                if (pairIndex == (AppsSettingsHolder.getPairsPerDay() -1)) {
                    String c = ColorEnum.CELL_BORDER.getColor();
                    cell.setStyle(cell.getStyle() + String.format("; -fx-border-color: %s %s black %s;", c, c, c));
                }

                // контекстное меню
                contextMenu = applicationContext.getBean(TimetableContextMenu.class);
                cell.getTimetableListDto().setContextMenu(contextMenu);
                contextMenu.setTimetableLabel(cell);
                cell.setOnContextMenuRequested(new TimetableContextMenuEvent(contextMenu));
                cell.setOnMouseClicked(e -> onTimetableClicked(e, borderPane));

                timetableGridPane.add(cell, i, j);
            }
        }
        return timetableGridPane;
    }

    /**
     * Заполняет расписание данными. Индексы групп совпадают с колонками, строки с verticalCellIndex
     * @param timetableList - данные расписания
     */
    public void fillTimetable(List<TimetableListDto> timetableList) {

        // обновляем грид расписания
        for (int i = 0; i < timetableGrid.getChildren().size(); i++) {
            TimetableLabel cell = (TimetableLabel) timetableGrid.getChildren().get(i);
            for (TimetableListDto listDto : timetableList) {

                // находим по группе и по verticalCellIndex(день и пара)
                if (cell.getTimetableListDto().getGroup().getStudyGroupId().equals(listDto.getGroup().getStudyGroupId()) &&
                        cell.getVerticalCellIndex() == listDto.getVerticalCellIndex()) {

                    cell.setTimetableListDto(listDto);
                    cell.setVerticalCellIndex(listDto.getVerticalCellIndex());
                    TimetableContextMenuEvent event = (TimetableContextMenuEvent) cell.getOnContextMenuRequested();
                    event.setContextMenu(listDto.getContextMenu());
                    listDto.getContextMenu().setTimetableLabel(cell);
                    cell.refresh();
                    break;
                }
            }
        }

        // обновляем грид нагрузки
        for (TimetableListDto listDto : timetableList) {
            StudyGroup group = listDto.getGroup();
            for (TimetableDto dto : listDto.getTimetableDtoList()) {
                for (Node loadNode : loadGrid.getChildren()) {
                    LoadLabel loadCell = (LoadLabel) loadNode;

                    // если нет связки в ячейке
                    if (loadCell.getLoadDto() == null) {
                        continue;
                    }

                    // находим по связке и по группе
                    if (loadCell.getLoadDto().getBranch().getTeacherBranchId().equals(
                            dto.getBranch().getTeacherBranchId()) &&
                            loadCell.getLoadDto().getGroup().getStudyGroupId().equals(group.getStudyGroupId())) {

                        // отнимаем нагрузку в зависимости от типа часа
                        if (loadCell.isHourLabel()) {
                            minusMinutes(loadCell, dto.getHourType());
                        }
                        dto.setLoadCell(loadCell);
                    }
                }
            }
        }
    }

    private void minusMinutes(LoadLabel loadCell, HourTypeEnum hourType) {
        int minutesInTwoWeek = (int) (AppsSettingsHolder.getHourTime() *  2 * hourType.getHour());
        int currentMinutes = loadCell.getLoadDto().getCountMinutesInTwoWeek();
        int minutes = currentMinutes - minutesInTwoWeek;
        loadCell.getLoadDto().setCountMinutesInTwoWeek(minutes);
        loadCell.refresh();
        loadCell.refreshCommonHourCell(minutesInTwoWeek, false);
    }

    void createInfoPanel(BorderPane borderPane) {
        VBox currentLoadInfo = new VBox();
        currentLoadInfo.setMinHeight(150);
        currentLoadInfo.setSpacing(10);
        currentLoadInfo.setStyle(String.format("-fx-border: solid; -fx-border-width: 2;" +
                " -fx-border-color: %s;", ColorEnum.CELL_BORDER.getColor()));

        VBox currentCellInfo = new VBox();
        currentCellInfo.setSpacing(10);
        currentCellInfo.setStyle("-fx-border-style: solid none none none; -fx-border-width: 2; -fx-border-color: black;");

        HBox statusHBox = new HBox(new Label("Статус:"));
        statusHBox.setStyle(String.format("-fx-border-style: solid none none none; -fx-border-width: 2;" +
                " -fx-border-color: %s;", ColorEnum.CELL_BORDER.getColor()));

        VBox infoPanel = new VBox(statusHBox, currentLoadInfo, currentCellInfo);
        infoPanel.setMinWidth(60);
        borderPane.setRight(infoPanel);
        refreshInfoPanel(infoPanel);
    }

    /**
     * Обновляет инфо-панель (подробная информация о текущей ячейке расписания и текущей ячейки нагрузки)
     * @param node инфо-панель
     */
    public void refreshInfoPanel(Node node) {
        if (node == null) {
            return;
        }
        VBox infoPanel = (VBox) node;
        VBox currentLoadInfo = (VBox) infoPanel.getChildren().get(1);
        VBox currentCellInfo = (VBox) infoPanel.getChildren().get(2);

        currentLoadInfo.getChildren().clear();
        currentCellInfo.getChildren().clear();

        if (selectedLoadLabel == null) {
            currentLoadInfo.getChildren().add(new Label("Нагрузка не выбрана"));
        } else {
            currentLoadInfo.getChildren().add(new Label("Выбранная нагрузка:"));

            // группа
            String group = "Группа: " + selectedLoadLabel.getLoadDto().getGroup().getName();
            currentLoadInfo.getChildren().add(new Label(group));

            // преподаватели
            String teachers = selectedLoadLabel.getLoadDto().getBranch().getTeacherSet().toString();
            VBox teacherVBox = new VBox(new Label("Преподаватели:"), new Label(teachers));
            currentLoadInfo.getChildren().add(teacherVBox);

            // часы
            int minutes = selectedLoadLabel.getLoadDto().getCountMinutesInTwoWeek();
            int hours = TimeUtil.convertMinuteToHourWithoutHalf(minutes);
            String sHours = "Часы: " + hours;
            if (TimeUtil.isRemainder(minutes)) {
                sHours += " + полпары";
            }
            currentLoadInfo.getChildren().add(new Label(sHours));

            // дисциплина
            String subject = "Дисциплина: " + selectedLoadLabel.getLoadDto().getBranch().getStudyLoad().getSubject().getName();
            currentLoadInfo.getChildren().add(new Label(subject));
        }

        if (selectedTimetableLabel == null) {
            currentCellInfo.getChildren().add(new Label("Ячейка расписания не выбрана"));
        } else {
            currentCellInfo.getChildren().add(new Label("Выбранная ячейка расписания:"));

            String group = "Группа: " + selectedTimetableLabel.getTimetableListDto().getGroup().getName();
            currentCellInfo.getChildren().add(new Label(group));

            String day = "День: " + TimeUtil.defineDay(selectedTimetableLabel.getTimetableListDto().getVerticalCellIndex()).getName();
            currentCellInfo.getChildren().add(new Label(day));

            String pair = "Пара: " + TimeUtil.definePair(selectedTimetableLabel.getTimetableListDto().getVerticalCellIndex());
            currentCellInfo.getChildren().add(new Label(pair));

            int i = 1;
            for (TimetableDto dto : selectedTimetableLabel.getTimetableListDto().getTimetableDtoList()) {
                VBox content = new VBox();
                content.setSpacing(5);
                // преподаватели
                String teachers = dto.getBranch().getTeacherSet().toString();
                VBox teacherVBox = new VBox(new Label(i + ". Преподаватели:"), new Label(teachers));
                content.getChildren().add(teacherVBox);

                // дисциплина
                String subject = "Дисциплина: " + dto.getBranch().getStudyLoad().getSubject().getName();
                content.getChildren().add(new Label(subject));

                // тип часа
                content.getChildren().add(new Label(dto.getHourType().getName()));

                content.setStyle(String.format("-fx-border-style: solid none none none; -fx-border-width: 2;" +
                        " -fx-border-color: %s;", ColorEnum.CELL_BORDER.getColor()));

                currentCellInfo.getChildren().add(content);
                i++;
            }
        }
    }

    GridPane getTimetableGrid() {
        return timetableGrid;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class TimetableContextMenuEvent implements EventHandler<ContextMenuEvent> {

        private TimetableContextMenu contextMenu;

        @Override
        public void handle(ContextMenuEvent e) {
            Node source = (Node) e.getSource();
            contextMenu.show(source, e.getScreenX(), e.getScreenY());
        }
    }

    private void onLoadClicked(MouseEvent e, BorderPane borderPane) {
        int previousCol = -1;
        if (selectedLoadLabel != null) {
            ColorUtil.setBackgroundColor(selectedLoadLabel, ColorEnum.WHITE.getColor());

            if (selectedLoadLabel.isHourLabel()) {
                // снимаем выделение со связки
                int columnIndex = GridPane.getColumnIndex(selectedLoadLabel) - 1;
                Node node = GridPaneUtil.getCell(loadGrid, GridPane.getRowIndex(selectedLoadLabel), columnIndex);
                if (node != null) {
                    ColorUtil.setBackgroundColor(node,  ColorEnum.WHITE.getColor());
                }
            } else {
                // снимаем выделение с часа
                ColorUtil.setBackgroundColor(selectedLoadLabel.getHourCell(),  ColorEnum.WHITE.getColor());
            }

            previousCol = selectedLoadLabel.getCol();
        }
        selectedLoadLabel = (LoadLabel) e.getSource();
        ColorUtil.setBackgroundColor(selectedLoadLabel, ColorEnum.LOAD_SELECTED.getColor());

        if (selectedLoadLabel.isHourLabel()) {
            // если нажали на час, выделяем связку
            int columnIndex = GridPane.getColumnIndex(selectedLoadLabel) - 1;
            Node node = GridPaneUtil.getCell(loadGrid, GridPane.getRowIndex(selectedLoadLabel), columnIndex);
            if (node != null) {
                ColorUtil.setBackgroundColor(node, ColorEnum.LOAD_SELECTED.getColor());
            }
        } else {
            // если нажали на связку, выделяем час
            ColorUtil.setBackgroundColor(selectedLoadLabel.getHourCell(), ColorEnum.LOAD_SELECTED.getColor());
        }

        colorService.paintTimetableColumn(selectedLoadLabel.getCol(), previousCol, selectedLoadLabel);

        refreshInfoPanel(borderPane.getRight());
    }

    private void onTimetableClicked(MouseEvent e, BorderPane borderPane) {
        selectedTimetableLabel = (TimetableLabel) e.getSource();
        refreshInfoPanel(borderPane.getRight());
    }

    LoadLabel getSelectedLoadLabel() {
        return selectedLoadLabel;
    }

    public void clearSelection() {
        selectedLoadLabel = null;
        selectedTimetableLabel = null;
    }
}
