package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.repository.SpecialtyRepository;
import bel.dmitrui98.timetable.repository.StudyGroupRepository;
import bel.dmitrui98.timetable.util.dto.timetable.CriteriaCheckComboBoxesDto;
import bel.dmitrui98.timetable.util.enums.DayEnum;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CriteriaService {

    private static final int MAX_WIDTH = 200;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @SuppressWarnings(value = "all")
    public void enableSelectAll(CriteriaCheckComboBoxesDto dto) {
        dto.getDepCheckComboBox().getCheckModel().getCheckedItems().addListener(new ListChangeListener() {
            private boolean change = false;
            private boolean previousSelectAll = true;
            @Override
            public void onChanged(Change c) {
                boolean currentSelectAll = dto.getDepCheckComboBox().getCheckModel().isChecked(0);
                if (currentSelectAll != previousSelectAll) {
                    if (!change && currentSelectAll) {
                        // trigger no more calls to checkAll when the selected items are modified by checkAll
                        change = true;
                        dto.getDepCheckComboBox().getCheckModel().checkAll();
                        change = false;
                    } else {
                        if (!change && !currentSelectAll) {
                            change = true;
                            dto.getDepCheckComboBox().getCheckModel().clearChecks();
                            change = false;
                        }
                    }
                }
                previousSelectAll = currentSelectAll;
            }
        });

        dto.getSpecialtyCheckComboBox().getCheckModel().getCheckedItems().addListener(new ListChangeListener() {
            private boolean change = false;
            private boolean previousSelectAll = true;
            @Override
            public void onChanged(Change c) {
                boolean currentSelectAll = dto.getSpecialtyCheckComboBox().getCheckModel().isChecked(0);
                if (currentSelectAll != previousSelectAll) {
                    if (!change && currentSelectAll) {
                        // trigger no more calls to checkAll when the selected items are modified by checkAll
                        change = true;
                        dto.getSpecialtyCheckComboBox().getCheckModel().checkAll();
                        change = false;
                    } else {
                        if (!change && !currentSelectAll) {
                            change = true;
                            dto.getSpecialtyCheckComboBox().getCheckModel().clearChecks();
                            change = false;
                        }
                    }
                }
                previousSelectAll = currentSelectAll;
            }
        });

        dto.getGroupCheckComboBox().getCheckModel().getCheckedItems().addListener(new ListChangeListener() {
            private boolean change = false;
            private boolean previousSelectAll = true;
            @Override
            public void onChanged(Change c) {
                boolean currentSelectAll = dto.getGroupCheckComboBox().getCheckModel().isChecked(0);
                if (currentSelectAll != previousSelectAll) {
                    if (!change && currentSelectAll) {
                        // trigger no more calls to checkAll when the selected items are modified by checkAll
                        change = true;
                        dto.getGroupCheckComboBox().getCheckModel().checkAll();
                        change = false;
                    } else {
                        if (!change && !currentSelectAll) {
                            change = true;
                            dto.getGroupCheckComboBox().getCheckModel().clearChecks();
                            change = false;
                        }
                    }
                }
                previousSelectAll = currentSelectAll;
            }
        });

        dto.getDayCheckComboBox().getCheckModel().getCheckedItems().addListener(new ListChangeListener() {
            private boolean change = false;
            private boolean previousSelectAll = true;
            @Override
            public void onChanged(Change c) {
                boolean currentSelectAll = dto.getDayCheckComboBox().getCheckModel().isChecked(0);
                if (currentSelectAll != previousSelectAll) {
                    if (!change && currentSelectAll) {
                        // trigger no more calls to checkAll when the selected items are modified by checkAll
                        change = true;
                        dto.getDayCheckComboBox().getCheckModel().checkAll();
                        change = false;
                    } else {
                        if (!change && !currentSelectAll) {
                            change = true;
                            dto.getDayCheckComboBox().getCheckModel().clearChecks();
                            change = false;
                        }
                    }
                }
                previousSelectAll = currentSelectAll;
            }
        });
    }

    public void tuningDataModel(CriteriaCheckComboBoxesDto dto) {
        dto.getDepCheckComboBox().setConverter(new StringConverter<Department>() {
            @Override
            public String toString(Department object) {
                if (object == null) {
                    return "выделить все";
                }
                return object.getName();
            }

            @Override
            public Department fromString(String string) {
                return null;
            }
        });
        dto.getDepCheckComboBox().setMaxWidth(MAX_WIDTH);

        dto.getSpecialtyCheckComboBox().setConverter(new StringConverter<Specialty>() {
            @Override
            public String toString(Specialty object) {
                if (object == null) {
                    return "выделить все";
                }
                return object.getName();
            }

            @Override
            public Specialty fromString(String string) {
                return null;
            }
        });
        dto.getSpecialtyCheckComboBox().setMaxWidth(MAX_WIDTH);

        dto.getGroupCheckComboBox().setConverter(new StringConverter<StudyGroup>() {
            @Override
            public String toString(StudyGroup object) {
                if (object == null) {
                    return "выделить все";
                }
                return object.getName();
            }

            @Override
            public StudyGroup fromString(String string) {
                return null;
            }
        });
        dto.getGroupCheckComboBox().setMaxWidth(MAX_WIDTH);

        dto.getDayCheckComboBox().setConverter(new StringConverter<DayEnum>() {
            @Override
            public String toString(DayEnum object) {
                if (object == null) {
                    return "выделить все";
                }
                return object.getName();
            }

            @Override
            public DayEnum fromString(String string) {
                return null;
            }
        });
        dto.getDayCheckComboBox().setMaxWidth(MAX_WIDTH);
    }

    private List<Integer> checkedIndexes;
    public void addOnHiddenListener(CriteriaCheckComboBoxesDto dto) {
        // обновляем критерии для отделений
        // были ли изменения
        dto.getDepCheckComboBox().addEventHandler(ComboBox.ON_SHOWN, event -> {
            checkedIndexes = new ArrayList<>(dto.getDepCheckComboBox().getCheckModel().getCheckedIndices());
        });
        dto.getDepCheckComboBox().addEventHandler(ComboBox.ON_HIDDEN, event -> {

            ObservableList<Integer> currentChecked = dto.getDepCheckComboBox().getCheckModel().getCheckedIndices();
            if (!CollectionUtils.isEqualCollection(checkedIndexes, currentChecked)) {
                ObservableList<Department> departments = dto.getDepCheckComboBox().getCheckModel().getCheckedItems();
                List<Specialty> specialties = specialtyRepository.findByDepartmentIn(departments);

                dto.getSpecialtyCheckComboBox().getItems().clear();
                dto.getGroupCheckComboBox().getItems().clear();
                if (!specialties.isEmpty()) {
                    dto.getSpecialtyCheckComboBox().getItems().add(null);
                    dto.getSpecialtyCheckComboBox().getItems().addAll(specialties);
                    dto.getSpecialtyCheckComboBox().getCheckModel().checkAll();

                    List<StudyGroup> groups = studyGroupRepository.findBySpecialtyIn(specialties);
                    if (!groups.isEmpty()) {

                        dto.getGroupCheckComboBox().getItems().add(null);
                        dto.getGroupCheckComboBox().getItems().addAll(groups);
                        dto.getGroupCheckComboBox().getCheckModel().checkAll();
                    }
                }
            }
        });

        // обновляем критерии для специальностей
        // были ли изменения
        dto.getSpecialtyCheckComboBox().addEventHandler(ComboBox.ON_SHOWN, event -> {
            checkedIndexes = new ArrayList<>(dto.getSpecialtyCheckComboBox().getCheckModel().getCheckedIndices());
        });
        dto.getSpecialtyCheckComboBox().addEventHandler(ComboBox.ON_HIDDEN, event -> {
            ObservableList<Integer> currentChecked = dto.getSpecialtyCheckComboBox().getCheckModel().getCheckedIndices();
            if (!CollectionUtils.isEqualCollection(checkedIndexes, currentChecked)) {
                ObservableList<Specialty> specialties = dto.getSpecialtyCheckComboBox().getCheckModel().getCheckedItems();
                List<StudyGroup> groups = studyGroupRepository.findBySpecialtyIn(specialties);
                dto.getGroupCheckComboBox().getItems().clear();
                if (!groups.isEmpty()) {

                    dto.getGroupCheckComboBox().getItems().add(null);
                    dto.getGroupCheckComboBox().getItems().addAll(groups);
                    dto.getGroupCheckComboBox().getCheckModel().checkAll();
                }
            }
        });
    }
}
