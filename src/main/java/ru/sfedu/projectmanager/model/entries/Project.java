package ru.sfedu.projectmanager.model.entries;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import ru.sfedu.projectmanager.model.enums.EntryType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Class Project
 */
public class Project extends WithId {

    @CsvBindByName
//    @CsvBindByPosition(position = 1)
    private String title;

    @CsvBindByName
//    @CsvBindByPosition(position = 2)
    private String description;

    @CsvBindByName
//    @CsvBindByPosition(position = 3)
    private String state;

    @CsvBindByName
//    @CsvBindByPosition(position = 4)
    private long createDate;
  
  //
  // Constructors
  //
    public Project() {
        super(EntryType.PROJECT);
        this.createDate = new Date().getTime();
    }

    public Project(Long id) {
        super(id, EntryType.PROJECT);
        this.createDate = new Date().getTime();
    }


    public Project(String title, String description, String state, long createDate, EntryType entryType) {
        super(EntryType.PROJECT);
        this.title = title;
        this.description = description;
        this.state = state;
        this.createDate = createDate;
    }

    public Project(String title, String description, String state, EntryType entryType) {
        super(EntryType.PROJECT);
        this.title = title;
        this.description = description;
        this.state = state;
        this.createDate = new Date().getTime();
    }

    /**
    * Set the value of title
    * @param newVar the new value of title
    */
    @Element
    public void setTitle (String newVar) {
    title = newVar;
    }

    /**
    * Get the value of title
    * @return the value of title
    */
    @Element
    public String getTitle () {
    return title;
    }

    /**
    * Set the value of description
    * @param newVar the new value of description
    */
    @Element
    public void setDescription (String newVar) {
    description = newVar;
    }

    /**
    * Get the value of description
    * @return the value of description
    */
    @Element
    public String getDescription () {
    return description;
    }

    /**
    * Set the value of state
    * @param state the new value of state
    */
    @Element
    public void setState(String state) {
        this.state = state;
    }

    /**
    * Get the value of state
    * @return the value of state
    */
    @Element
    public String getState() {
    return state;
    }


    @Element
    public void setCreateDate (long date) {
    createDate = date;
    }

    /**
    * Get the value of createDate
    * @return the value of createDate
    */
    @Element
    public long getCreateDate () {
    return createDate;
    }

    @Override
    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat();
        return id + ", " + title + ", " + state + ", " + description + ", " + format.format(new Date(createDate));
    }

    @Override
    public boolean equals(Object object){
        boolean isEquals = false;
        if (object != null && object instanceof Project)
        {
            isEquals = Objects.equals(this.toString(), object.toString());
        }
        return isEquals;
    }

    @Override
    public String toInsert(){
        SimpleDateFormat format = new SimpleDateFormat();
        return id + ", '" + title + "', '" + description + "', '" + state + "', " + createDate;
    }

}
