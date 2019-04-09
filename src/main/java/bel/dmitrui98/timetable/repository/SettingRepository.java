package bel.dmitrui98.timetable.repository;

import bel.dmitrui98.timetable.entity.Setting;
import bel.dmitrui98.timetable.util.enums.SettingEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SettingRepository extends JpaRepository<Setting, Integer> {

    @Query("FROM Setting s WHERE s.settingType = :settingType")
    Setting findBySettingType(@Param("settingType") SettingEnum settingType);
}
