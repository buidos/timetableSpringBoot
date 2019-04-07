package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Integer> {
}
