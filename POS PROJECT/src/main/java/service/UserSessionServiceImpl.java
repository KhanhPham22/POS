package service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.UserSessionDao;
import model.Employee;
import model.UserSession;

public class UserSessionServiceImpl implements UserSessionService {

    private static final Logger Log = LogManager.getLogger(UserSessionServiceImpl.class);

    private UserSessionDao userSessionDao;

    public UserSessionServiceImpl(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
        this.userSessionDao.setClass(UserSession.class);
    }

    @Override
    public UserSession createUserSession(Employee employee) {
        UserSession newSession = new UserSession(employee);

        // Các thông tin bổ sung (nếu cần)
        newSession.setSessionData(null); // có thể lưu JSON, token, state, v.v nếu sau này cần
        newSession.setCreatedBy(employee.getPersonId());
  

        newSession.setLastUpdatedDate(new Date());

        try {
            userSessionDao.create(newSession);
            Log.info("User session created successfully for employee: " + employee.getEmployeeNumber());
        } catch (Exception e) {
            Log.error("Failed to create user session for employee: " + employee.getEmployeeNumber(), e);
            e.printStackTrace();
        }

        return newSession;
    }
}

