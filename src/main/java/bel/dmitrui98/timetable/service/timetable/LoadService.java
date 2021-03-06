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

    @Autowired
    private ColorService colorService;

    /**
     * Установка нагрузки в расписание
     * @param cell куда устанавливается нагрузка
     * @param hourType тип часа
     * @param isDeleteFromCell удаляется ли нагрузка из ячейки расписания
     */
    public void setUpLoadToTimetable(TimetableLabel cell, LoadLabel loadCell, HourTypeEnum hourType, boolean isDeleteFromCell) {
        timetableService.setEdit(true);

        // сколько минут отнимается или прибавляется
        int minutesInTwoWeek = (int) ((AppsSettingsHolder.getHourTime() * 2) * hourType.getHour());
        List<TimetableListDto> timetableList = timetableService.getTimetableList();

        // обновляем общую ячейку с часами
        loadCell.refreshCommonHourCell(minutesInTwoWeek, isDeleteFromCell);

        if (!isDeleteFromCell) {
            // если нагрузка добавляется в ячейку расписания, то отнимаем нагрузку из связки учителей
            int currentMinutes = loadCell.getLoadDto().getCountMinutesInTwoWeek();
            int minutes = currentMinutes - minutesInTwoWeek;
            loadCell.getLoadDto().setCountMinutesInTwoWeek(minutes);
            loadCell.refresh();

            cell.getTimetableListDto().addDto(new TimetableDto(loadCell.getLoadDto().getBranch(), hourType, loadCell));
            cell.refresh();

            if (!timetableList.contains(cell.getTimetableListDto())) {
                timetableList.add(cell.getTimetableListDto());
            }

        } else {
            // нагрузка удаляется из ячейки расписания. Добавляем нагрузку к связке учителей
            // извлекаем ячейку, из которой бралась нагрузка для данной ячейки расписания

            // берем первый элемент с переданным типом часа
            loadCell = cell.getTimetableListDto().getTimetableDtoList().stream()
                    .filter(dto -> dto.getHourType() == hourType)
                    .findFirst().get().getLoadCell();

            int currentMinutes = loadCell.getLoadDto().getCountMinutesInTwoWeek();
            int minutes = currentMinutes + minutesInTwoWeek;
            loadCell.getLoadDto().setCountMinutesInTwoWeek(minutes);
            loadCell.refresh();
            cell.getTimetableListDto().removeDto(new TimetableDto(loadCell.getLoadDto().getBranch(), hourType, loadCell));
            cell.refresh();

            if (cell.getTimetableListDto().isEmpty()) {
                timetableList.remove(cell.getTimetableListDto());
            }
        }

        // обновляем инфо-панель
        timetableService.refreshInfoPanel();

        // перерисовываем текущую колонку
        LoadLabel selectedLoadCell = timetableService.getSelectedLoadLabel();
        if (selectedLoadCell != null) {
            colorService.paintTimetableColumn(selectedLoadCell.getCol(), -1, selectedLoadCell);
        }
    }
}
