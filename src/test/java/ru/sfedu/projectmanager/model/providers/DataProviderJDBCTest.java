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

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;

public class DataProviderJDBCTest {

    DataProviderJDBC<ru.sfedu.projectmanager.model.entries.WithId> dataProvider;
    private static Logger logger = Logger.getLogger(DataProviderJDBCTest.class);
    Random random = new Random();

    @Test
    public void usersTest(){
        String login = "login" + random.nextLong();
        User user = new User(login,"mr.alexn95@mail.com","mycastpass", null);
        Assert.assertEquals(dataProvider.saveRecord(user, EntryType.USER).getResult(), ResultType.SUCCESSFUL);

        Long id = 1L;
        login = "test_login";
        MethodsResult result = dataProvider.getRecordById(id, EntryType.USER);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(((User)result.getData()).getLogin(), login);
        Assert.assertEquals((result.getData()).getId(), id);
    }

    @Test
    public void tasksTest(){
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        Task task = new Task("title", "description", null, "state", "type", date);
        Assert.assertEquals(dataProvider.saveRecord(task, EntryType.TASK).getResult(),ResultType.SUCCESSFUL);

        Long id = 1L;
        MethodsResult result = dataProvider.getRecordById(id, EntryType.TASK);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(result.getData().getId(), id);
    }

    @Test
    public void projectTest(){
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        Project project = new Project("title", "description", "state", date);
        Assert.assertEquals(dataProvider.saveRecord(project, EntryType.PROJECT).getResult(),ResultType.SUCCESSFUL);

        Long id = 1L;
        MethodsResult result = dataProvider.getRecordById(id, EntryType.PROJECT);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(result.getData().getId(), id);
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
