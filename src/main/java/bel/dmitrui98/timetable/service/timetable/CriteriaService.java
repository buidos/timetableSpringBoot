package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.util.dto.timetable.CriteriaCheckComboBoxesDto;
import javafx.collections.ListChangeListener;
import org.springframework.stereotype.Service;

@Service
public class CriteriaService {
    public void enableSelectAll(CriteriaCheckComboBoxesDto dto) {
        dto.getDepCheckComboBox().getCheckModel().getCheckedItems().addListener(new ListChangeListener() {

            private boolean change = false;

            @Override
            public void onChanged(Change c) {
                if (!change && dto.getDepCheckComboBox().getCheckModel().isChecked(0)) {
                    // trigger no more calls to checkAll when the selected items are modified by checkAll
                    change = true;
                    dto.getDepCheckComboBox().getCheckModel().checkAll();
                    change = false;
                } else {
                    if (!change) {
                        change = true;
                        dto.getDepCheckComboBox().getCheckModel().clearChecks();
                        change = false;
                    }
                }
            }
        });

        dto.getSpecialtyCheckComboBox().getCheckModel().getCheckedItems().addListener(new ListChangeListener() {

            private boolean change = false;

            @Override
            public void onChanged(Change c) {
                if (!change && dto.getSpecialtyCheckComboBox().getCheckModel().isChecked(0)) {
                    // trigger no more calls to checkAll when the selected items are modified by checkAll
                    change = true;
                    dto.getSpecialtyCheckComboBox().getCheckModel().checkAll();
                    change = false;
                } else {
                    if (!change) {
                        change = true;
                        dto.getSpecialtyCheckComboBox().getCheckModel().clearChecks();
                        change = false;
                    }
                }
            }
        });

        dto.getDayCheckComboBox().getCheckModel().getCheckedItems().addListener(new ListChangeListener() {

            private boolean change = false;

            @Override
            public void onChanged(Change c) {
                if (!change && dto.getDayCheckComboBox().getCheckModel().isChecked(0)) {
                    // trigger no more calls to checkAll when the selected items are modified by checkAll
                    change = true;
                    dto.getDayCheckComboBox().getCheckModel().checkAll();
                    change = false;
                } else {
                    if (!change) {
                        change = true;
                        dto.getDayCheckComboBox().getCheckModel().clearChecks();
                        change = false;
                    }
                }
            }
        });

        dto.getGroupCheckComboBox().getCheckModel().getCheckedItems().addListener(new ListChangeListener() {

            private boolean change = false;

            @Override
            public void onChanged(Change c) {
                if (!change && dto.getGroupCheckComboBox().getCheckModel().isChecked(0)) {
                    // trigger no more calls to checkAll when the selected items are modified by checkAll
                    change = true;
                    dto.getGroupCheckComboBox().getCheckModel().checkAll();
                    change = false;
                } else {
                    if (!change) {
                        change = true;
                        dto.getGroupCheckComboBox().getCheckModel().clearChecks();
                        change = false;
                    }
                }
            }
        });
    }
}
