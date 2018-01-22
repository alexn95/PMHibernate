package ru.sfedu.projectmanager.model.providers.XMLTests;

import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.providers.DataGenerator;
import ru.sfedu.projectmanager.model.providers.DataProviderXML;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XMLUserTests {

    private static Logger logger = Logger.getLogger(XMLUserTests.class);
    private static DataProviderXML dataProvider;
    private static User user;


//    create user test
    @Test
    public void testA() throws InterruptedException{
        Thread.sleep(1);
        User fakeUser = DataGenerator.createUser();
        fakeUser.setLogin(user.getLogin());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(user).getResult());
        Assert.assertEquals(ResultType.ID_ALREADY_EXIST, dataProvider.saveRecord(user).getResult());
        Assert.assertEquals(ResultType.LOGIN_ALREADY_EXIST, dataProvider.saveRecord(fakeUser).getResult());

        MethodsResult trueResult = dataProvider.getRecordById(user);
        Assert.assertEquals(trueResult.getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(trueResult.getBean(), user);
    }

//    get user by login test
    @Test
    public void testB() {
        User fakeUser = DataGenerator.createUser();
        MethodsResult trueResult = dataProvider.getUserByLogin(user.getLogin());
        MethodsResult fakeResult = dataProvider.getUserByLogin(fakeUser.getLogin());
        Assert.assertEquals(ResultType.SUCCESSFUL, trueResult.getResult());
        Assert.assertEquals(trueResult.getBean(), user);
        Assert.assertEquals(ResultType.LOGIN_NOT_EXIST, fakeResult.getResult());
    }

//    update users test
    @Test
    public void testC(){
        User updateUser = DataGenerator.createUpdatedUser();
        updateUser.setId(user.getId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.updateRecord(updateUser).getResult());
        MethodsResult trueResult = dataProvider.getRecordById(updateUser);
        Assert.assertEquals(ResultType.SUCCESSFUL, trueResult.getResult());
        Assert.assertEquals(updateUser.getEmail(), ((User)trueResult.getBean()).getEmail());
    }

//    delete user test
    @Test
    public void testD(){
        Task updatedTestTask = DataGenerator.createTask();
        updatedTestTask.setUserId(user.getId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.saveRecord(updatedTestTask).getResult());

        Assert.assertEquals(dataProvider.deleteRecord(user).getResult(), ResultType.SUCCESSFUL);
        Assert.assertEquals(dataProvider.getRecordById(user).getResult(), ResultType.ID_NOT_EXIST);

        Assert.assertEquals(null, ((Task)dataProvider.getRecordById(updatedTestTask).getBean()).getUserId());
        Assert.assertEquals(ResultType.SUCCESSFUL, dataProvider.deleteRecord(updatedTestTask).getResult());
    }

//    get all users test
    @Test
    public void testF(){
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            user = DataGenerator.createUser();
            users.add(user);
            Assert.assertEquals(dataProvider.saveRecord(user).getResult(), ResultType.SUCCESSFUL);
        }
        MethodsResult result = dataProvider.getAllRecords(EntryType.USER);
        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
        List allUsers = result.getBeans();
        Assert.assertTrue(users.stream().allMatch(allUsers::contains));
        users.forEach(deletedRecord -> dataProvider.deleteRecord(deletedRecord));
    }


    @BeforeClass
    public static void setUp() throws Exception {
        dataProvider = new DataProviderXML();
        dataProvider.initDataSource();
        user = DataGenerator.createUser();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }

}