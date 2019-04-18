package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.TeacherBranchPK;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeachersBranchRepository extends JpaRepository<TeachersBranch, TeacherBranchPK> {

    @Query("SELECT tb FROM TeachersBranch tb " +
            "JOIN FETCH tb.studyLoad l " +
            "JOIN FETCH l.subject ORDER BY tb.teacherBranchPK.teacherBranchId")
    List<TeachersBranch> findAll();

    @Query("SELECT tb FROM TeachersBranch tb " +
            "JOIN FETCH tb.studyLoad l " +
            "JOIN FETCH l.subject " +
            "WHERE tb.studyGroup = :studyGroup " +
            "ORDER BY tb.teacherBranchPK.teacherBranchId")
    List<TeachersBranch> findByGroup(@Param("studyGroup") StudyGroup group);

    @Query("SELECT MAX(tb.teacherBranchPK.teacherBranchId) FROM TeachersBranch tb")
    Long getMaxId();
}
