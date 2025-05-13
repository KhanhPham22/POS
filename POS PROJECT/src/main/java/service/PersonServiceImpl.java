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
    private static final Logger Log = LogManager.getLogger(PersonServiceImpl.class); // Logger to log the activities

    private static EmployeeDao employeeDao; // DAO for Employee operations
    private static CustomerDao customerDao; // DAO for Customer operations
    private static OwnerDao ownerDao; // DAO for Owner operations
    
    // Constructor to initialize DAOs for Employee, Customer, and Owner
    public PersonServiceImpl(EmployeeDao employeeDao, CustomerDao customerDao, OwnerDao ownerDao) {
        this.employeeDao = employeeDao;
        this.customerDao = customerDao;
        this.ownerDao = ownerDao;
    }

    // Method to create an Employee in the system
    @Override
    public void createEmployee(Employee employee) throws Exception {
        try {
            employeeDao.create(employee); // Creating employee using DAO
            Log.info("Employee created"); // Log success message
        } catch (Exception e) {
            Log.error("Error while creating employee", e); // Log error if creation fails
        }
    }

    // Method to create a Customer in the system
    @Override
    public void createCustomer(Customer customer) throws Exception {
        try {
            customerDao.create(customer); // Creating customer using DAO
            Log.info("Customer created"); // Log success message
        } catch (Exception e) {
            Log.error("Error while creating customer", e); // Log error if creation fails
        }
    }

    // Method to create an Owner in the system
    @Override
    public void createOwner(Owner owner) throws Exception {
        try {
            ownerDao.create(owner); // Creating owner using DAO
            Log.info("Owner created"); // Log success message
        } catch (Exception e) {
            Log.error("Error while creating owner", e); // Log error if creation fails
        }
    }

    // Method to delete an Employee by ID
    @Override
    public void deleteEmployee(long personId) throws Exception {
        try {
            Employee employee = employeeDao.findById(personId); // Find the employee by ID
            employeeDao.delete(employee); // Delete the employee using DAO
            Log.info("Employee with id " + personId + " deleted"); // Log success message
        } catch (Exception e) {
            Log.error("Error while deleting employee", e); // Log error if deletion fails
        }
    }

    // Method to delete a Customer by ID
    @Override
    public void deleteCustomer(long personId) throws Exception {
        try {
            Customer customer = customerDao.findById(personId); // Find the customer by ID
            customerDao.delete(customer); // Delete the customer using DAO
            Log.info("Customer with id " + personId + " deleted"); // Log success message
        } catch (Exception e) {
            Log.error("Error while deleting customer", e); // Log error if deletion fails
        }
    }

    // Method to delete an Owner by ID
    @Override
    public void deleteOwner(long personId) throws Exception {
        try {
            Owner owner = ownerDao.findById(personId); // Find the owner by ID
            ownerDao.delete(owner); // Delete the owner using DAO
            Log.info("Owner with id " + personId + " deleted"); // Log success message
        } catch (Exception e) {
            Log.error("Error while deleting owner", e); // Log error if deletion fails
        }
    }

    // Method to update an Employee in the system
    @Override
    public void updateEmployee(Employee employee) throws Exception {
        try {
            employeeDao.update(employee); // Update the employee using DAO
            Log.info("Employee with id " + employee.getPersonId() + " updated"); // Log success message
        } catch (Exception e) {
            Log.error("Error while updating employee", e); // Log error if update fails
        }
    }

    // Method to update a Customer in the system
    @Override
    public void updateCustomer(Customer customer) throws Exception {
        try {
            customerDao.update(customer); // Update the customer using DAO
            Log.info("Customer with id " + customer.getPersonId() + " updated"); // Log success message
        } catch (Exception e) {
            Log.error("Error while updating customer", e); // Log error if update fails
        }
    }

    // Method to update an Owner in the system
    @Override
    public void updateOwner(Owner owner) throws Exception {
        try {
            ownerDao.update(owner); // Update the owner using DAO
            Log.info("Owner with id " + owner.getPersonId() + " updated"); // Log success message
        } catch (Exception e) {
            Log.error("Error while updating owner", e); // Log error if update fails
        }
    }

    // Method to retrieve an Employee by ID
    @Override
    public Employee getEmployee(long personId) throws Exception {
        Employee employee = employeeDao.findById(personId); // Retrieve the employee by ID
        Log.info("Employee with id " + personId + " retrieved"); // Log success message
        return employee;
    }

    // Method to retrieve a Customer by ID
    @Override
    public Customer getCustomer(long personId) throws Exception {
        Customer customer = customerDao.findById(personId); // Retrieve the customer by ID
        Log.info("Customer with id " + personId + " retrieved"); // Log success message
        return customer;
    }

    // Method to retrieve an Owner by ID
    @Override
    public Owner getOwner(long personId) throws Exception {
        Owner owner = ownerDao.findById(personId); // Retrieve the owner by ID
        Log.info("Owner with id " + personId + " retrieved"); // Log success message
        return owner;
    }

    // Method to retrieve a list of all Employees with pagination
    @Override
    public List<Employee> getAllEmployees(int pageNumber, int pageSize) throws Exception {
        List<Employee> employees = employeeDao.findAll(pageNumber, pageSize); // Retrieve employees from DAO with pagination
        Log.info("All Employees retrieved"); // Log success message
        return employees;
    }

    // Method to retrieve a list of all Customers with pagination
    @Override
    public List<Customer> getAllCustomers(int pageNumber, int pageSize) throws Exception {
        List<Customer> customers = customerDao.findAll(pageNumber, pageSize); // Retrieve customers from DAO with pagination
        Log.info("All Customers retrieved"); // Log success message
        return customers;
    }

    // Method to retrieve a list of all Owners with pagination
    @Override
    public List<Owner> getAllOwner(int pageNumber, int pageSize) throws Exception {
        List<Owner> owner = ownerDao.findAll(pageNumber, pageSize); // Retrieve owners from DAO with pagination
        Log.info("All Owner retrieved"); // Log success message
        return owner;
    }

    // Method to retrieve an Employee by username
    @Override
    public Employee getEmployeeByUsername(String username) throws Exception {
        Employee employee = employeeDao.findByUsername(username); // Retrieve employee by username
        Log.info("Employee with username " + username + " retrieved"); // Log success message
        return employee;
    }

    // Method to retrieve a Customer by name and phone
    @Override
    public Customer getCustomerByNameandPhone(String name, String phone) throws Exception {
        Customer customer = customerDao.findByNameAndPhone(name, phone); // Retrieve customer by name and phone
        Log.info("Customer with name " + name + " and phone " + phone + " retrieved"); // Log success message
        return customer;
    }

    // Method to retrieve a Customer by phone number
    @Override
    public Customer getCustomerByPhone(String phone) throws Exception {
        Customer customer = customerDao.findByPhone(phone); // Retrieve customer by phone number
        Log.info("Customer with phone " + phone + " retrieved"); // Log success message
        return customer;
    }

    // Method to retrieve an Owner by username
    @Override
    public Owner getOwnerByUsername(String username) throws Exception {
        Owner owner = ownerDao.findByUsername(username); // Retrieve owner by username
        Log.info("Owner with username " + username + " retrieved"); // Log success message
        return owner;
    }

    // Method to check if an email exists in Employee or Owner
    @Override
    public boolean existsByEmail(String email) throws Exception {
        try {
            boolean exists = employeeDao.findByEmail(email) != null || ownerDao.findByEmail(email) != null; // Check if email exists
            Log.info("Checked email existence for: " + email + ", exists: " + exists); // Log check result
            return exists;
        } catch (Exception e) {
            Log.error("Error while checking email existence: " + email, e); // Log error if check fails
            throw e;
        }
    }

    // Method to check if a username exists in Employee or Owner
    @Override
    public boolean existsByUsername(String username) throws Exception {
        try {
            boolean exists = employeeDao.findByUsername(username) != null || ownerDao.findByUsername(username) != null; // Check if username exists
            Log.info("Checked username existence for: " + username + ", exists: " + exists); // Log check result
            return exists;
        } catch (Exception e) {
            Log.error("Error while checking username existence: " + username, e); // Log error if check fails
            throw e;
        }
    }
}
