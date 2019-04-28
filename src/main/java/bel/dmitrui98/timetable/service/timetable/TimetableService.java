package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {

    private static final double CELL_WIDTH = 100;
    private static final double CELL_WIDTH_CONTENT = 200;
    private static final double CELL_WIDTH_HOUR = 25;
    private static final double CELL_HEIGHT = 50;
    private static final int DAY_ROTATE = -90;
    /**
     * Смещение после вращения поля с днем (вращается относительно центра)
     */
    private static final double SHIFT;
    private static final double FIRST_MARGIN_TOP_DAY;
    private static final double MARGIN_CONTENT;
    static {
        SHIFT = (CELL_WIDTH * 2) + CELL_HEIGHT;
        FIRST_MARGIN_TOP_DAY = SHIFT - CELL_WIDTH - (CELL_HEIGHT /2);
        MARGIN_CONTENT = CELL_HEIGHT / 2;
    }

    @Autowired
    private TeachersBranchRepository teachersBranchRepository;

    public void showTable(List<StudyGroup> groups, List<DayEnum> days, BorderPane borderPane) {
        // состоит из двух HBox: header и content
        VBox rootVBox = new VBox();
        ScrollPane rootScrollPane = new ScrollPane(rootVBox);
        rootScrollPane.setFitToHeight(true);
        rootScrollPane.setFitToWidth(true);
        // ускоряем скролл
        rootVBox.setOnScroll(e -> {
            double deltaH = e.getDeltaX() * 6;
            double width = rootScrollPane.getContent().getBoundsInLocal().getWidth();
            double hvalue = rootScrollPane.getHvalue();
            rootScrollPane.setHvalue(hvalue + (deltaH / width));
        });
        HBox headerHBox = getHeaderHBox(groups);

        // состоит из VBox(дни, пары, content(состоит из GridPane (расписание, нагрузка)))
        HBox contentHBox = new HBox();
        contentHBox.setAlignment(Pos.TOP_LEFT);
        ScrollPane contentScrollPane = new ScrollPane(contentHBox);

        contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setFitToHeight(true);
        // ускоряем скролл
        contentHBox.setOnScroll(e -> {
            double deltaY = e.getDeltaY() * 6;
            double height = contentScrollPane.getContent().getBoundsInLocal().getHeight();
            double vvalue = contentScrollPane.getVvalue();
            contentScrollPane.setVvalue(vvalue + (-deltaY / height));
            if (e.getDeltaX() != 0) {
                e.consume();
            }
        });

        contentScrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });

        VBox dayVBox = getDayVBox(days);
        contentHBox.getChildren().add(dayVBox);

        VBox pairVBox = getPairVBox(days.size());
        contentHBox.getChildren().add(pairVBox);

        VBox contentVBox = getContentVBox(groups, days.size());
        contentHBox.getChildren().add(contentVBox);
        HBox.setMargin(contentVBox, new Insets(0, 0, MARGIN_CONTENT, 0));

        rootVBox.getChildren().add(headerHBox);
        rootVBox.getChildren().add(contentScrollPane);

        borderPane.setCenter(rootScrollPane);
    }

    private HBox getHeaderHBox(List<StudyGroup> groups) {
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

    private VBox getContentVBox(List<StudyGroup> groups, int countDays) {
        VBox contentVBox = new VBox();
        Label cell;

        // расписание
        GridPane timetableGridPane = new GridPane();
        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < countDays * AppsSettingsHolder.getPairsPerDay(); j++) {
                cell = new Label();
                cell.setMaxSize(CELL_WIDTH_CONTENT, CELL_HEIGHT);
                cell.setMinSize(CELL_WIDTH_CONTENT, CELL_HEIGHT);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                cell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");
                timetableGridPane.add(cell, i, j);
            }
        }
        contentVBox.getChildren().add(timetableGridPane);

        // нагрузка
        GridPane loadGridPane = new GridPane();
        int groupIndex = 0;
        for (int i = 0; i < groups.size() * 2; i += 2) {
            StudyGroup group = groups.get(groupIndex++);
            List<TeachersBranch> branches = teachersBranchRepository.findByGroupOrderByHour(group);
            if (branches.isEmpty()) {
                // связка
                cell = new Label("Нет нагрузки");
                cell.setMaxSize(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT);
                cell.setMinSize(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                cell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");
                loadGridPane.add(cell, i, 0);

                // часы
                cell = new Label("0");
                cell.setMaxSize(CELL_WIDTH_HOUR, CELL_HEIGHT);
                cell.setMinSize(CELL_WIDTH_HOUR, CELL_HEIGHT);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                cell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");
                loadGridPane.add(cell, i + 1, 0);
                continue;
            }
            for (int j = 0; j < branches.size(); j++) {
                TeachersBranch tb = branches.get(j);

                // связка
                String text = tb.getTeacherSet().toString() + "\n" + tb.getStudyLoad().getSubject().getName();
                cell = new Label(text);
                cell.setTooltip(new Tooltip(tb.getTeacherSet().toString()));
                cell.setMaxSize(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT);
                cell.setMinSize(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                cell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");
                cell.setOnMouseClicked(e -> {
                    onLoadClicked(tb, group);
                });
                loadGridPane.add(cell, i, j);

                // часы
                cell = new Label(String.valueOf(tb.getStudyLoad().getCountMinutesInTwoWeek() / (AppsSettingsHolder.getHourTime() * 2)));

                if (cell.getText().length() > 2) {
                    cell.setTooltip(new Tooltip(cell.getText()));
                }

                cell.setMaxSize(CELL_WIDTH_HOUR, CELL_HEIGHT);
                cell.setMinSize(CELL_WIDTH_HOUR, CELL_HEIGHT);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                cell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BABABA");
                cell.setOnMouseClicked(e -> {
                    onLoadClicked(tb, group);
                });
                loadGridPane.add(cell, i + 1, j);
            }
        }
        contentVBox.getChildren().add(loadGridPane);

        return contentVBox;
    }

    private VBox getPairVBox(int countDays) {
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

    private VBox getDayVBox(List<DayEnum> days) {
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

    private TeachersBranch selectedBranch;
    private StudyGroup selectedGroup;
    private void onLoadClicked(TeachersBranch tb, StudyGroup group) {
        selectedGroup = group;
        selectedBranch = tb;

        refreshInfoPanel();
    }

    private Object selectedCell;
    private void onTimetableClicked() {
        refreshInfoPanel();
    }

    HBox currentLoad;
    HBox currentCellInfo;
    public void createInfoPanel(BorderPane borderPane) {
        currentLoad = new HBox();
        currentCellInfo = new HBox();
        VBox infoPanel = new VBox(new Label("Статус:"), currentLoad, currentCellInfo);
        infoPanel.setMinWidth(100);

        borderPane.setRight(infoPanel);
    }

    private void refreshInfoPanel() {
        currentLoad.getChildren().clear();
        currentCellInfo.getChildren().clear();
        if (selectedBranch == null || selectedGroup == null) {
            currentLoad.getChildren().add(new Label("Нагрузка не выбрана"));
        } else {
            currentLoad.getChildren().add(new Label(selectedBranch.getTeacherSet().toString()));
        }

        if (selectedCell == null) {
            currentCellInfo.getChildren().add(new Label("Ячейка расписания не выбрана"));
        }
    }
}
