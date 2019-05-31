package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.dictionary.Department;
import bel.dmitrui98.timetable.entity.dictionary.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {

    @Modifying
    @Query("delete from Specialty s where s.specialtyId in (:ids)")
    void deleteAllByIds(@Param("ids") List<Integer> ids);

    @Query("FROM Specialty s WHERE s.department.departmentId IN (:departmentIds)")
    List<Specialty> findByDepartmentIdIn(@Param("departmentIds") List<Integer> departmentIds);

    List<Specialty> findByDepartmentInOrderByName(List<Department> departments);
}
