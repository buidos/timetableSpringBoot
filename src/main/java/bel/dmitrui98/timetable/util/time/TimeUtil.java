package bel.dmitrui98.timetable.util.time;

import bel.dmitrui98.timetable.util.appssettings.AppsSettingsHolder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtil {
    public static final String TIME_PATTERN = "HH:mm";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    public static int convertHourToMinute(int hour) {
        return hour * (AppsSettingsHolder.getHourTime() * 2);
    }

    public static int convertMinuteToHour(int minute) {
        int hour = minute / (AppsSettingsHolder.getHourTime() * 2);
        // если есть еще половина пары, то добавляем целый час
        if (isRemainder(minute)) {
            hour++;
        }
        return hour;
    }

    /**
     * Есть ли остаток от деления
     */
    public static boolean isRemainder(int minute) {
        return (minute % (AppsSettingsHolder.getHourTime() * 2)) != 0;
    }

    /**
     * Преобразует время в строку
     */
    public static String formatToTimeString(LocalTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return TIME_FORMATTER.format(dateTime);
    }


    /**
     * Преобразует строку времени в объект времени
     * @throws DateTimeParseException не верный формат строки
     */
    public static LocalTime parseStringToDateTime(String dateTimeString) throws DateTimeParseException {
        return TIME_FORMATTER.parse(dateTimeString, LocalTime::from);
    }
}
