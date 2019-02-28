package bel.dmitrui98.timetable.controller;

import bel.dmitrui98.timetable.entity.Teacher;
import bel.dmitrui98.timetable.service.TeacherService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class MainController {
    @Autowired
    private TeacherService teacherService;

    // Инъекции JavaFX
    @FXML
    private TableView<Teacher> table;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;

    // Переменные
    private ObservableList<Teacher> data;

    /**
     * Инициализация контроллера от JavaFX.
     * Метод вызывается после того как FXML загрузчик произвел инъекции полей.
     * <p>
     * Обратите внимание, что имя метода <b>обязательно</b> должно быть "initialize",
     * в противном случае, метод не вызовется.
     * <p>
     * Также на этом этапе еще отсутствуют бины спринга
     * и для инициализации лучше использовать метод,
     * описанный аннотацией @PostConstruct.
     * Который вызовется спрингом, после того,
     * как им будут произведены все оставшиеся инъекции.
     * {@link MainController#init()}
     */
    @FXML
    public void initialize() {
    }

    /**
     * На этом этапе уже произведены все возможные инъекции.
     */
    @PostConstruct
    public void init() {
        List<Teacher> teachers = teacherService.findAll();
        data = FXCollections.observableArrayList(teachers);

        // Добавляем столбцы к таблице
        TableColumn<Teacher, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Teacher, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Teacher, String> phoneColumn = new TableColumn<>("Телефон");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Teacher, String> emailColumn = new TableColumn<>("E-mail");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().setAll(idColumn, nameColumn, phoneColumn, emailColumn);

        // Добавляем данные в таблицу
        table.setItems(data);
    }

    /**
     * Метод, вызываемый при нажатии на кнопку "Добавить".
     * Привязан к кнопке в FXML файле представления.
     */
    @FXML
    public void addContact() {
        Teacher teacher = new Teacher(txtName.getText(), txtPhone.getText(), txtEmail.getText());
        teacherService.save(teacher);
        data.add(teacher);

        // чистим поля
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }
}
