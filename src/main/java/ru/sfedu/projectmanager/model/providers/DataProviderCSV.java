package ru.sfedu.projectmanager.model.providers;

import com.opencsv.exceptions.CsvException;
import ru.sfedu.projectmanager.exceptions.*;
import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.entries.WithId;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private static Logger logger = Logger.getLogger(DataProviderCSV.class);
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
            if (beans.stream().filter(tempBean -> Objects.equals(tempBean.getId(), insertedBean.getId()))
                    .findFirst().orElse(null) != null) {
                return new MethodsResult(ResultType.ID_ALREADY_EXIST);
            }
            entryConstrainVerification(beans, insertedBean, type);
            beanToCsv.write(insertedBean);
            writer.close();
            return new MethodsResult<>(ResultType.SUCCESSFUL);
        } catch (TitleNotUniqueException e) {
            logger.error(e);
            return new MethodsResult(ResultType.TITLE_ALREADY_EXIST);
        } catch (LoginNotUniqueException e) {
            logger.error(e);
            return new MethodsResult(ResultType.LOGIN_ALREADY_EXIST);
        } catch (IntegrityConstrainException e) {
            logger.error(e);
            return new MethodsResult(ResultType.SQL_INTEGRITY_CONSTRAIN_EXCEPTION);
        } catch (IOException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.IO_EXCEPTION);
        } catch (Exception e){
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
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
                updateRelatedEntry(id, type);
                return new MethodsResult<>(ResultType.SUCCESSFUL);
            } else {
                writer.close();
                return new MethodsResult<>(ResultType.ID_NOT_EXIST);
            }
        } catch (IOException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.IO_EXCEPTION);
        } catch (Exception e){
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
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
                return new MethodsResult<>(ResultType.ID_NOT_EXIST);
            }
        } catch (FileNotFoundException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.FILE_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public MethodsResult updateRecord(T bean, EntryType type){
        try {
            initCsvToBean(type);
            List<T> beans = csvToBean.parse();
            T updatedBean = beans.stream().filter(currentBean -> Objects.equals(bean.getId(), currentBean.getId()))
                    .findFirst().orElse(null);
            if (updatedBean == null){
                return new MethodsResult(ResultType.ID_NOT_EXIST);
            }
            int index = beans.indexOf(updatedBean);
            beans.remove(updatedBean);
            entryConstrainVerification(beans, bean, type);
            beans.add(index, bean);
            initBeanToScv(type, false);
            beanToCsv.write(beans);
            writer.close();
            return new MethodsResult<>(ResultType.SUCCESSFUL);
        } catch (TitleNotUniqueException e) {
            logger.error(e);
            return new MethodsResult(ResultType.TITLE_ALREADY_EXIST);
        } catch (LoginNotUniqueException e) {
            logger.error(e);
            return new MethodsResult(ResultType.LOGIN_ALREADY_EXIST);
        } catch (IntegrityConstrainException e) {
            logger.error(e);
            return new MethodsResult(ResultType.SQL_INTEGRITY_CONSTRAIN_EXCEPTION);
        } catch (IdNotUniqueException e){
            logger.error(e);
            return new MethodsResult(ResultType.ID_ALREADY_EXIST);
        } catch (IOException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.IO_EXCEPTION);
        } catch (Exception e){
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult getUserByLogin(String login){
        try {
            initCsvToBean(EntryType.USER);
            List<User> beans = csvToBean.parse();
            User searchedBean = beans.stream().filter(bean -> Objects.equals(login, (bean).getLogin())).findFirst().orElse(null);
            if (searchedBean != null){
                return new MethodsResult<>(ResultType.SUCCESSFUL, searchedBean);
            } else {
                return new MethodsResult<>(ResultType.LOGIN_NOT_EXIST);
            }
        } catch (FileNotFoundException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.FILE_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public MethodsResult getTasksByTitle(String title){
        try {
            initCsvToBean(EntryType.TASK);
            List<Task> beans = csvToBean.parse();
            return new MethodsResult( ResultType.SUCCESSFUL,
                    beans.stream().filter(bean -> Objects.equals(title, (bean).getTitle())).collect(Collectors.toList()));
        } catch (FileNotFoundException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.FILE_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public MethodsResult getProjectByTitle(String title){
        try {
            initCsvToBean(EntryType.PROJECT);
            List<Project> beans = csvToBean.parse();
            Project searchedBean =
                    beans.stream().filter(bean -> Objects.equals(title, (bean).getTitle())).findFirst().orElse(null);
            if (searchedBean != null){
                return new MethodsResult<>(ResultType.SUCCESSFUL, searchedBean);
            } else {
                return new MethodsResult<>(ResultType.TITLE_NOT_EXIST);
            }
        } catch (FileNotFoundException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.FILE_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public MethodsResult getAllRecords(EntryType type){
        try {
            initCsvToBean(type);
            List<T> beans = csvToBean.parse();
                return new MethodsResult<>(ResultType.SUCCESSFUL, beans);
        } catch (IOException e) {
            logger.error(e);
            return new MethodsResult<>(ResultType.IO_EXCEPTION);
        }
    }

    @Override
    public MethodsResult initDataSource() {
        try {
            users_path = ConfigurationUtil.getConfigurationEntry(Constants.SYSTEM_PATH)
                    + ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_USERS);
            projects_path = ConfigurationUtil.getConfigurationEntry(Constants.SYSTEM_PATH)
                    + ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_PROJECTS);
            tasks_path = ConfigurationUtil.getConfigurationEntry(Constants.SYSTEM_PATH)
                    + ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_TASKS);
        } catch (IOException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.IO_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL);
    }


    private void initCsvToBean(EntryType type) throws FileNotFoundException {
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
                logger.info("Wrong entries type");
            }
    }

    private void initBeanToScv(EntryType type, Boolean append) throws IOException{
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
        }
    }

    private void entryConstrainVerification(List<T> beans, T bean, EntryType type) throws ConstrainException, FileNotFoundException {
        List<Project> projects;
        List<User> users;
        switch (type){
            case USER:
                initCsvToBean(EntryType.PROJECT);
                projects = csvToBean.parse();
                User user = (User)bean;
                if (beans.stream().filter(tempBean -> Objects.equals(user.getLogin(), ((User)tempBean).getLogin()))
                        .findFirst().orElse(null) != null){
                    throw new LoginNotUniqueException();
                } else if (user.getProjectId() != null &&
                        projects.stream().filter(tempBean -> Objects.equals(tempBean.getId(), ((User) bean).getProjectId()))
                        .findFirst().orElse(null) == null) {
                    throw new IntegrityConstrainException();
                }
                break;
            case TASK:
                initCsvToBean(EntryType.PROJECT);
                projects = csvToBean.parse();
                initCsvToBean(EntryType.USER);
                users = csvToBean.parse();
                Task task = (Task)bean;
                if (task.getUserId() != null &&
                        users.stream().filter(tempBean -> Objects.equals(tempBean.getId(), ((Task) bean).getUserId()))
                                .findFirst().orElse(null) == null) {
                    throw new IntegrityConstrainException();
                } else if (task.getProjectId() != null &&
                        projects.stream().filter(tempBean -> Objects.equals(tempBean.getId(), ((Task) bean).getProjectId()))
                                .findFirst().orElse(null) == null) {
                    throw new IntegrityConstrainException();
                }
                break;
            case PROJECT:
                Project project = (Project)bean;
                if (beans.stream().filter(tempBean -> Objects.equals(project.getTitle(), ((Project)tempBean).getTitle()))
                        .findFirst().orElse(null) != null) {
                    throw new TitleNotUniqueException();
                }
                break;
            default:
                break;
        }
    }

    private void updateRelatedEntry(long id, EntryType type) throws IOException, CsvException{
        List<Task> tasks;
        switch (type){
            case PROJECT:
                initCsvToBean(EntryType.USER);
                List<User> users = csvToBean.parse();
                users.forEach(user -> { if(user.getProjectId() == id) user.setProjectId(null); });
                initBeanToScv(EntryType.USER, false);
                beanToCsv.write(users);
                writer.close();

                initCsvToBean(EntryType.TASK);
                tasks = csvToBean.parse();
                List<Task> newTasks = tasks.stream().filter(task -> task.getProjectId() != id).collect(Collectors.toList());
                initBeanToScv(EntryType.TASK, false);
                beanToCsv.write(newTasks);
                writer.close();
                break;

            case USER:
                initCsvToBean(EntryType.TASK);
                tasks = csvToBean.parse();
                tasks.forEach(task -> { if( task.getUserId() == id) task.setUserId(null); });
                initBeanToScv(EntryType.TASK, false);
                beanToCsv.write(tasks);
                writer.close();
                break;
        }

    }
}
