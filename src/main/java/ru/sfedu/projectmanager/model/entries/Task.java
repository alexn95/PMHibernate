package ru.sfedu.projectmanager.model.entries;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import ru.sfedu.projectmanager.model.enums.EntryType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Class Task
 */
public class Task extends WithId {

    @CsvBindByName
//    @CsvBindByPosition(position = 1)
    private String title;

    @CsvBindByName
//    @CsvBindByPosition(position = 2)
    private String description;

    @CsvBindByName
//    @CsvBindByPosition(position = 3)
    private Long projectId;

    @CsvBindByName
//    @CsvBindByPosition(position = 4)
    private String state;

    @CsvBindByName
//    @CsvBindByPosition(position = 5)
    private String type;

    @CsvBindByName
//    @CsvBindByPosition(position = 6)
    private long createDate;

    @CsvBindByName
//    @CsvBindByPosition(position = 7)
    private Long userId;
  
  //
  // Constructors
  //
    public Task (Long id) {
        super(id, EntryType.TASK);
        this.createDate =  new Date().getTime();;
        this.userId = null;
        this.projectId = null;
    }

    public Task () {
        super(EntryType.TASK);
        this.createDate =  new Date().getTime();;
        this.userId = null;
        this.projectId = null;
    }

    public Task(String title, String description, Long projectId, String state, String type, long createDate) {
        super(EntryType.TASK);
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.state = state;
        this.type = type;
        this.createDate = createDate;
    }

    public Task(String title, String description, String state, String type, Long projectId) {
        super(EntryType.TASK);
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.state = state;
        this.type = type;
        this.createDate = new Date().getTime();
    }

  //
  // Methods
  //


  //
  // Accessor methods
  //

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
    @Element(required=false)
    public void setDescription (String newVar) {
    description = newVar;
  }

  /**
   * Get the value of description
   * @return the value of description
   */
    @Element(required=false)
    public String getDescription () {
    return description;
  }

  /**
   * Set the value of projectId
   * @param newVar the new value of projectId
   */
    @Element(required=false)
    public void setProjectId (Long newVar) {
    projectId = newVar;
  }

  /**
   * Get the value of projectId
   * @return the value of projectId
   */
    @Element(required=false)
    public Long getProjectId () {
    return projectId;
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

  /**
   * Set the value of type
   * @param newVar the new value of type
   */
    @Element
    public void setType(String newVar) {
    type = newVar;
  }

  /**
   * Get the value of type
   * @return the value of type
   */
    @Element
    public String getType() {
        return type;
    }

  /**
   * Set the value of createDate
   * @param newVar the new value of createDate
   */
    @Element
    public void setCreateDate (long newVar) {
    createDate = newVar;
  }

  /**
   * Get the value of createDate
   * @return the value of createDate
   */
    @Element
    public long getCreateDate () {
    return createDate;
  }

    @Element(required=false)
    public Long getUserId() {
    return userId;
  }

    @Element(required=false)
    public void setUserId(Long userId) {
    this.userId = userId;
  }

    @Override
    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat();
        return id + ", " + title + ", " + state + ", " + type + ", " + description + ", "
                + userId + ", " + projectId + ", " + format.format(new Date(createDate));
    }

    @Override
    public boolean equals(Object object){
        boolean isEquals = false;
        if (object != null && object instanceof Task)
        {
            isEquals = Objects.equals(this.toString(), object.toString());
        }
        return isEquals;
    }

    @Override
    public String toInsert(){
        SimpleDateFormat format = new SimpleDateFormat();
        return id + ", '" + title + "', '" + description + "', '" + type + "', '" + state + "', "
                + userId + ", " + projectId + ", " + createDate;
    }
}
