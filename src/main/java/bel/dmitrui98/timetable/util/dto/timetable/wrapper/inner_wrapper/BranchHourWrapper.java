package bel.dmitrui98.timetable.util.dto.timetable.wrapper.inner_wrapper;

import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class BranchHourWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long branchId;
    private HourTypeEnum hourType;
}
