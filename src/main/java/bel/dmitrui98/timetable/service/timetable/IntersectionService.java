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
     * @return есть ли пересечение для переданного dto ячейки расписания
     */
    public boolean isIntersects(TimetableListDto currentDto, LoadDto loadDto, HourTypeEnum hourType) {
        int currentIndex = currentDto.getVerticalCellIndex();
        List<TimetableListDto> timetableList = timetableService.getTimetableList();
        List<TimetableListDto> list = timetableList.stream()
                .filter(o -> o.getVerticalCellIndex() == currentIndex)
                .collect(Collectors.toList());

        for (TimetableListDto dto : list) {
            Map<HourTypeEnum, List<Teacher>> hourTeachersMap = getHourTeachersMap(dto);
            for (Map.Entry<HourTypeEnum, List<Teacher>> entry : hourTeachersMap.entrySet()) {
                for (Teacher teacher : entry.getValue()) {
                    for (Teacher t : loadDto.getBranch().getTeacherSet()) {
                        if (hourType.equals(entry.getKey()) && teacher.equals(t)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Map<HourTypeEnum, List<Teacher>> getHourTeachersMap(TimetableListDto currentDto) {
        Map<TeachersBranch, HourTypeEnum> branchHourMap = currentDto.getTimetableDtoList().stream()
                .collect(Collectors.toMap(TimetableDto::getBranch, TimetableDto::getHourType));
        Map<HourTypeEnum, List<Teacher>> teacherHourMap = new HashMap<>();
        for (Map.Entry<TeachersBranch, HourTypeEnum> entry : branchHourMap.entrySet()) {
            List<Teacher> list = new ArrayList<>(entry.getKey().getTeacherSet());
            List<Teacher> resultHourList = teacherHourMap.get(entry.getValue());
            if (resultHourList == null) {
                teacherHourMap.put(entry.getValue(), list);
            } else {
                resultHourList.addAll(list);
            }
        }
        return teacherHourMap;
    }
}
