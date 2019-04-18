package bel.dmitrui98.timetable.util.dto.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.controlsfx.control.CheckComboBox;

@AllArgsConstructor
@Getter
public class CriteriaCheckComboBoxesDto {
    private CheckComboBox<Department> depCheckComboBox;
    private CheckComboBox<Specialty> specialtyCheckComboBox;
    private CheckComboBox<StudyGroup> groupCheckComboBox;
    private CheckComboBox<DayEnum> dayCheckComboBox;
}
