package service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.UserSessionDao;
import model.Employee;
import model.Owner;
import model.UserSession;

/**
 * Implementation of the UserSessionService interface.
 * Handles creation and management of user sessions for both employees and owners.
 */
public class UserSessionServiceImpl implements UserSessionService {

    private static final Logger Log = LogManager.getLogger(UserSessionServiceImpl.class);

    private UserSessionDao userSessionDao;

    /**
     * Constructor initializes the UserSessionDao and sets its entity class.
     * 
     * @param userSessionDao DAO for managing UserSession entities
     */
    public UserSessionServiceImpl(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
        this.userSessionDao.setClass(UserSession.class);
    }

    /**
     * Creates a new session for an employee.
     * 
     * @param employee The employee to associate with the session
     * @return A newly created UserSession
     */
    @Override
    public UserSession createUserSession(Employee employee) {
        // Create session with associated employee and no owner
        UserSession newSession = new UserSession(employee, null);

        // Set optional session data (can be used for custom payloads like JSON, state, etc.)
        newSession.setSessionData(null);
        newSession.setCreatedBy(employee.getPersonId());
        newSession.setLastUpdatedDate(new Date()); // Update the timestamp

        try {
            // Persist session to database
            userSessionDao.create(newSession);
            Log.info("User session created successfully for employee: " + employee.getEmployeeNumber());
        } catch (Exception e) {
            Log.error("Failed to create user session for employee: " + employee.getEmployeeNumber(), e);
            e.printStackTrace();
        }

        return newSession;
    }

    /**
     * Creates a new session for an owner.
     * 
     * @param owner The owner to associate with the session
     * @return A newly created UserSession
     */
    @Override
    public UserSession createUserSessionForOwner(Owner owner) {
        // Create session with associated owner and no employee
        UserSession newSession = new UserSession(null, owner);

        // Set session metadata
        newSession.setSessionData(null);
        newSession.setCreatedBy(owner.getPersonId());
        newSession.setCreatedDate(new Date());
        newSession.setLastUpdatedDate(new Date());

        try {
            // Persist session to database
            userSessionDao.create(newSession);
            Log.info("User session created successfully for owner: " + owner.getOwnerNumber());
        } catch (Exception e) {
            Log.error("Failed to create user session for owner: " + owner.getOwnerNumber(), e);
            e.printStackTrace();
        }

        return newSession;
    }

}

