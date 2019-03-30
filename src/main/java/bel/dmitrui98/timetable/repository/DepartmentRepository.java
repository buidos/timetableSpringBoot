package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.dictionary.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
