package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Modifying
    @Query("delete from Teacher t where t.teacherId in (:ids)")
    void deleteAllByIds(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT t FROM Teacher t " +
            "LEFT JOIN FETCH t.teachersBranchSet " +
            "WHERE t IN ?1")
    List<Teacher> branchFetch(List<Teacher> teachers);
}
