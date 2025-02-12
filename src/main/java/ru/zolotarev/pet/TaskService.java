package ru.zolotarev.pet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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

    TaskRepository taskRepository = new TaskRepository();


    static LocalDate checkAndSetDeadlineByDate(LocalDate deadline) {
        if (deadline == null || !LocalDate.now().minusDays(1).isBefore(deadline)) {
            System.out.println("Формат даты неверный. Устанавливается дедлайн на завтра.");
            deadline = LocalDate.now().plusDays(1);
        }
        return deadline;
    }

    static LocalDate createDeadline(String deadline) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            return LocalDate.parse(deadline, dtf);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void changeTaskName(String taskNameOrID, String newTaskName) {
        Task task = taskRepository.getTask(taskNameOrID);
        task.setName(newTaskName);
    }

    public void changeTaskDeadline(String taskNameOrID, LocalDate newDeadline) {
        Task task = taskRepository.getTask(taskNameOrID);
        task.setDeadline(newDeadline);
    }

    public void changeTaskDescription(String taskNameOrID, String newDescription) {
        Task task = taskRepository.getTask(taskNameOrID);
        task.setDescription(newDescription);
    }

    public void changeTaskStatus(String taskNameOrID, TaskStatus status) {
        Task task = taskRepository.getTask(taskNameOrID);
        task.setStatus(status);
    }

    public List<Task> getSortedTasksByStatus() {
        return taskRepository.getAllTasks().stream()
                .sorted((t1, t2) -> t1.getStatus().compareTo(t2.getStatus()))
                .collect(Collectors.toList());
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

    public List<Task> getTasksByStatus(TaskStatus status){
        return taskRepository.getAllTasks().stream()
                .filter(task -> task.getStatus()==status)
                .collect(Collectors.toList());
    }
}