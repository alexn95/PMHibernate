package ru.sfedu.projectmanager.model.providers;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.projectmanager.Constants;
import ru.sfedu.projectmanager.exceptions.*;
import ru.sfedu.projectmanager.model.entries.*;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.utils.ConfigurationUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataProviderXML<T extends WithId> implements IDataProvider<T> {

    protected static Logger logger = Logger.getLogger(DataProviderXML.class);
    private Serializer serializer;

    private String users_path;
    private String projects_path;
    private String tasks_path;
    private File file;


    @Override
    public MethodsResult saveRecord(T insertedBean, EntryType type) {
        try {
            initFile(type);
            XMLBeanList list = serializer.read(XMLBeanList.class, file);
            List<T> beans = list.getBeans();
            if (beans.stream().filter(tempBean -> Objects.equals(tempBean.getId(), insertedBean.getId()))
                    .findFirst().orElse(null) != null) {
                return new MethodsResult(ResultType.ID_ALREADY_EXIST);
            }
            entryConstrainVerification(beans, insertedBean, type);
            beans.add(insertedBean);
            initFile(type);
            serializer.write(new XMLBeanList<>(beans), file);
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
        } catch (Exception e){
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult deleteRecord(long id, EntryType type){
        try {
            initFile(type);
            XMLBeanList list = serializer.read(XMLBeanList.class, file);
            List<T> beans = list.getBeans();
            T deletedBean = beans.stream().filter(currentBean -> Objects.equals(currentBean.getId(), id)).findFirst().orElse(null);
            if (deletedBean != null){
                beans.remove(deletedBean);
                serializer.write(new XMLBeanList<>(beans), file);
                updateRelatedEntry(id, type);
                return new MethodsResult<>(ResultType.SUCCESSFUL);
            } else {
                return new MethodsResult<>(ResultType.ID_NOT_EXIST);
            }
        } catch (Exception e){
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult getRecordById(long id, EntryType type){
        try {
            initFile(type);
            List<T> beans = serializer.read(XMLBeanList.class, file).getBeans();
            T searchedBean = beans.stream().filter(bean -> id == bean.getId()).findFirst().orElse(null);
            if (searchedBean != null){
                return new MethodsResult<T>(ResultType.SUCCESSFUL, searchedBean);
            } else {
                return new MethodsResult<>(ResultType.ID_NOT_EXIST);
            }
        } catch (Exception e) {
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult getUserByLogin(String login){
        try {
            initFile(EntryType.USER);
            List<User> beans = serializer.read(XMLBeanList.class, file).getBeans();
            User searchedBean = beans.stream().filter(bean -> Objects.equals(login, bean.getLogin())).findFirst().orElse(null);
            if (searchedBean != null){
                return new MethodsResult<User>(ResultType.SUCCESSFUL, searchedBean);
            } else {
                return new MethodsResult<>(ResultType.LOGIN_NOT_EXIST);
            }
        } catch (Exception e) {
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult getTasksByTitle(String title){
        try {
            initFile(EntryType.TASK);
            List<Task> beans = serializer.read(XMLBeanList.class, file).getBeans();
            return new MethodsResult( ResultType.SUCCESSFUL,
                    beans.stream().filter(bean -> Objects.equals(title, bean.getTitle())).collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult getProjectByTitle(String title){
        try {
            initFile(EntryType.PROJECT);
            List<Project> beans = serializer.read(XMLBeanList.class, file).getBeans();
            Project searchedBean =
                    beans.stream().filter(bean -> Objects.equals(title, bean.getTitle())).findFirst().orElse(null);
            if (searchedBean != null){
                return new MethodsResult<>(ResultType.SUCCESSFUL, searchedBean);
            } else {
                return new MethodsResult<>(ResultType.TITLE_NOT_EXIST);
            }
        } catch (Exception e) {
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult updateRecord(T bean, EntryType type){
        try {
            initFile(type);
            XMLBeanList list = serializer.read(XMLBeanList.class, file);
            List<T> beans = list.getBeans();
            T updatedBean = beans.stream().filter(currentBean -> Objects.equals(currentBean.getId(), bean.getId()))
                    .findFirst().orElse(null);
            if (updatedBean == null) {
                return new MethodsResult<>(ResultType.ID_NOT_EXIST);
            }
            int index = beans.indexOf(updatedBean);
            beans.remove(updatedBean);
            entryConstrainVerification(beans, bean, type);
            beans.add(index, bean);
            initFile(type);
            serializer.write(new XMLBeanList<>(beans), file);
            return new MethodsResult(ResultType.SUCCESSFUL);
        } catch (TitleNotUniqueException e) {
            logger.error(e);
            return new MethodsResult(ResultType.TITLE_ALREADY_EXIST);
        } catch (LoginNotUniqueException e) {
            logger.error(e);
            return new MethodsResult(ResultType.LOGIN_ALREADY_EXIST);
        } catch (IntegrityConstrainException e) {
            logger.error(e);
            return new MethodsResult(ResultType.SQL_INTEGRITY_CONSTRAIN_EXCEPTION);
        } catch (Exception e){
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult getAllRecords(EntryType type){
        try {
            initFile(type);
            List<T> beans = serializer.read(XMLBeanList.class, file).getBeans();
            return new MethodsResult<>(ResultType.SUCCESSFUL, beans);
        } catch (Exception e) {
            logger.error(e);
            return new MethodsResult<>(ResultType.EXCEPTION);
        }
    }

    @Override
    public MethodsResult initDataSource() {
        serializer = new Persister();
        try {
            users_path = ConfigurationUtil.getConfigurationEntry(Constants.SYSTEM_PATH)
                    + ConfigurationUtil.getConfigurationEntry(Constants.XML_PATH_USERS);
            projects_path = ConfigurationUtil.getConfigurationEntry(Constants.SYSTEM_PATH)
                    + ConfigurationUtil.getConfigurationEntry(Constants.XML_PATH_PROJECTS);
            tasks_path = ConfigurationUtil.getConfigurationEntry(Constants.SYSTEM_PATH)
                    + ConfigurationUtil.getConfigurationEntry(Constants.XML_PATH_TASKS);
            return new MethodsResult<>(ResultType.SUCCESSFUL);
        } catch (IOException e){
            logger.error(e);
        }
        return new MethodsResult<>(ResultType.IO_EXCEPTION);
    }


    protected void initFile(EntryType type) {
            switch (type) {
                case TASK:
                    file = new File(tasks_path);
                    break;
                case USER:
                    file = new File(users_path);
                    break;
                case PROJECT:
                    file = new File(projects_path);
                    break;
                default:
                    logger.info("Wrong entity type");
            }
    }

    private void entryConstrainVerification(List<T> beans, T bean, EntryType type) throws Exception {
        List<Project> projects;
        List<User> users;
        switch (type){
            case USER:
                initFile(EntryType.PROJECT);
                projects = serializer.read(XMLBeanList.class, file).getBeans();
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
                initFile(EntryType.PROJECT);
                projects = serializer.read(XMLBeanList.class, file).getBeans();
                initFile(EntryType.USER);
                users = serializer.read(XMLBeanList.class, file).getBeans();
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

    private void updateRelatedEntry(long id, EntryType type) throws Exception {
        List<Task> tasks;
        switch (type){
            case PROJECT:
                initFile(EntryType.USER);
                List<User> users = serializer.read(XMLBeanList.class, file).getBeans();
                users.forEach(user -> { if(user.getProjectId() == id) user.setProjectId(null); });
                serializer.write(new XMLBeanList<>(users), file);

                initFile(EntryType.TASK);
                tasks = serializer.read(XMLBeanList.class, file).getBeans();
                List<Task> newTasks = tasks.stream().filter(task -> task.getProjectId() != id).collect(Collectors.toList());
                serializer.write(new XMLBeanList<>(newTasks), file);
                break;

            case USER:
                initFile(EntryType.TASK);
                tasks = serializer.read(XMLBeanList.class, file).getBeans();
                tasks.forEach(task -> { if(task.getUserId() == id) task.setUserId(null); });
                serializer.write(new XMLBeanList<>(tasks), file);
                break;
        }

    }

}
