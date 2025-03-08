package tasks.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TasksService;
import tasks.view.Main;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class Controller {
    private static final Logger log = Logger.getLogger(Controller.class.getName());
    public ObservableList<Task> tasksList;
    TasksService service;
    DateService dateService;

    public static Stage editNewStage;
    public static Stage infoStage;

    public static TableView mainTable;

    @FXML
    public  TableView tasks;
    @FXML
    private TableColumn<Task, String> columnTitle;
    @FXML
    private TableColumn<Task, String> columnTime;
    @FXML
    private TableColumn<Task, String> columnRepeated;
    @FXML
    private Label labelCount;
    @FXML
    private DatePicker datePickerFrom;
    @FXML
    private TextField fieldTimeFrom;
    @FXML
    private DatePicker datePickerTo;
    @FXML
    private TextField fieldTimeTo;

    public void setService(TasksService service){
        this.service=service;
        this.dateService=new DateService(service);
        this.tasksList=service.getObservableList();
        updateCountLabel(tasksList);
        tasks.setItems(tasksList);
        mainTable = tasks;

        tasksList.addListener((ListChangeListener.Change<? extends Task> c) -> {
                    updateCountLabel(tasksList);
                    tasks.setItems(tasksList);
                }
        );
    }

    @FXML
    public void initialize(){
        log.info("Main controller initializing");
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("formattedDateStart"));
        columnRepeated.setCellValueFactory(new PropertyValueFactory<>("formattedRepeated"));
    }
    private void updateCountLabel(ObservableList<Task> list){
        labelCount.setText(list.size()+ " elements");
    }

    @FXML
    public void showTaskDialog(ActionEvent actionEvent){
        Object source = actionEvent.getSource();
        NewEditController.setClickedButton((Button) source);

        try {
            editNewStage = new Stage();
            NewEditController.setCurrentStage(editNewStage);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/new-edit-task.fxml"));
            Parent root = loader.load();//getClass().getResource("/fxml/new-edit-task.fxml"));
            NewEditController editCtrl = loader.getController();
            editCtrl.setService(service);
            editCtrl.setTasksList(tasksList);
            editCtrl.setCurrentTask((Task)mainTable.getSelectionModel().getSelectedItem());
            editNewStage.setScene(new Scene(root, 600, 350));
            editNewStage.setResizable(false);
            editNewStage.initOwner(Main.primaryStage);
            editNewStage.initModality(Modality.APPLICATION_MODAL);//??????
            editNewStage.show();
        }
        catch (IOException e){
            log.error("Error loading new-edit-task.fxml");
        }
    }
    @FXML
    public void deleteTask(){
        Task toDelete = (Task)tasks.getSelectionModel().getSelectedItem();

        // Changing by calling the service, not the tasks list directly
        if (toDelete != null) {
            service.deleteTask(toDelete, tasksList);
        }
        else {
            log.warn("No task selected for deletion!");
            showErrorDialog("No task selected for deletion!");
        }
//        tasksList.remove(toDelete);
//        TaskIO.rewriteFile(tasksList);
    }

    @FXML
    public void showDetailedInfo(){
        if (tasks.getSelectionModel().getSelectedItem() == null) {
            log.warn("No task selected to show details!");
            showErrorDialog("No task selected to show details!");
            return;
        }

        try {
            Stage stage = new Stage();
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/fxml/task-info.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 550, 350));
            stage.setResizable(false);
            stage.setTitle("Info");
            stage.initModality(Modality.APPLICATION_MODAL);//??????
            infoStage = stage;
            stage.show();
        }
        catch (IOException e){
            log.error("error loading task-info.fxml");
        }
    }
    @FXML
    public void showFilteredTasks(){
        try {
            // Adding validation for the filter dates
            if (datePickerFrom.getValue() == null || datePickerTo.getValue() == null) {
                log.warn("Filter dates not properly set!");
                return;
            }

            if (fieldTimeFrom.getText().isEmpty() || fieldTimeTo.getText().isEmpty()) {
                log.warn("Filter times not properly set!");
                return;
            }

            Date start = getDateFromFilterField(datePickerFrom.getValue(), fieldTimeFrom.getText());
            Date end = getDateFromFilterField(datePickerTo.getValue(), fieldTimeTo.getText());

            // The end date should be after the start date :)
            if (start.after(end)) {
                log.warn("Filter start date is after end date!");
                return;
            }

            Iterable<Task> filtered = service.filterTasks(start, end);

            ObservableList<Task> observableTasks = FXCollections.observableArrayList();
            filtered.forEach(observableTasks::add);

            tasks.setItems(observableTasks);
            updateCountLabel(observableTasks);
        } catch (IllegalArgumentException e) {
            log.error("Error in filter parameters: " + e.getMessage());
            showErrorDialog("Error in filter parameters: " + e.getMessage());
        }
    }

    private Date getDateFromFilterField(LocalDate localDate, String time){
        try {
            Date date = dateService.getDateValueFromLocalDate(localDate);
            return dateService.getDateMergedWithTime(time, date);
        } catch (IllegalArgumentException e) {
            log.error("Invalid time format: " + e.getMessage());
            throw e;
        }
    }


    public void resetFilteredTasks(){
        tasks.setItems(tasksList);
        updateCountLabel(tasksList);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}