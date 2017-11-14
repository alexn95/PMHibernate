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
import ru.sfedu.projectmanager.model.providers.DataProviderResult;
import ru.sfedu.projectmanager.model.providers.DataProviderXML;

public class DataProviderXMLTest {

    DataProviderXML dataProvider;
    private static Logger logger = Logger.getLogger(DataProviderXML.class);

    @Test
    public void test() throws Exception{
        saveRecord();
        getRecordById();
        deleteRecord();
    }

    public void saveRecord() throws Exception {
        User user = new User(10L,"lehaLogin","mr.alexn95@mail.com","mycastpass");
        Assert.assertEquals(dataProvider.saveRecord(user, EntityType.USER).getResult(), ResultType.SUCCESSFUL);

        Task task = new Task(10L, "title", "description", 1L, 1L, 1L, "date");
        Assert.assertEquals(dataProvider.saveRecord(task, EntityType.TASK).getResult(), ResultType.SUCCESSFUL);

        Project project = new Project(10L, "title", "description", 1L, "date");
        Assert.assertEquals(dataProvider.saveRecord(project, EntityType.PROJECT).getResult(), ResultType.SUCCESSFUL);
    }

    public void getRecordById() throws Exception {
        DataProviderResult result = dataProvider.getRecordById(10L,EntityType.USER);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals((long)result.getData().getId(), 10L);

        DataProviderResult result2 = dataProvider.getRecordById(10L, EntityType.TASK);
        Assert.assertEquals(result2.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals((long)result2.getData().getId(), 10L);

        DataProviderResult result3 = dataProvider.getRecordById(10L, EntityType.PROJECT);
        Assert.assertEquals(result3.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals((long)result3.getData().getId(), 10L);
    }

    public void deleteRecord() throws Exception {
        User user = new User(10L,"lehaLogin","mr.alexn95@mail.com","mycastpass");
        Assert.assertEquals(dataProvider.deleteRecord(user, EntityType.USER).getResult(), ResultType.SUCCESSFUL);

        Task task = new Task(10L, "title", "description", 1L, 1L, 1L, "date");
        Assert.assertEquals(dataProvider.deleteRecord(task, EntityType.TASK).getResult(), ResultType.SUCCESSFUL);

        Project project = new Project(10L, "title", "description", 1L, "date");
        Assert.assertEquals(dataProvider.deleteRecord(project, EntityType.PROJECT).getResult(), ResultType.SUCCESSFUL);
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