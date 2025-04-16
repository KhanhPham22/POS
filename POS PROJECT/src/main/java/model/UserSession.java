package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class UserSession extends BaseEntity {

    private String sessionToken;
    private Timestamp timestamp;
    private Timestamp expiryTime;
    private String employeeNumber;
    private String ownerNumber;
    private Employee employee;
    private Owner owner;
    private String sessionData;

    // Default Constructor (required by Hibernate)
    public UserSession() {}

    // Constructor with Employee,Owner
    public UserSession(Employee employee, Owner owner) {
        if (employee != null) {
            this.employee = employee;
            this.employeeNumber = employee.getEmployeeNumber();
        }

        if (owner != null) {
            this.owner = owner;
            this.ownerNumber = owner.getOwnerNumber();
        }

        this.timestamp = Timestamp.from(Instant.now());
        this.sessionToken = generateSessionToken();
        this.expiryTime = calculateExpiryDate();
    }


    // Constructor with all fields
    public UserSession(Long id, String sessionToken, Timestamp timestamp, Timestamp expiryTime,
                       String employeeNumber, Employee employee,String ownerNumber, Owner owner ,String sessionData) {
        setId(id); // Use BaseEntity's id
        this.sessionToken = sessionToken;
        this.timestamp = timestamp;
        this.expiryTime = expiryTime;
        this.employeeNumber = employeeNumber;
        this.employee = employee;
        this.ownerNumber = ownerNumber;
        this.owner = owner;
        this.sessionData = sessionData;
    }

    // Generate unique session token
    private String generateSessionToken() {
        return UUID.randomUUID().toString();
    }

    // Calculate expiry time (8 hours from now)
    private Timestamp calculateExpiryDate() {
        Instant now = Instant.now();
        Instant expiryInstant = now.plus(8, ChronoUnit.HOURS);
        return Timestamp.from(expiryInstant);
    }

    // Check if session is expired
    public boolean isExpired() {
        return Instant.now().isAfter(expiryTime.toInstant());
    }

    // Getter and Setter methods
    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Timestamp expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    
    public String getOwnerNumber() {
    	return ownerNumber;
    }
    
    public void setOwnerNumber(String ownerNumber) {
    	this.ownerNumber = ownerNumber;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public Owner getOwner() {
    	return owner;
    }
    
    public void setOwner(Owner owner) {
    	this.owner = owner;
    }

    public String getSessionData() {
        return sessionData;
    }

    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }
}