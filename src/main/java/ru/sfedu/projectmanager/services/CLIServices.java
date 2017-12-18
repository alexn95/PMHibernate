package ru.sfedu.projectmanager.services;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.xml.internal.bind.v2.model.core.ID;
import ru.sfedu.projectmanager.model.entries.Project;
import ru.sfedu.projectmanager.model.entries.Task;
import ru.sfedu.projectmanager.model.entries.User;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.model.providers.DataProviderCSV;
import ru.sfedu.projectmanager.model.providers.DataProviderJDBC;
import ru.sfedu.projectmanager.model.providers.DataProviderXML;
import ru.sfedu.projectmanager.model.providers.IDataProvider;

import java.util.Scanner;

public class CLIServices {

    public static IDataProvider choseDataSource(String[] commands){
        IDataProvider dataProvider;
        switch (commands[1]) {
            case "db":
                dataProvider = new DataProviderJDBC();
                dataProvider.initDataSource();
                System.out.println("Data source - db");
                break;
            case "xml":
                dataProvider = new DataProviderXML();
                dataProvider.initDataSource();
                System.out.println("Data source - xml");
                break;
            case "csv":
                dataProvider = new DataProviderCSV();
                dataProvider.initDataSource();
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
        try {
            user.setLogin(userData[0]);
            user.setEmail(userData[1]);
            user.setPassword(userData[2]);
            System.out.println(dataProvider.saveRecord(user, EntryType.USER).getResult());
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Input data is invalid");
        }
    }

    public static void saveTask(String[] commands, IDataProvider dataProvider){
        Scanner scan = new Scanner(System.in);
        try {
            if (commands[2].equals("into")){
                MethodsResult projectResult = dataProvider.getProjectByTitle(commands[3]);
                if (projectResult.getResult() != ResultType.SUCCESSFUL){
                    System.out.println(projectResult.getResult());
                } else {
                    Task task = new Task();
                    System.out.println("Task: title, type, state");
                    System.out.print("Task: ");
                    String temp = scan.nextLine();
                    String[] taskData = temp.split(",[ ]*");
                    task.setTitle(taskData[0]);
                    task.setType(taskData[1]);
                    task.setState(taskData[2]);
                    task.setProjectId(projectResult.getBean().getId());
                    try {
                        System.out.print("Task description: ");
                        temp = scan.nextLine();
                        task.setDescription(temp);
                        MethodsResult result = dataProvider.saveRecord(task, EntryType.TASK);
                        System.out.println(result.getResult());
                    } catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("Input data is invalid");
                    }
                }
            } else {
                System.out.println("To create Task enter: create Task into [Project_title]");
            }
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("To create Task enter: create Task into [Project_title]");
        }
    }

    public static void saveProject(IDataProvider dataProvider){
        Scanner scan = new Scanner(System.in);
        Project project = new Project();
        System.out.println("Project: title, state");
        System.out.print("Project: ");
        String temp = scan.nextLine();
        String[] projectData = temp.split(",[ ]*");
        project.setTitle(projectData[0]);
        project.setState(projectData[1]);
        try{
            System.out.print("Project description: ");
            temp = scan.nextLine();
            project.setDescription(temp);
            MethodsResult result = dataProvider.saveRecord(project, EntryType.PROJECT);
            System.out.println(result.getResult());
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Input data is invalid");
        }
    }


    public static void selectEntries(String[] commands, IDataProvider dataProvider){
        switch (commands[1]) {
            case "user":
                break;
            case "task":
                break;
            case "project":
                break;
            case "users":
                selectEntry(dataProvider, EntryType.USER);
                break;
            case "tasks":
                selectEntry(dataProvider, EntryType.TASK);
                break;
            case "projects":
                selectEntry(dataProvider, EntryType.PROJECT);
                break;
            default:
                System.out.println("Entry invalid");
        }
    }

    private static void selectEntry(IDataProvider dataProvider, EntryType type){
        MethodsResult result = dataProvider.getAllRecords(type);
        if (result.getResult() == ResultType.SUCCESSFUL){
            if (result.getBeans().size() < 1){
                System.out.println("Empty");
            } else {
                dataProvider.getAllRecords(type).getBeans().forEach(bean -> System.out.println(bean.toString()));
            }
        } else {
            System.out.println(result.getResult());
        }
    }

}
