package ru.sfedu.projectmanager.model.providers;

import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.entries.WithId;
import java.io.*;
import java.util.List;
import java.util.Objects;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.apache.log4j.Logger;
import ru.sfedu.projectmanager.Constants;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.utils.ConfigurationUtil;

public class DataProviderCSV<T extends WithId> implements IDataProvider<T> {

    protected static Logger logger = Logger.getLogger(DataProviderCSV.class);
    private String users_path;
    private String projects_path;
    private String tasks_path;

    private StatefulBeanToCsv beanToCsv;
    private CsvToBean csvToBean;

    private FileWriter writer;
    private FileReader reader;


    @Override
    public MethodsResult saveRecord(T insertedBean, EntryType type) {
        try {
            initCsvToBean(type);
            initBeanToScv(type, true);
            List<T> beans = csvToBean.parse();
            if (beans.stream().filter(bean -> Objects.equals(insertedBean.getId(), bean.getId())).findFirst().orElse(null) == null){
                beanToCsv.write(insertedBean);
                writer.close();
                return new MethodsResult(ResultType.SUCCESSFUL);
            } else {
                writer.close();
                return new MethodsResult(ResultType.ID_ALREADY_EXIST);
            }
        } catch (Exception e){
            logger.error(e);
        }
        return new MethodsResult(ResultType.SOME_ERROR);
    }

    @Override
    public MethodsResult deleteRecord(long id, EntryType type){
        try {
            initCsvToBean(type);
            List<T> beans = csvToBean.parse();
            T deletedBean = beans.stream().filter(currentBean -> Objects.equals(id, currentBean.getId())).findFirst().orElse(null);
            if (deletedBean != null){
                beans.remove(deletedBean);
                initBeanToScv(type, false);
                beanToCsv.write(beans);
                writer.close();
                return new MethodsResult(ResultType.SUCCESSFUL);
            } else {
                writer.close();
                return new MethodsResult(ResultType.ID_NOT_EXIST);
            }
        } catch (Exception e){
            logger.error(e);
        }
        return new MethodsResult(ResultType.SOME_ERROR);
    }

    @Override
    public MethodsResult initDataSource() {
        try {
            users_path = ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_USERS);
            projects_path = ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_PROJECTS);
            tasks_path = ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_TASKS);
        } catch (IOException e){
            logger.error(e);
        }
        return new MethodsResult(ResultType.SUCCESSFUL);
    }

    @Override
    public MethodsResult getRecordById(long id, EntryType type){
        try {
            initCsvToBean(type);
            List<T> beans = csvToBean.parse();
            T searchedBean = beans.stream().filter(bean -> id == bean.getId()).findFirst().orElse(null);
            if (searchedBean != null){
                return new MethodsResult<T>(ResultType.SUCCESSFUL, searchedBean);
            } else {
                return new MethodsResult(ResultType.ID_NOT_EXIST);
            }
        } catch (Exception e){
            logger.error(e);
        }
        return new MethodsResult(ResultType.SOME_ERROR);
    }

    @Override
    public MethodsResult updateRecord(T bean, EntryType type){
        return null;
    }

    @Override
    public MethodsResult getUserByLogin(String login){
        try {
            initCsvToBean(EntryType.USER);
            List<User> beans = csvToBean.parse();
            User searchedBean = beans.stream().filter(bean -> Objects.equals(login, (bean).getLogin())).findFirst().orElse(null);
            if (searchedBean != null){
                return new MethodsResult<User>(ResultType.SUCCESSFUL, searchedBean);
            } else {
                return new MethodsResult(ResultType.ID_NOT_EXIST);
            }
        } catch (Exception e){
            logger.error(e);
        }
        return new MethodsResult(ResultType.SOME_ERROR);
    }


    protected void initCsvToBean(EntryType type) throws Exception{
        try {
            switch (type) {
                case TASK:
                    reader = new FileReader(tasks_path);
                    csvToBean = new CsvToBeanBuilder(reader).withType(Task.class).build();
                    break;
                case USER:
                    reader = new FileReader(users_path);
                    csvToBean = new CsvToBeanBuilder(reader).withType(User.class).build();
                    break;
                case PROJECT:
                    reader = new FileReader(projects_path);
                    csvToBean = new CsvToBeanBuilder(reader).withType(Project.class).build();
                    break;
                default:
                    logger.info("Wrong entity type");
                    throw new Exception("Wrong entity type");
            }
        } catch (FileNotFoundException e) {
            logger.error(e);
        }
    }

    protected void initBeanToScv(EntryType type, Boolean append) throws Exception{
        try {
            switch (type) {
                case TASK:
                    writer = new FileWriter(tasks_path, append);
                    beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                    break;
                case USER:
                    writer = new FileWriter(users_path, append);
                    beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                    break;
                case PROJECT:
                    writer = new FileWriter(projects_path, append);
                    beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                    break;
                default:
                    logger.info("Wrong entity type");
                    throw new Exception("Wrong entity type");
            }
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new Exception("Wrong entity type");
        }
    }
}
