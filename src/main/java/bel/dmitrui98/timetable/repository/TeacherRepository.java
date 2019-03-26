package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
