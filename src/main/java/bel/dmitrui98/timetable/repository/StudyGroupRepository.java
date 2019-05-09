package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    @Modifying
    @Query("delete from StudyGroup g where g.studyGroupId in (:ids)")
    void deleteAllByIds(@Param("ids") List<Long> ids);

    List<StudyGroup> findBySpecialtyIn(List<Specialty> specialties);

    List<StudyGroup> findByStudyGroupIdIn(List<Long> ids);

    @Query("SELECT distinct g FROM StudyGroup g " +
            "LEFT JOIN FETCH g.teachersBranchSet " +
            "WHERE g = ?1")
    StudyGroup branchFetch(StudyGroup studyGroup);

    @Query("SELECT distinct g FROM StudyGroup g " +
            "LEFT JOIN FETCH g.teachersBranchSet " +
            "WHERE g IN ?1")
    List<StudyGroup> branchByGroupInFetch(List<StudyGroup> studyGroups);
}
