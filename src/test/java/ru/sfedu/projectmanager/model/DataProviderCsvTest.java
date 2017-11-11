package ru.sfedu.projectmanager.model;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sfedu.projectmanager.model.enrities.Project;
import ru.sfedu.projectmanager.model.enrities.Task;
import ru.sfedu.projectmanager.model.enrities.User;
import ru.sfedu.projectmanager.model.entityType.EntityType;

import java.util.ArrayList;
import java.util.List;


public class DataProviderCsvTest {

    private static Logger logger = Logger.getLogger(DataProviderCsv.class);
    private DataProviderCsv dataProviderUser;
    private DataProviderCsv dataProviderTask;
    private DataProviderCsv dataProviderProject;


    @Test
    public void saveRecord() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "myTask", "some description", 1L, 1L, 1L, "06.11.2017"));
        tasks.add(new Task(2L, "myTask", "some description", 1L, 1L, 1L, "06.11.2017"));
        dataProviderTask.saveRecord(tasks, EntityType.TASK);

        List<User> users = new ArrayList<>();
        users.add(new User(3L,"lehaLogin","mr.alexn95@mail.com","mycastpass"));
        users.add(new User(4L,"lehaLogin","mr.alexn95@mail.com","mycastpass"));
        dataProviderUser.saveRecord(users, EntityType.USER);

        List<Project> projects = new ArrayList<>();
        projects.add(new Project(5L, "some project", "description", 1L,"some date"));
        projects.add(new Project(6L, "some project", "description", 1L,"some date"));
        dataProviderProject.saveRecord(projects, EntityType.PROJECT);
    }

    @Test
    public void deleteRecord() throws Exception {
        dataProviderUser.deleteRecord(new User(3L,"asdf","mr.alexn95@mail.com","mycastpass"), EntityType.USER);

        dataProviderProject.deleteRecord(new Project(5L, "asdf", "description", 1L,"some date"), EntityType.PROJECT);

        dataProviderTask.deleteRecord(new Task(2L, "asdf", "some description", 1L, 1L, 1L, "06.11.2017"), EntityType.TASK);
    }

    @Test
    public void getRecordById() throws Exception {
        System.out.println((dataProviderTask.getRecordById(1L,EntityType.TASK).getId()));
        System.out.println((dataProviderUser.getRecordById(4L,EntityType.USER).getId()));
        System.out.println((dataProviderProject.getRecordById(6L,EntityType.PROJECT).getId()));

    }

    @Before
    public void setUp() throws Exception {
        dataProviderUser = new DataProviderCsv<User>();
        dataProviderUser.initDataSource();

        dataProviderTask = new DataProviderCsv<Task>();
        dataProviderTask.initDataSource();

        dataProviderProject = new DataProviderCsv<Project>();
        dataProviderProject.initDataSource();
    }

    @After
    public void tearDown() throws Exception {
    }



}