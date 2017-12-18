package ru.sfedu.projectmanager.model.entries;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

import com.opencsv.bean.*;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


/**
 * Class User
 */
public class User implements WithId, Serializable {

    @Attribute
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByPosition(position = 1)
    private String login;

    @CsvBindByPosition(position = 2)
    private String email;

    @CsvBindByPosition(position = 3)
    private String password;

    @CsvBindByPosition(position = 4)
    private Long projectId;

    private Random random = new Random();
  
  //
  // Constructors
  //
    public User() {
        this.id = random.nextLong();
    }

    public User(Long id, String login, String email, String password) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public User(Long id, String login, String email, String password, Long projectId) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.projectId = projectId;
    }

    public User( String login, String email, String password, Long projectId) {
        this.id = random.nextLong();
        this.login = login;
        this.email = email;
        this.password = password;
        this.projectId = projectId;
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
    * Set the value of login
    * @param newVar the new value of login
    */
    @Element
    public void setLogin (String newVar) {
    login = newVar;
    }

    /**
    * Get the value of login
    * @return the value of login
    */
    @Element
    public String getLogin () {
    return login;
    }

    /**
    * Set the value of email
    * @param newVar the new value of email
    */
    @Element
    public void setEmail (String newVar) {
    email = newVar;
    }

    /**
    * Get the value of email
    * @return the value of email
    */
    @Element
    public String getEmail () {
    return email;
    }

    /**
    * Set the value of password
    * @param newVar the new value of password
    */
    @Element
    public void setPassword (String newVar) {
    password = newVar;
    }

    /**
    * Get the value of password
    * @return the value of password
    */
    @Element
    public String getPassword () {
    return password;
    }

    //
    // Other methods
    //

    @Element(required=false)
    public Long getProjectId() {
    return projectId;
    }

    @Element(required=false)
    public void setProjectId(Long projectId) {
    this.projectId = projectId;
    }

    @Override
    public String toString() {
    return login + ", " +  email + ", " + password+ ", " + projectId;
    }

    @Override
    public String toInsert() {
    return "'" + login + "', '" +  email + "', '" + password+ "', " + projectId;
    }

    @Override
    public boolean equals(Object object){
        boolean isEquals = false;
        if (object != null && object instanceof User)
        {
          isEquals = Objects.equals(this.toString(), object.toString());
        }
        return isEquals;
    }
}
