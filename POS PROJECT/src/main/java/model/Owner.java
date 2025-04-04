package model;

import java.util.UUID;

public class Owner extends Person { 

    private String ownerNumber;
    private String description;
    private String loginUsername;
    private String loginPassword;

    public Owner() {
    }

    public void generateOwnerId() {
        this.ownerNumber = "OWN-" + UUID.randomUUID().toString();
    }

    // Getter and Setter for ownerNumber
    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for loginUsername
    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    // Getter and Setter for loginPassword
    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
