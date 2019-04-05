package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Modifying
    @Query("delete from Subject s where s.subjectId in (:ids)")
    void deleteAllByIds(@Param("ids") List<Long> ids);
}
