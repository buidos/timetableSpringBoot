package bel.dmitrui98.timetable.service.timetable.save;

import bel.dmitrui98.timetable.TimetableApplication;
import bel.dmitrui98.timetable.repository.StudyGroupRepository;
import bel.dmitrui98.timetable.repository.TeachersBranchRepository;
import bel.dmitrui98.timetable.service.timetable.TimetableService;
import bel.dmitrui98.timetable.util.alerts.AlertsUtil;
import bel.dmitrui98.timetable.util.dto.timetable.wrapper.CellListWrapper;
import bel.dmitrui98.timetable.util.dto.timetable.wrapper.inner_wrapper.CellWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.prefs.Preferences;

/**
 * Сервис сохранения и загрузки расписания в xml файл
 */
@Service
@Log4j2
public class TimetableSaveService {

    private static final String TIMETABLE_FILE_PATH = "timetableFilePath";
    @Autowired
    private TimetableService timetableService;

    @Autowired
    private ApplicationContext applicationContext;

    public void loadTimetableFromFile(File file) {
        try {

            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);

            CellListWrapper wrapper = (CellListWrapper) oi.readObject();
            wrapper.setApplicationContext(applicationContext);
            wrapper.setStudyGroupRepository(applicationContext.getBean(StudyGroupRepository.class));
            for (CellWrapper listDtoInnerWrapper : wrapper.getTimetableList()) {
                listDtoInnerWrapper.getListDtoWrapper().setTeachersBranchRepository(applicationContext.getBean(TeachersBranchRepository.class));
            }


            timetableService.getTimetableList().clear();
            timetableService.getTimetableList().addAll(wrapper.getTimetableListDto());

            // Сохраняем путь к файлу в реестре.
            saveFilePath(file);

        } catch (Exception e) { // catches ANY exception
            log.error("error loading timetable", e);
            AlertsUtil.showErrorAlert("Ошибка при загрузке расписания",
                    "Не удалось загрузить расписание из файла " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Сохраняет текущую информацию об адресатах в указанном файле
     */
    public boolean saveTimetableToFile(File file) {
        try {

            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            CellListWrapper wrapper = new CellListWrapper(timetableService.getTimetableList());
            objectOut.writeObject(wrapper);
            objectOut.close();

            // Сохраняем путь к файлу в реестре.
            saveFilePath(file);
            timetableService.setEdit(false);
        } catch (Exception e) {
            log.error("error saving timetable", e);
            AlertsUtil.showErrorAlert("Ошибка при сохранении расписания", "Не удалось сохранить расписание" +
                    " или путь к файлу в реестре", e);
            return false;
        }
        return true;
    }

    /**
     * Возвращает последний открытый файл.
     * preference считывается из реестра, специфичного для конкретной
     * операционной системы. Если preference не был найден, то возвращается null.
     *
     */
    public File getTimetableFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(TimetableApplication.class);
        String filePath = prefs.get(TIMETABLE_FILE_PATH, null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Задаёт путь текущему загруженному файлу. Этот путь сохраняется
     * в реестре, специфичном для конкретной операционной системы.
     *
     * @param file - файл или null, чтобы удалить путь
     */
    private void saveFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(TimetableApplication.class);
        if (file != null) {
            prefs.put(TIMETABLE_FILE_PATH, file.getPath());
        } else {
            prefs.remove(TIMETABLE_FILE_PATH);
        }
    }
}
