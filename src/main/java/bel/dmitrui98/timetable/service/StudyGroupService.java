package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.repository.StudyGroupRepository;
import bel.dmitrui98.timetable.util.exception.AppsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static bel.dmitrui98.timetable.util.exception.ExceptionType.*;

@Service
public class StudyGroupService implements BaseService<StudyGroup, Long> {

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    public void save(StudyGroup entity) throws AppsException {
        try {
            studyGroupRepository.save(entity);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    public void delete(Long id) throws AppsException {
        try {
            studyGroupRepository.deleteById(id);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    public StudyGroup findByIdThrow(Long id) throws AppsException {
        return studyGroupRepository.findById(id).orElseThrow(() ->
                new AppsException(REC_NOT_FOUND, String.format("Преподаватель с id %d не найден", id)));
    }

    @Override
    public List<StudyGroup> findAll() {
        return studyGroupRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional
    public void deleteAll(List<StudyGroup> entities) throws AppsException {
        List<Long> ids = entities.stream()
                .map(StudyGroup::getStudyGroupId)
                .collect(Collectors.toList());
        try {
            studyGroupRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    @Transactional
    public void deleteAllByIds(List<Long> ids) throws AppsException {
        try {
            studyGroupRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }
}
