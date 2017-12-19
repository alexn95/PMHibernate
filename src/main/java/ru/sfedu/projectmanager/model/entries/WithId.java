package ru.sfedu.projectmanager.model.entries;

public interface WithId {

    public Long getId();

    public void setId(Long id);

    public String toInsert();

    public String toCLI();
}
