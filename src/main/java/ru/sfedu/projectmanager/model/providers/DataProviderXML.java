package ru.sfedu.projectmanager.model.providers;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.projectmanager.Constants;
import ru.sfedu.projectmanager.model.entries.WithId;
import ru.sfedu.projectmanager.model.entries.XMLBeanList;
import ru.sfedu.projectmanager.model.enums.EntityType;
import ru.sfedu.projectmanager.model.enums.ResultType;
import ru.sfedu.projectmanager.utils.ConfigurationUtil;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataProviderXML<T extends WithId> implements IDataProvider<T> {

    private static Logger logger = Logger.getLogger(DataProviderXML.class);
    private Serializer serializer;

    private String users_path;
    private String projects_path;
    private String tasks_path;
    private File file;


    @Override
    public DataProviderResult saveRecord(T insertedBean, EntityType type) {
        try {
            initFile(type);
            try {
                XMLBeanList list = serializer.read(XMLBeanList.class, file);
                List<T> beans = list.getBeans();
                if (beans.stream().filter(bean -> Objects.equals(insertedBean.getId(), bean.getId())).findFirst().orElse(null) == null){
                    beans.add(insertedBean);
                    serializer.write(new XMLBeanList<>(beans), file);
                    return new DataProviderResult(ResultType.SUCCESSFUL);
                } else {
                    return new DataProviderResult(ResultType.ID_ALREADY_EXIST);
                }
            } catch (XMLStreamException e) {
                List<T> returnList = new ArrayList<>();
                returnList.add(insertedBean);
                serializer.write(new XMLBeanList<>(returnList), file);
                return new DataProviderResult(ResultType.SUCCESSFUL);
            }
        } catch (Exception e){
            logger.error(e);
        }
        return new DataProviderResult(ResultType.SOME_ERROR);
    }

    @Override
    public DataProviderResult deleteRecord(T bean, EntityType type){
        try {
            initFile(type);
            XMLBeanList list = serializer.read(XMLBeanList.class, file);
            List<T> beans = list.getBeans();
            T deletedBean = beans.stream().filter(currentBean -> Objects.equals(currentBean.getId(), bean.getId())).findFirst().orElse(null);
            if (deletedBean != null){
                beans.remove(deletedBean);
                serializer.write(new XMLBeanList<>(beans), file);
                return new DataProviderResult(ResultType.SUCCESSFUL);
            } else {
                return new DataProviderResult(ResultType.ID_NOT_EXIST);
            }
        } catch (XMLStreamException e){
            return new DataProviderResult(ResultType.EMPTY_LIST);
        } catch (Exception e){
            logger.error(e);
        }
        return new DataProviderResult(ResultType.SOME_ERROR);
    }

    @Override
    public DataProviderResult getRecordById(long id, EntityType type){
        try {
            initFile(type);
            List<T> beans = serializer.read(XMLBeanList.class, file).getBeans();
            T searchedBean = beans.stream().filter(bean -> id == bean.getId()).findFirst().orElse(null);
            if (searchedBean != null){
                return new DataProviderResult<T>(ResultType.SUCCESSFUL, searchedBean);
            } else {
                return new DataProviderResult(ResultType.ID_NOT_EXIST);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return new DataProviderResult(ResultType.SOME_ERROR);
    }

    @Override
    public DataProviderResult initDataSource() {
        serializer = new Persister();
        try {
            users_path = ConfigurationUtil.getConfigurationEntry(Constants.XML_PATH_USERS);
            projects_path = ConfigurationUtil.getConfigurationEntry(Constants.XML_PATH_PROJECTS);
            tasks_path = ConfigurationUtil.getConfigurationEntry(Constants.XML_PATH_TASKS);
            return new DataProviderResult(ResultType.SUCCESSFUL);
        } catch (IOException e){
            logger.error(e);
        }
        return new DataProviderResult(ResultType.IO_EXCEPTION);
    }

    private void initFile(EntityType type) throws IllegalArgumentException{
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
                    throw new IllegalArgumentException("Wrong entity type");
            }
    }

}
