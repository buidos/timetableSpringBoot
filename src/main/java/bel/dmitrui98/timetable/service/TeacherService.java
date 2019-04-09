package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.repository.TeacherRepository;
import bel.dmitrui98.timetable.util.exception.AppsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

@Service
public class TeacherService implements BaseService<Teacher, Long> {

    @Autowired
    private TeacherRepository teacherRepository;

    public void save(Teacher entity) throws AppsException {
        try {
            teacherRepository.save(entity);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    public void delete(Long id) throws AppsException {
        try {
            teacherRepository.deleteById(id);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    public Teacher findByIdThrow(Long id) throws AppsException {
        return teacherRepository.findById(id).orElseThrow(() ->
                new AppsException(REC_NOT_FOUND, String.format("Преподаватель с id %d не найден", id)));
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll(Sort.by("surname"));
    }

    @Override
    @Transactional
    public void deleteAll(List<Teacher> entities) throws AppsException {
        List<Long> ids = entities.stream()
                .map(Teacher::getTeacherId)
                .collect(Collectors.toList());
        try {
            teacherRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    @Transactional
    public void deleteAllByIds(List<Long> ids) throws AppsException {
        try {
            teacherRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }
}
