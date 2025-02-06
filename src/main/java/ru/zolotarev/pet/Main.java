package ru.zolotarev.pet;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static TaskService taskService = new TaskService();

    public static void main(String[] args) {

        while (true) {
            System.out.println("Введите команду ('help' - список доступных команд)");
            switch (sc.nextLine()) {
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

    private static String help = "add - добавляет задачу" +
            "\nlist - вывести список задач" +
            "\nedit - редактировать задачу" +
            "\nexit - завершение программы";

    private static void addTask() {
        Task task = new Task();
        System.out.println("Введите имя новой задачи (не менее 3х символов): ");
        String name = sc.nextLine();
        if (name.length() < 3) {
            System.out.println("Имя слишком короткое. Устанавливается имя по умолчанию.");
            name = "DefaultName_" + task.getID();
        }
        System.out.println("Введите срок выполнения в формате 'DD.MM.YYYY': ");
        LocalDate deadline = TaskService.checkAndSetDeadlineByDate(TaskService.createDeadline(sc.nextLine()));
        System.out.println("Введите описание задачи: ");
        String desc = sc.nextLine();
        task.setName(name);
        task.setDeadline(deadline);
        task.setDescription(desc);
        taskService.taskRepository.addTask(task);
    }

    private static void tasksList() {
        System.out.println("Список задач:");
        taskService.taskRepository.getAllTasks()
                .forEach(task -> System.out.println(task.toString()));
    }

    private static void editTask() {
        System.out.println("Введите имя/ID задачи: ");
        Task task = taskService.taskRepository.getTask(sc.nextLine());
        if (task == null) {
            System.out.println("Задачи с таким именем не существует. Выход в главное меню.");
            return;
        }
        while (true) {
            System.out.println("\nname - изменить задачу" +
                    "\ndesc - изменить описание" +
                    "\ndate - изменить дедлайн" +
                    "\nstatus - изменить статус" +
                    "\nout - выйти в главное меню");
            switch (sc.nextLine().toLowerCase()) {
                case "name": {
                    System.out.print("Введите новое название задачи: ");
                    task.setName(sc.nextLine());
                    break;
                }
                case "desc": {
                    System.out.print("Введите новое описание задачи: ");
                    task.setDescription(sc.nextLine());
                    break;
                }
                case "date": {
                    System.out.print("Введите срок выполнения (дедлайн) в формате 'DD.MM.YYYY': ");
                    LocalDate deadline = TaskService.createDeadline(sc.nextLine());
                    task.setDeadline(deadline);
                    break;
                }
                case "status": {
                    System.out.print("Введите новый статус (TODO, IN_PROGRESS, DONE): ");
                    TaskStatus status = TaskStatus.valueOf(sc.nextLine().toUpperCase());
                    task.setStatus(status);
                    break;
                }
                case "out": {
                    System.out.println("Изменения сохранены.");
                    return;
                }
            }
        }
    }
}

