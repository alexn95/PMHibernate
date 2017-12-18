package ru.sfedu.projectmanager.model.entries;


import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Class Project
 */
public class Project implements WithId, Serializable {

    @Attribute
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByPosition(position = 1)
    private String title;

    @CsvBindByPosition(position = 2)
    private String description;

    @CsvBindByPosition(position = 3)
    private String state;

    @CsvBindByPosition(position = 4)
    private long createDate;

    private Random random = new Random();
  
  //
  // Constructors
  //
    public Project () {
      this.id = random.nextLong();
      this.createDate = new Date().getTime();
    }

    public Project(Long id, String title, String description, String state, long createDate) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.state = state;
    this.createDate = createDate;
    }

    public Project(String title, String description, String state, long createDate) {
    this.id = random.nextLong();
    this.title = title;
    this.description = description;
    this.state = state;
    this.createDate = createDate;
    }

    public Project(String title, String description, String state) {
        this.id = random.nextLong();
        this.title = title;
        this.description = description;
        this.state = state;
        this.createDate = new Date().getTime();
    }


    /**
    * Set the value of id
    * @param newVar the new value of id
    */
    public void setId (Long newVar) {
    id = newVar;
    }

    /**
    * Get the value of id
    * @return the value of id
    */
    public Long getId () {
    return id;
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
    * @param newVar the new value of state
    */
    @Element
    public void setState(String newVar) {
    state = newVar;
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
      return "'" + title + "', '" + description + "', '" + state + "', '" + format.format(new Date(createDate)) + "'";
    }

    @Override
    public String toInsert(){
        return "'" + title + "', '" + description + "', '" + state + "', '" + createDate + "'";
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


}
