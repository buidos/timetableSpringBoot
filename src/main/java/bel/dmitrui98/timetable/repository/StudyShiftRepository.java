package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.dictionary.StudyShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyShiftRepository extends JpaRepository<StudyShift, Integer> {

    @Modifying
    @Query("delete from StudyShift s where s.studyShiftId in (:ids)")
    void deleteAllByIds(@Param("ids") List<Integer> ids);
}
