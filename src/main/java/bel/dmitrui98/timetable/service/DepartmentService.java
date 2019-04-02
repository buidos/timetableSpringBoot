package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.repository.DepartmentRepository;
import bel.dmitrui98.timetable.util.exception.AppsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

@Service
public class DepartmentService implements BaseService<Department, Integer> {

    @Autowired
    private DepartmentRepository departmentRepository;

    public void save(Department entity) throws AppsException {
        try {
            departmentRepository.save(entity);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    public void delete(Integer id) throws AppsException {
        try {
            departmentRepository.deleteById(id);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    public Department findByIdThrow(Integer id) throws AppsException {
        return departmentRepository.findById(id).orElseThrow(() ->
                new AppsException(REC_NOT_FOUND, String.format("Отделение с id %d не найдено", id)));
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional
    public void deleteAll(List<Department> entities) throws AppsException {
        List<Integer> ids = entities.stream()
                .map(Department::getDepartmentId)
                .collect(Collectors.toList());
        try {
            departmentRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }
}
