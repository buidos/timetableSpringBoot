package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.entity.StudyPair;
import bel.dmitrui98.timetable.repository.StudyPairRepository;
import bel.dmitrui98.timetable.util.exception.AppsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

@Service
public class StudyPairService implements BaseService<StudyPair, Integer> {

    @Autowired
    private StudyPairRepository studyPairRepository;

    public void save(StudyPair entity) throws AppsException {
        try {
            studyPairRepository.save(entity);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    public void delete(Integer id) throws AppsException {
        try {
            studyPairRepository.deleteById(id);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    public StudyPair findByIdThrow(Integer id) throws AppsException {
        return studyPairRepository.findById(id).orElseThrow(() ->
                new AppsException(REC_NOT_FOUND, String.format("Пара с id %d не найден", id)));
    }

    @Override
    public List<StudyPair> findAll() {
        return studyPairRepository.findAll(Sort.by("pairNumber"));
    }

    @Override
    @Transactional
    public void deleteAll(List<StudyPair> entities) throws AppsException {
        List<Integer> ids = entities.stream()
                .map(StudyPair::getStudyPairId)
                .collect(Collectors.toList());
        try {
            studyPairRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    @Transactional
    public void deleteAllByIds(List<Integer> ids) throws AppsException {
        try {
            studyPairRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }
}
