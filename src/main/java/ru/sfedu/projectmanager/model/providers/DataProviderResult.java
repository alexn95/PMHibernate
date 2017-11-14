package ru.sfedu.projectmanager.model.providers;

import ru.sfedu.projectmanager.model.entries.WithId;
import ru.sfedu.projectmanager.model.enums.ResultType;

public class DataProviderResult<T extends WithId> {

    private ResultType result;

    private T data = null;

    public DataProviderResult(ResultType result, T data) {
        this.data = data;
        this.result = result;
    }

    public DataProviderResult(ResultType result) {
        this.result = result;
    }

    public DataProviderResult() {
    }

    public ResultType getResult() {
        return result;
    }

    public T getData() {
        return data;
    }

    public void setResult(ResultType result) {
        this.result = result;
    }

    public void setData(T data) {
        this.data = data;
    }
}
