package bel.dmitrui98.timetable.service.database.dictionary;

import bel.dmitrui98.timetable.entity.dictionary.StudyShift;
import bel.dmitrui98.timetable.repository.StudyShiftRepository;
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
public class StudyShiftService implements BaseService<StudyShift, Integer> {

    @Autowired
    private StudyShiftRepository studyShiftRepository;

    public void save(StudyShift entity) throws AppsException {
        try {
            studyShiftRepository.save(entity);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    public void delete(Integer id) throws AppsException {
        try {
            studyShiftRepository.deleteById(id);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    public StudyShift findByIdThrow(Integer id) throws AppsException {
        return studyShiftRepository.findById(id).orElseThrow(() ->
                new AppsException(REC_NOT_FOUND, String.format("Отделение с id %d не найдено", id)));
    }

    @Override
    public List<StudyShift> findAll() {
        return studyShiftRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional
    public void deleteAll(List<StudyShift> entities) throws AppsException {
        List<Integer> ids = entities.stream()
                .map(StudyShift::getStudyShiftId)
                .collect(Collectors.toList());

        try {
            studyShiftRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    @Transactional
    public void deleteAllByIds(List<Integer> ids) throws AppsException {
        try {
            studyShiftRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }
}
