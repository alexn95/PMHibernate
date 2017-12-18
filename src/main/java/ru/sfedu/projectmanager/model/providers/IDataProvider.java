package ru.sfedu.projectmanager.model.providers;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;

public abstract interface IDataProvider<T> {

    public MethodsResult saveRecord(T bean, EntryType type);

    public MethodsResult deleteRecord(long id, EntryType type);

    public MethodsResult getRecordById(long id, EntryType type);

    public MethodsResult updateRecord(T bean, EntryType type);

    public MethodsResult getAllRecords(EntryType type);

    public MethodsResult initDataSource();

    public MethodsResult getUserByLogin(String login);

    public MethodsResult getTasksByTitle(String title);

    public MethodsResult getProjectByTitle(String title);
}
