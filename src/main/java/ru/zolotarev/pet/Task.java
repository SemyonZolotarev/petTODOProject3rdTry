package ru.zolotarev.pet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
public class Task {
    private static int count = 0;
    private final int ID = ++count;
    private String name;
    private String description;
    private LocalDate deadline;
    private TaskStatus status = TaskStatus.TODO;

    public String toString() {
        return "\n------------" +
                "\nTaskName: '" + this.getName() + "'. ID: " + this.getID()
                + ".\n Status: " + this.getStatus()
                + ".\n Deadline: " + this.getDeadline()
                + ".\n Description: " + this.getDescription() +
                "\n------------";
    }
}
