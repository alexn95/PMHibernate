package ru.sfedu.projectmanager.model.enrities;
import java.io.Serializable;
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

    @Element
    @CsvBindByPosition(position = 1)
    private String login;

    @Element
    @CsvBindByPosition(position = 2)
    private String email;

    @Element
    @CsvBindByPosition(position = 3)
    private String password;
  
  //
  // Constructors
  //
  public User() {};

  public User(Long id, String login, String email, String password) {
    this.id = id;
    this.login = login;
    this.email = email;
    this.password = password;
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
  public void setLogin (String newVar) {
    login = newVar;
  }

  /**
   * Get the value of login
   * @return the value of login
   */
  public String getLogin () {
    return login;
  }

  /**
   * Set the value of email
   * @param newVar the new value of email
   */
  public void setEmail (String newVar) {
    email = newVar;
  }

  /**
   * Get the value of email
   * @return the value of email
   */
  public String getEmail () {
    return email;
  }

  /**
   * Set the value of password
   * @param newVar the new value of password
   */
  public void setPassword (String newVar) {
    password = newVar;
  }

  /**
   * Get the value of password
   * @return the value of password
   */
  public String getPassword () {
    return password;
  }

  //
  // Other methods
  //

  @Override
  public String toString() {
    return id.toString() + login.toString() + email.toString() + password.toString();
  }
}
