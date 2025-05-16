package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dao.EmployeeDao;
import dao.OwnerDao;
import dao.UserSessionDao;
import model.Employee;
import model.Owner;
import model.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthenticationService;

import service.HashService;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class AuthenticationServiceImplTest {

	private EmployeeDao employeeDao;
	private OwnerDao ownerDao;
	private UserSessionDao userSessionDao;
	private HashService hashService;
	private AuthenticationService authService;

	// Set up mocks and initialize AuthenticationService before each test
	@BeforeEach
	void setUp() {
		employeeDao = mock(EmployeeDao.class);
		ownerDao = mock(OwnerDao.class);
		userSessionDao = mock(UserSessionDao.class);
		hashService = mock(HashService.class);
		authService = new AuthenticationService(employeeDao, ownerDao, userSessionDao, hashService);
	}

	// Test successful login with valid employee credentials
	@Test
	void testLoginWithValidEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setLoginPassword("hashedPass");

		// Mock DAO and hash service behavior
		when(employeeDao.findByUsername("user")).thenReturn(employee);
		when(hashService.verify("plainPass", "hashedPass")).thenReturn(true);
		when(userSessionDao.create(any(UserSession.class))).thenReturn(true);

		UserSession result = authService.login("user", "plainPass");

		// Verify session is created with correct employee
		assertNotNull(result);
		assertEquals(employee, result.getEmployee());
		assertNull(result.getOwner());
	}

	// Test login failure with incorrect employee password
	@Test
	void testLoginWithWrongPasswordForEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setLoginPassword("hashedPass");

		// Mock DAO and hash service for incorrect password
		when(employeeDao.findByUsername("user")).thenReturn(employee);
		when(hashService.verify("wrongPass", "hashedPass")).thenReturn(false);

		// Expect exception for wrong password
		Exception exception = assertThrows(Exception.class, () -> authService.login("user", "wrongPass"));
		assertEquals("Sai mật khẩu", exception.getMessage());
	}

	// Test successful login with valid owner credentials
	@Test
	void testLoginWithValidOwner() throws Exception {
		Owner owner = new Owner();
		owner.setLoginPassword("hashedPass");

		// Mock DAO and hash service behavior
		when(employeeDao.findByUsername("user")).thenReturn(null);
		when(ownerDao.findByUsername("user")).thenReturn(owner);
		when(hashService.verify("plainPass", "hashedPass")).thenReturn(true);
		when(userSessionDao.create(any(UserSession.class))).thenReturn(true);

		UserSession result = authService.login("user", "plainPass");

		// Verify session is created with correct owner
		assertNotNull(result);
		assertEquals(owner, result.getOwner());
		assertNull(result.getEmployee());
	}

	// Test login failure with non-existent username
	@Test
	void testLoginWithInvalidUsername() throws Exception {
		// Mock DAO to return null for unknown user
		when(employeeDao.findByUsername("unknown")).thenReturn(null);
		when(ownerDao.findByUsername("unknown")).thenReturn(null);

		// Expect exception for non-existent account
		Exception exception = assertThrows(Exception.class, () -> authService.login("unknown", "any"));
		assertEquals("Tài khoản không tồn tại", exception.getMessage());
	}

	// Test successful logout with valid token
	@Test
	void testLogoutSuccess() throws Exception {
		UserSession session = new UserSession();
		session.setSessionToken("token123");

		// Mock DAO to return session and allow deletion
		when(userSessionDao.findAll(anyInt(), anyInt())).thenReturn(List.of(session));
		when(userSessionDao.delete(session)).thenReturn(true);

		boolean result = authService.logout("token123", 0, Integer.MAX_VALUE);
		assertTrue(result);
	}

	// Test logout failure when token is not found
	@Test
	void testLogoutTokenNotFound() throws Exception {
		// Mock DAO to return empty session list
		when(userSessionDao.findAll(anyInt(), anyInt())).thenReturn(List.of());

		boolean result = authService.logout("tokenXYZ", 0, Integer.MAX_VALUE);
		assertFalse(result);
	}

	// Test retrieving user session with valid token
	@Test
	void testGetUserFromTokenValid() throws Exception {
		UserSession session = new UserSession();
		session.setSessionToken("token123");
		session.setExpiryTime(Timestamp.from(Instant.now().plus(Duration.ofHours(8))));
		session.setActive(true);

		// Mock DAO to return valid session
		when(userSessionDao.findAll(anyInt(), anyInt())).thenReturn(List.of(session));

		UserSession result = authService.getUserFromToken("token123", 0, Integer.MAX_VALUE);

		// Verify correct session is returned
		assertNotNull(result);
		assertEquals("token123", result.getSessionToken());
	}

	// Test retrieving user session with expired token
	@Test
	void testGetUserFromTokenInvalid() throws Exception {
		UserSession session = new UserSession();
		session.setSessionToken("token123");
		session.setExpiryTime(Timestamp.from(Instant.now().minus(Duration.ofHours(8))));
		session.setActive(true);

		// Mock DAO to return expired session
		when(userSessionDao.findAll(anyInt(), anyInt())).thenReturn(List.of(session));

		// Expect exception for invalid/expired token
		Exception ex = assertThrows(Exception.class,
				() -> authService.getUserFromToken("token123", 0, Integer.MAX_VALUE));
		assertEquals("Token không hợp lệ hoặc đã hết hạn", ex.getMessage());
	}
}