package service;

import model.Employee;
import model.Owner;
import model.UserSession;

public interface UserSessionService {
	
	 UserSession createUserSession(Employee employee);
	 UserSession createUserSessionForOwner(Owner owner);


}
