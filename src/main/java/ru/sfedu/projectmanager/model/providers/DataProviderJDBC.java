package ru.sfedu.projectmanager.model.providers;

import org.apache.log4j.Logger;
import ru.sfedu.projectmanager.Constants;
import ru.sfedu.projectmanager.model.entries.*;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.utils.ConfigurationUtil;

import java.io.IOException;
import java.sql.*;

public class DataProviderJDBC<T extends WithId> implements IDataProvider<T>  {

    protected static Logger logger = Logger.getLogger(DataProviderJDBC.class);
    private static Connection connection;
    private static Statement statement;

    @Override
    public MethodsResult saveRecord(T bean, EntryType type){
        String query = "";
        switch (type){
            case USER:
                query = "INSERT INTO  Users(login, email, password, projectId) " +
                        " VALUES (" + bean.toString() + ");";
                break;
            case TASK:
                query = "INSERT INTO  Tasks(title, description, projectId, state, type, createDate, userId) " +
                        " VALUES (" + bean.toString() + ");";
                break;
            case PROJECT:
                query = "INSERT INTO  Projects(title, description, state, createDate) " +
                        " VALUES (" + bean.toString() + ");";
                break;
        }
        try {
            statement.executeUpdate(query);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL);
    }

    @Override
    public MethodsResult deleteRecord(long id, EntryType type){
        String query = "";
        switch (type){
            case USER:
                query = "DELETE FROM Users WHERE id = " + id;
                break;
            case TASK:
                query = "DELETE FROM Tasks WHERE id = " + id;
                break;
            case PROJECT:
                query = "DELETE FROM Projects WHERE id = " + id;
                break;
        }
        try {
            statement.executeUpdate(query);
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL);
    }

    @Override
    public MethodsResult getRecordById(long id, EntryType type){
        switch (type){
            case USER:
                MethodsResult<WithId> user = selectUserById(id);
                return new MethodsResult<>(user.getResult(), user.getData());
            case TASK:
                MethodsResult<WithId> task = selectTaskById(id);
                return new MethodsResult<>(task.getResult(), task.getData());
            case PROJECT:
                MethodsResult<WithId> project = selectProjectById(id);
                return new MethodsResult<>(project.getResult(), project.getData());
        }
        return new MethodsResult<>(ResultType.SOME_ERROR);
    }

    @Override
    public MethodsResult getUserByLogin(String login){
        MethodsResult<WithId> user = selectUserByLogin(login);
        return new MethodsResult<>(user.getResult(), user.getData());
    }

    @Override
    public MethodsResult updateRecord(T bean, EntryType type){
        return null;
    }



    @Override
    public MethodsResult initDataSource(){
        try {
            String url = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_URL);
            String user = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_USER);
            String password = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_PASSWORD);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
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

    private MethodsResult<WithId> selectUserByLogin(String login){
        String query = "SELECT * FROM Users WHERE login = '" + login + "'";
        ResultSet resultSet;
        User user = new User();
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            user.setId(resultSet.getLong("id"));
            user.setLogin(resultSet.getString("login"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setProjectId(resultSet.getLong("projectId"));
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL, user);
    }

    private MethodsResult<WithId> selectUserById(long id){
        String query = "SELECT * FROM Users WHERE id = " + id;
        ResultSet resultSet;
        User user = new User();
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            user.setId(resultSet.getLong("id"));
            user.setLogin(resultSet.getString("login"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setProjectId(resultSet.getLong("projectId"));
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL, user);
    }

    private MethodsResult<WithId> selectTaskById(long id){
        String query = "SELECT * FROM Tasks WHERE id = " + id;
        ResultSet resultSet;
        Task task = new Task();
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            task.setId(resultSet.getLong("id"));
            task.setTitle(resultSet.getString("title"));
            task.setDescription(resultSet.getString("description"));
            task.setProjectId(resultSet.getLong("projectId"));
            task.setState(resultSet.getString("state"));
            task.setType(resultSet.getString("type"));
            task.setCreateDate(resultSet.getDate("createDate"));
            task.setUserId(resultSet.getLong("userId"));
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL, task);
    }

    private MethodsResult<WithId> selectProjectById(long id){
        String query = "SELECT * FROM Projects WHERE id = " + id;
        ResultSet resultSet;
        Project project = new Project();
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            project.setId(resultSet.getLong("id"));
            project.setTitle(resultSet.getString("title"));
            project.setDescription(resultSet.getString("description"));
            project.setState(resultSet.getString("state"));
            project.setCreateDate(resultSet.getDate("createDate"));
        } catch (SQLException e){
            logger.error(e);
            return new MethodsResult<>(ResultType.SQL_EXCEPTION);
        }
        return new MethodsResult<>(ResultType.SUCCESSFUL, project);

    }
}
