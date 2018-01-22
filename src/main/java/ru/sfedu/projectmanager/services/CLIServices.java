package ru.sfedu.projectmanager.services;

import com.sun.deploy.util.ParameterUtil;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.apache.log4j.Logger;
import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.entries.WithId;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.providers.DataProviderCSV;
import ru.sfedu.projectmanager.model.providers.DataProviderJDBC;
import ru.sfedu.projectmanager.model.providers.DataProviderXML;
import ru.sfedu.projectmanager.model.providers.IDataProvider;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CLIServices {

    protected static Logger logger = Logger.getLogger(CLIServices.class);

    public static IDataProvider choseDataSource(String[] commands){
        IDataProvider<WithId> dataProvider;
        switch (commands[1]) {
            case "db":
                dataProvider = new DataProviderJDBC();
                dataProvider.initDataSource();
                System.out.println("Data source - db");
                break;
            case "xml":
                dataProvider = new DataProviderXML();
                dataProvider.initDataSource();
                dataProvider.integrityCheck();
                System.out.println("Data source - xml");
                break;
            case "csv":
                dataProvider = new DataProviderCSV();
                dataProvider.initDataSource();
                dataProvider.integrityCheck();
                System.out.println("Data source - csv");
                break;
            default:
                dataProvider = null;
                System.out.println("Choose db, csv or xml");
        }
        return dataProvider;
    }

    public static void saveUser(IDataProvider dataProvider){
        Scanner scan = new Scanner(System.in);
        User user = new User();
        System.out.println("User: login, email, password");
        System.out.print("User: ");
        String temp = scan.nextLine();
        String[] userData = temp.split(",[ ]*");
        if (userData.length != 3 ) {
            System.out.println("Input data is invalid");
            return;
        }
        if (userData[0].contains(" ")){
            System.out.println("User login can not contain space symbol");
            return;
        }
        user.setLogin(userData[0]);
        user.setEmail(userData[1]);
        user.setPassword(userData[2]);
        System.out.println(dataProvider.saveRecord(user).getResult());
    }

    public static void updateUser(IDataProvider dataProvider){
        Long id;
        Scanner scan = new Scanner(System.in);
        System.out.println("Entry list:");
        selectEntry(dataProvider, EntryType.USER, true);
        System.out.print("Enter entry id to update: ");
        String temp = scan.nextLine();
        try {
            id = Long.parseLong(temp);
        } catch (NumberFormatException e){
            System.out.println("Invalid id");
            return;
        }
        MethodsResult result = dataProvider.getRecordById(new User(id));
        if (!result.getResult().equals(ResultType.SUCCESSFUL)){
            System.out.println(result.getResult());
            return;
        }
        User user = (User)result.getBean();
        System.out.println("User: login, mail, pass, project title");
        System.out.print("User: ");
        temp = scan.nextLine();
        String[] userData = temp.split(",[ ]*");
        if (userData.length != 4 ) {
            System.out.println("Input data is invalid");
            return;
        }
        user.setLogin(userData[0]);
        user.setEmail(userData[1]);
        user.setPassword(userData[2]);
        if (userData[3].equals("null")) {
            user.setProjectId(null);
        } else {
            MethodsResult projectResult = dataProvider.getProjectByTitle(userData[3]);
            if (!projectResult.getResult().equals(ResultType.SUCCESSFUL)){
                System.out.println("Project not exist");
                return;
            }
            user.setProjectId(projectResult.getBean().getId());
        }
        System.out.println(dataProvider.updateRecord(user).getResult());
    }

    public static void saveTask(String[] commands, IDataProvider dataProvider){
        Scanner scan = new Scanner(System.in);
        if (commands.length != 4 || !commands[2].equals("into")) {
            System.out.println("To create Task enter: create task into [Project_title]");
            return;
        }
        MethodsResult projectResult = dataProvider.getProjectByTitle(commands[3]);
        if (projectResult.getResult() != ResultType.SUCCESSFUL){
            System.out.println(projectResult.getResult());
        } else {
            Task task = new Task();
            System.out.println("Task: title, type, state");
            System.out.print("Task: ");
            String temp = scan.nextLine();
            String[] taskData = temp.split(",[ ]*");
            if (taskData.length != 3 ) {
                System.out.println("Input data is invalid");
                return;
            }
            if (taskData[0].contains(" ")){
                System.out.println("Task title can not contain space symbol");
                return;
            }
            task.setTitle(taskData[0]);
            task.setType(taskData[1]);
            task.setState(taskData[2]);
            task.setProjectId(projectResult.getBean().getId());
            System.out.print("Task description: ");
            temp = scan.nextLine();
            task.setDescription(temp);
            MethodsResult result = dataProvider.saveRecord(task);
            System.out.println(result.getResult());
        }
    }

    public static void updateTask(IDataProvider dataProvider){
        Long id;
        Scanner scan = new Scanner(System.in);
        System.out.println("Entry list:");
        selectEntry(dataProvider, EntryType.TASK, true);
        System.out.print("Enter entry id to update: ");
        String temp = scan.nextLine();
        try {
            id = Long.parseLong(temp);
        } catch (NumberFormatException e){
            System.out.println("Invalid id");
            return;
        }
        MethodsResult result = dataProvider.getRecordById(id);
        if (!result.getResult().equals(ResultType.SUCCESSFUL)){
            System.out.println(result.getResult());
            return;
        }
        Task task = (Task) result.getBean();
        System.out.println("Task: title, state, type, description, user login, project title");
        System.out.print("Task: ");
        temp = scan.nextLine();
        String[] taskData = temp.split(",[ ]*");
        if (taskData.length != 6 ) {
            System.out.println("Input data is invalid");
            return;
        }
        task.setTitle(taskData[0]);
        task.setState(taskData[1]);
        task.setType(taskData[2]);
        task.setDescription(taskData[3]);
        if (taskData[4].equals("null")) {
            task.setUserId(null);
        } else {
            MethodsResult userResult = dataProvider.getUserByLogin(taskData[4]);
            if (!userResult.getResult().equals(ResultType.SUCCESSFUL)){
                System.out.println("User not exist");
                return;
            }
            task.setUserId(userResult.getBean().getId());
        }
        if (taskData[5].equals("null")) {
            task.setProjectId(null);
        } else {
            MethodsResult projectResult = dataProvider.getProjectByTitle(taskData[5]);
            if (!projectResult.getResult().equals(ResultType.SUCCESSFUL)){
                System.out.println("User not exist");
                return;
            }
            task.setProjectId(projectResult.getBean().getId());
        }
        System.out.println(dataProvider.updateRecord(task).getResult());
    }


    public static void saveProject(IDataProvider dataProvider){
        Scanner scan = new Scanner(System.in);
        Project project = new Project();
        System.out.println("Project: title, state");
        System.out.print("Project: ");
        String temp = scan.nextLine();
        String[] projectData = temp.split(",[ ]*");
        if (projectData.length < 2 ) {
            System.out.println("Input data is invalid");
            return;
        }
        if (projectData[0].contains(" ")){
            System.out.println("Project title can not contain space symbol");
            return;
        }
        project.setTitle(projectData[0]);
        project.setState(projectData[1]);
        System.out.print("Project description: ");
        temp = scan.nextLine();
        project.setDescription(temp);
        MethodsResult result = dataProvider.saveRecord(project);
        System.out.println(result.getResult());
    }

    public static void updateProject(IDataProvider dataProvider){
        Long id;
        Scanner scan = new Scanner(System.in);
        System.out.println("Entry list:");
        selectEntry(dataProvider, EntryType.PROJECT, true);
        System.out.print("Enter entry id to update: ");
        String temp = scan.nextLine();
        try {
            id = Long.parseLong(temp);
        } catch (NumberFormatException e){
            System.out.println("Invalid id");
            return;
        }
        MethodsResult result = dataProvider.getRecordById(new Project(id));
        if (!result.getResult().equals(ResultType.SUCCESSFUL)){
            System.out.println(result.getResult());
            return;
        }
        Project project = (Project) result.getBean();
        System.out.println("Project: title, state, description");
        System.out.print("Project: ");
        temp = scan.nextLine();
        String[] userData = temp.split(",[ ]*");
        if (userData.length != 3 ) {
            System.out.println("Input data is invalid");
            return;
        }
        project.setTitle(userData[0]);
        project.setState(userData[1]);
        project.setDescription(userData[2]);

        System.out.println(dataProvider.updateRecord(project).getResult());
    }


    public static void selectEntries(String[] commands, IDataProvider dataProvider){
        MethodsResult result;
        switch (commands[1]) {
            case "user":
                if (commands.length != 3){
                    System.out.println("No such user");
                    return;
                }
                result = dataProvider.getUserByLogin(commands[2]);
                if (result.getResult().equals(ResultType.SUCCESSFUL)){
                    System.out.println(result.getBean());
                } else {
                    System.out.println(result.getResult());
                }
                break;
            case "task":
                if (commands.length != 3){
                    System.out.println("No such task");
                    return;
                }
                result = dataProvider.getTasksByTitle(commands[2]);
                if (result.getResult().equals(ResultType.SUCCESSFUL)){
                    result.getBeans().forEach(System.out::println);
                } else {
                    System.out.println(result.getResult());
                }
                break;
            case "project":
                if (commands.length != 3){
                    System.out.println("No such project");
                    return;
                }
                result = dataProvider.getProjectByTitle(commands[2]);
                if (result.getResult().equals(ResultType.SUCCESSFUL)){
                    System.out.println(result.getBean());
                } else {
                    System.out.println(result.getResult());
                }
                break;
            case "users":
                selectEntry(dataProvider, EntryType.USER, false);
                break;
            case "tasks":
                selectEntry(dataProvider, EntryType.TASK, false);
                break;
            case "projects":
                selectEntry(dataProvider, EntryType.PROJECT, false);
                break;
            default:
                System.out.println("Entry invalid");
        }
    }

    public static void deleteEntries(String[] commands, IDataProvider dataProvider){

        switch (commands[1]) {
            case "user":
                deleteEntry(dataProvider, EntryType.USER);
                break;
            case "task":
                deleteEntry(dataProvider, EntryType.TASK);
                break;
            case "project":
                deleteEntry(dataProvider, EntryType.PROJECT);
                break;
            default:
                System.out.println("Entry invalid");
        }
    }

    public static void setUserProject(String[] commands, IDataProvider dataProvider){
        if (commands.length != 4 || !commands[2].equals("to")){
            System.out.println("To add users to project enter: add [user login] to [project title]");
            return;
        }
        MethodsResult userResult = dataProvider.getUserByLogin(commands[1]);
        if (!userResult.getResult().equals(ResultType.SUCCESSFUL)) {
            System.out.println(userResult.getResult());
            return;
        }
        MethodsResult projectResult = dataProvider.getProjectByTitle(commands[3]);
        if (!projectResult.getResult().equals(ResultType.SUCCESSFUL)){
            System.out.println(projectResult.getResult());
            return;
        }
        User user = (User)userResult.getBean();
        user.setProjectId(projectResult.getBean().getId());
        System.out.println(dataProvider.updateRecord(user).getResult());
    }

    public static void takeTask(String[] commands, IDataProvider dataProvider){
        Scanner scan = new Scanner(System.in);
        if (commands.length != 4 || !commands[2].equals("by")){
            System.out.println("To take task by user enter: take [task title] by [user login]");
            return;
        }
        MethodsResult userResult = dataProvider.getUserByLogin(commands[3]);
        if (!userResult.getResult().equals(ResultType.SUCCESSFUL)) {
            System.out.println(userResult.getResult());
            return;
        }
        User user = (User)userResult.getBean();
        if (user.getProjectId() == null){
            System.out.println("user did not enter the project");
            return;
        }
        MethodsResult tasksResult = dataProvider.getTasksByTitle(commands[1]);
        if (!tasksResult.getResult().equals(ResultType.SUCCESSFUL)){
            System.out.println(tasksResult.getResult());
            return;
        }
        Task task;
        List<Task> tasks = tasksResult.getBeans();
        tasks = tasks.stream().filter(bean -> bean.getProjectId().equals(user.getProjectId())).collect(Collectors.toList());
        if (tasks.size() == 0){
            System.out.println("Task not exist in user project");
            return;
        } else if (tasks.size() > 1){
            Long id;
            System.out.println("Chose task id for taking");
            tasks.forEach(bean ->  {
                System.out.print(bean.getId() + ", ");
                System.out.println(bean);
            });
            String temp = scan.nextLine();
            try {
                id = Long.parseLong(temp);
            } catch (NumberFormatException e){
                System.out.println("Invalid id");
                return;
            }
            task = tasks.stream().filter(bean -> bean.getId().equals(id)).findFirst().orElse(null);
            if (task == null){
                System.out.println("Id not exist");
                return;
            }
        } else {
            task = tasks.get(0);
        }
        task.setUserId(user.getId());
        System.out.println(dataProvider.updateRecord(task).getResult());
    }

    public static void help(){
        System.out.println("To select db enter:                 use db/xml/csv");
        System.out.println("To create entry enter:              create user/task/project");
        System.out.println("To delete entry enter:              delete user/task/project");
        System.out.println("To select all entries enter:        select users/tasks/projects");
        System.out.println("To select entry by title enter:     select user/task/project [login/title]");
        System.out.println("To update entry enter:              update users/tasks/projects");
        System.out.println("To take task by user enter:         take [task title] by [user login]");
        System.out.println("To add user to project enter:       add [user login] to [project title]");
    }

    private static void selectEntry(IDataProvider dataProvider, EntryType type, boolean withId){
        MethodsResult result = dataProvider.getAllRecords(type);
        if (result.getResult() == ResultType.SUCCESSFUL){
            if (result.getBeans().size() < 1){
                System.out.println("Empty");
            } else {
                dataProvider.getAllRecords(type).getBeans().forEach(bean -> {
                    if (withId) System.out.print(((WithId)bean).getId() + ", ");
                    System.out.println(bean);
                });
            }
        } else {
            System.out.println(result.getResult());
        }
    }

    private static void deleteEntry(IDataProvider dataProvider, EntryType type){
        Long id;
        Scanner scan = new Scanner(System.in);
        System.out.println("Entry list:");
        selectEntry(dataProvider, type, true);
        System.out.print("Enter entry id to delete: ");
        String temp = scan.nextLine();
        try {
             id = Long.parseLong(temp);
        } catch (NumberFormatException e){
            System.out.println("Invalid id");
            return;
        }
        System.out.println(dataProvider.deleteRecord(new WithId(id, type)).getResult());
    }



}
