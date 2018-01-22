package ru.sfedu.projectmanager.model.entries;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

public class XMLBeanList<T> {

    private List<T> beans;

    @ElementList()
    public XMLBeanList(List<T> beans) {
        this.beans = beans;
    }

    public XMLBeanList() {}

    public List<T> getBeans() {
        return beans;
    }

    public void setBeans(List<T> beans) {
        this.beans = beans;
    }
}
