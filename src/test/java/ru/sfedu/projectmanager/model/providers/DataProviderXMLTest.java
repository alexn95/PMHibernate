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
import ru.sfedu.projectmanager.model.providers.DataProviderXML;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;

public class DataProviderXMLTest {

    DataProviderXML dataProvider;
    private static Logger logger = Logger.getLogger(DataProviderXMLTest.class);
    private final Random random = new Random();

    @Test
    public void userTest() throws Exception {
        Long id = random.nextLong();
        User user = new User(id,"lehaLogin","mr.alexn95@mail.com","mycastpass");
        Assert.assertEquals(dataProvider.saveRecord(user, EntryType.USER).getResult(), ResultType.SUCCESSFUL);

        MethodsResult result = dataProvider.getRecordById(id, EntryType.USER);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(result.getData().getId(), id);

        result = dataProvider.getUserByLogin(user.getLogin());
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(((User)result.getData()).getLogin(), user.getLogin());

        Assert.assertEquals(dataProvider.deleteRecord(user.getId(), EntryType.USER).getResult(), ResultType.SUCCESSFUL);
    }

    @Test
    public void taskTest() throws Exception {
        Long id = random.nextLong();
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        Task task = new Task(id, "title", "description", 1L, "state", "type", date);
        Assert.assertEquals(dataProvider.saveRecord(task, EntryType.TASK).getResult(), ResultType.SUCCESSFUL);

        MethodsResult result2 = dataProvider.getRecordById(id, EntryType.TASK);
        Assert.assertEquals(result2.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(result2.getData().getId(), id);

        task = new Task(id, "title", "description", 1L, "state", "type", date);
        Assert.assertEquals(dataProvider.deleteRecord(task.getId(), EntryType.TASK).getResult(), ResultType.SUCCESSFUL);
    }

    @Test
    public void projectTest() throws Exception {
        Long id = random.nextLong();
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        Project project = new Project(id, "title", "description", "state", date);
        Assert.assertEquals(dataProvider.saveRecord(project, EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);

        MethodsResult result3 = dataProvider.getRecordById(id, EntryType.PROJECT);
        Assert.assertEquals(result3.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(result3.getData().getId(), id);

        project = new Project(id, "title", "description", "state", date);
        Assert.assertEquals(dataProvider.deleteRecord(project.getId(), EntryType.PROJECT).getResult(), ResultType.SUCCESSFUL);
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