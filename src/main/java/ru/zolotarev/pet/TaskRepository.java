package ru.zolotarev.pet;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    /*
     * List<Task> tasks +
     * add task +
     * get task by name/ID +
     * get List of all tasks +
     * */

    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    private Task getTaskByName(String name) {
        return tasks.stream().filter(task -> task.getName().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Задача с таким именем не найдена."));
    }

    private Task getTaskByID(int id) {
        return tasks.stream().filter(task -> task.getID() == id)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Задача с таким ID не найдена."));
    }

    public Task getTask(String name) {
        if (name.matches("\\d+")) {
            return getTaskByID(Integer.parseInt(name));
        }
        return getTaskByName(name);
    }

    public List<Task> getTaskList() {
        return tasks;
    }
}