package bel.dmitrui98.timetable.controller.database.dictionary;

import bel.dmitrui98.timetable.repository.SettingRepository;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.enums.SubjectTypeEnum;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Arrays;

public class SettingController {

    @Autowired
    private SettingRepository settingRepository;

    @FXML
    private TextField hourTimeField;

    @FXML
    private TextField maxCourseField;

    @FXML
    private TextField pairsPerDayField;

    @FXML
    private ComboBox<SubjectTypeEnum> subjectTypeComboBox;

    @FXML
    private void save() {
        int hourTime, maxCourse, pairsPerDay;
        try {
            hourTime = Integer.parseInt(hourTimeField.getText());
            maxCourse = Integer.parseInt(maxCourseField.getText());
            pairsPerDay = Integer.parseInt(pairsPerDayField.getText());
        } catch (NumberFormatException ex) {
            AlertsUtil.showErrorAlert("Ошибка валидации", "Неправильный ввод числовых данных", ex);
            return;
        }
        AppsSettingsHolder.setHourTime(hourTime);
        AppsSettingsHolder.setMaxCourse(maxCourse);
        AppsSettingsHolder.setPairsPerDay(pairsPerDay);
        AppsSettingsHolder.setSubjectType(subjectTypeComboBox.getValue());

        saveSettingsToDB();
    }

    @FXML
    private void setToDefault() {
        boolean isConfirm = AlertsUtil.showConfirmAlert("Сбросить настройки?",
                "Текущие настройки будут установлены по умолчанию");
        if (isConfirm) {
            AppsSettingsHolder.setSettingsToDefault();
            saveSettingsToDB();
        }
    }

    private void saveSettingsToDB() {
        System.out.println("save to db");
    }

    @PostConstruct
    public void init() {
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

        subjectTypeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(SubjectTypeEnum.values())));

        setNumberFormatter(hourTimeField);
        setNumberFormatter(maxCourseField);
        setNumberFormatter(pairsPerDayField);
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
}
