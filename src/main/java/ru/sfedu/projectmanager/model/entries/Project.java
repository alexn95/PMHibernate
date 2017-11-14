package ru.sfedu.projectmanager.model.entries;


import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.io.Serializable;

/**
 * Class Project
 */
public class Project implements WithId, Serializable {

    @Attribute
    @CsvBindByPosition(position = 0)
    private Long id;

    @Element
    @CsvBindByPosition(position = 1)
    private String title;

    @Element
    @CsvBindByPosition(position = 2)
    private String description;

    @Element
    @CsvBindByPosition(position = 3)
    private Long state;

    @Element
    @CsvBindByPosition(position = 4)
    private String createDate;
  
  //
  // Constructors
  //
  public Project () { };

  public Project(Long id, String title, String description, Long state, String createDate) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.state = state;
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
  public void setTitle (String newVar) {
    title = newVar;
  }

  /**
   * Get the value of title
   * @return the value of title
   */
  public String getTitle () {
    return title;
  }

  /**
   * Set the value of description
   * @param newVar the new value of description
   */
  public void setDescription (String newVar) {
    description = newVar;
  }

  /**
   * Get the value of description
   * @return the value of description
   */
  public String getDescription () {
    return description;
  }

  /**
   * Set the value of state
   * @param newVar the new value of state
   */
  public void setState(Long newVar) {
    state = newVar;
  }

  /**
   * Get the value of state
   * @return the value of state
   */
  public Long getState() {
    return state;
  }

  /**
   * Set the value of createDate
   * @param newVar the new value of createDate
   */
  public void setCreateDate (String newVar) {
    createDate = newVar;
  }

  /**
   * Get the value of createDate
   * @return the value of createDate
   */
  public String getCreateDate () {
    return createDate;
  }


}
