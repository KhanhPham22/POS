package service;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dao.EmployeeDao;
import dao.UserSessionDao;
import model.UserSession;
import model.Employee;
import util.Injector;
public class AuthenticationService {

    private final EmployeeDao employeeDao;
    private final UserSessionDao userSessionDao;
    private final HashService hashService;

    private static final Logger Log = LogManager.getLogger(AuthenticationService.class);

    public AuthenticationService(EmployeeDao employeeDao, UserSessionDao userSessionDao, HashService hashService) {
        this.employeeDao = employeeDao;
        this.userSessionDao = userSessionDao;
        this.hashService = hashService;
        this.userSessionDao.setClass(UserSession.class);
        this.employeeDao.setClass(Employee.class);
    }


    // Đăng nhập
    public UserSession login(String username, String password) throws Exception {
        Log.info("Login attempt for username: " + username);

        Employee employee = employeeDao.findByUsername(username);
        if (employee == null) {
            Log.warn("No employee found with username: " + username);
            throw new Exception("Tài khoản không tồn tại");
        }

        if (!hashService.verify(password, employee.getLoginPassword())) {
            Log.warn("Incorrect password for username: " + username);
            throw new Exception("Sai mật khẩu");
        }

        // Tạo UserSession mới
        UserSession session = new UserSession(employee);
        userSessionDao.create(session);
        Log.info("Login successful for username: " + username);
        return session;
    }

    // Kiểm tra token có hợp lệ không
    public boolean isTokenValid(String token) throws Exception {
        List<UserSession> allSessions = userSessionDao.findAll();
        for (UserSession session : allSessions) {
            if (session.getSessionToken().equals(token) && !session.isExpired()) {
                return true;
            }
        }
        return false;
    }

    // Logout theo session token
    public boolean logout(String token) throws Exception {
        List<UserSession> allSessions = userSessionDao.findAll();
        for (UserSession session : allSessions) {
            if (session.getSessionToken().equals(token)) {
                return userSessionDao.delete(session);
            }
        }
        Log.warn("Logout failed: Token not found");
        return false;
    }
}

