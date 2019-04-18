package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.entity.TeacherBranchPK;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.util.dto.TeacherBranchDto;
import bel.dmitrui98.timetable.util.exception.AppsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public void deleteAllDto(List<TeacherBranchDto> dtoList) throws AppsException {
        for (TeacherBranchDto dto : dtoList) {
            for (Teacher t : dto.getTeacherBranch()) {
                teachersBranchRepository.deleteById(new TeacherBranchPK(dto.getTeacherBranchId(), t));
            }
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
     * Получает существующую связку для группы по переданным учителям
     * @param teachers учителя, по которым ищется связка
     * @param group группа, для которой ищутся связки
     */
    public List<TeachersBranch> getTeachersBranches(List<Teacher> teachers, StudyGroup group) {
        List<TeachersBranch> allTeachersBranches = teachersBranchRepository.findByGroup(group);
        List<TeachersBranch> resultTeachersBranches = new ArrayList<>();

        List<Long> teacherIds = teachers.stream()
                .map(Teacher::getTeacherId)
                .collect(Collectors.toList());

        int size = allTeachersBranches.size();
        // берем ту связку, где больше всего совпадений
        for (int i = 0; i < size; i++) {
            TeachersBranch tb = allTeachersBranches.get(i);
            Long teacherBranchId = tb.getTeacherBranchId();
            boolean correctBranch = true;
            while (i < size && tb.getTeacherBranchId().equals(teacherBranchId)) {

                // если связка неправильная, пропускаем
                if (correctBranch) {
                    if (teacherIds.contains(tb.getTeacher().getTeacherId())) {
                        resultTeachersBranches.add(tb);
                    } else {
                        // не та связка
                        resultTeachersBranches.clear();
                        correctBranch = false;
                    }
                }

                i++;
                if (i < size) {
                    tb = allTeachersBranches.get(i);
                }
            }

            if (correctBranch && teachers.size() == resultTeachersBranches.size()) {
                // связка найдена, выходим
                break;
            }
            resultTeachersBranches.clear();
            i--;
        }
        return resultTeachersBranches;
    }
}
