package ru.sfedu.projectmanager.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.sfedu.projectmanager.model.enrities.User;
import ru.sfedu.projectmanager.model.entityType.EntityType;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DataProviderXMLTest {
    @Test
    public void saveRecord() throws Exception {
        DataProviderXML dataProviderXMLUser = new DataProviderXML<User>();
        dataProviderXMLUser.saveRecord(new ArrayList(), EntityType.USER);
    }

    @Test
    public void deleteRecord() throws Exception {
    }

    @Test
    public void getRecordById() throws Exception {
    }

    @Before
    public void setUp(){

    }

    @After
    public void tearDown(){

    }

}