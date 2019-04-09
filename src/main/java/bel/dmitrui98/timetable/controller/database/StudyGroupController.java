package bel.dmitrui98.timetable.controller.database;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.enums.StudyFormEnum;
import bel.dmitrui98.timetable.util.enums.StudyShiftEnum;
import bel.dmitrui98.timetable.util.enums.StudyTypeEnum;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.validation.AppsValidation;
import bel.dmitrui98.timetable.util.validation.ValidConditions;
import javafx.beans.binding.Bindings;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

public class StudyGroupController {

    private static final String EDIT_LABEL_NAME = "Номер для редактирования: ";
    private static final String EDIT_LABEL_INIT = "не установлен";
    private static final String DELETE_LABEL_NAME = "Номера для удаления: ";
    private static final String DELETE_LABEL_INIT = "не установлены";

    @FXML
    private Button defaultButton;

    @Autowired
    private BaseService<Specialty, Integer> specialtyService;

    @Autowired
    private BaseService<StudyGroup, Long> studyGroupService;
    private ObservableList<StudyGroup> groups;

    @FXML
    private TableView<StudyGroup> tableView;
    @FXML
    private TableColumn<StudyGroup, Void> indexCol;
    @FXML
    private TableColumn<StudyGroup, String> nameCol;
    @FXML
    private TableColumn<StudyGroup, Integer> courseCol;
    @FXML
    private TableColumn<StudyGroup, String> specialtyCol;
    @FXML
    private TableColumn<StudyGroup, String> studyTypeCol;
    @FXML
    private TableColumn<StudyGroup, String> studyFormCol;
    @FXML
    private TableColumn<StudyGroup, String> studyShiftCol;

    @FXML
    private TextField nameField;
    @FXML
    private TextField courseField;
    @FXML
    private ComboBox<Specialty> specialtyComboBox;
    @FXML
    private ComboBox<StudyTypeEnum> studyTypeComboBox;
    @FXML
    private ComboBox<StudyFormEnum> studyFormComboBox;
    @FXML
    private ComboBox<StudyShiftEnum> studyShiftComboBox;

    @FXML
    private Label editIndexLabel;
    @FXML
    private Label deleteIndexesLabel;

    @FXML
    private void add() throws AppsException {
        String name = nameField.getText().toLowerCase();

        final String validError = "Ошибка валидации";

        // валидируем курс группы
        int course;
        try {
            course = Integer.parseInt(courseField.getText());
            String contentText = null;
            if (course < 0) {
                contentText = "Курс группы не должен быть отрицательным";
            } else if (course > AppsSettingsHolder.getMaxCourse()) {
                contentText = "Курс группы не должен превышать максимальный курс, установленный в настройках приложения (максимальный курс: " +
                        AppsSettingsHolder.getMaxCourse() + ")";
            }
            if (contentText != null) {
                AlertsUtil.showErrorAlert(validError, contentText);
                return;
            }
        } catch (NumberFormatException ex) {
            AlertsUtil.showErrorAlert(validError, "Курс группы должен быть целочисленным", ex);
            return;
        }
        Specialty specialty = specialtyComboBox.getSelectionModel().getSelectedItem();
        if (specialty == null) {
            AlertsUtil.showErrorAlert(validError, "У группы должна быть специальность");
            return;
        }
        StudyTypeEnum studyType = studyTypeComboBox.getSelectionModel().getSelectedItem();
        if (studyType == null) {
            AlertsUtil.showErrorAlert(validError, "У группы должн быть тип обучения");
            return;
        }
        StudyFormEnum studyForm = studyFormComboBox.getSelectionModel().getSelectedItem();
        if (studyForm == null) {
            AlertsUtil.showErrorAlert(validError, "У группы должна быть форма обучения");
            return;
        }
        StudyShiftEnum studyShift = studyShiftComboBox.getSelectionModel().getSelectedItem();
        if (studyShift == null) {
            AlertsUtil.showErrorAlert(validError, "У группы должна быть смена");
            return;
        }


        if (isValid(name)) {
            StudyGroup studyGroup = new StudyGroup(name, course, studyShift, studyType, studyForm, specialty);
            studyGroupService.save(studyGroup);
            groups.add(studyGroup);
        }
    }

    @FXML
    private void edit() throws AppsException {
        StudyGroup entityForEdit  = tableView.getSelectionModel().getSelectedItem();
        if (entityForEdit == null) {
            AlertsUtil.showInfoAlert("Не выбрана группа", "Выберите хотя бы одну группу для редактирования");
            return;
        }
        String name = nameField.getText().toLowerCase();

        final String validError = "Ошибка валидации";

        // валидируем курс группы
        int course;
        try {
            course = Integer.parseInt(courseField.getText());
            String contentText = null;
            if (course < 0) {
                contentText = "Курс группы не должен быть отрицательным";
            } else if (course > AppsSettingsHolder.getMaxCourse()) {
                contentText = "Курс группы не должен превышать максимальный курс, установленный в настройках приложения (максимальный курс: " +
                        AppsSettingsHolder.getMaxCourse() + ")";
            }
            if (contentText != null) {
                AlertsUtil.showErrorAlert(validError, contentText);
                return;
            }
        } catch (NumberFormatException ex) {
            AlertsUtil.showErrorAlert(validError, "Курс группы должен быть целочисленным", ex);
            return;
        }

        Specialty specialty = specialtyComboBox.getSelectionModel().getSelectedItem();
        if (specialty == null) {
            AlertsUtil.showErrorAlert(validError, "У группы должна быть специальность");
            return;
        }
        StudyTypeEnum studyType = studyTypeComboBox.getSelectionModel().getSelectedItem();
        if (studyType == null) {
            AlertsUtil.showErrorAlert(validError, "У группы должн быть тип обучения");
            return;
        }
        StudyFormEnum studyForm = studyFormComboBox.getSelectionModel().getSelectedItem();
        if (studyForm == null) {
            AlertsUtil.showErrorAlert(validError, "У группы должна быть форма обучения");
            return;
        }
        StudyShiftEnum studyShift = studyShiftComboBox.getSelectionModel().getSelectedItem();
        if (studyShift == null) {
            AlertsUtil.showErrorAlert(validError, "У группы должна быть смена");
            return;
        }


        // если изменений не было
        if (entityForEdit.getName().equals(name) && entityForEdit.getCourse() == course
                && entityForEdit.getSpecialty().getSpecialtyId() == specialty.getSpecialtyId()
                && entityForEdit.getStudyType().equals(studyType)
                && entityForEdit.getStudyForm().equals(studyForm)
                && entityForEdit.getStudyShift().equals(studyShift)) {
            AlertsUtil.showInfoAlert("Изменений не зафиксировано",
                    "Измените хотя бы одно поле");
            return;
        }

        int ignoreIndex = tableView.getSelectionModel().getSelectedIndex();

        if (isValid(name, ignoreIndex)) {
            entityForEdit.setName(name);
            entityForEdit.setCourse(course);
            entityForEdit.setSpecialty(specialty);
            entityForEdit.setStudyForm(studyForm);
            entityForEdit.setStudyType(studyType);
            entityForEdit.setStudyShift(studyShift);
            studyGroupService.save(entityForEdit);
            tableView.refresh();
        }
    }

    @FXML
    private void delete() throws AppsException {
        ObservableList<StudyGroup> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбрана группа", "Выберите хотя бы одну группу для удаления");
            return;
        }
        List<Integer> selectedNumbers = tableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        boolean isConfirmed = AlertsUtil.showConfirmAlert("Вы точно хотите удалить выделенные группы?",
                "Группы с номерами " + selectedNumbers + " будут удалены");

        if (isConfirmed) {
            try {
                studyGroupService.deleteAll(selectedItems);
            } catch (Exception ex) {
                AlertsUtil.showErrorAlert("Ошибка удаления", "Группы не удалены. Возможно, на данные группы установлена нагрузка." +
                        "Сначала удалите связанную с данными группами нагрузку", ex);
                return;
            }

            groups.clear();
            groups.setAll(studyGroupService.findAll());
            refreshLabels();
        }
    }

    private boolean isValid(String name) {
        return isValid(name, -1);
    }

    private boolean isValid(String name, int ignoreIndex) {
        try {
            List<String> names = tableView.getItems().stream()
                    .map(StudyGroup::getName)
                    .collect(Collectors.toList());
            AppsValidation.validate(name, new ValidConditions(false, false), names, ignoreIndex);
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(VALID_EMPTY_VALUE)) {
                contentText = "Название не должно быть пустым";
            } else if (ex.getExceptionType().equals(VALID_LONG_VALUE)) {
                contentText = "Строка не должна превышать длину в " + ValidConditions.MAX_STRING_LENGTH + " символов";
            } else if (ex.getExceptionType().equals(VALID_DUPLICATE_VALUE)) {
                contentText = "Группа с названием \"" + name + "\" уже существует";
            }
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, contentText);
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {

        indexCol.setCellFactory(col -> {
            TableCell<StudyGroup, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));

            return cell ;
        });
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        courseCol.setCellValueFactory(cellData -> cellData.getValue().courseProperty().asObject());
        specialtyCol.setCellValueFactory(cellData -> cellData.getValue().getSpecialty().nameProperty());
        studyFormCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudyForm().getName()));
        studyShiftCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudyShift().getName()));
        studyTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudyType().getName()));

        groups = FXCollections.observableArrayList(studyGroupService.findAll());
        tableView.setItems(groups);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getName());
                courseField.setText(String.valueOf(newValue.getCourse()));
                specialtyComboBox.getSelectionModel().select(newValue.getSpecialty());
                studyFormComboBox.getSelectionModel().select(newValue.getStudyForm());
                studyTypeComboBox.getSelectionModel().select(newValue.getStudyType());
                studyShiftComboBox.getSelectionModel().select(newValue.getStudyShift());
                refreshLabels();
            }
        });
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tuningComboBoxes();

        setNumberFormatter(courseField);

        refresh();
    }

    private void tuningComboBoxes() {
        specialtyComboBox.setCellFactory((listView) -> {
            return new ListCell<Specialty>() {
                @Override
                protected void updateItem(Specialty item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            };
        });

        specialtyComboBox.setConverter(new StringConverter<Specialty>() {
            @Override
            public String toString(Specialty object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getName();
                }
            }

            @Override
            public Specialty fromString(String string) {
                return null;
            }
        });

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

        studyTypeComboBox.setCellFactory((listView) -> {
            return new ListCell<StudyTypeEnum>() {
                @Override
                protected void updateItem(StudyTypeEnum item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            };
        });

        studyTypeComboBox.setConverter(new StringConverter<StudyTypeEnum>() {
            @Override
            public String toString(StudyTypeEnum object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getName();
                }
            }

            @Override
            public StudyTypeEnum fromString(String string) {
                return null;
            }
        });

        studyFormComboBox.setCellFactory((listView) -> {
            return new ListCell<StudyFormEnum>() {
                @Override
                protected void updateItem(StudyFormEnum item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            };
        });

        studyFormComboBox.setConverter(new StringConverter<StudyFormEnum>() {
            @Override
            public String toString(StudyFormEnum object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getName();
                }
            }

            @Override
            public StudyFormEnum fromString(String string) {
                return null;
            }
        });

        studyFormComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(StudyFormEnum.values())));
        studyFormComboBox.getSelectionModel().selectFirst();

        studyShiftComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(StudyShiftEnum.values())));
        studyShiftComboBox.getSelectionModel().selectFirst();

        studyTypeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(StudyTypeEnum.values())));
        studyTypeComboBox.getSelectionModel().selectFirst();
    }

    private void setNumberFormatter(TextField numberField){
        TextFormatter<String> numberFormatter = new TextFormatter<>(change -> {
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
        numberField.setTextFormatter(numberFormatter);
    }

    public void refresh() {
        refreshLabels();
        List<Specialty> specialties = specialtyService.findAll();
        specialtyComboBox.setItems(FXCollections.observableArrayList(specialties));
        specialtyComboBox.getSelectionModel().selectFirst();
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
