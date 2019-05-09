package bel.dmitrui98.timetable.util.dto.timetable.wrapper;

import bel.dmitrui98.timetable.control.TimetableContextMenu;
import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.repository.StudyGroupRepository;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableListDto;
import bel.dmitrui98.timetable.util.dto.timetable.wrapper.inner_wrapper.CellWrapper;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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
public class CellListWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CellWrapper> timetableList = new ArrayList<>();

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public CellListWrapper(List<TimetableListDto> timetableList) {
        for (TimetableListDto listDto : timetableList) {
            StudyGroup group = listDto.getGroup();
            int verticalCellIndex = listDto.getVerticalCellIndex();
            BranchHourListWrapper listDtoWrapper = new BranchHourListWrapper(listDto.getTimetableDtoList());
            ContextMenuWrapper contextMenuWrapper = new ContextMenuWrapper(listDto.getContextMenu());
            this.timetableList.add(new CellWrapper(group.getStudyGroupId(), verticalCellIndex, listDtoWrapper, contextMenuWrapper));
        }
    }

    /**
     * Восстановление расписание
     * @return восстановленное расписание. Ячейка, от куда бралась нагрузка восстанавливается по связке и группе
     * при отображении расписания
     */
    public List<TimetableListDto> getTimetableListDto() {
        List<TimetableListDto> list = new ArrayList<>();

        List<Long> groupIds = this.timetableList.stream()
                .map(CellWrapper::getGroupId)
                .collect(Collectors.toList());
        List<StudyGroup> groups = studyGroupRepository.findByStudyGroupIdIn(groupIds);

        for (CellWrapper innerWrapper : this.timetableList) {

            for (StudyGroup group : groups) {
                if (group.getStudyGroupId().equals(innerWrapper.getGroupId())) {
                    TimetableListDto listDto = new TimetableListDto(innerWrapper.getVerticalCellIndex(), group);
                    TimetableContextMenu contextMenu = applicationContext.getBean(TimetableContextMenu.class);
                    setIsSelected(contextMenu, innerWrapper);
                    listDto.setContextMenu(contextMenu);
                    listDto.setTimetableDtoList(innerWrapper.getListDtoWrapper().getTimetableDtoList());

                    list.add(listDto);
                }
            }
        }
        return list;
    }

    private void setIsSelected(TimetableContextMenu contextMenu, CellWrapper innerClassDto) {
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
