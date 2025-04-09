package service;
import model.Employee;
import model.Owner;

import java.util.List;

import model.Customer;
public interface PersonService {
	void createEmployee(Employee employee) throws Exception;

	void createCustomer(Customer customer) throws Exception;
	
	void createOwner(Owner owner) throws Exception;
	
	void deleteEmployee(long personId) throws Exception;

	void deleteCustomer(long personId) throws Exception;
	
	void deleteOwner(long personId) throws Exception;
	
	void updateEmployee(Employee employee) throws Exception;

	void updateCustomer(Customer customer) throws Exception;
	
	void updateOwner(Owner owner) throws Exception;
	
	Employee getEmployee(long personId) throws Exception;

	Customer getCustomer(long personId) throws Exception;
	
	Owner getOwner(long personId) throws Exception;
	
	Employee getEmployeeByUsername(String username) throws Exception;
	
	Customer getCustomerByNameandPhone(String name,String phone) throws Exception;
	
	Owner getOwnerByUsername(String username) throws Exception;
	
	List<Employee> getAllEmployees() throws Exception;

	List<Customer> getAllCustomers() throws Exception;
	
	List<Owner> getAllOwner() throws Exception;
}
