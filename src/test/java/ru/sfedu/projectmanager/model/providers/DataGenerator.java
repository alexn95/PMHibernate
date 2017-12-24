package ru.sfedu.projectmanager.model.providers;

import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;

import java.util.Date;
import java.util.Random;

public class DataGenerator {

    public static User createUser(){
        User user = new User();
        user.setId(new Date().getTime());
        user.setLogin("login_" + new Date().getTime());
        user.setEmail("test@mail.com");
        user.setPassword("test_password");
        user.setProjectId(null);
        return user;
    }

    public static User createUpdatedUser(){
        User user = new User();
        user.setId(new Date().getTime());
        user.setLogin("login_" + new Date().getTime());
        user.setEmail("update_mail@mail.com");
        user.setPassword("update_password");
        user.setProjectId(null);
        return user;
    }

    public static Project createProject( ){
        Project project = new Project();
        project.setId(new Date().getTime());
        project.setTitle("projectTitle_" + new Date().getTime());
        project.setCreateDate(new Date().getTime());
        project.setState("test_state_open");
        project.setDescription("test_description");
        return project;
    }

    public static Project createUpdatedProject(){
        Project project = new Project();
        project.setId(new Date().getTime());
        project.setTitle("projectTitle_" + new Date().getTime());
        project.setCreateDate(new Date().getTime());
        project.setState("update_state");
        project.setDescription("update_desc");
        return project;
    }

    public static Task createTask(){
        Task task = new Task();
        task.setId(new Date().getTime());
        task.setTitle("taskTitle_" + new Date().getTime());
        task.setType("bug");
        task.setState("test_state_open");
        task.setDescription("test_description");
        task.setCreateDate(new Date().getTime());
        task.setUserId(null);
        task.setProjectId(null);
        return task;
    }

    public static Task createUpdatedTask(){
        Task task = new Task();
        task.setId(new Date().getTime());
        task.setTitle("taskTitle_" + new Date().getTime());
        task.setType("update_type");
        task.setState("update_state");
        task.setDescription("update_desc");
        task.setCreateDate(new Date().getTime());
        task.setUserId(null);
        task.setProjectId(null);
        return task;
    }
}
