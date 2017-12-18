/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.projectmanager;

import org.apache.log4j.Logger;
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


/**
 *
 * @author alexey
 */
public class Client {
     private static Logger logger = Logger.getLogger(Client.class);

    public static void main(String[] args) {
        IDataProvider dataProvider = null;
        Scanner scan = new Scanner(System.in);
        while (true){
            String temp = scan.nextLine();
            if (temp.equals("q") || temp.equals("quit")){
                break;
            }
            String[] commands = temp.split("[ ]+");
            switch (commands[0]) {
                case "use":
                    switch (commands[1]) {
                        case "db":
                            dataProvider = new DataProviderJDBC();
                            dataProvider.initDataSource();
                            System.out.println("data source - db");
                            break;
                        case "xml":
                            dataProvider = new DataProviderXML();
                            dataProvider.initDataSource();
                            System.out.println("data source - xml");
                            break;
                        case "csv":
                            dataProvider = new DataProviderCSV();
                            dataProvider.initDataSource();
                            System.out.println("data source - csv");
                            break;
                        default:
                            System.out.println("choose db, csv or xml");
                    }
                case "create":
                    switch (commands[1]) {
                        case "user":
                            User user = new User();
                            System.out.println("User: login, email, password");
                            System.out.print("User: ");
                            temp = scan.nextLine();
                            String[] userData = temp.split(",[ ]*");
                            try {
                                user.setLogin(userData[0]);
                                user.setEmail(userData[1]);
                                user.setPassword(userData[2]);
                                System.out.println(dataProvider.saveRecord(user, EntryType.USER).getResult());
                            } catch (ArrayIndexOutOfBoundsException e){
                                System.out.println("Input data is invalid");
                            }
                            break;
                        case "task":
                            try {
                                if (commands[2].equals("into")){
                                    MethodsResult projectResult = dataProvider.getProjectByTitle(commands[3]);
                                    if (projectResult.getResult() != ResultType.SUCCESSFUL){
                                        System.out.println(projectResult.getResult());
                                    } else {
                                        Task task = new Task();
                                        System.out.println("Task: title, type, state");
                                        System.out.print("Task: ");
                                        temp = scan.nextLine();
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
                                break;
                            } catch (ArrayIndexOutOfBoundsException e){
                                System.out.println("To create Task enter: create Task into [Project_title]");
                            }
                            break;
                        case "project":
                            Project project = new Project();
                            System.out.println("Project: title, state");
                            System.out.print("Project: ");
                            temp = scan.nextLine();
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
                case "select":
                    switch (commands[1]) {
                        case "user":
                            break;
                        case "task":
                            break;
                        case "project":
                            break;
                        case "users":
                            dataProvider.getAllRecords(EntryType.USER).getBeans().forEach(bean -> System.out.println(bean.toString()));
                            break;
                        case "tasks":
                            dataProvider.getAllRecords(EntryType.TASK).getBeans().forEach(bean -> System.out.println(bean.toString()));
                            break;
                        case "projects":
                            dataProvider.getAllRecords(EntryType.PROJECT).getBeans().forEach(bean -> System.out.println(bean.toString()));
                            break;
                    }
            }

        }
    }
     
     public void logBasicSystemInfo() {        
         logger.info("Launching the application...");
         logger.info("Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
         logger.info("JRE: " + System.getProperty("java.version"));
         logger.info("Java Launched From: " + System.getProperty("java.home"));
         logger.info("Class Path: " + System.getProperty("java.class.path"));
         logger.info("Library Path: " + System.getProperty("java.library.path"));
         logger.info("User Home Directory: " + System.getProperty("user.home"));
         logger.info("User Working Directory: " + System.getProperty("user.dir"));
         logger.info("Test INFO logging."); }
}
