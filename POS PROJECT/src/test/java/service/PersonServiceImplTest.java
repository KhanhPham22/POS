package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import service.PersonServiceImpl;
import dao.EmployeeDao;
import dao.CustomerDao;
import dao.OwnerDao;
import model.Owner;
import model.Employee;
import model.Customer;

public class PersonServiceImplTest {

	private EmployeeDao employeeDao;
	private CustomerDao customerDao;
	private OwnerDao ownerDao;
	private PersonServiceImpl personService;

	/**
	 * Set up mocks and service before each test.
	 */
	@BeforeEach
	public void setup() {
		employeeDao = mock(EmployeeDao.class);
		customerDao = mock(CustomerDao.class);
		ownerDao = mock(OwnerDao.class);
		personService = new PersonServiceImpl(employeeDao, customerDao, ownerDao);
	}

	// ======= Employee Tests =======

	/**
	 * Test that creating an employee correctly delegates the call to the
	 * EmployeeDao.
	 */
	@Test
	public void testCreateEmployee() throws Exception {
		Employee employee = new Employee();
		personService.createEmployee(employee);
		verify(employeeDao).create(employee);
	}

	/**
	 * Test that fetching an employee by ID returns the correct employee.
	 */
	@Test
	public void testGetEmployee() throws Exception {
		Employee employee = new Employee();
		when(employeeDao.findById(1L)).thenReturn(employee);
		Employee result = personService.getEmployee(1L);
		assertEquals(employee, result);
	}

	/**
	 * Test that fetching all employees returns the correct list size.
	 */
	@Test
	public void testGetAllEmployees() throws Exception {
		List<Employee> employees = Arrays.asList(new Employee(), new Employee());
		when(employeeDao.findAll(0, 10)).thenReturn(employees);
		List<Employee> result = personService.getAllEmployees(0, 10);
		assertEquals(2, result.size());
	}

	/**
	 * Test that deleting an employee delegates the delete operation to the DAO.
	 */
	@Test
	public void testDeleteEmployee() throws Exception {
		Employee employee = new Employee();
		when(employeeDao.findById(1L)).thenReturn(employee);
		personService.deleteEmployee(1L);
		verify(employeeDao).delete(employee);
	}

	/**
	 * Test that updating an employee calls the update method on the DAO.
	 */
	@Test
	public void testUpdateEmployee() throws Exception {
		Employee employee = new Employee();
		personService.updateEmployee(employee);
		verify(employeeDao).update(employee);
	}

	/**
	 * Test that fetching an employee by username returns the correct result.
	 */
	@Test
	public void testGetEmployeeByUsername() throws Exception {
		Employee employee = new Employee();
		when(employeeDao.findByUsername("john")).thenReturn(employee);
		Employee result = personService.getEmployeeByUsername("john");
		assertEquals(employee, result);
	}

	// ======= Customer Tests =======

	/**
	 * Test that creating a customer correctly calls the DAO create method.
	 */
	@Test
	public void testCreateCustomer() throws Exception {
		Customer customer = new Customer();
		personService.createCustomer(customer);
		verify(customerDao).create(customer);
	}

	/**
	 * Test that fetching a customer by ID returns the correct customer.
	 */
	@Test
	public void testGetCustomer() throws Exception {
		Customer customer = new Customer();
		when(customerDao.findById(2L)).thenReturn(customer);
		Customer result = personService.getCustomer(2L);
		assertEquals(customer, result);
	}

	/**
	 * Test that fetching all customers returns a list with the expected size.
	 */
	@Test
	public void testGetAllCustomers() throws Exception {
		List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());
		when(customerDao.findAll(0, 5)).thenReturn(customers);
		List<Customer> result = personService.getAllCustomers(0, 5);
		assertEquals(3, result.size());
	}

	/**
	 * Test that deleting a customer correctly delegates the operation to the DAO.
	 */
	@Test
	public void testDeleteCustomer() throws Exception {
		Customer customer = new Customer();
		when(customerDao.findById(2L)).thenReturn(customer);
		personService.deleteCustomer(2L);
		verify(customerDao).delete(customer);
	}

	/**
	 * Test that updating a customer invokes the correct DAO method.
	 */
	@Test
	public void testUpdateCustomer() throws Exception {
		Customer customer = new Customer();
		personService.updateCustomer(customer);
		verify(customerDao).update(customer);
	}

	/**
	 * Test finding a customer by name and phone number returns the expected result.
	 */
	@Test
	public void testGetCustomerByNameandPhone() throws Exception {
		Customer customer = new Customer();
		when(customerDao.findByNameAndPhone("Alice", "0123456789")).thenReturn(customer);
		Customer result = personService.getCustomerByNameandPhone("Alice", "0123456789");
		assertEquals(customer, result);
	}

	/**
	 * Test finding a customer by phone number returns the correct customer.
	 */
	@Test
	public void testGetCustomerByPhone() throws Exception {
		Customer customer = new Customer();
		when(customerDao.findByPhone("9999999999")).thenReturn(customer);
		Customer result = personService.getCustomerByPhone("9999999999");
		assertEquals(customer, result);
	}

	// ======= Owner Tests =======

	/**
	 * Test that creating an owner delegates to the OwnerDao.
	 */
	@Test
	public void testCreateOwner() throws Exception {
		Owner owner = new Owner();
		personService.createOwner(owner);
		verify(ownerDao).create(owner);
	}

	/**
	 * Test fetching an owner by ID returns the expected owner.
	 */
	@Test
	public void testGetOwner() throws Exception {
		Owner owner = new Owner();
		when(ownerDao.findById(3L)).thenReturn(owner);
		Owner result = personService.getOwner(3L);
		assertEquals(owner, result);
	}

	/**
	 * Test fetching all owners returns the expected number of owners.
	 */
	@Test
	public void testGetAllOwner() throws Exception {
		List<Owner> owners = Arrays.asList(new Owner(), new Owner());
		when(ownerDao.findAll(1, 2)).thenReturn(owners);
		List<Owner> result = personService.getAllOwner(1, 2);
		assertEquals(2, result.size());
	}

	/**
	 * Test deleting an owner correctly uses the OwnerDao.
	 */
	@Test
	public void testDeleteOwner() throws Exception {
		Owner owner = new Owner();
		when(ownerDao.findById(3L)).thenReturn(owner);
		personService.deleteOwner(3L);
		verify(ownerDao).delete(owner);
	}

	/**
	 * Test that updating an owner delegates to the correct DAO method.
	 */
	@Test
	public void testUpdateOwner() throws Exception {
		Owner owner = new Owner();
		personService.updateOwner(owner);
		verify(ownerDao).update(owner);
	}

	/**
	 * Test that getting an owner by username returns the expected result.
	 */
	@Test
	public void testGetOwnerByUsername() throws Exception {
		Owner owner = new Owner();
		when(ownerDao.findByUsername("admin")).thenReturn(owner);
		Owner result = personService.getOwnerByUsername("admin");
		assertEquals(owner, result);
	}

	// ======= Email & Username Existence Tests =======

	/**
	 * Test that email existence is correctly checked in the EmployeeDao.
	 */
	@Test
	public void testExistsByEmail_WhenExistsInEmployee() throws Exception {
		when(employeeDao.findByEmail("email@example.com")).thenReturn(new Employee());
		boolean exists = personService.existsByEmail("email@example.com");
		assertTrue(exists);
	}

	/**
	 * Test that email existence is checked in OwnerDao if not found in EmployeeDao.
	 */
	@Test
	public void testExistsByEmail_WhenExistsInOwner() throws Exception {
		when(employeeDao.findByEmail("email@example.com")).thenReturn(null);
		when(ownerDao.findByEmail("email@example.com")).thenReturn(new Owner());
		boolean exists = personService.existsByEmail("email@example.com");
		assertTrue(exists);
	}

	/**
	 * Test that email is correctly reported as non-existent if missing in both
	 * DAOs.
	 */
	@Test
	public void testExistsByEmail_WhenNotExists() throws Exception {
		when(employeeDao.findByEmail("email@example.com")).thenReturn(null);
		when(ownerDao.findByEmail("email@example.com")).thenReturn(null);
		boolean exists = personService.existsByEmail("email@example.com");
		assertFalse(exists);
	}

	/**
	 * Test that username existence is correctly checked in EmployeeDao.
	 */
	@Test
	public void testExistsByUsername_WhenExistsInEmployee() throws Exception {
		when(employeeDao.findByUsername("user1")).thenReturn(new Employee());
		boolean exists = personService.existsByUsername("user1");
		assertTrue(exists);
	}

	/**
	 * Test that username existence is correctly checked in OwnerDao if not in
	 * EmployeeDao.
	 */
	@Test
	public void testExistsByUsername_WhenExistsInOwner() throws Exception {
		when(employeeDao.findByUsername("user1")).thenReturn(null);
		when(ownerDao.findByUsername("user1")).thenReturn(new Owner());
		boolean exists = personService.existsByUsername("user1");
		assertTrue(exists);
	}

	/**
	 * Test that username is reported as not existing if not found in either DAO.
	 */
	@Test
	public void testExistsByUsername_WhenNotExists() throws Exception {
		when(employeeDao.findByUsername("user1")).thenReturn(null);
		when(ownerDao.findByUsername("user1")).thenReturn(null);
		boolean exists = personService.existsByUsername("user1");
		assertFalse(exists);
	}
}
