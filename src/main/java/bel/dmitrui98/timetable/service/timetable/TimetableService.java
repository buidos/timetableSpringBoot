package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {

    private static final double CELL_WIDTH = 100;
    private static final double CELL_HEIGHT = 25;
    private static final int DAY_ROTATE = -90;

    public void showTable(List<StudyGroup> groups, List<DayEnum> days, BorderPane borderPane) {
        VBox rootVBox = new VBox();
        HBox headerHBox = getHeaderHBox();

        HBox contentHBox = new HBox();
        contentHBox.setAlignment(Pos.TOP_LEFT);
        ScrollPane scrollPane = new ScrollPane(contentHBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        // ускоряем скролл
        contentHBox.setOnScroll(e -> {
            double deltaY = e.getDeltaY() * 4;
            double width = scrollPane.getContent().getBoundsInLocal().getWidth();
            double vvalue = scrollPane.getVvalue();
            scrollPane.setVvalue(vvalue + (-deltaY / width));
        });
        BorderPane.setMargin(rootVBox, new Insets(10, 0, 0, 0));

        VBox dayVBox = getDayVBox(days);
        contentHBox.getChildren().add(dayVBox);

        rootVBox.getChildren().add(headerHBox);
        rootVBox.getChildren().add(scrollPane);

        borderPane.setCenter(rootVBox);
    }

    private HBox getHeaderHBox() {
        HBox headerHBox = new HBox();

        TextField cell = new TextField("Дни");
        cell.setEditable(false);
        cell.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
        cell.setMinSize(CELL_WIDTH, CELL_HEIGHT);
        headerHBox.getChildren().add(cell);
        return headerHBox;
    }

    private VBox getDayVBox(List<DayEnum> days) {
        VBox dayVBox = new VBox();

        TextField cell;
        int i = 0;
        double marginTop = 43.5;
        for (DayEnum day : days) {
            cell = new TextField(day.getName());
            cell.setEditable(false);
            cell.setMaxHeight(CELL_WIDTH);
            cell.setMinHeight(CELL_WIDTH);
            cell.setRotate(DAY_ROTATE);
            dayVBox.getChildren().add(cell);
            cell.setTranslateX(cell.getTranslateX() - 45);

            if (i++ == 1) {
                marginTop = 87;
            } else if (i == days.size()) {
                VBox.setMargin(cell, new Insets(marginTop, 0, 44, 0));
                break;
            }
            VBox.setMargin(cell, new Insets(marginTop, 0, 0, 0));
        }
        return dayVBox;
    }
}
