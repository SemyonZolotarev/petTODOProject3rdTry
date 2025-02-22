package ru.zolotarev.pet;

import lombok.Getter;

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

    @Getter
    public TaskRepository taskRepository = new TaskRepository();

    private static LocalDate validDate() {
        System.out.println("Формат даты неверный. Устанавливается дедлайн на завтра.\n");
        return LocalDate.now().plusDays(1);
    }

    public LocalDate checkDeadlineByDate(String deadline) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate date = LocalDate.parse(deadline, dtf);
            if (!LocalDate.now().minusDays(1).isBefore(date)) {
                return date;
            } else {
                return validDate();
            }
        } catch (DateTimeParseException e) {
            return validDate();
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
        if (task.getName().equals("DefaultName_")) {
            task.setName("DefaultName_" + task.getID());
        }
    }

    public void addTask(Task task){
        taskRepository.addTask(task);
    }

    public List<Task> getTaskList(){
        return taskRepository.getTaskList();
    }

    public List<Task> getSortedTasksByStatusOrder(TaskStatus first, TaskStatus second, TaskStatus third) {
        Map<TaskStatus, Integer> statusOrder = new EnumMap<>(TaskStatus.class);
        statusOrder.put(first, 1);
        statusOrder.put(second, 2);
        statusOrder.put(third, 3);

        return taskRepository.getTaskList().stream()
                .sorted((t1, t2) -> Integer.compare(statusOrder.get(t1.getStatus()), statusOrder.get(t2.getStatus())))
                .collect(Collectors.toList());
    }

    public List<Task> getSortedTasksByDeadline() {
        return taskRepository.getTaskList().stream()
                .sorted((t1, t2) ->
                        t1.getDeadline().compareTo(t2.getDeadline()))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.getTaskList().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getSortedTasksByStatus() {
        return taskRepository.getTaskList().stream()
                .sorted((t1, t2) -> t1.getStatus().compareTo(t2.getStatus()))
                .collect(Collectors.toList());
    }
    
}