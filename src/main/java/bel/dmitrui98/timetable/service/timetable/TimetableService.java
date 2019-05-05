package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.control.LoadLabel;
import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableListDto;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с расписанием
 */
@Service
public class TimetableService {

    @Autowired
    private TimetableUtil timetableUtil;

    /**
     * Непосредственно расписание. Сохраняется в xml файл
     */
    private List<TimetableListDto> timetableList = new ArrayList<>();

    private BorderPane borderPane;

    /**
     * Отображение расписания
     * @param groups группы, для которых будет составлятся расписание
     * @param days дни, на которые будет составлятся расписание
     * @param borderPane панель, где будет отображено расписание
     */
    public void showTable(List<StudyGroup> groups, List<DayEnum> days, BorderPane borderPane) {
        timetableUtil.clearSelection();
        this.borderPane = borderPane;

        if (AppsSettingsHolder.getPairsPerDay() < 1) {
            AlertsUtil.showInfoAlert("Не установлено количество пар в день", "Установите количество" +
                    " пар в день, чтобы отобразить расписание. Текущее значение " + AppsSettingsHolder.getPairsPerDay());
            return;
        }

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
        HBox headerHBox = timetableUtil.getHeaderHBox(groups);

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

        VBox dayVBox = timetableUtil.getDayVBox(days);
        contentHBox.getChildren().add(dayVBox);

        VBox pairVBox = timetableUtil.getPairVBox(days.size());
        contentHBox.getChildren().add(pairVBox);

        VBox contentVBox = timetableUtil.getContentVBox(groups, days, borderPane);
        contentHBox.getChildren().add(contentVBox);
        HBox.setMargin(contentVBox, new Insets(0, 0, TimetableUtil.MARGIN_CONTENT, 0));

        rootVBox.getChildren().add(headerHBox);
        rootVBox.getChildren().add(contentScrollPane);

        borderPane.setCenter(rootScrollPane);

        timetableUtil.createInfoPanel(borderPane);
    }



    public boolean changeVisibleInfoPanel() {
        if (borderPane != null) {
            if (borderPane.getRight() == null) {
                timetableUtil.createInfoPanel(borderPane);
            } else {
                borderPane.setRight(null);
            }
            return true;
        } else {
            return false;
        }
    }

    public LoadLabel getSelectedLoadLabel() {
        return timetableUtil.getSelectedLoadLabel();
    }

    public List<TimetableListDto> getTimetableList() {
        return timetableList;
    }

    public void refreshInfoPanel() {
        timetableUtil.refreshInfoPanel(borderPane.getRight());
    }
}
