package service;
import model.Employee;

import java.util.List;

import model.Customer;
public interface PersonService {
	void createEmployee(Employee employee) throws Exception;

	void createCustomer(Customer customer) throws Exception;
	
	void deleteEmployee(long personId) throws Exception;

	void deleteCustomer(long personId) throws Exception;
	
	void updateEmployee(Employee employee) throws Exception;

	void updateCustomer(Customer customer) throws Exception;
	
	Employee getEmployee(long personId) throws Exception;

	Customer getCustomer(long personId) throws Exception;
	
	Employee getEmployeeByUsername(String username) throws Exception;
	
	List<Employee> getAllEmployees() throws Exception;

	List<Customer> getAllCustomers() throws Exception;
}
