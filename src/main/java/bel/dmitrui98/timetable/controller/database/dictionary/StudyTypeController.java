package bel.dmitrui98.timetable.controller.database.dictionary;

import bel.dmitrui98.timetable.util.enums.StudyTypeEnum;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.annotation.PostConstruct;
import java.util.Arrays;

public class StudyTypeController {
    @FXML
    private TableView<StudyTypeEnum> tableView;
    @FXML
    private TableColumn<StudyTypeEnum, Void> indexCol;
    @FXML
    private TableColumn<StudyTypeEnum, String> nameCol;
    @FXML
    private TableColumn<StudyTypeEnum, String> enumNameCol;

    @PostConstruct
    public void init() {
        indexCol.setCellFactory(col -> {
            TableCell<StudyTypeEnum, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));

            return cell ;
        });
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        enumNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));

        // загружаем enum в таблицу
        tableView.setItems(FXCollections.observableArrayList(Arrays.asList(StudyTypeEnum.values())));
    }
}
