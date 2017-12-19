package ru.sfedu.projectmanager.model.providers;

import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.enums.MethodsResult;

import java.util.Date;
import java.util.Calendar;
import java.util.Random;

public class DataGenerator {
    private static final Random random = new Random();
    private static final long date = new Date().getTime();

    public static User createUser(){
        User user = new User();
        user.setId(random.nextLong());
        user.setLogin("login_" + random.nextInt());
        user.setEmail("test@mail.com");
        user.setPassword("test_password");
        user.setProjectId(null);
        return user;
    }

    public static Project createProject( ){
        Project project = new Project();
        project.setId(random.nextLong());
        project.setTitle("projectTitle_" + random.nextInt());
        project.setCreateDate(date);
        project.setState("test_state_open");
        project.setDescription("test_description");
        return project;
    }

    public static Task createTask(){
        Task task = new Task();
        task.setId(random.nextLong());
        task.setTitle("taskTitle_" + random.nextInt());
        task.setType("bug");
        task.setState("test_state_open");
        task.setDescription("test_description");
        task.setCreateDate(date);
        task.setUserId(null);
        task.setProjectId(null);
        return task;
    }
}
