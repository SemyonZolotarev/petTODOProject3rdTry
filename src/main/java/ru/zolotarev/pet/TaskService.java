package ru.zolotarev.pet;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService {
    public static final int MIN_NAME_LENGTH = 3;

    @Getter
    public static final String DEFAULT_NAME = "DefaultName_";

    @Getter
    public TaskRepository taskRepository = new TaskRepository();

    private static @NotNull LocalDate validDate() {
        System.out.println("Формат даты неверный. Устанавливается дедлайн на завтра.\n");
        return LocalDate.now().plusDays(1);
    }

    public LocalDate checkDeadlineByDate(String deadline) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate date = LocalDate.parse(deadline, dtf);
            if (LocalDate.now().minusDays(1).isBefore(date)) {
                return date;
            } else {
                return validDate();
            }
        } catch (DateTimeParseException e) {
            return validDate();
        }
    }

    public String checkingNameLengthOrCreteDefault(@NotNull String name) {
        if (name.length() < MIN_NAME_LENGTH) {
            System.out.println("Имя слишком короткое. Устанавливается имя по умолчанию.\n");
            name = DEFAULT_NAME;
        }
        return name;
    }

    public void checkingForADefaultName(@NotNull Task task) {
        if (task.getName().equals(DEFAULT_NAME)) {
            task.setName(DEFAULT_NAME + task.getID());
        }
    }

    public void addTask(Task task) {
        taskRepository.addTask(task);
    }

    public List<Task> getTaskList() {
        return taskRepository.getTaskList();
    }

    public List<Task> getTaskListByStatus(TaskStatus status) {
        return taskRepository.getTaskList().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getSortedTaskListByStatusOrder(TaskStatus first, TaskStatus second, TaskStatus third) {
        Map<TaskStatus, Integer> statusOrder = new EnumMap<>(TaskStatus.class);
        statusOrder.put(first, 1);
        statusOrder.put(second, 2);
        statusOrder.put(third, 3);

        return taskRepository.getTaskList().stream()
                .sorted((t1, t2) -> Integer.compare(statusOrder.get(t1.getStatus()), statusOrder.get(t2.getStatus())))
                .collect(Collectors.toList());
    }

    public List<Task> getSortedTaskListByDeadline() {
        return taskRepository.getTaskList().stream()
                .sorted((t1, t2) ->
                        t1.getDeadline().compareTo(t2.getDeadline()))
                .collect(Collectors.toList());
    }

    public List<Task> getSortedTaskListByStatus() {
        return taskRepository.getTaskList().stream()
                .sorted((t1, t2) -> t1.getStatus().compareTo(t2.getStatus()))
                .collect(Collectors.toList());
    }

}