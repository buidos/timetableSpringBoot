package bel.dmitrui98.timetable.controller.database;

import bel.dmitrui98.timetable.entity.StudyPair;
import bel.dmitrui98.timetable.repository.StudyPairRepository;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.enums.StudyShiftEnum;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.time.TimeUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudyPairController {

    private static final String EDIT_LABEL_NAME = "Номер для редактирования: ";
    private static final String EDIT_LABEL_INIT = "не установлен";
    private static final String DELETE_LABEL_NAME = "Номера для удаления: ";
    private static final String DELETE_LABEL_INIT = "не установлены";

    private static final String VALIDATION_ERROR = "Ошибка валидации";

    @FXML
    private Button defaultButton;

    @Autowired
    private StudyPairRepository studyPairRepository;

    @Autowired
    private BaseService<StudyPair, Integer> studyPairService;
    private ObservableList<StudyPair> studyPairs;

    @FXML
    private TableView<StudyPair> tableView;
    @FXML
    private TableColumn<StudyPair, String> indexCol;
    @FXML
    private TableColumn<StudyPair, String> studyShiftCol;
    @FXML
    private TableColumn<StudyPair, String> timeBeginOneCol;
    @FXML
    private TableColumn<StudyPair, String> timeEndOneCol;
    @FXML
    private TableColumn<StudyPair, String> timeBeginTwoCol;
    @FXML
    private TableColumn<StudyPair, String> timeEndTwoCol;

    @FXML
    private ComboBox<StudyShiftEnum> studyShiftComboBox;
    @FXML
    private TextField pairNumberField;
    @FXML
    private TextField timeBeginOneField;
    @FXML
    private TextField timeEndOneField;
    @FXML
    private TextField timeBeginTwoField;
    @FXML
    private TextField timeEndTwoField;


    @FXML
    private Label editIndexLabel;
    @FXML
    private Label deleteIndexesLabel;

    @FXML
    private void add() throws AppsException {
        StudyShiftEnum studyShift = studyShiftComboBox.getSelectionModel().getSelectedItem();
        if (studyShift == null) {
            AlertsUtil.showErrorAlert(VALIDATION_ERROR, "Смена у пары не должна быть пустой");
            return;
        }
        Integer pairNumber = getLastPairNumber() + 1;
        int pairsPerDay = AppsSettingsHolder.getPairsPerDay();
        if (pairNumber > pairsPerDay) {
            AlertsUtil.showErrorAlert(VALIDATION_ERROR, String.format("Количество пар в день (%d) превышает значение, " +
                    "указанное в настройках (%d)", pairNumber, pairsPerDay));
            return;
        }
        try {
            LocalTime timeBeginOne = TimeUtil.parseStringToDateTime(timeBeginOneField.getText());
            LocalTime timeBeginTwo = TimeUtil.parseStringToDateTime(timeBeginTwoField.getText());
            LocalTime timeEndOne = TimeUtil.parseStringToDateTime(timeEndOneField.getText());
            LocalTime timeEndTwo = TimeUtil.parseStringToDateTime(timeEndTwoField.getText());

            long pairDurationOne = ChronoUnit.MINUTES.between(timeBeginOne, timeEndOne);
            long pairDurationTwo = ChronoUnit.MINUTES.between(timeBeginTwo, timeEndTwo);

            if (isValidPairDuration(pairDurationOne, pairDurationTwo)) {
                StudyPair studyPair = new StudyPair(studyShift, pairNumber, timeBeginOne, timeEndOne, timeBeginTwo,
                        timeEndTwo);
                studyPairService.save(studyPair);
                studyPairs.add(studyPair);
                // устанавливаем следующий номер пары
                pairNumberField.setText(String.valueOf(pairNumber+1));
            }
        } catch (DateTimeParseException ex) {
            AlertsUtil.showErrorAlert(VALIDATION_ERROR, "Не верно введено время. Формат времени: " +
                    TimeUtil.TIME_PATTERN, ex);
        }
    }

    @FXML
    private void edit() throws AppsException {
        StudyPair entityForEdit  = tableView.getSelectionModel().getSelectedItem();
        if (entityForEdit == null) {
            AlertsUtil.showInfoAlert("Не выбрана пара для редактирования", "Выберите хотя бы одну пару для редактирования");
            return;
        }

        StudyShiftEnum studyShift = studyShiftComboBox.getSelectionModel().getSelectedItem();
        if (studyShift == null) {
            AlertsUtil.showErrorAlert(VALIDATION_ERROR, "Смена у пары не должна быть пустой");
            return;
        }

        try {
            LocalTime timeBeginOne = TimeUtil.parseStringToDateTime(timeBeginOneField.getText());
            LocalTime timeBeginTwo = TimeUtil.parseStringToDateTime(timeBeginTwoField.getText());
            LocalTime timeEndOne = TimeUtil.parseStringToDateTime(timeEndOneField.getText());
            LocalTime timeEndTwo = TimeUtil.parseStringToDateTime(timeEndTwoField.getText());

            // если не было изменений
            if (entityForEdit.getStudyShift().equals(studyShift) && entityForEdit.getBeginTimeFirstHalf().equals(timeBeginOne) &&
                entityForEdit.getBeginTimeSecondHalf().equals(timeBeginTwo) && entityForEdit.getEndTimeFirstHalf().equals(timeEndOne) &&
                entityForEdit.getEndTimeSecondHalf().equals(timeEndTwo)) {
                AlertsUtil.showInfoAlert("Изменений не зафиксировано",
                        "Измените хотя бы одно поле");
                return;
            }

            long pairDurationOne = ChronoUnit.MINUTES.between(timeBeginOne, timeEndOne);
            long pairDurationTwo = ChronoUnit.MINUTES.between(timeBeginTwo, timeEndTwo);

            if (isValidPairDuration(pairDurationOne, pairDurationTwo)) {
                entityForEdit.setStudyShift(studyShift);
                entityForEdit.setBeginTimeFirstHalf(timeBeginOne);
                entityForEdit.setBeginTimeSecondHalf(timeBeginTwo);
                entityForEdit.setEndTimeSecondHalf(timeEndTwo);
                entityForEdit.setEndTimeFirstHalf(timeEndOne);
                studyPairService.save(entityForEdit);
                tableView.refresh();
            }
        } catch (DateTimeParseException ex) {
            AlertsUtil.showErrorAlert(VALIDATION_ERROR, "Не верно введено время. Формат времени: " +
                    TimeUtil.TIME_PATTERN, ex);
        }
    }

    @FXML
    private void delete() throws AppsException {
        ObservableList<StudyPair> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбрана пара", "Выберите хотя бы одну пару для удаления");
            return;
        }
        List<Integer> selectedNumbers = tableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        boolean isConfirmed = AlertsUtil.showConfirmAlert("Вы точно хотите удалить выделенные пары?",
                "Пары с номерами " + selectedNumbers + " будут удалены");

        if (isConfirmed) {
            studyPairService.deleteAll(selectedItems);

            studyPairs.clear();
            // обновляем pairNumber
            List<StudyPair> studyPairs = studyPairRepository.findAllOrderByPairNumber();
            int pairNumber = 1;
            for (StudyPair sp : studyPairs) {
                sp.setPairNumber(pairNumber);
                pairNumber++;
                studyPairService.save(sp);
            }
            this.studyPairs.setAll(studyPairs);
            refreshLabels();
            tableView.refresh();
            // устанавливаем следующий номер пары
            pairNumberField.setText(String.valueOf(pairNumber));
        }
    }

    private boolean isValidPairDuration(long pairDurationOne, long pairDurationTwo) {
        int hourTime = AppsSettingsHolder.getHourTime();
        if (pairDurationOne != hourTime) {
            AlertsUtil.showErrorAlert(VALIDATION_ERROR, String.format("Длительность первой половины пары (%d мин) " +
                            "не совпадает с длительностью половины пары, указанной в настройках (%d мин)",
                    pairDurationOne, hourTime));
            return false;
        }

        if (pairDurationTwo != hourTime) {
            AlertsUtil.showErrorAlert(VALIDATION_ERROR, String.format("Длительность второй половины пары (%d мин) " +
                            "не совпадает с длительностью половины пары, указанной в настройках (%d мин)",
                    pairDurationTwo, hourTime));
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {

        // определяем последний номер пары
        studyPairs = FXCollections.observableArrayList(studyPairRepository.findAllOrderByPairNumber());
        Integer lastPairNumber = getLastPairNumber();
        pairNumberField.setText(String.valueOf(lastPairNumber + 1));

        // запрещаем ввод всех символов, кроме цифр и двоеточия
        setTimeFormatter(timeBeginOneField);
        setTimeFormatter(timeBeginTwoField);
        setTimeFormatter(timeEndTwoField);
        setTimeFormatter(timeEndOneField);

        tuningComboBoxes();

        indexCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPairNumber())));
        studyShiftCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStudyShift().getName())));

        timeBeginOneCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                TimeUtil.formatToTimeString(cellData.getValue().getBeginTimeFirstHalf())));
        timeBeginTwoCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                TimeUtil.formatToTimeString(cellData.getValue().getBeginTimeSecondHalf())));
        timeEndOneCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                TimeUtil.formatToTimeString(cellData.getValue().getEndTimeFirstHalf())));
        timeEndTwoCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                TimeUtil.formatToTimeString(cellData.getValue().getEndTimeSecondHalf())));

        studyPairs = FXCollections.observableArrayList(studyPairService.findAll());
        tableView.setItems(studyPairs);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                studyShiftComboBox.getSelectionModel().select(newValue.getStudyShift());
                pairNumberField.setText(String.valueOf(newValue.getPairNumber()));

                timeBeginOneField.setText(TimeUtil.formatToTimeString(newValue.getBeginTimeFirstHalf()));
                timeBeginTwoField.setText(TimeUtil.formatToTimeString(newValue.getBeginTimeSecondHalf()));
                timeEndTwoField.setText(TimeUtil.formatToTimeString(newValue.getEndTimeSecondHalf()));
                timeEndOneField.setText(TimeUtil.formatToTimeString(newValue.getEndTimeFirstHalf()));

                refreshLabels();
            }
        });
        VBox.setVgrow(tableView, Priority.ALWAYS);

        refreshLabels();
    }

    private Integer getLastPairNumber() {
        Integer lastPairNumber = 0;
        if (!studyPairs.isEmpty()) {
            Optional<StudyPair> optStudyPair = studyPairs.stream().skip(studyPairs.size() - 1).findFirst();
            if (optStudyPair.isPresent()) {
                lastPairNumber = optStudyPair.get().getPairNumber();
            }
        }
        return lastPairNumber;
    }

    private void tuningComboBoxes() {
        studyShiftComboBox.setCellFactory((listView) -> {
            return new ListCell<StudyShiftEnum>() {
                @Override
                protected void updateItem(StudyShiftEnum item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            };
        });

        studyShiftComboBox.setConverter(new StringConverter<StudyShiftEnum>() {
            @Override
            public String toString(StudyShiftEnum object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getName();
                }
            }

            @Override
            public StudyShiftEnum fromString(String string) {
                return null;
            }
        });

        studyShiftComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(StudyShiftEnum.values())));
        studyShiftComboBox.getSelectionModel().selectFirst();
    }

    private void setTimeFormatter(TextField numberField){
        TextFormatter<String> numberFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }
            String newValue = change.getControlNewText();

            // если не цифра, пропускаем
            if (!newValue.matches("\\d{0,2}|\\d{0,2}:\\d{0,2}")) {
                return null;
            }
            return change;
        });
        numberField.setTextFormatter(numberFormatter);
    }

    private void refreshLabels() {
        List<Integer> selectedNumbers = tableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        if (!selectedNumbers.isEmpty()) {
            editIndexLabel.setText(EDIT_LABEL_NAME + (tableView.getSelectionModel().getSelectedIndex() + 1));
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
