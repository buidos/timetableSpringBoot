package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.control.LoadLabel;
import bel.dmitrui98.timetable.control.TimetableLabel;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с нагрузкой. Вычитает или прибавляет часы нагрузки связки преподавателей в зависимости от типа часа
 */
@Service
public class LoadService {

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private TeachersBranchRepository teachersBranchRepository;

    /**
     * Установка нагрузки в расписание
     * @param cell куда устанавливается нагрузка
     * @param loadCell выбранная ячейка с нагрузкой
     * @param hourType тип часа
     * @param isDelete удаляется ли нагрузка из ячейки
     */
    public void setUpLoadToTimetable(TimetableLabel cell, LoadLabel loadCell, HourTypeEnum hourType, boolean isDelete) {
        System.out.println(hourType + " isDelete " + isDelete);
    }
}
