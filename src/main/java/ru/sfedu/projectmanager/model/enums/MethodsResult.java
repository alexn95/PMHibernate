package ru.sfedu.projectmanager.model.enums;

import ru.sfedu.projectmanager.model.entries.WithId;

import java.util.List;

public class MethodsResult<T extends WithId> {

    private ResultType result;

    private T bean = null;

    private List<T> beans = null;

    public MethodsResult(ResultType result, T bean) {
        this.bean = bean;
        this.result = result;
    }

    public MethodsResult(ResultType result, List<T> beans) {
        this.beans = beans;
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

    public T getBean() {
        return bean;
    }

    public List<T> getBeans(){
        return beans;
    }

    public void setResult(ResultType result) {
        this.result = result;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }

    public void setBeans(List<T> beans) {
        this.beans = beans;
    }
}
