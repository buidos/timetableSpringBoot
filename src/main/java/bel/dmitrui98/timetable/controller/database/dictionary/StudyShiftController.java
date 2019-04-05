package bel.dmitrui98.timetable.controller.database.dictionary;

import bel.dmitrui98.timetable.entity.dictionary.StudyShift;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.validation.AppsValidation;
import bel.dmitrui98.timetable.util.validation.ValidConditions;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

public class StudyShiftController {

    private static final String EDIT_LABEL_NAME = "Номер для редактирования: ";
    private static final String EDIT_LABEL_INIT = "не установлен";
    private static final String DELETE_LABEL_NAME = "Номера для удаления: ";
    private static final String DELETE_LABEL_INIT = "не установлены";

    @FXML
    private Button defaultButton;

    @Autowired
    private BaseService<StudyShift, Integer> studyShiftService;
    private ObservableList<StudyShift> studyShifts;

    @FXML
    private TableView<StudyShift> studyShiftTableView;
    @FXML
    private TableColumn<StudyShift, Void> studyShiftIndexCol;
    @FXML
    private TableColumn<StudyShift, String> studyShiftNameCol;
    @FXML
    private TableColumn<StudyShift, Integer> studyShiftValueCol;
    @FXML
    private TextField studyShiftNameTextField;
    @FXML
    private TextField studyShiftValueTextField;

    @FXML
    private Label editIndexLabel;
    @FXML
    private Label deleteIndexesLabel;

    @FXML
    private void addStudyShift() throws AppsException {
        String name = studyShiftNameTextField.getText().toLowerCase();
        String sValue = studyShiftValueTextField.getText();

        if (!isValid(sValue, true)) {
            return;
        }

        int value;
        try {
            value = Integer.parseInt(sValue);
        } catch (NumberFormatException ex) {
            AlertsUtil.showErrorAlert("Неверный формат ввода значения",
                    String.format("Не удалось преобразовать %s в число, введите корректное числовое значение", sValue));
            return;
        }

        if (isValid(name)) {
            StudyShift studyShift = new StudyShift(value, name);
            studyShiftService.save(studyShift);
            studyShifts.add(studyShift);
        }
    }

    @FXML
    private void editStudyShift() throws AppsException {
        StudyShift studyShiftForEdit  = studyShiftTableView.getSelectionModel().getSelectedItem();
        if (studyShiftForEdit == null) {
            AlertsUtil.showInfoAlert("Не выбрана запись", "Выберите хотя бы одну учебную смену для редактирования");
            return;
        }

        String sValue = studyShiftValueTextField.getText();
        if (!isValid(sValue, true)) {
            return;
        }

        int value;
        try {
            value = Integer.parseInt(sValue);
        } catch (NumberFormatException ex) {
            AlertsUtil.showErrorAlert("Неверный формат ввода значения",
                    String.format("Не удалось преобразовать %s в число, введите корректное числовое значение", sValue));
            return;
        }

        String name = studyShiftNameTextField.getText().toLowerCase();

        // если изменений не было
        if (studyShiftForEdit.getName().equals(name) && studyShiftForEdit.getValue() == value) {
            AlertsUtil.showInfoAlert("Изменений не зафиксировано",
                    "Измените название или значение");
            return;
        }

        if (isValid(name)) {
            studyShiftForEdit.setName(name);
            studyShiftForEdit.setValue(value);
            studyShiftService.save(studyShiftForEdit);
        }
    }

    @FXML
    private void deleteStudyShift() throws AppsException {
        ObservableList<StudyShift> selectedItems = studyShiftTableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбрана запись", "Выберите хотя бы одну учебную смену для удаления");
            return;
        }
        List<Integer> selectedNumbers = studyShiftTableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        boolean isConfirmed = AlertsUtil.showConfirmAlert("Вы точно хотите удалить выделенные записи?",
                "Учебные смены с номерами " + selectedNumbers + " будут удалены");

        if (isConfirmed) {
            studyShiftService.deleteAll(selectedItems);

            studyShifts.clear();
            studyShifts.setAll(studyShiftService.findAll());
            refreshLabels();
        }
    }

    private boolean isValid(String name) {
        return isValid(name, false);
    }

    public boolean isValid(String name, boolean allowDuplicate) {
        try {
            List<String> names = studyShiftTableView.getItems().stream()
                    .map(StudyShift::getName)
                    .collect(Collectors.toList());
            AppsValidation.validate(name, new ValidConditions(false, allowDuplicate), names);
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(VALID_EMPTY_VALUE)) {
                contentText = "Имя смены не должно быть пустым";
            } else if (ex.getExceptionType().equals(VALID_LONG_VALUE)) {
                contentText = "Имя смены не дожно превышать длину в " + ValidConditions.MAX_STRING_LENGTH + " символов";
            } else if (ex.getExceptionType().equals(VALID_DUPLICATE_VALUE)) {
                contentText = "Смена с именем \"" + name + "\" уже существует";
            }
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, contentText);
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }
            String newValue = change.getControlNewText();

            // если не цифра, пропускаем
            if (!newValue.matches("\\d+") && !newValue.equals("")) {
                return null;
            }
            return change;
        });
        studyShiftValueTextField.setTextFormatter(textFormatter);

        studyShiftIndexCol.setCellFactory(col -> {
            TableCell<StudyShift, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));

            return cell ;
        });
        studyShiftNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        studyShiftValueCol.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());

        studyShifts = FXCollections.observableArrayList(studyShiftService.findAll());
        studyShiftTableView.setItems(studyShifts);

        studyShiftTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studyShiftTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                studyShiftNameTextField.setText(newValue.getName());
                studyShiftValueTextField.setText(String.valueOf(newValue.getValue()));
                refreshLabels();
            }
        });
        VBox.setVgrow(studyShiftTableView, Priority.ALWAYS);

        refreshLabels();
    }

    private void refreshLabels() {
        List<Integer> selectedNumbers = studyShiftTableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        if (!selectedNumbers.isEmpty()) {
            editIndexLabel.setText(EDIT_LABEL_NAME + (studyShiftTableView.getSelectionModel().getSelectedIndex() + 1));
            deleteIndexesLabel.setText(DELETE_LABEL_NAME + selectedNumbers);
        } else {
            deleteIndexesLabel.setText(DELETE_LABEL_NAME + DELETE_LABEL_INIT);
            editIndexLabel.setText(EDIT_LABEL_NAME + EDIT_LABEL_INIT);
        }
    }

    public Button getDefaultButton() {
        return defaultButton;
    }
}
