package ru.sfedu.projectmanager.model.providers.CSVTests;

import org.apache.log4j.Logger;
import org.junit.*;
import org.omg.PortableInterceptor.SUCCESSFUL;
import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.providers.DataGenerator;
import ru.sfedu.projectmanager.model.providers.DataProviderCSV;


public class CSVConstrainTests {

    private static Logger logger = Logger.getLogger(CSVTaskTests.class);
    private static DataProviderCSV dataProvider;
    private static Task task;
    private static User user;
    private static Project project;

    @BeforeClass
    public static void setUp(){
        dataProvider = new DataProviderCSV<>();
        dataProvider.initDataSource();
        task = DataGenerator.createTask();
        user = DataGenerator.createUser();
        project = DataGenerator.createProject();

    }


    @Test
    public void integrityConstraintsTestA(){
        user.setProjectId(project.getId());
        task.setProjectId(project.getId());
        task.setUserId(user.getId());

        Assert.assertEquals(ResultType.INTEGRITY_CONSTRAIN_EXCEPTION, dataProvider.saveRecord(user).getResult());
        Assert.assertEquals(ResultType.INTEGRITY_CONSTRAIN_EXCEPTION, dataProvider.saveRecord(task).getResult());

        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(project).getResult());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(user).getResult());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(task).getResult());
    }

    @Test
    public void integrityConstraintsTestB() {
        dataProvider.integrityCheck();
        Task testTask = (Task)dataProvider.getRecordById(task).getBean();
        Project testProject = new Project();
        testProject.setId(testTask.getProjectId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.getRecordById(testProject).getResult());
    }

//    @Test
    public void integrityConstraintsTestD() {
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.deleteRecord(user).getResult());
        Task newTask = (Task)dataProvider.getRecordById(task).getBean();
        Assert.assertNull(newTask.getUserId());

        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.deleteRecord(project).getResult());
        Assert.assertEquals(ResultType.ID_NOT_EXIST, dataProvider.deleteRecord(task).getResult());
    }

    @AfterClass
    public static void tearDown(){

    }
}
