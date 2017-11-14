package ru.sfedu.projectmanager.model;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.enums.EntityType;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.providers.DataProviderCsv;
import ru.sfedu.projectmanager.model.providers.DataProviderResult;

import java.util.ArrayList;
import java.util.List;


public class DataProviderCsvTest {

    private static Logger logger = Logger.getLogger(DataProviderCsv.class);
    private DataProviderCsv dataProvider;


    @Test
    public void test() throws Exception{
        saveRecords();
        getRecordsById();
        deleteRecords();
    }

    public void saveRecords() throws Exception {
        User user = new User(1L,"lehaLogin","mr.alexn95@mail.com","mycastpass");
        Assert.assertEquals(dataProvider.saveRecord(user, EntityType.USER).getResult(), ResultType.SUCCESSFUL);

        Task task = new Task(1L, "title", "description", 1L, 1L, 1L, "date");
        Assert.assertEquals(dataProvider.saveRecord(task, EntityType.TASK).getResult(), ResultType.SUCCESSFUL);

        Project project = new Project(1L, "title", "description", 1L, "date");
        Assert.assertEquals(dataProvider.saveRecord(project, EntityType.PROJECT).getResult(), ResultType.SUCCESSFUL);
    }

    public void getRecordsById() throws Exception {
        DataProviderResult result1 = dataProvider.getRecordById(1L, EntityType.USER);
        Assert.assertEquals(result1.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals((long)result1.getData().getId(), 1L);

        DataProviderResult result2 = dataProvider.getRecordById(1L, EntityType.TASK);
        Assert.assertEquals(result2.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals((long)result2.getData().getId(), 1L);

        DataProviderResult result3 = dataProvider.getRecordById(1L, EntityType.PROJECT);
        Assert.assertEquals(result3.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals((long)result3.getData().getId(), 1L);
    }

    public void deleteRecords() throws Exception {
        User user = new User(1L,"lehaLogin","mr.alexn95@mail.com","mycastpass");
        Assert.assertEquals(dataProvider.deleteRecord(user, EntityType.USER).getResult(), ResultType.SUCCESSFUL);

        Task task = new Task(1L, "title", "description", 1L, 1L, 1L, "date");
        Assert.assertEquals(dataProvider.deleteRecord(task, EntityType.TASK).getResult(), ResultType.SUCCESSFUL);

        Project project = new Project(1L, "title", "description", 1L, "date");
        Assert.assertEquals(dataProvider.deleteRecord(project, EntityType.PROJECT).getResult(), ResultType.SUCCESSFUL);
    }


    @Before
    public void setUp() throws Exception {
        dataProvider = new DataProviderCsv<>();
        dataProvider.initDataSource();

    }

    @After
    public void tearDown() throws Exception {
    }

}