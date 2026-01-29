package com.project.back_end.DTO;

/**
 * Data Transfer Object (DTO) for Login request data.
 * 
 * This DTO is used to receive login credentials from the client in @RequestBody
 * parameters.
 * It is typically used in controller methods to deserialize the login request
 * body.
 * 
 * Note: This class contains NO persistence annotations like @Entity or @Id,
 * as it is used only for authentication input and is not stored in the
 * database.
 */
public class Login {

    private String identifier;
    private String password;

    /**
     * Default constructor.
     * This constructor is used for deserialization of the login request body.
     */
    public Login() {
    }

    /**
     * Parameterized constructor.
     * 
     * @param identifier The unique identifier of the user (email for
     *                   Doctor/Patient, username for Admin)
     * @param password   The password provided by the user
     */
    public Login(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    // ============ Getter Methods ============

    /**
     * Gets the unique identifier of the user attempting to log in.
     * This can be an email for Doctor/Patient or a username for Admin.
     * 
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the password provided by the user.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    // ============ Setter Methods ============

    /**
     * Sets the unique identifier of the user attempting to log in.
     * 
     * @param identifier the identifier to set (email for Doctor/Patient, username
     *                   for Admin)
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Sets the password provided by the user.
     * 
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
