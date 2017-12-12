//package ru.sfedu.projectmanager.services.EntryServices;
//
//import org.apache.log4j.Logger;
//import ru.sfedu.projectmanager.Constants;
//import ru.sfedu.projectmanager.model.entries.User;
//import ru.sfedu.projectmanager.model.enums.EntryType;
//import ru.sfedu.projectmanager.model.enums.MethodsResult;
//import ru.sfedu.projectmanager.model.enums.ResultType;
//import ru.sfedu.projectmanager.model.providers.UserDataProvider.UserDataProviderXML;
//import ru.sfedu.projectmanager.utils.ConfigurationUtil;
//
//import java.io.IOException;
//
//public class UserService {
//
//    private UserDataProvider dataProvider;
//    private static Logger logger = Logger.getLogger(UserService.class);
//
//    public MethodsResult saveUser(User entry) {
//        dataProvider.initDataSource();
//        if (entry.getProjectId() != null){
//            MethodsResult projectResult = dataProvider.getRecordById(entry.getProjectId(), EntryType.PROJECT);
//            if (projectResult.getResult() != ResultType.SUCCESSFUL) return projectResult;
//        }
//        if (dataProvider.getUserByLogin(entry.getLogin()).getResult() == ResultType.SUCCESSFUL)
//            return new MethodsResult(ResultType.LOGIN_ALREADY_EXIST);
//        dataProvider.saveRecord(entry, EntryType.USER);
//        return new MethodsResult(ResultType.SUCCESSFUL);
//    }
//
//    public MethodsResult deleteUser(User entry) {
//        dataProvider.initDataSource();
//        return dataProvider.deleteRecord(entry.getId(), EntryType.USER);
//    }
//
//    public MethodsResult getUserByLogin(String login) {
//        dataProvider.initDataSource();
//        return dataProvider.getUserByLogin(login);
//    }
//
//    public MethodsResult getUserById(Long id) {
//        dataProvider.initDataSource();
//        return dataProvider.getRecordById(id, EntryType.USER);
//    }
//
//    public MethodsResult initDataProvider() {
//        String dataSource;
//        try {
//            dataSource = ConfigurationUtil.getConfigurationEntry(Constants.TYPE_OF_SOURCE);
//        } catch (IOException e){
//            logger.trace(e.getMessage());
//            return new MethodsResult(ResultType.IO_EXCEPTION);
//        }
//
//        if (dataSource.equals("csv")){
//            dataProvider = new UserDataProviderCSV();
//            return new MethodsResult(ResultType.SUCCESSFUL);
//        } else if (dataSource.equals("xml")){
//            dataProvider = new UserDataProviderXML();
//            return new MethodsResult(ResultType.SUCCESSFUL);
//        }
//        return new MethodsResult(ResultType.SOME_ERROR);
//    }
//}
