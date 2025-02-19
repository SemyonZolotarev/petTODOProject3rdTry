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

    public final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    public Task getTask(String name) {
        try {
            int id = Integer.parseInt(name);
            return getTaskByID(id);
        } catch (NumberFormatException e) {
            try {
                return getTaskByName(name);
            } catch (IllegalArgumentException r) {
                return null;
            }
        }
    }

    public Task getTaskByName(String name) {
        return tasks.stream().filter(task -> task.getName().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Задача с таким именем не найдена."));
    }

    public Task getTaskByID(int id) {
        return tasks.stream().filter(task -> task.getID() == id)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Задача с таким ID не найдена."));
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}