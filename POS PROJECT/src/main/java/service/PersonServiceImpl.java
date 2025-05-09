package service;
import model.Employee;
import model.Customer;
import dao.EmployeeDao;
import dao.CustomerDao;
import model.Owner;
import dao.OwnerDao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class PersonServiceImpl implements PersonService {
	private static final Logger Log = LogManager.getLogger(PersonServiceImpl.class);

	private static EmployeeDao employeeDao ;
	private static CustomerDao customerDao ;
	private static OwnerDao ownerDao;
	
	public PersonServiceImpl(EmployeeDao employeeDao, CustomerDao customerDao, OwnerDao ownerDao) {
		this.employeeDao = employeeDao;
		this.customerDao = customerDao;
		this.ownerDao = ownerDao;
		
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
	public void createOwner(Owner owner) throws Exception {
		try {
			ownerDao.create(owner);
			Log.info("Owner created");
		} catch (Exception e) {
			Log.error("Error while creating owner", e);
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
	public void deleteOwner(long personId) throws Exception {
		try {
			Owner owner = ownerDao.findById(personId);
			ownerDao.delete(owner);
			Log.info("Owner with id " + personId + " deleted");
		} catch (Exception e) {
			Log.error("Error while deleting owner", e);
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
	public void updateOwner(Owner owner) throws Exception {
		try {
			ownerDao.update(owner);
			Log.info("Owner with id " + owner.getPersonId() + " updated");
		} catch (Exception e) {
			Log.error("Error while updating owner", e);
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
	public Owner getOwner(long personId) throws Exception {
		Owner owner = ownerDao.findById(personId);
		Log.info("Owner with id " + personId + " retrieved");
		return owner;
	}
	
	@Override
	public List<Employee> getAllEmployees(int pageNumber, int pageSize) throws Exception {
		List<Employee> employees = employeeDao.findAll(pageNumber,pageSize);
		Log.info("All Employees retrieved");
		return employees;

	}

	@Override
	public List<Customer> getAllCustomers(int pageNumber, int pageSize) throws Exception {
		List<Customer> customers = customerDao.findAll(pageNumber,pageSize);
		Log.info("All Customers retrieved");
		return customers;
	}
	
	@Override
	public List<Owner> getAllOwner(int pageNumber, int pageSize) throws Exception {
		List<Owner> owner = ownerDao.findAll(pageNumber,pageSize);
		Log.info("All Owner retrieved");
		return owner;
	}
	
	@Override
	public Employee getEmployeeByUsername(String username) throws Exception {
		Employee employee = employeeDao.findByUsername(username);
		Log.info("Employee with username " + username + " retrieved");
		return employee;
	}
	
	@Override
	public Customer getCustomerByNameandPhone(String name,String phone) throws Exception {
		Customer customer = customerDao.findByNameAndPhone(name, phone);
		Log.info("Customer with name " + name + " and phone " + phone + " retrieved");
		return customer;
	}
	
	  @Override
	    public Customer getCustomerByPhone(String phone) throws Exception {
	        Customer customer = customerDao.findByPhone(phone);
	        Log.info("Customer with phone " + phone + " retrieved");
	        return customer;
	    }
	
	@Override
	public Owner getOwnerByUsername(String username) throws Exception {
		Owner owner = ownerDao.findByUsername(username);
		Log.info("Owner with username " + username + " retrieved");
		return owner;
	}
	
	 @Override
	    public boolean existsByEmail(String email) throws Exception {
	        try {
	            boolean exists = employeeDao.findByEmail(email) != null || ownerDao.findByEmail(email) != null ;
	            Log.info("Checked email existence for: " + email + ", exists: " + exists);
	            return exists;
	        } catch (Exception e) {
	            Log.error("Error while checking email existence: " + email, e);
	            throw e;
	        }
	    }

	    @Override
	    public boolean existsByUsername(String username) throws Exception {
	        try {
	            boolean exists = employeeDao.findByUsername(username) != null || ownerDao.findByUsername(username) != null;
	            Log.info("Checked username existence for: " + username + ", exists: " + exists);
	            return exists;
	        } catch (Exception e) {
	            Log.error("Error while checking username existence: " + username, e);
	            throw e;
	        }
	    }
}
