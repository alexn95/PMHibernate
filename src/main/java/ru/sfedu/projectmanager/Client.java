/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.projectmanager;

import org.apache.log4j.Logger;
import ru.sfedu.projectmanager.model.providers.IDataProvider;
import ru.sfedu.projectmanager.services.CLIServices;
import ru.sfedu.projectmanager.utils.ConfigurationUtil;

import java.util.Scanner;


/**
 *
 * @author alexey
 */
public class Client {
     private static Logger logger = Logger.getLogger(Client.class);
     private static IDataProvider dataProvider;

    public static void main(String[] args) {
        dataProvider = null;
        System.out.println(args[0]);
        ConfigurationUtil.setConfigPath(args[0]);

        Scanner scan = new Scanner(System.in);
        while (true){
            String temp = scan.nextLine();
            if (temp.equals("q") || temp.equals("quit")){
                break;
            }
            String[] commands = temp.split("[ ]+");
            if (commands.length < 2 && !commands[0].equals("help")) {
                System.out.println("Command not exist");
                continue;
            }
            switch (commands[0]) {
                case "use":
                    dataProvider = CLIServices.choseDataSource(commands);
                    break;
                case "create":
                    if (dataProvider == null) {
                        System.out.println("You should select data source");
                        continue;
                    }
                    switch (commands[1]) {
                        case "user":
                            CLIServices.saveUser(dataProvider);
                            break;
                        case "task":
                            CLIServices.saveTask(commands, dataProvider);
                            break;
                        case "project":
                            CLIServices.saveProject(dataProvider);
                            break;
                        default:
                            System.out.println("Entry invalid");
                    }
                    break;
                case "select":
                    if (dataProvider == null) {
                        System.out.println("You should select data source");
                        continue;
                    }
                    CLIServices.selectEntries(commands, dataProvider);
                    break;
                case "delete":
                    if (dataProvider == null) {
                        System.out.println("You should select data source");
                        continue;
                    }
                    CLIServices.deleteEntries(commands, dataProvider);
                    break;
                case "update":
                    if (dataProvider == null) {
                        System.out.println("You should select data source");
                        continue;
                    }
                    switch (commands[1]) {
                        case "user":
                            CLIServices.updateUser(dataProvider);
                            break;
                        case "task":
                            CLIServices.updateTask(dataProvider);
                            break;
                        case "project":
                            CLIServices.updateProject(dataProvider);
                            break;
                        default:
                            System.out.println("Entry invalid");
                    }
                    break;
                case "add":
                    if (dataProvider == null) {
                        System.out.println("You should select data source");
                        continue;
                    }
                    CLIServices.setUserProject(commands, dataProvider);
                    break;
                case "take":
                    if (dataProvider == null) {
                        System.out.println("You should select data source");
                        continue;
                    }
                    CLIServices.takeTask(commands, dataProvider);
                    break;
                case "help":
                    CLIServices.help();
                    break;
                default:
                    System.out.println("Command not exist");
            }

        }
    }

}
