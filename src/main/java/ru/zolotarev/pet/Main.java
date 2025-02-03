package ru.zolotarev.pet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static TaskService taskService = new TaskService();

    public static void main(String[] args) {

        while (true) {
            System.out.println("Введите команду ('help' - список доступных команд)");
            String str = sc.nextLine();
            switch (str.toLowerCase()) {
                case "help":
                    System.out.println(help);
                    break;
                case "add":
                    addTask();
                    break;
                case "list":
                    tasksList();
                    break;
                case "edit":
                    editTask();
                    break;
                case "exit":
                    return;
            }
        }

    }

    private static LocalDate createDeadline(String deadline) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            return LocalDate.parse(deadline, dtf);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static String help = "add - добавляет задачу" +
            "\nlist - вывести список задач" +
            "\nedit - редактировать задачу" +
            "\nexit - завершение программы";

    private static void addTask() {
        System.out.println("Введите имя новой задачи (не менее 3х символов): ");
        String name = sc.nextLine();
        System.out.println("Введите срок выполнения (дедлайн) в формате 'YYYY-MM-DD': ");
        String data = sc.nextLine();
        LocalDate deadline = createDeadline(data);
        System.out.println("Введите описание задачи: ");
        String description = sc.nextLine();
        taskService.createTaskAndAdd(name, description, deadline, TaskStatus.TODO);
    }

    private static void tasksList() {
        System.out.println("Список задач:");
        taskService.taskRepository.getAllTasks()
                .forEach(task -> System.out.println(task.toString()));
    }

    private static void editTask() {
        System.out.println("Введите имя/ID задачи: ");
        String taskNameOrId = sc.nextLine();
        Task task = taskService.taskRepository.getTask(taskNameOrId);
        System.out.print("Введите новое название задачи: ");
        task.setName(sc.nextLine());
        System.out.print("Введите новое описание задачи: ");
        task.setDescription(sc.nextLine());
        System.out.print("Введите новый срок выполнения (YYYY-MM-DD): ");
        LocalDate deadline = createDeadline(sc.nextLine());
        task.setDeadline(deadline);
        System.out.print("Введите новый статус (TODO, IN_PROGRESS, DONE): ");
        TaskStatus status = TaskStatus.valueOf(sc.nextLine().toUpperCase());
        task.setStatus(status);
        System.out.println("Задача обновлена.");
    }
}

