package ru.sfedu.projectmanager.model.providers;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.projectmanager.model.enums.EntryType;
import ru.sfedu.projectmanager.model.enums.MethodsResult;

public abstract interface IDataProvider<T> {

    public MethodsResult saveRecord(T bean);

    public MethodsResult deleteRecord(T bean);

    public MethodsResult getRecordById(T bean);

    public MethodsResult updateRecord(T bean);

    public MethodsResult getAllRecords(EntryType type);

    public MethodsResult initDataSource();

    public MethodsResult getUserByLogin(String login);

    public MethodsResult getTasksByTitle(String title);

    public MethodsResult getProjectByTitle(String title);

    public MethodsResult integrityCheck();
}
