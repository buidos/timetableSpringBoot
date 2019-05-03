package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.util.dto.timetable.LoadDto;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableListDto;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum.*;

/**
 * Сервис для определения пересечения преподавателей в расписании
 */
@Service
public class IntersectionService {

    @Autowired
    private TimetableService timetableService;

    /**
     * Есть ли пересечение (один и тот же преподаватель не может вести пару в одно и то же время)
     * @param currentDto ячейка расписания, которую нужно проверить
     * @param loadDto dto выделенной ячейки нагрузки
     * @param hourType тип часа, который нужно проверить
     * @return есть ли пересечение для переданного dto ячейки расписания
     */
    public boolean isIntersects(TimetableListDto currentDto, LoadDto loadDto, HourTypeEnum hourType) {
        int currentIndex = currentDto.getVerticalCellIndex();
        List<TimetableListDto> timetableList = timetableService.getTimetableList();
        List<TimetableListDto> list = timetableList.stream()
                .filter(o -> o.getVerticalCellIndex() == currentIndex)
                .collect(Collectors.toList());

        boolean isIntersects = false;
        for (TimetableListDto dto : list) {
            Map<HourTypeEnum, List<Teacher>> hourTeachersMap = getHourTeachersMap(dto);
            for (Map.Entry<HourTypeEnum, List<Teacher>> entry : hourTeachersMap.entrySet()) {
                for (Teacher teacher : entry.getValue()) {
                    for (Teacher t : loadDto.getBranch().getTeacherSet()) {
                        if (teacher.equals(t)) {
                            if (hourType.equals(entry.getKey())) {
                                return true;
                            }
                            isIntersects = checkByHourType(entry.getKey(), hourType);
                                if (isIntersects) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return isIntersects;
    }

    private boolean checkByHourType(HourTypeEnum installedHourType, HourTypeEnum hourType) {
        boolean isIntersects = false;
        switch (installedHourType) {
            case TWO_WEEKS:
                isIntersects = true;
                break;
            case NUMERATOR:
                if (hourType == NUM_HALF_BEGIN || hourType == NUM_HALF_END || hourType == TWO_WEEKS
                        || hourType == WEEK_HALF_BEGIN || hourType == WEEK_HALF_END) {
                    isIntersects = true;
                }
                break;
            case DENOMINATOR:
                if (hourType == DEN_HALF_BEGIN || hourType == DEN_HALF_END || hourType == TWO_WEEKS
                        || hourType == WEEK_HALF_BEGIN || hourType == WEEK_HALF_END) {
                    isIntersects = true;
                }
                break;
            case NUM_HALF_BEGIN:
                if (hourType == NUMERATOR || hourType == TWO_WEEKS || hourType == WEEK_HALF_BEGIN) {
                    isIntersects = true;
                }
                break;
            case NUM_HALF_END:
                if (hourType == NUMERATOR || hourType == TWO_WEEKS || hourType == WEEK_HALF_END) {
                    isIntersects = true;
                }
                break;
            case DEN_HALF_BEGIN:
                if (hourType == DENOMINATOR || hourType == TWO_WEEKS || hourType == WEEK_HALF_BEGIN) {
                    isIntersects = true;
                }
                break;
            case DEN_HALF_END:
                if (hourType == DENOMINATOR || hourType == TWO_WEEKS || hourType == WEEK_HALF_END) {
                    isIntersects = true;
                }
                break;
        }

        return isIntersects;
    }

    private Map<HourTypeEnum, List<Teacher>> getHourTeachersMap(TimetableListDto currentDto) {
        Map<TeachersBranch, HourTypeEnum> branchHourMap = currentDto.getTimetableDtoList().stream()
                .collect(Collectors.toMap(TimetableDto::getBranch, TimetableDto::getHourType));
        Map<HourTypeEnum, List<Teacher>> hourTeachersMap = new HashMap<>();
        for (Map.Entry<TeachersBranch, HourTypeEnum> entry : branchHourMap.entrySet()) {
            List<Teacher> list = new ArrayList<>(entry.getKey().getTeacherSet());
            List<Teacher> resultHourList = hourTeachersMap.get(entry.getValue());
            if (resultHourList == null) {
                hourTeachersMap.put(entry.getValue(), list);
            } else {
                resultHourList.addAll(list);
            }
        }
        return hourTeachersMap;
    }
}
