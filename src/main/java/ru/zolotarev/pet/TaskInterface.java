package ru.zolotarev.pet;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Getter
public class TaskInterface {

    private Scanner sc;
    private TaskService taskService;
    private String help = "add - добавить задачу" +
            "\nlist - вывести список задач" +
            "\nedit - редактировать задачу" +
            "\nsort - сортировка задач" +
            "\ndel - удалить задачу" +
            "\nfilter - отфильтровать задачу по статусу" +
            "\nexit - завершение программы";

    private TaskInterface() {
        sc = new Scanner(System.in);
        taskService = new TaskService();
    }

    private enum Commands {
        HELP, ADD, LIST, EDIT, SORT, DEL, FILTER, EXIT
    }

    private enum TasksCommands {
        NAME, DESC, DATE, STATUS, OUT
    }

    public static void main(String[] args) {
        TaskInterface taskInterface = new TaskInterface();
        while (true) {
            System.out.println("\nВведите команду ('help' - список доступных команд)");
            try {
                switch (Commands.valueOf(taskInterface.getSc().nextLine().toUpperCase())) {
                    case HELP -> System.out.println(taskInterface.getHelp());
                    case ADD -> taskInterface.addTask();
                    case LIST -> taskInterface.printTasks();
                    case EDIT -> taskInterface.editTask();
                    case SORT -> taskInterface.sortTaskList();
                    case DEL -> taskInterface.deleteTask();
                    case FILTER -> taskInterface.filterTaskList();
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
        taskService.getTaskRepository().addTask(task);
    }

    private void printTasks() {
        System.out.println("Список задач:");
        List<Task> tasks = taskService.getTaskRepository().getAllTasks();
        tasks.stream()
                .findFirst().ifPresentOrElse(
                        task -> tasks.forEach(t -> System.out.println(t.toString())),
                        () -> System.out.println("Задачи не найдены.\n")
                );
    }

    private void editTask() {

        String commands = "\nname - изменить наименование" +
                "\ndesc - изменить описание" +
                "\ndate - изменить дедлайн" +
                "\nstatus - изменить статус" +
                "\nout - выйти в главное меню\n";

        System.out.println("Введите имя/ID задачи: ");
        Task task = taskService.getTaskRepository().getTask(sc.nextLine());
        if (task == null) {
            System.out.println("Задачи с таким именем не существует. Выход в главное меню.");
            return;
        }
        while (true) {
            System.out.println(commands);
            try {
                switch (TasksCommands.valueOf(sc.nextLine().toUpperCase())) {
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
                    case OUT ->  {
                        System.out.println("Изменения сохранены.\n");
                        return;
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Неверная команда. Попробуйте снова.");
            }
        }
    }

    private void sortTaskList() {
        System.out.println("Введите команду:" +
                "\n'1' - сортировка по статусу" +
                "\n'2' - сортировка по сроку выполнения");
        switch (sc.nextLine()) {
            case "1":
                System.out.println("Сортировка по статусу:" +
                        "'1' - быстрая сортировка" +
                        "'2' - сортировка с параметрами");
                switch (sc.nextLine()) {
                    case "1":
                        System.out.println(taskService.getSortedTasksByStatus());
                        return;
                    case "2":
                        System.out.println("Введите порядок сортировки по статусу (TODO, IN_PROCESS, DONE):" +
                                "\nПервый элемент:");
                        TaskStatus first = TaskStatus.valueOf(sc.nextLine().toUpperCase());
                        System.out.println("Второй элемент:");
                        TaskStatus second = TaskStatus.valueOf(sc.nextLine().toUpperCase());
                        System.out.println("Третий элемент:");
                        TaskStatus third = TaskStatus.valueOf(sc.nextLine().toUpperCase());
                        System.out.println(taskService.getSortedTasksByStatusOrder(first, second, third));
                        return;
                    default:
                        System.out.println("Неизвестная команда. Выход в главное меню.");
                        return;
                }
            case "2":
                System.out.println("Сортировка по сроку выполнения:");
                System.out.println(taskService.getSortedTasksByDeadline());
                return;
            default:
                System.out.println("Неизвестная команда. Выход в главное меню.");
        }
    }

    private void deleteTask() {
        System.out.println("Введите имя/ID задачи: ");
        Task task = taskService.getTaskRepository().getTask(sc.nextLine());
        if (task == null) {
            System.out.println("Задачи с таким именем не существует. Выход в главное меню.");
            return;
        }
        taskService.getTaskRepository().deleteTask(task);
        System.out.println("Задача успешно удалена.");
    }

    private void filterTaskList() {
        System.out.println("Введите нужный статус (TODO, IN_PROCESS, DONE)");
        switch (sc.nextLine().toUpperCase()) {
            case "TODO":
                System.out.println(taskService.getTasksByStatus(TaskStatus.TODO));
                return;
            case "IN_PROCESS":
                System.out.println(taskService.getTasksByStatus(TaskStatus.IN_PROCESS));
                return;
            case "DONE":
                System.out.println(taskService.getTasksByStatus(TaskStatus.DONE));
                return;
            default:
                System.out.println("Неизвестная команда. Выход в главное меню.");
        }
    }
}

