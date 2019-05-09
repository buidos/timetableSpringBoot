package bel.dmitrui98.timetable.util.dto.timetable.wrapper;

import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import bel.dmitrui98.timetable.util.dto.timetable.wrapper.inner_wrapper.BranchHourWrapper;
import bel.dmitrui98.timetable.util.enums.timetable.HourTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class BranchHourListWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<BranchHourWrapper> timetableDtoInnerClassList = new ArrayList<>();

    @Autowired
    private TeachersBranchRepository teachersBranchRepository;

    public BranchHourListWrapper(List<TimetableDto> list) {
        for (TimetableDto dto : list) {
            TeachersBranch branch = dto.getBranch();
            HourTypeEnum hourType = dto.getHourType();
            this.timetableDtoInnerClassList.add(new BranchHourWrapper(branch.getTeacherBranchId(), hourType));
        }
    }

    public List<TimetableDto> getTimetableDtoList() {
        List<TimetableDto> timetableDtoList = new ArrayList<>();
        List<Long> branchIds = timetableDtoInnerClassList.stream()
                .map(BranchHourWrapper::getBranchId)
                .collect(Collectors.toList());
        List<TeachersBranch> branches = teachersBranchRepository.findByTeacherBranchIdIn(branchIds);
        for (BranchHourWrapper innerClassDto : timetableDtoInnerClassList) {
            for (TeachersBranch branch : branches) {
                if (branch.getTeacherBranchId().equals(innerClassDto.getBranchId())) {
                    TimetableDto dto = new TimetableDto(branch, innerClassDto.getHourType());
                    timetableDtoList.add(dto);
                }
            }
        }
        return timetableDtoList;
    }

    @XmlTransient
    public TeachersBranchRepository getTeachersBranchRepository() {
        return teachersBranchRepository;
    }
}
