package ru.sfedu.projectmanager.model.providers.XMLTests;

import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.providers.DataGenerator;
import ru.sfedu.projectmanager.model.providers.DataProviderXML;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XMLTaskTests {

    private static Logger logger = Logger.getLogger(XMLTaskTests.class);
    private static DataProviderXML dataProvider;
    private static Task task;


//    create task test
    @Test
    public void testA() {
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(task).getResult());
        Assert.assertEquals(ResultType.ID_ALREADY_EXIST, dataProvider.saveRecord(task).getResult());
        MethodsResult trueResult = dataProvider.getRecordById(task);
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(trueResult.getBean(), task);
    }

//    get task by title test
    @Test
    public void testB() {
        Task fakeTask = DataGenerator.createTask();
        MethodsResult trueResult = dataProvider.getTasksByTitle(task.getTitle());
        MethodsResult fakeResult = dataProvider.getTasksByTitle(fakeTask.getTitle());
        Assert.assertEquals(ResultType.SUCCESSFUL, trueResult.getResult());
        Assert.assertTrue(trueResult.getBeans().contains(task));
        Assert.assertEquals(0, fakeResult.getBeans().size());
    }

//    update task test
    @Test
    public void testC(){
        Task updateTask = DataGenerator.createUpdatedTask();
        updateTask.setId(task.getId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.updateRecord(updateTask).getResult());
        MethodsResult trueResult = dataProvider.getRecordById(updateTask);
        Assert.assertEquals(ResultType.SUCCESSFUL, trueResult.getResult());
        Assert.assertEquals(updateTask.getDescription(), ((Task)trueResult.getBean()).getDescription());
    }

//    delete task test
    @Test
    public void testD(){
        Assert.assertEquals(dataProvider.deleteRecord(task).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(task).getResult(), ResultType.ID_NOT_EXIST);
    }

//    get all task test
    @Test
    public void testF(){
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            task = DataGenerator.createTask();
            tasks.add(task);
            Assert.assertEquals(dataProvider.saveRecord(task).getResult(), ResultType.SUCCESSFUL);
        }
        MethodsResult result = dataProvider.getAllRecords(EntryType.TASK);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        List allTasks = result.getBeans();
        Assert.assertTrue(tasks.stream().allMatch(allTasks::contains));
        tasks.forEach(deletedRecord -> dataProvider.deleteRecord(deletedRecord));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        dataProvider = new DataProviderXML();
        dataProvider.initDataSource();
        task = DataGenerator.createTask();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }

}