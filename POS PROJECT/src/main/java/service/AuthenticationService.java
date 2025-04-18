package service;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dao.EmployeeDao;
import dao.UserSessionDao;
import dao.OwnerDao;
import model.UserSession;
import model.Owner;
import model.Employee;
import util.Injector;
public class AuthenticationService {

    private final EmployeeDao employeeDao;
    private final OwnerDao ownerDao;
    private final UserSessionDao userSessionDao;
    private final HashService hashService;

    private static final Logger Log = LogManager.getLogger(AuthenticationService.class);

    public AuthenticationService(EmployeeDao employeeDao, OwnerDao ownerDao ,UserSessionDao userSessionDao, HashService hashService) {
        this.employeeDao = employeeDao;
		this.ownerDao =  ownerDao;
        this.userSessionDao = userSessionDao;
        this.hashService = hashService;
        this.userSessionDao.setClass(UserSession.class);
        this.employeeDao.setClass(Employee.class);
        this.ownerDao.setClass(Owner.class);
    }


    // Đăng nhập
    public UserSession login(String username, String password) throws Exception {
        Log.info("Login attempt for username: " + username);

        // Thử tìm Employee
        Employee employee = employeeDao.findByUsername(username);
        if (employee != null) {
            if (!hashService.verify(password, employee.getLoginPassword())) {
                Log.warn("Incorrect password for employee username: " + username);
                throw new Exception("Sai mật khẩu");
            }

            UserSession session = new UserSession(employee, null);
            userSessionDao.create(session);
            Log.info("Employee login successful for username: " + username);
            return session;
        }

        // Nếu không tìm thấy Employee, thử tìm Owner
        Owner owner = ownerDao.findByUsername(username);
        if (owner != null) {
            if (!hashService.verify(password, owner.getLoginPassword())) {
                Log.warn("Incorrect password for owner username: " + username);
                throw new Exception("Sai mật khẩu");
            }

            UserSession session = new UserSession(null, owner);
            userSessionDao.create(session);
            Log.info("Owner login successful for username: " + username);
            return session;
        }

        Log.warn("No account found with username: " + username);
        throw new Exception("Tài khoản không tồn tại");
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
    
    // Lấy thông tin người dùng từ token
    public UserSession getUserFromToken(String token) throws Exception {
        List<UserSession> allSessions = userSessionDao.findAll();
        for (UserSession session : allSessions) {
            if (session.getSessionToken().equals(token) && !session.isExpired()) {
                return session;
            }
        }
        Log.warn("Token không hợp lệ hoặc đã hết hạn: " + token);
        throw new Exception("Token không hợp lệ hoặc đã hết hạn");
    }
}



