package bel.dmitrui98.timetable.util.dto.timetable.wrapper;

import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.dto.timetable.TimetableDto;
import bel.dmitrui98.timetable.util.dto.timetable.wrapper.inner_wrapper.TimetableDtoInnerWrapper;
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
public class TimetableListDtoWrapper implements Serializable {

    private List<TimetableDtoInnerWrapper> timetableDtoInnerClassList = new ArrayList<>();

    @Autowired
    private TeachersBranchRepository teachersBranchRepository;

    public TimetableListDtoWrapper(List<TimetableDto> list) {
        for (TimetableDto dto : list) {
            TeachersBranch branch = dto.getBranch();
            HourTypeEnum hourType = dto.getHourType();
            this.timetableDtoInnerClassList.add(new TimetableDtoInnerWrapper(branch.getTeacherBranchId(), hourType));
        }
    }

    public List<TimetableDto> getTimetableDtoList() {
        List<TimetableDto> timetableDtoList = new ArrayList<>();
        List<Long> branchIds = timetableDtoInnerClassList.stream()
                .map(TimetableDtoInnerWrapper::getBranchId)
                .collect(Collectors.toList());
        List<TeachersBranch> branches = teachersBranchRepository.findByTeacherBranchIdIn(branchIds);
        for (TimetableDtoInnerWrapper innerClassDto : timetableDtoInnerClassList) {
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
