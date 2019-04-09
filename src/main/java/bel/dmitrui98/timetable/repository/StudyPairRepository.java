package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.StudyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyPairRepository extends JpaRepository<StudyPair, Integer> {
    @Modifying
    @Query("delete from StudyPair p where p.studyPairId in (:ids)")
    void deleteAllByIds(@Param("ids") List<Integer> ids);
}
