package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class UserSession extends BaseEntity {

    // Fields to store session information
    private String sessionToken;           // Unique session token (UUID)
    private Timestamp timestamp;           // Session creation time
    private Timestamp expiryTime;          // Session expiration time
    private String employeeNumber;         // Employee ID (if session belongs to employee)
    private String ownerNumber;            // Owner ID (if session belongs to owner)
    private Employee employee;             // Reference to employee (nullable)
    private Owner owner;                   // Reference to owner (nullable)
    private String sessionData;            // Optional session data (e.g., JSON)
    private boolean active;                // Indicates if the session is currently active

    // Default constructor (needed for Hibernate and other frameworks)
    public UserSession() {}

    // Constructor that accepts an Employee and/or Owner object
    public UserSession(Employee employee, Owner owner) {
        if (employee != null) {
            this.employee = employee;
            this.employeeNumber = employee.getEmployeeNumber();
        }

        if (owner != null) {
            this.owner = owner;
            this.ownerNumber = owner.getOwnerNumber();
        }

        this.timestamp = Timestamp.from(Instant.now()); // Set current time
        this.sessionToken = generateSessionToken();     // Generate unique token
        this.expiryTime = calculateExpiryDate();        // Set expiration time
        this.active = true;                             // Mark session as active
    }

    // Constructor that initializes all fields
    public UserSession(long id, String sessionToken, Timestamp timestamp, Timestamp expiryTime,
                       String employeeNumber, Employee employee, String ownerNumber, Owner owner, String sessionData) {
        setId(id); // Set inherited ID from BaseEntity
        this.sessionToken = sessionToken;
        this.timestamp = timestamp;
        this.expiryTime = expiryTime;
        this.employeeNumber = employeeNumber;
        this.employee = employee;
        this.ownerNumber = ownerNumber;
        this.owner = owner;
        this.sessionData = sessionData;
        this.active = true;
    }

    // Generate a new UUID-based session token
    private String generateSessionToken() {
        return UUID.randomUUID().toString();
    }

    // Calculate session expiration time (8 hours from now)
    private Timestamp calculateExpiryDate() {
        Instant now = Instant.now();
        Instant expiryInstant = now.plus(8, ChronoUnit.HOURS);
        return Timestamp.from(expiryInstant);
    }

    // Check if the session is expired
    public boolean isExpired() {
        return Instant.now().isAfter(expiryTime.toInstant());
    }

    // Get username depending on whether session belongs to an employee or owner
    public String getUsername() {
        if (employee != null) {
            return employee.getLoginUsername();
        } else if (owner != null) {
            return owner.getLoginUsername();
        }
        throw new IllegalStateException("No user associated with session");
    }

    // ----- Getters and Setters -----

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

    // Refresh session timestamp and extend expiration time (e.g., for active use)
    public void refreshSession() {
        this.timestamp = Timestamp.from(Instant.now());
        this.expiryTime = calculateExpiryDate();
    }

    // Invalidate the session immediately
    public void invalidate() {
        this.active = false;
        this.expiryTime = Timestamp.from(Instant.now()); // Expire it immediately
    }

    // Check if the session is both active and not expired
    public boolean isValid() {
        return active && !isExpired();
    }

    // Get/set the active status
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Determine if session belongs to an employee
    public boolean isEmployeeSession() {
        return employee != null;
    }

    // Determine if session belongs to an owner
    public boolean isOwnerSession() {
        return owner != null;
    }
}
