package ru.sfedu.projectmanager.model.providers;


import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import org.apache.log4j.Logger;
import ru.sfedu.projectmanager.Constants;
import ru.sfedu.projectmanager.model.entries.*;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.utils.ConfigurationUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataProviderJDBC<T extends WithId> implements IDataProvider<T>  {

    protected static Logger logger = Logger.getLogger(DataProviderJDBC.class);
    private static Connection connection;
    private static Statement statement;

    @Override
    public MethodsResult saveRecord(T bean, EntryType type){
        String query = "";
        switch (type){
            case USER:
                query = "INSERT INTO  Users(id, login, email, password, projectId) " +
                        " VALUES (" + bean.toInsert() + ");";
                break;
            case TASK:
                query = "INSERT INTO  Tasks(id, title, description, type, state, userId, projectId, createDate) " +
                        " VALUES (" + bean.toInsert() + ");";
                break;
            case PROJECT:
                query = "INSERT INTO  Projects(id, title, description, state, createDate) " +
                        " VALUES (" + bean.toInsert() + ");";
                break;
        }
        try {
            statement.executeUpdate(query);
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_INTEGRITY_CONSTRAIN_EXCEPTION);
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL);
    }

    @Override
    public MethodsResult deleteRecord(long id, EntryType type){
        String query = "";
        ResultSet resultSet;
        try {
            switch (type) {
                case USER:
                    query = "SELECT * FROM Users WHERE id = " + id;
                    resultSet = statement.executeQuery(query);
                    if (!resultSet.next()) return new MethodsResult(ResultType.ID_NOT_EXIST);
                    query = "DELETE FROM Users WHERE id = " + id;
                    break;
                case TASK:
                    query = "SELECT * FROM Tasks WHERE id = " + id;
                    resultSet = statement.executeQuery(query);
                    if (!resultSet.next()) return new MethodsResult(ResultType.ID_NOT_EXIST);
                    query = "DELETE FROM Tasks WHERE id = " + id;
                    break;
                case PROJECT:
                    query = "SELECT * FROM Projects WHERE id = " + id;
                    resultSet = statement.executeQuery(query);
                    if (!resultSet.next()) return new MethodsResult(ResultType.ID_NOT_EXIST);
                    query = "DELETE FROM Projects WHERE id = " + id;
                    break;
            }
            statement.executeUpdate(query);
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL);
    }

    @Override
    public MethodsResult getRecordById(long id, EntryType type){
        String query;
        ResultSet resultSet;
        try {
            switch (type){
                case USER:
                    query = "SELECT * FROM Users WHERE id = " + id;
                    resultSet = statement.executeQuery(query);
                    if (!resultSet.next()) return new MethodsResult(ResultType.ID_NOT_EXIST);
                    User user = selectUser(resultSet);
                    return new MethodsResult<>(ResultType.SUCCESSFUL, user);
                case TASK:
                    query = "SELECT * FROM Tasks WHERE id = " + id;
                    resultSet = statement.executeQuery(query);
                    if (!resultSet.next()) return new MethodsResult(ResultType.ID_NOT_EXIST);
                    Task task = selectTask(resultSet);
                    return new MethodsResult<>(ResultType.SUCCESSFUL, task);
                case PROJECT:
                    query = "SELECT * FROM Projects WHERE id = " + id;
                    resultSet = statement.executeQuery(query);
                    if (!resultSet.next()) return new MethodsResult(ResultType.ID_NOT_EXIST);
                    Project project = selectProject(resultSet);
                    return new MethodsResult<>(ResultType.SUCCESSFUL, project);
                default:
                    return new MethodsResult(ResultType.WRONG_ENTRY_TYPE_EXCEPTION);
            }
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult(ResultType.SQL_EXCEPTION);
        }
    }

    @Override
    public MethodsResult getUserByLogin(String login){
        String query = "SELECT * FROM Users WHERE login = '" + login + "'";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) return new MethodsResult(ResultType.LOGIN_NOT_EXIST);
            User user = selectUser(resultSet);
            return new MethodsResult<>(ResultType.SUCCESSFUL, user);
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
    }

    @Override
    public MethodsResult getTasksByTitle(String title){
        String query = "SELECT * FROM Tasks WHERE title = '" + title + "'";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) return new MethodsResult(ResultType.TITLE_NOT_EXIST);
            List<Task> tasks = new ArrayList<>();
            tasks.add(selectTask(resultSet));
            while (resultSet.next()){
                tasks.add(selectTask(resultSet));
            }
            return new MethodsResult<>(ResultType.SUCCESSFUL, tasks);
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
    }

    @Override
    public MethodsResult getProjectByTitle(String title){
        String query = "SELECT * FROM Projects WHERE title = '" + title + "'";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) return new MethodsResult(ResultType.TITLE_NOT_EXIST);
            Project project = selectProject(resultSet);
            return new MethodsResult<>(ResultType.SUCCESSFUL, project);
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
    }

    @Override
    public MethodsResult updateRecord(T bean, EntryType type){
        String query = "";
        switch (type){
            case USER:
                User user = (User)bean;
                query = "UPDATE Users SET " +
                        "login = '" + user.getLogin() + "', " +
                        "email = '" + user.getEmail() + "', " +
                        "password = '" + user.getPassword() + "', " +
                        "projectId = " + user.getProjectId() + " " +
                        "WHERE id = " + bean.getId() + ";";
                logger.info(query);
                break;
            case TASK:
                Task task = (Task)bean;
                query = "UPDATE Tasks SET " +
                        "title = '" + task.getTitle() + "', " +
                        "description = '" + task.getDescription() + "', " +
                        "projectId = " + task.getProjectId() + ", " +
                        "state = '" + task.getState() + "', " +
                        "type = '" + task.getType() + "', " +
                        "userId = " + task.getUserId() + " " +
                        "WHERE id = " + bean.getId() + ";";
                logger.info(query);
                break;
            case PROJECT:
                Project project = (Project)bean;
                query = "UPDATE Projects SET " +
                        "title = '" + project.getTitle() + "', " +
                        "description = '" + project.getDescription() + "', " +
                        "state = '" + project.getState() + "' " +
                        "WHERE id = " + bean.getId() + ";";
                logger.info(query);
                break;
        }
        try {
            statement.executeUpdate(query);
            return new MethodsResult<>(ResultType.SUCCESSFUL);
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_INTEGRITY_CONSTRAIN_EXCEPTION);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
    }

    @Override
    public MethodsResult getAllRecords(EntryType type){
        String query;
        ResultSet resultSet;
        try {
            switch (type){
                case USER:
                    query = "SELECT * FROM Users";
                    resultSet = statement.executeQuery(query);
                    List<User> users = new ArrayList<>();
                    while (resultSet.next()){
                        users.add(selectUser(resultSet));
                    }
                    return new MethodsResult<>(ResultType.SUCCESSFUL, users);
                case TASK:
                    query = "SELECT * FROM Tasks";
                    resultSet = statement.executeQuery(query);
                    List<Task> tasks = new ArrayList<>();
                    while (resultSet.next()){
                        tasks.add(selectTask(resultSet));
                    }
                    return new MethodsResult<>(ResultType.SUCCESSFUL, tasks);
                case PROJECT:
                    query = "SELECT * FROM Projects";
                    resultSet = statement.executeQuery(query);
                    List<Project> projects = new ArrayList<>();
                    while (resultSet.next()){
                        projects.add(selectProject(resultSet));
                    }
                    return new MethodsResult<>(ResultType.SUCCESSFUL, projects);
                default:
                    return new MethodsResult(ResultType.WRONG_ENTRY_TYPE_EXCEPTION);
            }
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult(ResultType.SQL_EXCEPTION);
        }
    }

    @Override
    public MethodsResult initDataSource(){
        try {
            String url = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_URL);
            String user = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_USER);
            String password = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_PASSWORD);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (CommunicationsException e){
            logger.error(e);
            initDataSource();
            return new MethodsResult(ResultType.COMMUNICATIONS_EXCEPTION);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        } catch (IOException e){
            logger.info(e.getMessage());
            return new MethodsResult<>(ResultType.IO_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL);
    }

    public MethodsResult<WithId> closeConnection(){
        try{
            connection.close();
            statement.close();
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL);
    }

    private User selectUser(ResultSet resultSet) throws SQLException{
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setLogin(resultSet.getString("login"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        if (resultSet.getObject("projectId") != null){
            user.setProjectId(resultSet.getLong("projectId"));
        }
        return user;
    }

    private Task selectTask(ResultSet resultSet) throws SQLException{
        Task task = new Task();
        task.setId(resultSet.getLong("id"));
        task.setTitle(resultSet.getString("title"));
        task.setDescription(resultSet.getString("description"));
        if (resultSet.getObject("projectId") != null){
            task.setProjectId(resultSet.getLong("projectId"));
        }
        task.setState(resultSet.getString("state"));
        task.setType(resultSet.getString("type"));
        task.setCreateDate(resultSet.getLong("createDate"));
        if (resultSet.getObject("userId") != null){
            task.setUserId(resultSet.getLong("userId"));
        }
        return task;
    }

    private Project selectProject(ResultSet resultSet) throws SQLException{
        Project project = new Project();
        project.setId(resultSet.getLong("id"));
        project.setTitle(resultSet.getString("title"));
        project.setDescription(resultSet.getString("description"));
        project.setState(resultSet.getString("state"));
        project.setCreateDate(resultSet.getLong("createDate"));
        return project;
    }
}
