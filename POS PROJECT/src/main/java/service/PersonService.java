package service;
import model.Employee;
import model.Owner;

import java.util.List;

import model.Customer;
public interface PersonService {
	//create
	void createEmployee(Employee employee) throws Exception;
	void createCustomer(Customer customer) throws Exception;
	void createOwner(Owner owner) throws Exception;
	//delete
	void deleteEmployee(long personId) throws Exception;
	void deleteCustomer(long personId) throws Exception;
	void deleteOwner(long personId) throws Exception;
	//update
	void updateEmployee(Employee employee) throws Exception;
	void updateCustomer(Customer customer) throws Exception;
	void updateOwner(Owner owner) throws Exception;
	//get
	Employee getEmployee(long personId) throws Exception;
	Customer getCustomer(long personId) throws Exception;	
	Owner getOwner(long personId) throws Exception;
	//getby
	Employee getEmployeeByUsername(String username) throws Exception;	
	Customer getCustomerByNameandPhone(String name,String phone) throws Exception;
	Owner getOwnerByUsername(String username) throws Exception;
	//pagination
	List<Employee> getAllEmployees(int pageNumber, int pageSize) throws Exception;
	List<Customer> getAllCustomers(int pageNumber, int pageSize) throws Exception;	
	List<Owner> getAllOwner(int pageNumber, int pageSize) throws Exception;
	
	boolean existsByEmail(String email) throws Exception;
    boolean existsByUsername(String username) throws Exception;
}
