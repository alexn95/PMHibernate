package ru.sfedu.projectmanager.model.providers.JDBCTests;

import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.providers.DataGenerator;
import ru.sfedu.projectmanager.model.providers.DataProviderJDBC;
import ru.sfedu.projectmanager.model.providers.DataProviderXML;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskTests {

    private static Logger logger = Logger.getLogger(TaskTests.class);
    private static DataProviderJDBC dataProvider;
    private static Task task;


//    create task test
    @Test
    public void testA() {
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(task, EntryType.TASK).getResult());
        Assert.assertEquals(ResultType.SQL_INTEGRITY_CONSTRAIN_EXCEPTION, dataProvider.saveRecord(task, EntryType.TASK).getResult());
        MethodsResult trueResult = dataProvider.getRecordById(task.getId(), EntryType.TASK);
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
        Assert.assertNull(fakeResult.getBeans());
    }

//    update task test
    @Test
    public void testC(){
        Task updateTask = DataGenerator.createUpdatedTask();
        updateTask.setId(task.getId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.updateRecord(updateTask, EntryType.TASK).getResult());
        MethodsResult trueResult = dataProvider.getRecordById(updateTask.getId(), EntryType.TASK);
        Assert.assertEquals(ResultType.SUCCESSFUL, trueResult.getResult());
        Assert.assertEquals(updateTask.getDescription(), ((Task)trueResult.getBean()).getDescription());
    }

//    delete task test
    @Test
    public void testD(){
        Assert.assertEquals(dataProvider.deleteRecord(task.getId(), EntryType.TASK).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(task.getId(), EntryType.TASK).getResult(), ResultType.ID_NOT_EXIST);
    }

//    get all task test
    @Test
    public void testF() throws InterruptedException{
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Thread.sleep(1);
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

    @BeforeClass
    public static void setUp() throws Exception {
        dataProvider = new DataProviderJDBC();
        dataProvider.initDataSource();
        task = DataGenerator.createTask();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }

}