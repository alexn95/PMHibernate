package ru.sfedu.projectmanager.model.enums;

import ru.sfedu.projectmanager.model.entries.WithId;
import ru.sfedu.projectmanager.model.enums.ResultType;

public class MethodsResult<T extends WithId> {

    private ResultType result;

    private T data = null;

    public MethodsResult(ResultType result, T data) {
        this.data = data;
        this.result = result;
    }

    public MethodsResult(ResultType result) {
        this.result = result;
    }

    public MethodsResult() {
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
