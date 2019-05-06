package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.control.LoadLabel;
import bel.dmitrui98.timetable.control.TimetableLabel;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.util.color.ColorUtil;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import bel.dmitrui98.timetable.util.enums.ColorEnum;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для отрисовки колонки расписания при выделении нагрузки
 */
@Service
public class ColorService {

    @Autowired
    private IntersectionService intersectionService;

    @Autowired
    private TimetableService timetableService;

    public void paintTimetableColumn(int currentCol, int previousCol, LoadLabel loadLabel) {
        GridPane timetableGrid = timetableService.getTimetableGrid();

        for (Node node : timetableGrid.getChildren()) {
            if (GridPane.getColumnIndex(node) == currentCol) {
                TimetableLabel cell = (TimetableLabel) node;
                paintCell(loadLabel, cell);
            } else if (GridPane.getColumnIndex(node) == previousCol) {
                ColorUtil.setBackgroundColor(node, ColorEnum.WHITE.getColor());
            }
        }
    }

    public void paintCell(LoadLabel loadLabel, TimetableLabel cell) {
        if (cell.getTimetableListDto().isEmpty()) {
            ColorUtil.setBackgroundColor(cell, ColorEnum.GREEN.getColor());
            return;
        }
        // обрабатываем одну и ту же ячейку
        // блокируем, если в одну и ту же группу в одно и то же время пытаются поставить еще одну связку
        List<HourTypeEnum> hourTypes = cell.getTimetableListDto().getTimetableDtoList().stream()
                .map(TimetableDto::getHourType)
                .collect(Collectors.toList());
        int countDisable = 0;
        for (HourTypeEnum hourType : HourTypeEnum.values()) {
            if (intersectionService.checkByHourTypesSameCell(hourTypes, hourType)) {
                countDisable++;
                continue;
            }

            // обрабатываем одну и ту же связку в одну и ту же ячейку
            List<TeachersBranch> teachersBranches = cell.getTimetableListDto().getTimetableDtoList().stream()
                    .map(TimetableDto::getBranch)
                    .collect(Collectors.toList());
            if (teachersBranches.contains(loadLabel.getLoadDto().getBranch())) {
                if (intersectionService.checkByHourTypesSameBranch(hourTypes, hourType)) {
                    countDisable++;
                    continue;
                }
            }
            // блокируем если есть пересечение (один и тот же преподаватель не может вести пару в одно и то же время)
            if (intersectionService.isIntersects(cell.getTimetableListDto(), loadLabel.getLoadDto(), hourType)) {
                countDisable++;
            }
        }
        if (countDisable == HourTypeEnum.values().length) {
            ColorUtil.setBackgroundColor(cell, ColorEnum.RED.getColor());
        } else {
            ColorUtil.setBackgroundColor(cell, ColorEnum.YELLOW.getColor());
        }
    }
}
