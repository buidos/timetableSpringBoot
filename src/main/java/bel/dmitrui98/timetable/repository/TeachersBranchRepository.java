package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.StudyGroup;
import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.entity.TeachersBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeachersBranchRepository extends JpaRepository<TeachersBranch, Long> {

    @Query("SELECT tb FROM TeachersBranch tb " +
            "JOIN FETCH tb.studyLoad l " +
            "JOIN FETCH l.subject " +
            "ORDER BY l.countMinutesInTwoWeek DESC")
    List<TeachersBranch> findAll();

    @Query("SELECT DISTINCT tb FROM TeachersBranch tb " +
            "JOIN FETCH tb.studyLoad l " +
            "JOIN FETCH tb.teacherSet " +
            "JOIN FETCH l.subject " +
            "JOIN tb.studyGroupSet g ON g = :studyGroup " +
            "ORDER BY tb.teacherBranchId, l.countMinutesInTwoWeek DESC")
    List<TeachersBranch> findByGroup(@Param("studyGroup") StudyGroup group);

    @Query("SELECT DISTINCT tb FROM TeachersBranch tb " +
            "JOIN FETCH tb.studyLoad l " +
            "JOIN FETCH tb.teacherSet " +
            "JOIN FETCH l.subject " +
            "JOIN tb.studyGroupSet g ON g = :studyGroup " +
            "ORDER BY l.countMinutesInTwoWeek DESC")
    List<TeachersBranch> findByGroupOrderByHour(@Param("studyGroup") StudyGroup group);

    @Query("SELECT tb FROM TeachersBranch tb " +
            "JOIN tb.teacherSet t ON t IN :teachers " +
            "JOIN tb.studyGroupSet g ON g = :group_ " +
            "ORDER BY tb.teacherBranchId")
    List<TeachersBranch> findByTeachersInAndGroup(@Param("teachers") List<Teacher> teachers, @Param("group_") StudyGroup group);

    TeachersBranch findTopByOrderByTeacherBranchIdDesc();

    @Query("SELECT DISTINCT tb FROM TeachersBranch tb " +
            "LEFT JOIN FETCH tb.studyGroupSet g " +
            "LEFT JOIN FETCH g.teachersBranchSet " +
            "LEFT JOIN FETCH tb.teacherSet t " +
            "LEFT JOIN FETCH t.teachersBranchSet " +
            "WHERE tb IN ?1")
    List<TeachersBranch> groupsAndTeacherFetch(List<TeachersBranch> branches);

    List<TeachersBranch> findByTeacherBranchIdIn(List<Long> ids);

}
