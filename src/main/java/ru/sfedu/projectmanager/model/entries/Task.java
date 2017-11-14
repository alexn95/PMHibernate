package ru.sfedu.projectmanager.model.entries;


import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.io.Serializable;

/**
 * Class Task
 */
public class Task implements WithId, Serializable {


    @Attribute
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByPosition(position = 1)
    private String title;

    @CsvBindByPosition(position = 2)
    private String description;

    @CsvBindByPosition(position = 3)
    private Long projectId;

    @CsvBindByPosition(position = 4)
    private Long state;

    @CsvBindByPosition(position = 5)
    private Long type;

    @CsvBindByPosition(position = 6)
    private String createDate;
  
  //
  // Constructors
  //
  public Task () { }

  public Task(Long id, String title, String description, Long projectId, Long state, Long type, String createDate) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.projectId = projectId;
    this.state = state;
    this.type = type;
    this.createDate = createDate;
  }

  //
  // Methods
  //


  //
  // Accessor methods
  //

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
   * Set the value of projectId
   * @param newVar the new value of projectId
   */
  @Element
  public void setProjectId (Long newVar) {
    projectId = newVar;
  }

  /**
   * Get the value of projectId
   * @return the value of projectId
   */
  @Element
  public Long getProjectId () {
    return projectId;
  }

  /**
   * Set the value of state
   * @param newVar the new value of state
   */
  @Element
  public void setState(Long newVar) {
    state = newVar;
  }

  /**
   * Get the value of state
   * @return the value of state
   */
  @Element
  public Long getState() {
    return state;
  }

  /**
   * Set the value of type
   * @param newVar the new value of type
   */
  @Element
  public void setType(Long newVar) {
    type = newVar;
  }

  /**
   * Get the value of type
   * @return the value of type
   */
  @Element
  public Long getType() {
    return type;
  }

  /**
   * Set the value of createDate
   * @param newVar the new value of createDate
   */
  @Element
  public void setCreateDate (String newVar) {
    createDate = newVar;
  }

  /**
   * Get the value of createDate
   * @return the value of createDate
   */
  @Element
  public String getCreateDate () {
    return createDate;
  }

}
