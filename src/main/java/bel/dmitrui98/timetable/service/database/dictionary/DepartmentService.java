package bel.dmitrui98.timetable.service.database.dictionary;

import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.repository.DepartmentRepository;
import bel.dmitrui98.timetable.repository.SpecialtyRepository;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.exception.AppsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

@Service
public class DepartmentService implements BaseService<Department, Integer> {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

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

        List<Specialty> specialties = specialtyRepository.findByDepartmentIdIn(ids);
        List<String> specialityNames = new ArrayList<>();

        for (Specialty specialty : specialties) {
            if (ids.contains(specialty.getDepartment().getDepartmentId())) {
                specialityNames.add(specialty.getName());
            }
        }
        if (!specialityNames.isEmpty()) {
            throw new AppsException(REC_NOT_DELETED_RELATION, "Невозможно удалить выделенные отделения, так как на них ссылаются специальности." +
                    " Зависимые специальности: " + specialityNames);
        }

        try {
            departmentRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    @Transactional
    public void deleteAllByIds(List<Integer> ids) throws AppsException {
        try {
            departmentRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }
}
