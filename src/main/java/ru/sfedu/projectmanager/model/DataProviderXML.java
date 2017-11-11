package ru.sfedu.projectmanager.model;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.projectmanager.Constants;
import ru.sfedu.projectmanager.model.IDataProvider;
import ru.sfedu.projectmanager.model.enrities.User;
import ru.sfedu.projectmanager.model.enrities.WithId;
import ru.sfedu.projectmanager.model.entityType.EntityType;
import ru.sfedu.projectmanager.utils.ConfigurationUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataProviderXML<T extends WithId> implements IDataProvider<T> {

    private static Logger logger = Logger.getLogger(DataProviderXML.class);

    @Override
    public void saveRecord(List<T> beans, EntityType type) {
        try {
            Serializer serializer = new Persister();
            User user = new User(1L,"lehaLogin","mr.alexn95@mail.com","mycastpass");
            File result = new File(ConfigurationUtil.getConfigurationEntry(Constants.XML_PATH_USERS));
            serializer.write(user, result);
        } catch (Exception e){
            logger.info(e.getMessage());
        }

    }

    @Override
    public void deleteRecord(T bean, EntityType type){}


    @Override
    public T getRecordById(long id, EntityType type){
        try {
            Serializer serializer = new Persister();
            File source = new File(ConfigurationUtil.getConfigurationEntry(Constants.XML_PATH_USERS));
            User user = serializer.read(User.class, source);
            System.out.println(user.getEmail());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public void initDataSource() {

    }

}
