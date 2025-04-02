package model;

import java.io.Serializable;


public class Person extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private long personId;
	private String personFirstName;
	private String personMiddleName;
	private String personLastName;
	private String personGender;
	private String dateOfBirth;
	private String phone;
	private String email;
	private String address;
	private String city;
	private String state;
	private String country;
	private boolean enabledFlag = true;

	// Constructor mặc định
	public Person() {
	}
}
