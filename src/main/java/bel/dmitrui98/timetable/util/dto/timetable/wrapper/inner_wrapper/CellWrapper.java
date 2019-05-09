package bel.dmitrui98.timetable.util.dto.timetable.wrapper.inner_wrapper;

import bel.dmitrui98.timetable.util.dto.timetable.wrapper.BranchHourListWrapper;
import bel.dmitrui98.timetable.util.dto.timetable.wrapper.ContextMenuWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class CellWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private long groupId;
    private int verticalCellIndex;
    private BranchHourListWrapper listDtoWrapper;
    private ContextMenuWrapper contextMenuWrapper;
}
