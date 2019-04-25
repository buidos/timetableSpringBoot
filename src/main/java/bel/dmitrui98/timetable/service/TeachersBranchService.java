package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.dto.TeacherBranchDto;
import bel.dmitrui98.timetable.util.exception.AppsException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.REC_NOT_DELETED;
import static bel.dmitrui98.timetable.util.exception.ExceptionType.REC_NOT_SAVED;

@Service
public class TeachersBranchService implements BaseService<TeachersBranch, Long> {

    @Autowired
    private TeachersBranchRepository teachersBranchRepository;

    @Override
    public void save(TeachersBranch entity) throws AppsException {
        try {
            teachersBranchRepository.save(entity);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    @Override
    public void delete(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(List<TeachersBranch> entities) throws AppsException {
        try {
            teachersBranchRepository.deleteAll(entities);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Transactional(rollbackFor = AppsException.class)
    public void deleteAllByDto(List<TeacherBranchDto> dtoList) throws AppsException {
        List<TeachersBranch> branchList = dtoList.stream()
                .map(TeacherBranchDto::getTeachersBranch)
                .collect(Collectors.toList());
        branchList = teachersBranchRepository.groupsAndTeacherFetch(branchList);
        for (TeachersBranch tb : branchList) {
            for (StudyGroup g : tb.getStudyGroupSet()) {
                g.getTeachersBranchSet().remove(tb);
            }
            for (Teacher t : tb.getTeacherSet()) {
                t.getTeachersBranchSet().remove(tb);
            }
        }
        try {
            teachersBranchRepository.deleteAll(branchList);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    public void deleteAllByIds(List<Long> longs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TeachersBranch findByIdThrow(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TeachersBranch> findAll() {
        return teachersBranchRepository.findAll();
    }

    /**
     * Находит связку в базе для данной группы по учителям
     * @param teachers учителя из связки
     * @param group группа
     * @return связка для группы
     */
    public TeachersBranch findByTeachersAndGroup(List<Teacher> teachers, StudyGroup group) {
        List<TeachersBranch> teachersBranches = teachersBranchRepository.findByTeachersInAndGroup(teachers, group);

        TeachersBranch resultTeacherBranch = null;
        for (TeachersBranch tb : teachersBranches) {
            Set<Teacher> branchTeachers = tb.getTeacherSet();
            if (CollectionUtils.isEqualCollection(branchTeachers, teachers)) {
                resultTeacherBranch = tb;
                break;
            }
        }
        return resultTeacherBranch;
    }
}
