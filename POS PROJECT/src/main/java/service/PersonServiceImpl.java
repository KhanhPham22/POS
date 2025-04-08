package service;
import model.Employee;
import model.Customer;
import dao.EmployeeDao;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.CustomerDao;

public class PersonServiceImpl implements PersonService {
	private static final Logger Log = LogManager.getLogger(PersonServiceImpl.class);

	private static EmployeeDao employeeDao ;
	private static CustomerDao customerDao ;
	
	public PersonServiceImpl(EmployeeDao employeeDao, CustomerDao customerDao) {
		this.employeeDao = employeeDao;
		this.customerDao = customerDao;
		
	}
	
	@Override
	public void createEmployee(Employee employee) throws Exception {
		try {
			employeeDao.create(employee);
			Log.info("Employee created");
		} catch (Exception e) {
			Log.error("Error while creating employee", e);
		}

	}

	@Override
	public void createCustomer(Customer customer) throws Exception {
		try {
			customerDao.create(customer);
			Log.info("Customer created");
		} catch (Exception e) {
			Log.error("Error while creating customer", e);
		}
	}
	
	@Override
	public void deleteEmployee(long personId) throws Exception {
		try {
			Employee employee = employeeDao.findById(personId);
			employeeDao.delete(employee);
			Log.info("Employee with id " + personId + " deleted");
		} catch (Exception e) {
			Log.error("Error while deleting employee", e);
		}

	}

	@Override
	public void deleteCustomer(long personId) throws Exception {
		try {
			Customer customer = customerDao.findById(personId);
			customerDao.delete(customer);
			Log.info("Customer with id " + personId + " deleted");
		} catch (Exception e) {
			Log.error("Error while deleting customer", e);
		}
	}
	
	@Override
	public void updateEmployee(Employee employee) throws Exception {
		try {
			employeeDao.update(employee);
			Log.info("Employee with id " + employee.getPersonId() + " updated");
		} catch (Exception e) {
			Log.error("Error while updating employee", e);
		}

	}

	@Override
	public void updateCustomer(Customer customer) throws Exception {
		try {
			customerDao.update(customer);
			Log.info("Customer with id " + customer.getPersonId() + " updated");
		} catch (Exception e) {
			Log.error("Error while updating customer", e);
		}

	}
	
	@Override
	public Employee getEmployee(long personId) throws Exception {
		Employee employee = employeeDao.findById(personId);
		Log.info("Employee with id " + personId + " retrieved");
		return employee;

	}

	@Override
	public Customer getCustomer(long personId) throws Exception {
		Customer customer = customerDao.findById(personId);
		Log.info("Customer with id " + personId + " retrieved");
		return customer;
	}
	
	@Override
	public List<Employee> getAllEmployees() throws Exception {
		List<Employee> employees = employeeDao.findAll();
		Log.info("All Employees retrieved");
		return employees;

	}

	@Override
	public List<Customer> getAllCustomers() throws Exception {
		List<Customer> customers = customerDao.findAll();
		Log.info("All Customers retrieved");
		return customers;
	}
	
	@Override
	public Employee getEmployeeByUsername(String username) throws Exception {
		Employee employee = employeeDao.findByUsername(username);
		Log.info("Employee with username " + username + " retrieved");
		return employee;
	}
}
