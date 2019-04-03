package bel.dmitrui98.timetable.service.database.dictionary;

import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import bel.dmitrui98.timetable.repository.SpecialtyRepository;
import bel.dmitrui98.timetable.service.BaseService;
import bel.dmitrui98.timetable.util.exception.AppsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

@Service
public class SpecialtyService implements BaseService<Specialty, Integer> {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    public void save(Specialty entity) throws AppsException {
        try {
            specialtyRepository.save(entity);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    public void delete(Integer id) throws AppsException {
        try {
            specialtyRepository.deleteById(id);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    public Specialty findByIdThrow(Integer id) throws AppsException {
        return specialtyRepository.findById(id).orElseThrow(() ->
                new AppsException(REC_NOT_FOUND, String.format("Специальность с id %d не найдена", id)));
    }

    @Override
    public List<Specialty> findAll() {
        return specialtyRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional
    public void deleteAll(List<Specialty> entities) throws AppsException {
        List<Integer> ids = entities.stream()
                .map(Specialty::getSpecialtyId)
                .collect(Collectors.toList());
        try {
            specialtyRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    @Transactional
    public void deleteAllByIds(List<Integer> ids) throws AppsException {
        try {
            specialtyRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }
}
