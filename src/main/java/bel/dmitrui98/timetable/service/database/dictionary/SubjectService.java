package bel.dmitrui98.timetable.service.database.dictionary;

import bel.dmitrui98.timetable.entity.Subject;
import bel.dmitrui98.timetable.repository.SubjectRepository;
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
public class SubjectService implements BaseService<Subject, Long> {

    @Autowired
    private SubjectRepository subjectRepository;

    public void save(Subject entity) throws AppsException {
        try {
            subjectRepository.save(entity);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_SAVED, ex);
        }
    }

    public void delete(Long id) throws AppsException {
        try {
            subjectRepository.deleteById(id);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    public Subject findByIdThrow(Long id) throws AppsException {
        return subjectRepository.findById(id).orElseThrow(() ->
                new AppsException(REC_NOT_FOUND, String.format("Специальность с id %d не найдена", id)));
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional
    public void deleteAll(List<Subject> entities) throws AppsException {
        List<Long> ids = entities.stream()
                .map(Subject::getSubjectId)
                .collect(Collectors.toList());
        try {
            subjectRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }

    @Override
    @Transactional
    public void deleteAllByIds(List<Long> ids) throws AppsException {
        try {
            subjectRepository.deleteAllByIds(ids);
        } catch (Exception ex) {
            throw new AppsException(REC_NOT_DELETED, ex);
        }
    }
}
