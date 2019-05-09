package bel.dmitrui98.timetable.util.dto.timetable.wrapper.inner_wrapper;

import bel.dmitrui98.timetable.util.dto.timetable.wrapper.ContextMenuWrapper;
import bel.dmitrui98.timetable.util.dto.timetable.wrapper.TimetableListDtoWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class ListDtoInnerWrapper implements Serializable {
    private long groupId;
    private int verticalCellIndex;
    private TimetableListDtoWrapper listDtoWrapper;
    private ContextMenuWrapper contextMenuWrapper;
}
