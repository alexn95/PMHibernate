package ru.sfedu.projectmanager.model.entries;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import ru.sfedu.projectmanager.model.enums.EntryType;

import java.io.Serializable;
import java.util.Date;

public class WithId implements Serializable {

    @Attribute
    @CsvBindByName
    public Long id = 1L;

    private EntryType entryType;

    public WithId(){
        id = new Date().getTime();
    }

    public WithId(Long id, EntryType entryType){
        this.id = id;
        this.entryType = entryType;
    }

    public WithId(EntryType entryType){
        id = new Date().getTime();
        this.entryType = entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toInsert(){
        return "";
    }


}
