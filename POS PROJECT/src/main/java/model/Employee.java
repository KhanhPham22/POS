package model;

import java.io.Serializable;
import java.util.UUID;


public class Employee extends Person implements Serializable {

	private static final long serialVersionUID = 1L;

	private String employeeNumber;
	private String employeeType;
	private String description;
	private String loginUsername;
	private String loginPassword;

	public Employee() {
	}

	// Thay thế @PrePersist bằng phương thức gọi thủ công trong Service/DAO
	public void generateEmployeeNumber() {
		this.employeeNumber = "EMP-" + UUID.randomUUID().toString();
	}
}
