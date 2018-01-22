package ru.sfedu.projectmanager.model.providers.JDBCTests;

import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.providers.DataGenerator;
import ru.sfedu.projectmanager.model.providers.DataProviderJDBC;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCProjectTests {

    private static Logger logger = Logger.getLogger(JDBCProjectTests.class);
    private static DataProviderJDBC dataProvider;
    private static Project project;

    //    create project test
    @Test
    public void testA() {
        Project fakeProject = DataGenerator.createProject();
        fakeProject.setTitle(project.getTitle());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(project).getResult());
        Assert.assertEquals(ResultType.INTEGRITY_CONSTRAIN_EXCEPTION, dataProvider.saveRecord(project).getResult());
        Assert.assertEquals(ResultType.INTEGRITY_CONSTRAIN_EXCEPTION, dataProvider.saveRecord(fakeProject).getResult());

        MethodsResult trueResult = dataProvider.getRecordById(project);
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(trueResult.getBean(), project);
    }

    //    get project by title test
    @Test
    public void testB() {
        Project fakeProject = DataGenerator.createProject();
        MethodsResult trueResult = dataProvider.getProjectByTitle(project.getTitle());
        MethodsResult fakeResult = dataProvider.getProjectByTitle(fakeProject.getTitle());
        Assert.assertEquals(ResultType.SUCCESSFUL, trueResult.getResult());
        Assert.assertEquals(trueResult.getBean(), project);
        Assert.assertEquals(ResultType.TITLE_NOT_EXIST, fakeResult.getResult());
    }

    //    update project test
    @Test
    public void testC(){
        Project updateProject = DataGenerator.createUpdatedProject();
        updateProject.setId(project.getId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.updateRecord(updateProject).getResult());
        MethodsResult trueResult = dataProvider.getRecordById(updateProject);
        Assert.assertEquals(ResultType.SUCCESSFUL, trueResult.getResult());
        Assert.assertEquals(updateProject.getDescription(), ((Project)trueResult.getBean()).getDescription());
    }

    //    delete project test
    @Test
    public void testD(){
        Task updatedTestTask = DataGenerator.createTask();
        updatedTestTask.setProjectId(project.getId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(updatedTestTask).getResult());

        User updatedTestUser = DataGenerator.createUser();
        updatedTestUser.setProjectId(project.getId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(updatedTestUser).getResult());

        Assert.assertEquals(dataProvider.deleteRecord(project).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(project).getResult(), ResultType.ID_NOT_EXIST);

        Assert.assertEquals(ResultType.ID_NOT_EXIST, dataProvider.getRecordById(updatedTestTask).getResult());

        Assert.assertEquals(null, ((User)dataProvider.getRecordById(updatedTestUser).getBean()).getProjectId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.deleteRecord(updatedTestUser).getResult());

    }

    //    get all project test
    @Test
    public void testF() throws InterruptedException{
        ArrayList<Project> projects = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            project = DataGenerator.createProject();
            Thread.sleep(1);
            projects.add(project);
            Assert.assertEquals(dataProvider.saveRecord(project).getResult(), ResultType.SUCCESSFUL);
        }
        MethodsResult result = dataProvider.getAllRecords(EntryType.PROJECT);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        List allProjects = result.getBeans();
        Assert.assertTrue(projects.stream().allMatch(allProjects::contains));
        projects.forEach(deletedRecord -> dataProvider.deleteRecord(deletedRecord));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        dataProvider = new DataProviderJDBC();
        dataProvider.initDataSource();
        project = DataGenerator.createProject();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        dataProvider.closeConnection();
    }

}