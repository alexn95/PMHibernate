package ru.sfedu.projectmanager.model;

import com.sun.org.apache.bcel.internal.generic.SWITCH;
import ru.sfedu.projectmanager.model.enrities.Project;
import ru.sfedu.projectmanager.model.enrities.Task;
import ru.sfedu.projectmanager.model.enrities.User;
import ru.sfedu.projectmanager.model.enrities.WithId;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.util.Arrays;
import org.apache.log4j.Logger;
import ru.sfedu.projectmanager.Constants;
import ru.sfedu.projectmanager.model.entityType.EntityType;
import ru.sfedu.projectmanager.utils.ConfigurationUtil;

public class DataProviderCsv<T extends WithId> implements IDataProvider<T> {

    private static Logger logger = Logger.getLogger(DataProviderCsv.class);
    private String users_path;
    private String projects_path;
    private String tasks_path;

    private StatefulBeanToCsv beanToCsv;
    private CsvToBean csvToBean;

    private FileWriter writer;
    private FileReader reader;


    @Override
    public void saveRecord(List<T> beans, EntityType type) {
        try {
            initCsvToBean(type);
            initBeanToScv(type, true);
//            TODO
//            Arrays.stream();
            for (T bean : beans) {
                if (getRecordById(bean.getId(), type) == null) {
                    try {
                        beanToCsv.write(bean);
                    } catch (CsvException e) {
                        logger.info(e.getMessage());
                    }
                }
            }
            writer.close();
        } catch (Exception e){
            logger.info(e);
        }
    }

    @Override
    public void deleteRecord(T bean, EntityType type){
        try {
            initCsvToBean(type);
            List<T> entities = csvToBean.parse();
            T deletableEntity = null;
            for (T entity: entities) {
                if (Objects.equals(entity.getId(), bean.getId())) {
                    deletableEntity = entity;
                }
            }
            if (deletableEntity == null) return;
            entities.remove(deletableEntity);
            try {
                initBeanToScv(type, false);
                beanToCsv.write(entities);
            } catch (CsvException e) {
                logger.info(e.getMessage());
            }
            writer.close();
        } catch (Exception e){
            logger.info(e.getMessage());
        }

    }

    @Override
    public T getRecordById(long id, EntityType type){
        try {
            initCsvToBean(type);
            List<T> entities = csvToBean.parse();
            for (T entity: entities) {
                if (entity.getId() == id) {
                    return entity;
                }
            }
            return null;
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public void initDataSource() {
        try {
            users_path = ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_USERS);
            projects_path = ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_PROJECTS);
            tasks_path = ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH_TASKS);
        } catch (IOException e){
            logger.info(e.getMessage());
        }
    }

    private void initCsvToBean(EntityType type) throws Exception{
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
            logger.info(e.getMessage());
        }
    }

    private void initBeanToScv(EntityType type, Boolean append) throws Exception{
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
            logger.info(e.getMessage());
            throw new Exception("Wrong entity type");
        }
    }
}
