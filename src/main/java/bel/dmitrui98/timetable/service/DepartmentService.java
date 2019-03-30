package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.repository.DepartmentRepository;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.exception.AppsException;
import bel.dmitrui98.timetable.util.exception.ExceptionType;
import bel.dmitrui98.timetable.util.validation.AppsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.REC_NOT_DELETED;
import static bel.dmitrui98.timetable.util.exception.ExceptionType.REC_NOT_SAVED;

@Service
public class DepartmentService implements BaseService<Department, Integer> {

    @Autowired
    private DepartmentRepository departmentRepository;

    public boolean save(Department entity) throws AppsException {
        try {
            AppsValidation.validateString(entity.getName());
        } catch (AppsException ex) {
            String contentText = "";
            if (ex.getExceptionType().equals(ExceptionType.VALID_EMPTY_VALUE)) {
                contentText = "Имя отделения не должно быть пустым";
            } else if (ex.getExceptionType().equals(ExceptionType.VALID_LONG_VALUE)) {
                contentText = "Имя отделения не дожно превышать длину в " + AppsValidation.MAX_STRING_LENGTH + " символов";
            }
            AlertsUtil.showErrorAlert(AppsException.VALIDATION_ERROR, contentText);
            return false;
        }
        try {
            departmentRepository.save(entity);
            return true;
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    public boolean delete(Integer id) throws AppsException {
        try {
            departmentRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    public List<Department> findAll() {
        return departmentRepository.findAll(Sort.by("name"));
    }
}
