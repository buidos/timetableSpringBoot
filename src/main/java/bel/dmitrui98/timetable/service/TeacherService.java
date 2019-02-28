package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.entity.Teacher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService {

    private List<Teacher> teachers = new ArrayList<>();

    public TeacherService() {
        teachers.add(new Teacher("vacya", "13423525", "asg@mail.ru"));
    }

    public List<Teacher> findAll() {
        return teachers;
    }

    public void save(Teacher teacher) {
        teachers.add(teacher);
    }
}
