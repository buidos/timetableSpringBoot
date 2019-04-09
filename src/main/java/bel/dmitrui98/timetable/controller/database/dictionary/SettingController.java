package bel.dmitrui98.timetable.controller.database.dictionary;

import bel.dmitrui98.timetable.entity.Setting;
import bel.dmitrui98.timetable.repository.SettingRepository;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.enums.SettingEnum;
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
import java.util.List;

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

        try {
            saveSettingsToDB();
            AlertsUtil.showInfoAlert("Успех", "Настройки сохранены");
        } catch (Exception ex) {
            refresh();
            throw ex;
        }
    }

    @FXML
    private void setToDefault() {
        boolean isConfirm = AlertsUtil.showConfirmAlert("Сбросить настройки?",
                "Текущие настройки будут установлены по умолчанию");
        if (isConfirm) {
            AppsSettingsHolder.setSettingsToDefault();
            try {
                saveSettingsToDB();
                AlertsUtil.showInfoAlert("Успех", "Значения настроек установлены по умолчанию");
            } finally {
                refresh();
            }

        }
    }

    private void saveSettingsToDB() {
        Setting setting;

        // учебный час
        setting = settingRepository.findBySettingType(SettingEnum.HOUR_TIME);
        if (setting == null) {
            setting = new Setting(SettingEnum.HOUR_TIME, String.valueOf(AppsSettingsHolder.getHourTime()));
            settingRepository.save(setting);
        } else {
            setting.setValue(String.valueOf(AppsSettingsHolder.getHourTime()));
            settingRepository.save(setting);
        }

        // максимальный курс
        setting = settingRepository.findBySettingType(SettingEnum.MAX_COURSE);
        if (setting == null) {
            setting = new Setting(SettingEnum.MAX_COURSE, String.valueOf(AppsSettingsHolder.getMaxCourse()));
            settingRepository.save(setting);
        } else {
            setting.setValue(String.valueOf(AppsSettingsHolder.getMaxCourse()));
            settingRepository.save(setting);
        }

        // количество пар в день
        setting = settingRepository.findBySettingType(SettingEnum.PAIRS_PER_DAY);
        if (setting == null) {
            setting = new Setting(SettingEnum.PAIRS_PER_DAY, String.valueOf(AppsSettingsHolder.getPairsPerDay()));
            settingRepository.save(setting);
        } else {
            setting.setValue(String.valueOf(AppsSettingsHolder.getPairsPerDay()));
            settingRepository.save(setting);
        }

        // предпочтительная дисциплина в субботу
        setting = settingRepository.findBySettingType(SettingEnum.SATURDAY_SUBJECT_TYPE_ID);
        String subjectTypeId =
                AppsSettingsHolder.getSubjectType() != null ? AppsSettingsHolder.getSubjectType().name() : null;
        if (setting == null) {
            setting = new Setting(SettingEnum.SATURDAY_SUBJECT_TYPE_ID, subjectTypeId);
            settingRepository.save(setting);
        } else {
            setting.setValue(subjectTypeId);
            settingRepository.save(setting);
        }
    }

    /**
     * Считывает настройки из базы и заносит их в статический класс и в поля формы
     */
    private void refresh() {
        List<Setting> settings = settingRepository.findAll();

        // если пустой, берем настройки из приложения
        if (settings.isEmpty()) {
            hourTimeField.setText(String.valueOf(AppsSettingsHolder.getHourTime()));
            maxCourseField.setText(String.valueOf(AppsSettingsHolder.getMaxCourse()));
            pairsPerDayField.setText(String.valueOf(AppsSettingsHolder.getPairsPerDay()));
            subjectTypeComboBox.getSelectionModel().select(AppsSettingsHolder.getSubjectType());
        }

        // считываем настройки из базы
        for (Setting s : settings) {
            if (s.getSettingType().equals(SettingEnum.HOUR_TIME)) {
                AppsSettingsHolder.setHourTime(Integer.parseInt(s.getValue()));
                hourTimeField.setText(s.getValue());
            } else if (s.getSettingType().equals(SettingEnum.MAX_COURSE)) {
                AppsSettingsHolder.setMaxCourse(Integer.parseInt(s.getValue()));
                maxCourseField.setText(s.getValue());
            } else if (s.getSettingType().equals(SettingEnum.PAIRS_PER_DAY)) {
                AppsSettingsHolder.setPairsPerDay(Integer.parseInt(s.getValue()));
                pairsPerDayField.setText(s.getValue());
            } else if (s.getSettingType().equals(SettingEnum.SATURDAY_SUBJECT_TYPE_ID)) {
                String subjectTypeId = s.getValue();
                if (subjectTypeId != null) {
                    SubjectTypeEnum subjectType = SubjectTypeEnum.valueOf(subjectTypeId);
                    AppsSettingsHolder.setSubjectType(subjectType);
                    subjectTypeComboBox.getSelectionModel().select(subjectType);
                } else {
                    subjectTypeComboBox.getSelectionModel().clearSelection();
                }
            }
        }
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

        refresh();
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
