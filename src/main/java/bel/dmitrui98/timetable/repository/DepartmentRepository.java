package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.dictionary.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Modifying
    @Query("delete from Department d where d.departmentId in (:ids)")
    void deleteAllByIds(@Param("ids") List<Integer> ids);
}
