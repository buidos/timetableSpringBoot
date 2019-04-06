package bel.dmitrui98.timetable.controller.database.dictionary;

import bel.dmitrui98.timetable.util.enums.StudyShiftEnum;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.annotation.PostConstruct;
import java.util.Arrays;

public class StudyShiftController {

    @FXML
    private TableView<StudyShiftEnum> tableView;
    @FXML
    private TableColumn<StudyShiftEnum, Void> indexCol;
    @FXML
    private TableColumn<StudyShiftEnum, String> nameCol;
    @FXML
    private TableColumn<StudyShiftEnum, String> enumNameCol;

    @PostConstruct
    public void init() {
        indexCol.setCellFactory(col -> {
            TableCell<StudyShiftEnum, Void> cell = new TableCell<>();
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
        tableView.setItems(FXCollections.observableArrayList(Arrays.asList(StudyShiftEnum.values())));
    }
}
