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

	// Dao objects for Employee, Owner, UserSession and the HashService to handle
	// login, session, and password hashing
	private final EmployeeDao employeeDao;
	private final OwnerDao ownerDao;
	private final UserSessionDao userSessionDao;
	private final HashService hashService;

	private static final Logger Log = LogManager.getLogger(AuthenticationService.class);

	// Constructor to initialize DAO classes and set the class types for DAOs
	public AuthenticationService(EmployeeDao employeeDao, OwnerDao ownerDao, UserSessionDao userSessionDao,
			HashService hashService) {
		this.employeeDao = employeeDao;
		this.ownerDao = ownerDao;
		this.userSessionDao = userSessionDao;
		this.hashService = hashService;
		this.userSessionDao.setClass(UserSession.class); // Setting the UserSession class type
		this.employeeDao.setClass(Employee.class); // Setting the Employee class type
		this.ownerDao.setClass(Owner.class); // Setting the Owner class type
	}

	// Method for login with username or email and password
	public UserSession login(String identifier, String password) throws Exception {
		Log.info("Login attempt for identifier: " + identifier);

		// Try to find Employee by username or email
		Employee employee = employeeDao.findByUsername(identifier);
		if (employee == null) {
			employee = employeeDao.findByEmail(identifier);
		}
		if (employee != null) {
			// Check if employee is inactive
			if (!employee.isEnabledFlag()) {
				Log.warn("Login attempt for inactive employee: " + identifier);
				throw new Exception("Tài khoản đang bị vô hiệu hóa");
			}
			// Verify password for the employee
			if (!hashService.verify(password, employee.getLoginPassword())) {
				Log.warn("Incorrect password for employee identifier: " + identifier);
				throw new Exception("Sai mật khẩu");
			}

			// Create a session and save it to the database
			UserSession session = new UserSession(employee, null);
			userSessionDao.create(session);
			Log.info("Employee login successful for identifier: " + identifier);
			return session;
		}

		// If no Employee found, try to find Owner by username or email
		Owner owner = ownerDao.findByUsername(identifier);
		if (owner == null) {
			owner = ownerDao.findByEmail(identifier);
		}
		if (owner != null) {
			// Check if owner is inactive
			if (!owner.isEnabledFlag()) {
				Log.warn("Login attempt for inactive owner: " + identifier);
				throw new Exception("Tài khoản đang bị vô hiệu hóa");
			}
			// Verify password for the owner
			if (!hashService.verify(password, owner.getLoginPassword())) {
				Log.warn("Incorrect password for owner identifier: " + identifier);
				throw new Exception("Sai mật khẩu");
			}

			// Create a session and save it to the database
			UserSession session = new UserSession(null, owner);
			userSessionDao.create(session);
			Log.info("Owner login successful for identifier: " + identifier);
			return session;
		}

		// If neither Employee nor Owner is found
		Log.warn("No account found with identifier: " + identifier);
		throw new Exception("Tài khoản không tồn tại");
	}

	// Method for logging out by invalidating the session based on the token
	public boolean logout(String token, int pageNumber, int pageSize) throws Exception {
		List<UserSession> allSessions = userSessionDao.findAll(pageNumber, pageSize);
		for (UserSession session : allSessions) {
			if (session.getSessionToken().equals(token)) {
				return userSessionDao.delete(session); // Delete the session to log out
			}
		}
		Log.warn("Logout failed: Token not found");
		return false;
	}

	// Method to retrieve the user associated with a token
	public UserSession getUserFromToken(String token, int pageNumber, int pageSize) throws Exception {
		List<UserSession> allSessions = userSessionDao.findAll(pageNumber, pageSize);
		for (UserSession session : allSessions) {
			if (session.getSessionToken().equals(token) && !session.isExpired()) {
				return session; // Return the session if token is valid and not expired
			}
		}
		Log.warn("Token không hợp lệ hoặc đã hết hạn: " + token);
		throw new Exception("Token không hợp lệ hoặc đã hết hạn"); // If token is invalid or expired
	}

	// Method to check if the password is strong enough
	public boolean isStrongPassword(String password) {
		if (password == null)
			return false;

		// Check password length (at least 6 characters)
		if (password.length() < 6)
			return false;

		// Check if password contains at least one uppercase letter, one lowercase
		// letter, one digit, and one special character
		boolean hasUppercase = password.matches(".*[A-Z].*"); // at least one uppercase letter
		boolean hasLowercase = password.matches(".*[a-z].*"); // at least one lowercase letter
		boolean hasDigit = password.matches(".*[0-9].*"); // at least one digit
		boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"); // at least one
																										// special
																										// character

		return hasUppercase && hasLowercase && hasDigit && hasSpecialChar; // Return true if all conditions are met
	}

	// Method to reset the password for an account
	public boolean resetPasswordByUsername(String username, String newPassword) throws Exception {
		Log.info("Reset password attempt for username: " + username);

		// Check if the new password is strong enough
		if (!isStrongPassword(newPassword)) {
			Log.warn("Weak password for username: " + username);
			throw new Exception("Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
		}

		// Try to find the Employee by username and update the password
		Employee employee = employeeDao.findByUsername(username);
		if (employee != null) {
			// Check if new password matches old password
			if (hashService.verify(newPassword, employee.getLoginPassword())) {
				Log.warn("New password matches old password for username: " + username);
				throw new Exception("Mật khẩu mới không được trùng với mật khẩu cũ");
			}
			String hashedPassword = hashService.hash(newPassword);
			employee.setLoginPassword(hashedPassword);
			return employeeDao.update(employee);
		}

		// Try to find the Owner by username and update the password
		Owner owner = ownerDao.findByUsername(username);
		if (owner != null) {
			// Check if new password matches old password
			if (hashService.verify(newPassword, owner.getLoginPassword())) {
				Log.warn("New password matches old password for username: " + username);
				throw new Exception("Mật khẩu mới không được trùng với mật khẩu cũ");
			}
			String hashedPassword = hashService.hash(newPassword);
			owner.setLoginPassword(hashedPassword);
			return ownerDao.update(owner);
		}

		Log.warn("No account found with username: " + username);
		throw new Exception("Không tìm thấy tài khoản với tên đăng nhập này"); // If no account is found
	}
}
