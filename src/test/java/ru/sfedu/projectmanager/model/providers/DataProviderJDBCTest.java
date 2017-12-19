package ru.sfedu.projectmanager.model.providers;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;

import java.util.List;
import java.util.Random;

public class DataProviderJDBCTest {

    DataProviderJDBC<ru.sfedu.projectmanager.model.entries.WithId> dataProvider;
    private static Logger logger = Logger.getLogger(DataProviderJDBCTest.class);
    Random random = new Random();

    @Test
    public void usersTest(){
        User user = DataGenerator.createUser();
        Assert.assertEquals(dataProvider.saveRecord(user, EntryType.USER).getResult(), ResultType.SUCCESSFUL);

        MethodsResult trueResult = dataProvider.getUserByLogin(user.getLogin());
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(((User)trueResult.getBean()).getLogin(), user.getLogin());

        Long id = trueResult.getBean().getId();
        String login = ((User) trueResult.getBean()).getLogin();
        user = (User) trueResult.getBean();
        trueResult = dataProvider.getRecordById(id, EntryType.USER);
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(trueResult.getBean().getId(), id);
        Assert.assertEquals(((User) trueResult.getBean()).getLogin(), login);

        user.setEmail("test_email");
        Assert.assertEquals(dataProvider.updateRecord(user, EntryType.USER).getResult(), ResultType.SUCCESSFUL);
        trueResult = dataProvider.getUserByLogin(user.getLogin());
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(((User) trueResult.getBean()).getEmail(), user.getEmail());

        MethodsResult result = dataProvider.getAllRecords(EntryType.USER);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        List users = result.getBeans();
        Assert.assertTrue(users.contains(user));

        Assert.assertEquals(dataProvider.deleteRecord(id, EntryType.USER).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(id, EntryType.USER).getResult(), ResultType.ID_NOT_EXIST);
    }

    @Test
    public void tasksTest(){
        Task task = DataGenerator.createTask();
        Assert.assertEquals(dataProvider.saveRecord(task, EntryType.TASK).getResult(), ResultType.SUCCESSFUL);

        MethodsResult trueResult = dataProvider.getTasksByTitle(task.getTitle());
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        List<Task> tasks = trueResult.getBeans();
        Assert.assertTrue(tasks.contains(task));
        task = tasks.get(0);

        task.setState("some_test_state");
        Assert.assertEquals(dataProvider.updateRecord(task, EntryType.TASK).getResult(), ResultType.SUCCESSFUL);
        trueResult = dataProvider.getTasksByTitle(task.getTitle());
        Assert.assertEquals(trueResult.getResult(),ResultType.SUCCESSFUL);
        tasks = trueResult.getBeans();
        Assert.assertTrue(tasks.contains(task));

        MethodsResult result = dataProvider.getAllRecords(EntryType.TASK);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        tasks = result.getBeans();
        Assert.assertTrue(tasks.contains(task));

        Assert.assertEquals(dataProvider.deleteRecord(task.getId(), EntryType.TASK).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(task.getId(), EntryType.TASK).getResult(), ResultType.ID_NOT_EXIST);
    }

    @Test
    public void projectTest(){
        Project project = DataGenerator.createProject();
        Assert.assertEquals(dataProvider.saveRecord(project, EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);

        MethodsResult trueResult = dataProvider.getProjectByTitle(project.getTitle());
        Project resultBean = (Project)trueResult.getBean();
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(resultBean, project);

        resultBean.setState("update_state");
        Assert.assertEquals(dataProvider.updateRecord(resultBean, EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);
        trueResult = dataProvider.getProjectByTitle(resultBean.getTitle());
        Assert.assertEquals(trueResult.getResult(),ResultType.SUCCESSFUL);
        Project searchedProject = (Project) trueResult.getBean();
        Assert.assertEquals(searchedProject, resultBean);

        MethodsResult result = dataProvider.getAllRecords(EntryType.PROJECT);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        List<Project> projects = result.getBeans();
        Assert.assertTrue(projects.contains(resultBean));

        Assert.assertEquals(dataProvider.deleteRecord(resultBean.getId(), EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(project.getId(), EntryType.PROJECT).getResult(), ResultType.ID_NOT_EXIST);
    }



    @Before
    public void setUp(){
        dataProvider = new DataProviderJDBC<>();
        dataProvider.initDataSource();
    }

    @After
    public void tearDown(){
        dataProvider.closeConnection();
    }
}
