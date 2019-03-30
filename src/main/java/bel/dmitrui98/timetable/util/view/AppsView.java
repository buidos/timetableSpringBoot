package bel.dmitrui98.timetable.util.view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppsView {
    /**
     * главная панель fxml
     */
    private Scene scene;
    /**
     * fxml контроллер
     */
    private Object controller;
    /**
     * ссылка на иконку fxml
     */
    private Image icon;
}
