/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkinn.model;

/**
 *
 * @author risha
 */
public class UserData {
     // Private attributes
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String securityQuestion;
    private String securityAnswer;
    
    // Constructors
    public UserData(String firstName, String lastName, String email, 
                   String password, String phoneNumber, 
                   String securityQuestion, String securityAnswer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    
    public UserData(String id, String firstName, String lastName, String email, 
                   String password, String phoneNumber, 
                   String securityQuestion, String securityAnswer) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getSecurityQuestion() {
        return securityQuestion;
    }
    
    public String getSecurityAnswer() {
        return securityAnswer;
    }
    
    // Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
    
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
    
    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "UserData{" + 
               "id=" + id + 
               ", firstName=" + firstName + 
               ", lastName=" + lastName + 
               ", email=" + email + 
               ", phoneNumber=" + phoneNumber + 
               '}';
    }
}
