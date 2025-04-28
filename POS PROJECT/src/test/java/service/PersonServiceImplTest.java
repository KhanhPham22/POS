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
    
    @BeforeEach
    public void setup() {
        employeeDao = mock(EmployeeDao.class);
        customerDao = mock(CustomerDao.class);
        ownerDao = mock(OwnerDao.class);
        personService = new PersonServiceImpl(employeeDao, customerDao, ownerDao);
    }
    
 // ======= Employee =======

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee();
        personService.createEmployee(employee);
        verify(employeeDao).create(employee);
    }

    @Test
    public void testGetEmployee() throws Exception {
        Employee employee = new Employee();
        when(employeeDao.findById(1L)).thenReturn(employee);
        Employee result = personService.getEmployee(1L);
        assertEquals(employee, result);
    }

//    @Test
//    public void testGetAllEmployees() throws Exception {
//        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
//        when(employeeDao.findAll()).thenReturn(employees);
//        List<Employee> result = personService.getAllEmployees();
//        assertEquals(2, result.size());
//    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Employee employee = new Employee();
        when(employeeDao.findById(1L)).thenReturn(employee);
        personService.deleteEmployee(1L);
        verify(employeeDao).delete(employee);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee employee = new Employee();
        personService.updateEmployee(employee);
        verify(employeeDao).update(employee);
    }

    @Test
    public void testGetEmployeeByUsername() throws Exception {
        Employee employee = new Employee();
        when(employeeDao.findByUsername("john")).thenReturn(employee);
        Employee result = personService.getEmployeeByUsername("john");
        assertEquals(employee, result);
    }
    
 // ======= Customer =======

    @Test
    public void testCreateCustomer() throws Exception {
        Customer customer = new Customer();
        personService.createCustomer(customer);
        verify(customerDao).create(customer);
    }

    @Test
    public void testGetCustomer() throws Exception {
        Customer customer = new Customer();
        when(customerDao.findById(2L)).thenReturn(customer);
        Customer result = personService.getCustomer(2L);
        assertEquals(customer, result);
    }

//    @Test
//    public void testGetAllCustomers() throws Exception {
//        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());
//        when(customerDao.findAll()).thenReturn(customers);
//        List<Customer> result = personService.getAllCustomers();
//        assertEquals(3, result.size());
//    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Customer customer = new Customer();
        when(customerDao.findById(2L)).thenReturn(customer);
        personService.deleteCustomer(2L);
        verify(customerDao).delete(customer);
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Customer customer = new Customer();
        personService.updateCustomer(customer);
        verify(customerDao).update(customer);
    }

    @Test
    public void testGetCustomerByNameandPhone() throws Exception {
        Customer customer = new Customer();
        when(customerDao.findByNameAndPhone("Alice", "0123456789")).thenReturn(customer);
        Customer result = personService.getCustomerByNameandPhone("Alice", "0123456789");
        assertEquals(customer, result);
    }
    
 // ======= Owner =======

    @Test
    public void testCreateOwner() throws Exception {
        Owner owner = new Owner();
        personService.createOwner(owner);
        verify(ownerDao).create(owner);
    }

    @Test
    public void testGetOwner() throws Exception {
        Owner owner = new Owner();
        when(ownerDao.findById(3L)).thenReturn(owner);
        Owner result = personService.getOwner(3L);
        assertEquals(owner, result);
    }

//    @Test
//    public void testGetAllOwners() throws Exception {
//        List<Owner> owners = Arrays.asList(new Owner(), new Owner());
//        when(ownerDao.findAll()).thenReturn(owners);
//        List<Owner> result = personService.getAllOwner();
//        assertEquals(2, result.size());
//    }

    @Test
    public void testDeleteOwner() throws Exception {
        Owner owner = new Owner();
        when(ownerDao.findById(3L)).thenReturn(owner);
        personService.deleteOwner(3L);
        verify(ownerDao).delete(owner);
    }

    @Test
    public void testUpdateOwner() throws Exception {
        Owner owner = new Owner();
        personService.updateOwner(owner);
        verify(ownerDao).update(owner);
    }

    @Test
    public void testGetOwnerByUsername() throws Exception {
        Owner owner = new Owner();
        when(ownerDao.findByUsername("admin")).thenReturn(owner);
        Owner result = personService.getOwnerByUsername("admin");
        assertEquals(owner, result);
    }
}
