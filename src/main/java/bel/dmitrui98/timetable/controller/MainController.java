package bel.dmitrui98.timetable.controller;

import bel.dmitrui98.timetable.controller.database.EditDBController;
import bel.dmitrui98.timetable.util.view.AppsView;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MainController {



    @Autowired
    @Qualifier("editDatabaseView")
    private AppsView editDatabaseView;


    @FXML
    private void showEditDatabase() {
        Stage stage = new Stage();
        stage.setTitle("Редактирование базы данных");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(editDatabaseView.getIcon());

        EditDBController editDBController = (EditDBController) editDatabaseView.getController();
        // заполняем табы содержимым
        Scene editDatabaseScene = editDBController.initTabs();

        stage.setScene(editDatabaseScene);
        stage.show();
    }
}
