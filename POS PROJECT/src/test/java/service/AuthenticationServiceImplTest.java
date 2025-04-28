package service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.EmployeeDao;
import dao.OwnerDao;
import dao.UserSessionDao;
import model.Employee;
import model.UserSession;
import model.Owner;

public class AuthenticationServiceImplTest {
	 private EmployeeDao employeeDao;
	    private OwnerDao ownerDao;
	    private UserSessionDao userSessionDao;
	    private HashService hashService;

	    private AuthenticationService authService;
	    
	    @BeforeEach
	    void setUp() {
	        employeeDao = mock(EmployeeDao.class);
	        ownerDao = mock(OwnerDao.class);
	        userSessionDao = mock(UserSessionDao.class);
	        hashService = mock(HashService.class);
	        authService = new AuthenticationService(employeeDao, ownerDao, userSessionDao, hashService);
	    }
	    @Test
	    void testLoginWithValidEmployee() throws Exception {
	        Employee employee = new Employee();
	        employee.setLoginPassword("hashedPass");

	        when(employeeDao.findByUsername("user")).thenReturn(employee);
	        when(hashService.verify("plainPass", "hashedPass")).thenReturn(true);

	        UserSession session = new UserSession(employee, null);
	        when(userSessionDao.create(any(UserSession.class))).thenReturn(true);

	        UserSession result = authService.login("user", "plainPass");

	        assertNotNull(result);
	        assertEquals(employee, result.getEmployee());
	        assertNull(result.getOwner());
	    }
	    
	    @Test
	    void testLoginWithWrongPasswordForEmployee() throws Exception {
	        Employee employee = new Employee();
	        employee.setLoginPassword("hashedPass");

	        when(employeeDao.findByUsername("user")).thenReturn(employee);
	        when(hashService.verify("wrongPass", "hashedPass")).thenReturn(false);

	        Exception exception = assertThrows(Exception.class, () -> authService.login("user", "wrongPass"));
	        assertEquals("Sai mật khẩu", exception.getMessage());
	    }

	    @Test
	    void testLoginWithValidOwner() throws Exception {
	        when(employeeDao.findByUsername("user")).thenReturn(null);

	        Owner owner = new Owner();
	        owner.setLoginPassword("hashedPass");

	        when(ownerDao.findByUsername("user")).thenReturn(owner);
	        when(hashService.verify("plainPass", "hashedPass")).thenReturn(true);
	        when(userSessionDao.create(any(UserSession.class))).thenReturn(true);

	        UserSession result = authService.login("user", "plainPass");

	        assertNotNull(result);
	        assertEquals(owner, result.getOwner());
	        assertNull(result.getEmployee());
	    }

	    @Test
	    void testLoginWithInvalidUsername() throws Exception {
	        when(employeeDao.findByUsername("unknown")).thenReturn(null);
	        when(ownerDao.findByUsername("unknown")).thenReturn(null);

	        Exception exception = assertThrows(Exception.class, () -> authService.login("unknown", "any"));
	        assertEquals("Tài khoản không tồn tại", exception.getMessage());
	    }
	    
//	    @Test
//	    void testIsTokenValid() throws Exception {
//	        UserSession session = new UserSession();
//	        session.setSessionToken("token123");
//
//	        // Set expiryTime trong tương lai => token còn hiệu lực
//	        jjava.sql.Timestamp futureExpiry = Timestamp.valueOf(Instant.now().plus(Duration.ofHours(8)).atZone(ZoneId.systemDefault()).toLocalDateTime());
//
//	        session.setExpiryTime(futureExpiry);
//	        
//	        // PHẢI có dòng này để token là hợp lệ
//	        session.setActive(true);
//
//	        when(userSessionDao.findAll()).thenReturn(List.of(session));
//
//	        boolean result = authService.isTokenValid("token123");
//
//	        assertTrue(result);
//	    }



//	    @Test
//	    void testIsTokenInvalid() throws Exception {
//	        UserSession session = new UserSession();
//	        session.setSessionToken("token123");
//
//	        // Token hết hạn
//	        java.sql.Timestamp expiredTime = Timestamp.from(Instant.now().minus(Duration.ofHours(8)));
//	        session.setExpiryTime(expiredTime);
//
//	        // Có thể thêm dòng này cho rõ
//	        session.setActive(true); // hoặc false — đều không hợp lệ
//
//	        when(userSessionDao.findAll()).thenReturn(List.of(session));
//
//	        boolean result = authService.isTokenValid("token123");
//
//	        assertFalse(result);
//	    }




//	    @Test
//	    void testLogoutSuccess() throws Exception {
//	        UserSession session = new UserSession();
//	        session.setSessionToken("token123");
//
//	        when(userSessionDao.findAll()).thenReturn(List.of(session));
//	        when(userSessionDao.delete(session)).thenReturn(true);
//
//	        boolean result = authService.logout("token123");
//
//	        assertTrue(result);
//	    }
//
//	    @Test
//	    void testLogoutTokenNotFound() throws Exception {
//	        when(userSessionDao.findAll()).thenReturn(List.of());
//
//	        boolean result = authService.logout("tokenXYZ");
//
//	        assertFalse(result);
//	    }

//	    @Test
//	    void testGetUserFromTokenValid() throws Exception {
//	        UserSession session = new UserSession();
//	        session.setSessionToken("token123");
//
//	        // Set expiryTime hợp lệ
//	        java.sql.Timestamp futureExpiry = Timestamp.from(Instant.now().plus(Duration.ofHours(8)));
//	        session.setExpiryTime(futureExpiry);
//	        
//	        // THÊM dòng này
//	        session.setActive(true);
//
//	        when(userSessionDao.findAll()).thenReturn(List.of(session));
//
//	        UserSession result = authService.getUserFromToken("token123");
//
//	        assertNotNull(result);
//	        assertEquals("token123", result.getSessionToken());
//	    }



//	    @Test
//	    void testGetUserFromTokenInvalid() throws Exception {
//	        UserSession session = new UserSession();
//	        session.setSessionToken("token123");
//
//	        // Token hết hạn
//	        java.sql.Timestamp expiredTime = Timestamp.from(Instant.now().minus(Duration.ofHours(8)));
//	        session.setExpiryTime(expiredTime);
//
//	        // Có thể thêm dòng này cho rõ
//	        session.setActive(true); // hoặc false đều được
//
//	        when(userSessionDao.findAll()).thenReturn(List.of(session));
//
//	        Exception ex = assertThrows(Exception.class, () -> authService.getUserFromToken("token123"));
//	        assertEquals("Token không hợp lệ hoặc đã hết hạn", ex.getMessage());
//	    }



}
