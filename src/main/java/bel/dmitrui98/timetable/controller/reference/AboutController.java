package bel.dmitrui98.timetable.controller.reference;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;

/**
 * Вне контекста спринга
 */
public class AboutController {
    @FXML
    private WebView html;

    @FXML
    private void initialize() {
        WebEngine engine = html.getEngine();
        File f = new File(getClass().getClassLoader().getResource("html/about.htm").getFile());
        engine.load(f.toURI().toString());
    }
}
