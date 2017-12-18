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
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataProviderXMLTest {

    DataProviderXML dataProvider;
    private static Logger logger = Logger.getLogger(DataProviderXMLTest.class);
    private final Random random = new Random();

    @Test
    public void userTest() throws Exception {
        User user = DataGenerator.createUser();
        User fakeUser = DataGenerator.createUser();
        Assert.assertEquals(dataProvider.saveRecord(user, EntryType.USER).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.saveRecord(user, EntryType.USER).getResult(), ResultType.ID_ALREADY_EXIST);

        MethodsResult trueResult = dataProvider.getRecordById(user.getId(), EntryType.USER);
        MethodsResult fakeResult = dataProvider.getRecordById(fakeUser.getId(), EntryType.USER);
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(trueResult.getBean().getId(), user.getId());
        Assert.assertEquals(fakeResult.getResult(), ResultType.ID_NOT_EXIST);

        trueResult = dataProvider.getUserByLogin(user.getLogin());
        fakeResult = dataProvider.getUserByLogin(fakeUser.getLogin());
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(((User)trueResult.getBean()).getLogin(),user.getLogin());
        Assert.assertEquals(fakeResult.getResult(), ResultType.LOGIN_NOT_EXIST);

        user.setEmail("test_email");
        Assert.assertEquals(dataProvider.updateRecord(user, EntryType.USER).getResult(), ResultType.SUCCESSFUL);
        trueResult = dataProvider.getRecordById(user.getId(), EntryType.USER);
        Assert.assertEquals(trueResult.getResult(),ResultType.SUCCESSFUL);
        Assert.assertEquals(((User) trueResult.getBean()).getEmail(), user.getEmail());

        Assert.assertEquals(dataProvider.deleteRecord(user.getId(), EntryType.USER).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(user.getId(), EntryType.USER).getResult(), ResultType.ID_NOT_EXIST);

        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            user = DataGenerator.createUser();
            users.add(user);
            Assert.assertEquals(dataProvider.saveRecord(user, EntryType.USER).getResult(), ResultType.SUCCESSFUL);
        }
        MethodsResult result = dataProvider.getAllRecords(EntryType.USER);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        List allUsers = result.getBeans();
        Assert.assertTrue(users.stream().allMatch(allUsers::contains));

        users.forEach(deletedRecord -> dataProvider.deleteRecord(deletedRecord.getId(), EntryType.USER));
    }

    @Test
    public void taskTest() throws Exception {
        Task task = DataGenerator.createTask();
        Task fakeTask = DataGenerator.createTask();
        Assert.assertEquals(dataProvider.saveRecord(task, EntryType.TASK).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.saveRecord(task, EntryType.TASK).getResult(), ResultType.ID_ALREADY_EXIST);

        MethodsResult trueResult = dataProvider.getRecordById(task.getId(), EntryType.TASK);
        MethodsResult fakeResult = dataProvider.getRecordById(fakeTask.getId(), EntryType.TASK);
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(trueResult.getBean().getId(), task.getId());
        Assert.assertEquals(fakeResult.getResult(), ResultType.ID_NOT_EXIST);

        trueResult = dataProvider.getTasksByTitle(task.getTitle());
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        List<Task> tasks = trueResult.getBeans();
        Assert.assertTrue(tasks.contains(task));

        task.setState("some_test_state");
        Assert.assertEquals(dataProvider.updateRecord(task, EntryType.TASK).getResult(), ResultType.SUCCESSFUL);
        trueResult = dataProvider.getRecordById(task.getId(), EntryType.TASK);
        Assert.assertEquals(trueResult.getResult(),ResultType.SUCCESSFUL);
        Assert.assertEquals(((Task) trueResult.getBean()).getState(), task.getState());

        Assert.assertEquals(dataProvider.deleteRecord(task.getId(), EntryType.TASK).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(task.getId(), EntryType.TASK).getResult(), ResultType.ID_NOT_EXIST);

        tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            task = DataGenerator.createTask();
            tasks.add(task);
            Assert.assertEquals(dataProvider.saveRecord(task, EntryType.TASK).getResult(), ResultType.SUCCESSFUL);
        }
        MethodsResult result = dataProvider.getAllRecords(EntryType.TASK);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        List allTasks = result.getBeans();
        Assert.assertTrue(tasks.stream().allMatch(allTasks::contains));

        tasks.forEach(deletedRecord -> dataProvider.deleteRecord(deletedRecord.getId(), EntryType.TASK));
    }

    @Test
    public void projectTest() throws Exception {
        Project project = DataGenerator.createProject();
        Project fakeProject = DataGenerator.createProject();
        Assert.assertEquals(dataProvider.saveRecord(project, EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.saveRecord(project, EntryType.PROJECT).getResult(), ResultType.ID_ALREADY_EXIST);

        MethodsResult trueResult = dataProvider.getRecordById(project.getId(), EntryType.PROJECT);
        MethodsResult fakeResult = dataProvider.getRecordById(fakeProject.getId(), EntryType.PROJECT);
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(trueResult.getBean().getId(), project.getId());
        Assert.assertEquals(fakeResult.getResult(), ResultType.ID_NOT_EXIST);

        trueResult = dataProvider.getProjectByTitle(project.getTitle());
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Project searchedProject = (Project)trueResult.getBean();
        logger.info(searchedProject);
        Assert.assertEquals(searchedProject, project);

        project.setState("some_test_state");
        Assert.assertEquals(dataProvider.updateRecord(project, EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);
        trueResult = dataProvider.getRecordById(project.getId(), EntryType.PROJECT);
        Assert.assertEquals(trueResult.getResult(),ResultType.SUCCESSFUL);
        Assert.assertEquals(((Project) trueResult.getBean()).getState(), project.getState());

        Assert.assertEquals(dataProvider.deleteRecord(project.getId(), EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(fakeProject.getId(), EntryType.PROJECT).getResult(), ResultType.ID_NOT_EXIST);

        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            project = DataGenerator.createProject();
            projects.add(project);
            Assert.assertEquals(dataProvider.saveRecord(project, EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);
        }
        MethodsResult result = dataProvider.getAllRecords(EntryType.PROJECT);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        List allProjects = result.getBeans();
        Assert.assertTrue(projects.stream().allMatch(allProjects::contains));

        projects.forEach(deletedRecord -> dataProvider.deleteRecord(deletedRecord.getId(), EntryType.PROJECT));
    }

    @Before
    public void setUp(){
        dataProvider = new DataProviderXML<>();
        dataProvider.initDataSource();
    }

    @After
    public void tearDown(){

    }

}