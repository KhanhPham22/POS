package service;

import model.Employee;
import model.UserSession;

public interface UserSessionService {
	
	 UserSession createUserSession(Employee employee);
	

}
