package ru.sfedu.projectmanager.model.providers;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.projectmanager.model.enums.EntityType;
import ru.sfedu.projectmanager.model.enums.ResultType;

import java.util.List;

public abstract interface IDataProvider<T> {

    public DataProviderResult saveRecord(T bean, EntityType type) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException;

    public DataProviderResult deleteRecord(T bean, EntityType type);

    public DataProviderResult getRecordById(long id, EntityType type);

    public DataProviderResult initDataSource();
}
