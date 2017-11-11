package ru.sfedu.projectmanager.model;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.projectmanager.model.enrities.User;
import ru.sfedu.projectmanager.model.entityType.EntityType;

import java.util.List;

public abstract interface IDataProvider<T> {

    public void saveRecord(List<T> bean, EntityType type) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException;

    public void deleteRecord(T bean, EntityType type);

    public T getRecordById(long id, EntityType type);

    public void initDataSource();
}
