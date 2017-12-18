//package ru.sfedu.projectmanager.servicesTests;
//
//import org.apache.log4j.Logger;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import ru.sfedu.projectmanager.model.entries.User;
//import ru.sfedu.projectmanager.model.enums.EntryType;
//import ru.sfedu.projectmanager.model.enums.MethodsResult;
//import ru.sfedu.projectmanager.model.enums.ResultType;
//import ru.sfedu.projectmanager.model.providers.DataProviderCSV;
//import ru.sfedu.projectmanager.services.EntryServices.UserService;
//
//import javax.jws.soap.SOAPBinding;
//import java.util.Random;
//
//public class UserServiceTest {
//    private static Logger logger = Logger.getLogger(DataProviderCSV.class);
//    private UserService service;
//    private final Random random = new Random();
//
//    @Test
//    public void userServiceTest(){
//        Long id = random.nextLong();
//        User user = new User(id,"lehaLogin","mr.alexn95@mail.com","mycastpass");
//        Assert.assertEquals(service.saveUser(user).getResult(), ResultType.SUCCESSFUL);
//        Assert.assertEquals(service.saveUser(user).getResult(), ResultType.LOGIN_ALREADY_EXIST);
//
//        MethodsResult result = service.getUserByLogin(user.getLogin());
//        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
//        Assert.assertEquals(result.getBean().getId(), user.getId());
//
//        result = service.getUserById(user.getId());
//        Assert.assertEquals(result.getResult(), ResultType.SUCCESSFUL);
//        Assert.assertEquals(result.getBean().getId(), user.getId());
//
//        Assert.assertEquals(service.deleteUser(user).getResult(), ResultType.SUCCESSFUL);
//        Assert.assertEquals(service.deleteUser(user).getResult(), ResultType.ID_NOT_EXIST);
//
//        Assert.assertEquals(service.getUserById(user.getId()).getResult(), ResultType.ID_NOT_EXIST);
//        Assert.assertEquals(service.getUserByLogin(user.getLogin()).getResult(), ResultType.ID_NOT_EXIST);
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        service = new UserService();
//        service.initDataProvider();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//}
