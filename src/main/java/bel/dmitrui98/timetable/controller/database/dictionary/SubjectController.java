package bel.dmitrui98.timetable.controller.database.dictionary;

import bel.dmitrui98.timetable.entity.Subject;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.enums.SubjectTypeEnum;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.exception.ExceptionType;
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

public class SubjectController {

    private static final String EDIT_LABEL_NAME = "Номер для редактирования: ";
    private static final String EDIT_LABEL_INIT = "не установлен";
    private static final String DELETE_LABEL_NAME = "Номера для удаления: ";
    private static final String DELETE_LABEL_INIT = "не установлены";

    @FXML
    private Button defaultButton;

    @Autowired
    private BaseService<Subject, Long> subjectService;
    private ObservableList<Subject> subjects;

    @FXML
    private TableView<Subject> subjectTableView;

    @FXML
    private TableColumn<Subject, Void> subjectIndexCol;
    @FXML
    private TableColumn<Subject, String> subjectNameCol;
    @FXML
    private TableColumn<Subject, String> subjectTypeNameCol;

    @FXML
    private TextField subjectNameTextField;
    @FXML
    private ComboBox<SubjectTypeEnum> subjectTypeComboBox;

    @FXML
    private Label editIndexLabel;
    @FXML
    private Label deleteIndexesLabel;

    @FXML
    private void addSubject() throws AppsException {
        String name = subjectNameTextField.getText().toLowerCase();

        if (isValid(name)) {
            SubjectTypeEnum subjectTypeEnum = subjectTypeComboBox.getSelectionModel().getSelectedItem();
            if (subjectTypeEnum == null) {
                AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR,
                        "Необходимо задать тип дисциплины для предмета");
                return;
            }
            Subject subject = new Subject(name, subjectTypeEnum);
            subjectService.save(subject);
            subjects.add(subject);
            subjectNameTextField.setText("");
        }
    }

    @FXML
    private void editSubject() throws AppsException {
        Subject subjectForEdit  = subjectTableView.getSelectionModel().getSelectedItem();
        if (subjectForEdit == null) {
            AlertsUtil.showInfoAlert("Не выбрана дисциплина",
                    "Выберите хотя бы одну дисциплину для редактирования");
            return;
        }
        String name = subjectNameTextField.getText().toLowerCase();

        SubjectTypeEnum subjectTypeEnum = subjectTypeComboBox.getSelectionModel().getSelectedItem();
        if (subjectTypeEnum == null) {
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR,
                    "Необходимо задать тип дисциплины для предмета");
            return;
        }

        // если изменений не было
        if (subjectForEdit.getSubjectType().equals(subjectTypeEnum) && subjectForEdit.getName().equals(name)) {
            AlertsUtil.showInfoAlert("Изменений не зафиксировано",
                    "Измените название или тип дисциплины");
            return;
        }

        int ignoreIndex = subjectTableView.getSelectionModel().getSelectedIndex();

        if (isValid(name, ignoreIndex)) {
            subjectForEdit.setName(name);
            subjectForEdit.setSubjectType(subjectTypeEnum);
            subjectService.save(subjectForEdit);
            subjectTableView.refresh();
        }
    }

    @FXML
    private void deleteSubject() throws AppsException {
        ObservableList<Subject> selectedItems = subjectTableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            AlertsUtil.showInfoAlert("Не выбрана дисциплина",
                    "Выберите хотя бы одну дисциплину для удаления");
            return;
        }
        List<Integer> selectedNumbers = subjectTableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        boolean isConfirmed = AlertsUtil.showConfirmAlert("Вы точно хотите удалить выделенные дисциплины?",
                "Дисциплины с номерами " + selectedNumbers + " будут удалены");

        if (isConfirmed) {
            subjectService.deleteAll(selectedItems);

            subjects.clear();
            subjects.setAll(subjectService.findAll());
            refreshLabels();
        }
    }

    private boolean isValid(String name) {
        return isValid(name, -1);
    }

    private boolean isValid(String name, int ignoreIndex) {
        try {
            List<String> names = subjectTableView.getItems().stream()
                    .map(Subject::getName)
                    .collect(Collectors.toList());
            AppsValidation.validate(name, new ValidConditions(false, false), names, ignoreIndex);
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(ExceptionType.VALID_EMPTY_VALUE)) {
                contentText = "Имя дисциплины не должно быть пустым";
            } else if (ex.getExceptionType().equals(ExceptionType.VALID_LONG_VALUE)) {
                contentText = "Имя дисциплины не дожно превышать длину в " + ValidConditions.MAX_STRING_LENGTH + " символов";
            } else if (ex.getExceptionType().equals(ExceptionType.VALID_DUPLICATE_VALUE)) {
                contentText = "Дисциплина с именем \"" + name + "\" уже существует";
            }
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, contentText);
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {

        // настраиваем всплывающий список отделений
        tuningComboBoxes();

        // настраиваем колонки таблицы
        tuningColumns();

        // настраиваем таблицу
        tuningTable();

        refresh();
    }

    private void tuningTable() {
        subjects = FXCollections.observableArrayList(subjectService.findAll());
        subjectTableView.setItems(subjects);

        subjectTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        subjectTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                subjectNameTextField.setText(newValue.getName());
                subjectTypeComboBox.getSelectionModel().select(newValue.getSubjectType());
                refreshLabels();
            }
        });
        VBox.setVgrow(subjectTableView, Priority.ALWAYS);
    }

    private void tuningColumns() {
        subjectIndexCol.setCellFactory(col -> {
            TableCell<Subject, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));

            return cell ;
        });
        subjectNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        subjectTypeNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSubjectType().getName()));
    }

    private void tuningComboBoxes() {
        subjectTypeComboBox.setCellFactory((listView) -> {
            return new ListCell<SubjectTypeEnum>() {
                @Override
                protected void updateItem(SubjectTypeEnum item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            };
        });

        subjectTypeComboBox.setConverter(new StringConverter<SubjectTypeEnum>() {
            @Override
            public String toString(SubjectTypeEnum object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getName();
                }
            }

            @Override
            public SubjectTypeEnum fromString(String string) {
                return null;
            }
        });
    }

    public void refresh() {
        refreshLabels();
        subjectTypeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(SubjectTypeEnum.values())));
        subjectTypeComboBox.getSelectionModel().selectFirst();
    }

    private void refreshLabels() {
        List<Integer> selectedNumbers = subjectTableView.getSelectionModel().getSelectedIndices().stream()
                .map(index -> index + 1)
                .collect(Collectors.toList());
        if (!selectedNumbers.isEmpty()) {
            editIndexLabel.setText(EDIT_LABEL_NAME + (subjectTableView.getSelectionModel().getSelectedIndex() + 1));
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
