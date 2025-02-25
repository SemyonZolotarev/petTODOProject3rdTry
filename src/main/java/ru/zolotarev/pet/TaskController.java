package ru.zolotarev.pet;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Getter
public class TaskController {

    private Scanner sc;
    private TaskService taskService;
    private String help = "add - добавить задачу" +
            "\nlist - вывести список задач" +
            "\nedit - редактировать задачу" +
            "\nsort - сортировка задач" +
            "\ndel - удалить задачу" +
            "\nfilter - отфильтровать задачу по статусу" +
            "\nexit - завершение программы";

    public TaskController() {
        sc = new Scanner(System.in);
        taskService = new TaskService();
    }

    public static void main(String[] args) {
        TaskController taskController = new TaskController();
        while (true) {
            System.out.println("\nВведите команду ('help' - список доступных команд)");
            try {
                switch (ControllerCommands.valueOf(taskController.getSc().nextLine().toUpperCase())) {
                    case HELP -> System.out.println(taskController.getHelp());
                    case ADD -> taskController.addTask();
                    case LIST -> taskController.printTasks();
                    case EDIT -> taskController.editTask();
                    case SORT -> taskController.printSortTaskList();
                    case DEL -> taskController.deleteTask();
                    case FILTER -> taskController.printTaskListByStatus();
                    case EXIT -> {
                        return;
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("\nКоманда не распознана. Попробуйте снова.");
            }

        }
    }

    private void addTask() {
        System.out.println("Введите имя новой задачи (не менее 3х символов): ");
        String name = sc.nextLine();
        name = taskService.checkingNameLengthOrCreteDefault(name);
        System.out.println("Введите срок выполнения в формате 'DD.MM.YYYY': ");
        LocalDate deadline = taskService.checkDeadlineByDate(sc.nextLine());
        System.out.println("Введите описание задачи: ");
        String desc = sc.nextLine();
        Task task = new Task(name, desc, deadline, TaskStatus.TODO);
        taskService.checkingForADefaultName(task);
        taskService.addTask(task);
    }

    private void printTasks() {
        System.out.println("Список задач:");
        List<Task> tasks = taskService.getTaskList();
        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены.\n");
        } else {
            tasks.forEach(t -> System.out.println(t.toString()));
        }

    }

    private void editTask() {

        String commands = "\nname - изменить наименование" +
                "\ndesc - изменить описание" +
                "\ndate - изменить дедлайн" +
                "\nstatus - изменить статус" +
                "\nout - выйти в главное меню\n";

        System.out.println("Введите имя/ID задачи: ");
        try {
            Task task = taskService.getTaskRepository().getTask(sc.nextLine());
            while (true) {
                System.out.println(commands);
                try {
                    switch (EditTasksCommands.valueOf(sc.nextLine().toUpperCase())) {
                        case NAME -> {
                            System.out.print("Введите новое название задачи: ");
                            taskService.checkingNameLengthOrCreteDefault(sc.nextLine());
                        }
                        case DESC -> {
                            System.out.print("Введите новое описание задачи: ");
                            task.setDescription(sc.nextLine());
                        }
                        case DATE -> {
                            System.out.print("Введите срок выполнения (дедлайн) в формате 'DD.MM.YYYY': ");
                            LocalDate deadline = taskService.checkDeadlineByDate(sc.nextLine());
                            task.setDeadline(deadline);
                        }
                        case STATUS -> {
                            System.out.print("Введите новый статус (TODO, IN_PROGRESS, DONE): ");
                            try {
                                TaskStatus status = TaskStatus.valueOf(sc.nextLine().toUpperCase());
                                task.setStatus(status);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Неверный формат даты, дедлайн не изменился.\n");
                            }
                        }
                        case OUT -> {
                            System.out.println("Изменения сохранены.\n");
                            return;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Неизвестная команда. Попробуйте снова.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Задачи с таким именем/ID не существует. Выход в главное меню.");
        }
    }

    private void printSortTaskList() {
        System.out.println("Введите команду:" +
                "\n'1' - сортировка по статусу" +
                "\n'2' - сортировка по сроку выполнения");
        switch (sc.nextLine()) {
            case "1" -> {
                System.out.println("Сортировка по статусу:" +
                        "\n'1' - быстрая сортировка" +
                        "\n'2' - сортировка с параметрами");

                switch (sc.nextLine()) {
                    case "1" -> System.out.println(taskService.getSortedTaskListByStatus());

                    case "2" -> {
                        System.out.println("Введите порядок сортировки по статусу (TODO, IN_PROCESS, DONE):" +
                                "\nПервый элемент:");
                        try {
                            TaskStatus first = TaskStatus.valueOf(sc.nextLine().toUpperCase());
                            System.out.println("Второй элемент:");
                            TaskStatus second = TaskStatus.valueOf(sc.nextLine().toUpperCase());
                            System.out.println("Третий элемент:");
                            TaskStatus third = TaskStatus.valueOf(sc.nextLine().toUpperCase());
                            System.out.println(taskService.getSortedTaskListByStatusOrder(first, second, third));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неизвестное значение. Выход в главное меню.");
                        }
                    }
                }
            }
            case "2" -> {
                System.out.println("Сортировка по сроку выполнения:");
                System.out.println(taskService.getSortedTaskListByDeadline());
            }

            default -> System.out.println("Неизвестная команда. Выход в главное меню.");
        }
    }

    private void deleteTask() {
        System.out.println("Введите имя/ID задачи: ");
        try {
            Task task = taskService.getTaskRepository().getTask(sc.nextLine());
            taskService.getTaskRepository().deleteTask(task);
            System.out.println("Задача успешно удалена.");
        } catch (IllegalArgumentException e) {
            System.out.println("Задачи с таким именем/ID не существует. Выход в главное меню.");
        }
    }

    private void printTaskListByStatus() {
        System.out.println("Введите нужный статус (TODO, IN_PROCESS, DONE)");
        try {
            switch (TaskStatus.valueOf(sc.nextLine().toUpperCase())) {
                case TODO -> System.out.println(taskService.getTaskListByStatus(TaskStatus.TODO));
                case IN_PROCESS -> System.out.println(taskService.getTaskListByStatus(TaskStatus.IN_PROCESS));
                case DONE -> System.out.println(taskService.getTaskListByStatus(TaskStatus.DONE));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестная команда. Выход в главное меню.");
        }
    }
}

