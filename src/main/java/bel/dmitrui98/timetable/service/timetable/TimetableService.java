package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
    private static final double CELL_WIDTH_HOUR = 50;
    private static final double CELL_HEIGHT = 25;
    private static final int DAY_ROTATE = -90;
    /**
     * Смещение после вращения поля с днем (вращается относительно центра)
     */
    private static final double SHIFT;
    static {
        SHIFT = Math.abs(CELL_WIDTH - CELL_HEIGHT);
    }

    @Autowired
    private TeachersBranchRepository teachersBranchRepository;

    public void showTable(List<StudyGroup> groups, List<DayEnum> days, BorderPane borderPane) {
        // состоит из двух HBox: header и content
        VBox rootVBox = new VBox();
        ScrollPane rootScrollPane = new ScrollPane(rootVBox);
        rootScrollPane.setFitToHeight(true);
        rootScrollPane.setFitToWidth(true);
        HBox headerHBox = getHeaderHBox(groups);

        // состоит из VBox(дни, пары, content(состоит из GridPane (расписание, нагрузка)))
        HBox contentHBox = new HBox();
        contentHBox.setAlignment(Pos.TOP_LEFT);
        ScrollPane contentScrollPane = new ScrollPane(contentHBox);
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setFitToHeight(true);
        // ускоряем скролл
        contentHBox.setOnScroll(e -> {
            double deltaY = e.getDeltaY() * 6;
            double width = contentScrollPane.getContent().getBoundsInLocal().getWidth();
            double vvalue = contentScrollPane.getVvalue();
            contentScrollPane.setVvalue(vvalue + (-deltaY / width));
        });

        VBox dayVBox = getDayVBox(days);
        contentHBox.getChildren().add(dayVBox);

        VBox pairVBox = getPairVBox(days.size());
        contentHBox.getChildren().add(pairVBox);

        VBox contentVBox = getContentVBox(groups, days.size());
        contentHBox.getChildren().add(contentVBox);


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
        TextField cell;

        // расписание
        GridPane timetableGridPane = new GridPane();
        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < countDays * AppsSettingsHolder.getPairsPerDay(); j++) {
                cell = new TextField();
                cell.setEditable(false);
                cell.setMaxSize(CELL_WIDTH_CONTENT, CELL_HEIGHT);
                cell.setMinSize(CELL_WIDTH_CONTENT, CELL_HEIGHT);
                cell.setTranslateX(cell.getTranslateX() - SHIFT);
                timetableGridPane.add(cell, i, j);
            }
        }
        contentVBox.getChildren().add(timetableGridPane);

        // нагрузка
        GridPane loadGridPane = new GridPane();
        int groupIndex = 0;
        for (int i = 0; i < groups.size() * 2; i += 2) {
            StudyGroup group = groups.get(groupIndex++);
            // TODO отсортировать по количеству часов
//            List<TeachersBranch> branches = teachersBranchRepository.findByGroup(group);
            // извлечь учителей
//            for (int j = 0; j < branches.size(); j++) {
//
//                // связка
//                cell = new TextField();
//                cell.setEditable(false);
//                cell.setMaxSize(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT);
//                cell.setMinSize(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT);
//                cell.setTranslateX(cell.getTranslateX() - SHIFT);
//                loadGridPane.add(cell, i, j);
//
//                // часы
//                cell = new TextField();
//                cell.setEditable(false);
//                cell.setMaxSize(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT);
//                cell.setMinSize(CELL_WIDTH_CONTENT - CELL_WIDTH_HOUR, CELL_HEIGHT);
//                cell.setTranslateX(cell.getTranslateX() - SHIFT);
//                loadGridPane.add(cell, i, j);
//            }
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
        double marginTop = 37;
        for (DayEnum day : days) {
            cell = new TextField(day.getName());
            cell.setEditable(false);
            double height = CELL_WIDTH;
            double width = AppsSettingsHolder.getPairsPerDay() * CELL_HEIGHT;
            cell.setMaxSize(width, height);
            cell.setMinSize(width, height);
            cell.setRotate(DAY_ROTATE);
            dayVBox.getChildren().add(cell);
            cell.setTranslateX(cell.getTranslateX() - SHIFT / 2);

            if (i++ == 1) {
                marginTop = 75;
            } else if (i == days.size()) {
                VBox.setMargin(cell, new Insets(marginTop, 0, 44, 0));
                break;
            }
            VBox.setMargin(cell, new Insets(marginTop, 0, 0, 0));
        }
        return dayVBox;
    }
}
