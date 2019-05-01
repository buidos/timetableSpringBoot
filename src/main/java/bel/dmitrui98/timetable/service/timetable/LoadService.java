package bel.dmitrui98.timetable.service.timetable;

import bel.dmitrui98.timetable.control.LoadLabel;
import bel.dmitrui98.timetable.control.TimetableLabel;
import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableListDto;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с нагрузкой. Вычитает или прибавляет часы нагрузки связки преподавателей в зависимости от типа часа
 */
@Service
public class LoadService {

    @Autowired
    private TimetableService timetableService;

    /**
     * Установка нагрузки в расписание
     * @param cell куда устанавливается нагрузка
     * @param loadCell выбранная ячейка с нагрузкой
     * @param hourType тип часа
     * @param isDeleteFromCell удаляется ли нагрузка из ячейки расписания
     */
    public void setUpLoadToTimetable(TimetableLabel cell, LoadLabel loadCell, HourTypeEnum hourType, boolean isDeleteFromCell) {
        int minutesInTwoWeek = (int) ((AppsSettingsHolder.getHourTime() * 2) * hourType.getHour());
        List<TimetableListDto> timetableList = timetableService.getTimetableList();

        if (!isDeleteFromCell) {
            // если нагрузка добавляется в ячейку расписания, то отнимаем нагрузку из связки учителей
            int currentMinutes = loadCell.getLoadDto().getCountMinutesInTwoWeek();
            int minutes = currentMinutes - minutesInTwoWeek;
            loadCell.getLoadDto().setCountMinutesInTwoWeek(minutes);
            loadCell.refresh(-minutesInTwoWeek);

            cell.getTimetableListDto().addDto(new TimetableDto(loadCell.getLoadDto().getBranch(), hourType, loadCell));
            cell.refresh();

            timetableList.add(cell.getTimetableListDto());
        } else {
            // нагрузка удаляется из ячейки расписания. Добавляем нагрузку к связке учителей
            // извлекаем ячейку, из которой бралась нагрузка для данной ячейки расписания
            loadCell = cell.getTimetableListDto().getTimetableDtoList().stream().findFirst().get().getLoadCell();
            int currentMinutes = loadCell.getLoadDto().getCountMinutesInTwoWeek();
            int minutes = currentMinutes + minutesInTwoWeek;
            loadCell.getLoadDto().setCountMinutesInTwoWeek(minutes);
            loadCell.refresh(minutesInTwoWeek);
            cell.getTimetableListDto().removeDto(new TimetableDto(loadCell.getLoadDto().getBranch(), hourType, loadCell));
            cell.refresh();

            if (cell.getTimetableListDto().isEmpty()) {
                timetableList.remove(cell.getTimetableListDto());
            }
        }


        System.out.println(hourType + " isDelete " + isDeleteFromCell);
    }
}
