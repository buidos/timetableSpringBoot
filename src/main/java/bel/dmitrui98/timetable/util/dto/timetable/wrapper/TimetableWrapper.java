package bel.dmitrui98.timetable.util.dto.timetable.wrapper;

import bel.dmitrui98.timetable.control.TimetableContextMenu;
import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.repository.StudyGroupRepository;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableListDto;
import bel.dmitrui98.timetable.util.dto.timetable.wrapper.inner_wrapper.ListDtoInnerWrapper;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-обертка. Используется для сохранения расписания
 */
@NoArgsConstructor
@Getter
@Setter
public class TimetableWrapper implements Serializable {

    private List<ListDtoInnerWrapper> timetableList = new ArrayList<>();

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    public TimetableWrapper(List<TimetableListDto> timetableList) {
        for (TimetableListDto listDto : timetableList) {
            StudyGroup group = listDto.getGroup();
            int verticalCellIndex = listDto.getVerticalCellIndex();
            TimetableListDtoWrapper listDtoWrapper = new TimetableListDtoWrapper(listDto.getTimetableDtoList());
            ContextMenuWrapper contextMenuWrapper = new ContextMenuWrapper(listDto.getContextMenu());
            this.timetableList.add(new ListDtoInnerWrapper(group.getStudyGroupId(), verticalCellIndex, listDtoWrapper, contextMenuWrapper));
        }
    }

    /**
     * Восстановление расписание
     * @param contextMenu пустой бин контекстного меню
     * @return восстановленное расписание. Ячейка, от куда бралась нагрузка восстанавливается по связке и группе
     * при отображении расписания
     */
    public List<TimetableListDto> getTimetableListDto(TimetableContextMenu contextMenu) {
        List<TimetableListDto> list = new ArrayList<>();

        List<Long> groupIds = this.timetableList.stream()
                .map(ListDtoInnerWrapper::getGroupId)
                .collect(Collectors.toList());
        List<StudyGroup> groups = studyGroupRepository.findByStudyGroupIdIn(groupIds);

        for (ListDtoInnerWrapper innerWrapper : this.timetableList) {

            for (StudyGroup group : groups) {
                if (group.getStudyGroupId().equals(innerWrapper.getGroupId())) {
                    TimetableListDto listDto = new TimetableListDto(innerWrapper.getVerticalCellIndex(), group);
                    setIsSelected(contextMenu, innerWrapper);
                    listDto.setContextMenu(contextMenu);
                    listDto.setTimetableDtoList(innerWrapper.getListDtoWrapper().getTimetableDtoList());

                    list.add(listDto);
                }
            }
        }
        return list;
    }

    private void setIsSelected(TimetableContextMenu contextMenu, ListDtoInnerWrapper innerClassDto) {
        Boolean isSelected;
        List<Boolean> isSelectedList = innerClassDto.getContextMenuWrapper().getIsSelectedList();
        for (int i = 0, j = 0; i < contextMenu.getItems().size() && j < isSelectedList.size(); i++) {
            MenuItem item = contextMenu.getItems().get(i);
            if (item instanceof SeparatorMenuItem) {
                continue;
            }

            CheckMenuItem checkItem;
            if (item instanceof Menu) {
                Menu menu = (Menu) item;
                for (MenuItem menuItem : menu.getItems()) {
                    isSelected = isSelectedList.get(j++);

                    checkItem = (CheckMenuItem) menuItem;
                    checkItem.setSelected(isSelected);
                }
            } else {
                isSelected = isSelectedList.get(j++);

                checkItem = (CheckMenuItem) item;
                checkItem.setSelected(isSelected);
            }
        }
    }
}
