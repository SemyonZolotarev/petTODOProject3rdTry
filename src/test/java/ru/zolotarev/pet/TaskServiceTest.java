package ru.zolotarev.pet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskServiceTest {

    private static final int TASK_YEAR = 2026;
    private static final int TASK_MONTH = 3;
    private static final int TASK_DAY_OF_MONTH = 13;
    private static final String DEFAULT_NAME = TaskService.getDEFAULT_NAME();
    public static final String TEST_DESCRIPTION = "TestDescription";

    private final TaskService taskService = new TaskService();

    @Test
    void checkDeadlineByDate_returnTheSetDate() {
        //pattern of deadline "dd.MM.yyyy", but date must be today or later.
        LocalDate theSetDate = LocalDate.now().plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = theSetDate.format(formatter);
        LocalDate resultDate = taskService.checkDeadlineByDate(date);
        Assertions.assertEquals(theSetDate, resultDate);

    }

    @Test
    void checkDeadlineByDate_returnValidDate() {
        //pattern of deadline "dd.MM.yyyy", but date must be today or later.
        LocalDate theSetDate = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = theSetDate.format(formatter);
        LocalDate resultDate = taskService.checkDeadlineByDate(date);
        Assertions.assertNotEquals(theSetDate, resultDate);

    }

    @Test
    void checkingNameLengthOrCreteDefault_ReturnChecked() {
        String expectedName = "TestTask";
        String resultName = taskService.checkingNameLengthOrCreteDefault(expectedName);
        Assertions.assertEquals(expectedName, resultName);
    }

    @Test
    void checkingNameLengthOrCreteDefault_CreateDefault() {
        String shortName = "SN";
        String resultName = taskService.checkingNameLengthOrCreteDefault(shortName);
        Assertions.assertEquals(DEFAULT_NAME, resultName);
    }

    @Test
    void getTaskListByStatusTest() {
        TaskStatus firstStatus = TaskStatus.TODO;
        TaskStatus secondStatus = TaskStatus.IN_PROCESS;
        TaskStatus thirdStatus = TaskStatus.DONE;

        Task firstTask = new Task(DEFAULT_NAME, TEST_DESCRIPTION,
                LocalDate.of(TASK_YEAR, TASK_MONTH, TASK_DAY_OF_MONTH), firstStatus);
        Task secondTask = new Task(DEFAULT_NAME, TEST_DESCRIPTION,
                LocalDate.of(TASK_YEAR, TASK_MONTH, TASK_DAY_OF_MONTH), secondStatus);
        Task thirdTask = new Task(DEFAULT_NAME, TEST_DESCRIPTION,
                LocalDate.of(TASK_YEAR, TASK_MONTH, TASK_DAY_OF_MONTH), thirdStatus);

        taskService.addTask(thirdTask);
        taskService.addTask(secondTask);
        taskService.addTask(firstTask);

        List<Task> expectedTaskList = List.of(firstTask, secondTask, thirdTask);
        List<Task> resultTaskList = taskService.getSortedTaskListByStatus();

        Assertions.assertEquals(expectedTaskList, resultTaskList);
    }

    @Test
    void getSortedTaskListByStatusOrderTest() {

        TaskStatus firstStatus = TaskStatus.TODO;
        TaskStatus secondStatus = TaskStatus.IN_PROCESS;
        TaskStatus thirdStatus = TaskStatus.DONE;

        Task firstTask = new Task(DEFAULT_NAME, TEST_DESCRIPTION,
                LocalDate.of(TASK_YEAR, TASK_MONTH, TASK_DAY_OF_MONTH), thirdStatus);
        Task secondTask = new Task(DEFAULT_NAME, TEST_DESCRIPTION,
                LocalDate.of(TASK_YEAR, TASK_MONTH, TASK_DAY_OF_MONTH), secondStatus);
        Task thirdTask = new Task(DEFAULT_NAME, TEST_DESCRIPTION,
                LocalDate.of(TASK_YEAR, TASK_MONTH, TASK_DAY_OF_MONTH), firstStatus);

        taskService.addTask(firstTask);
        taskService.addTask(secondTask);
        taskService.addTask(thirdTask);

        List<Task> expectedTaskList = List.of(firstTask, secondTask, thirdTask);
        List<Task> resultTaskList =
                taskService.getSortedTaskListByStatusOrder(firstStatus, secondStatus, thirdStatus);
        Assertions.assertEquals(expectedTaskList, resultTaskList);
    }

}
