package bel.dmitrui98.timetable.entity;

import bel.dmitrui98.timetable.util.enums.SettingEnum;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@NoArgsConstructor
@Entity
public class Setting {

    private ObjectProperty<SettingEnum> settingType = new SimpleObjectProperty<>();
    private StringProperty value = new SimpleStringProperty();


    @Id
    @Enumerated(EnumType.STRING)
    public SettingEnum getSettingType() {
        return settingType.get();
    }

    public ObjectProperty<SettingEnum> settingTypeProperty() {
        return settingType;
    }

    public void setSettingType(SettingEnum settingType) {
        this.settingType.set(settingType);
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
