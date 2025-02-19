package ru.zolotarev.pet;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskService {

    /*
     * List tasks (task repository) +
     *
     * deadline check +
     * createTaskAndAdd +
     * changeTaskName +
     * changeTaskDeadline +
     * changeTaskDescription +
     * changeTaskStatus +
     * getSortedTasksByStatus +
     * getSortedTasksByStatusOrder (custom sort) +
     * getSortedTasksByDeadline +
     * */

    @Getter
    TaskRepository taskRepository = new TaskRepository();


    public LocalDate checkDeadlineByDate(String deadline) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Optional<LocalDate> date = Optional.of(LocalDate.parse(deadline, dtf));

        if(date.isPresent() && !LocalDate.now().minusDays(1).isBefore(date.get())){
            return date.get();
        } else {
            System.out.println("Формат даты неверный. Устанавливается дедлайн на завтра.\n");
            return LocalDate.now().plusDays(1);
        }
    }

    public String checkingNameLengthOrCreteDefault(String name) {
        if (name.length() < 3) {
            System.out.println("Имя слишком короткое. Устанавливается имя по умолчанию.\n");
            name = "DefaultName_";
        }
        return name;
    }

    public void checkingForADefaultName(Task task) {
        if (task.getName() == "DefaultName_") {
            task.setName("DefaultName_" + task.getID());
        }
    }


    public List<Task> getSortedTasksByStatusOrder(TaskStatus first, TaskStatus second, TaskStatus third) {
        Map<TaskStatus, Integer> statusOrder = new EnumMap<>(TaskStatus.class);
        statusOrder.put(first, 1);
        statusOrder.put(second, 2);
        statusOrder.put(third, 3);

        return taskRepository.getAllTasks().stream()
                .sorted((t1, t2) -> Integer.compare(statusOrder.get(t1.getStatus()), statusOrder.get(t2.getStatus())))
                .collect(Collectors.toList());
    }

    public List<Task> getSortedTasksByDeadline() {
        return taskRepository.getAllTasks().stream()
                .sorted((t1, t2) ->
                        t1.getDeadline().compareTo(t2.getDeadline()))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.getAllTasks().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getSortedTasksByStatus() {
        return taskRepository.getAllTasks().stream()
                .sorted((t1, t2) -> t1.getStatus().compareTo(t2.getStatus()))
                .collect(Collectors.toList());
    }

//
//    public void changeTaskName(String taskNameOrID, String newTaskName) {
//        Task task = taskRepository.getTask(taskNameOrID);
//        task.setName(newTaskName);
//    }
//
//    public void changeTaskDeadline(String taskNameOrID, LocalDate newDeadline) {
//        Task task = taskRepository.getTask(taskNameOrID);
//        task.setDeadline(newDeadline);
//    }
//
//    public void changeTaskDescription(String taskNameOrID, String newDescription) {
//        Task task = taskRepository.getTask(taskNameOrID);
//        task.setDescription(newDescription);
//    }
//
//    public void changeTaskStatus(String taskNameOrID, TaskStatus status) {
//        Task task = taskRepository.getTask(taskNameOrID);
//        task.setStatus(status);
//    }
}