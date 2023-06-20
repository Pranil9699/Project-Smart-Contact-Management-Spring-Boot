package com.smart.entities;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message = "Name Field is Required!")
	@Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters long")
	private String name;
	@Column(unique = true)
	@NotBlank(message = "Email Field is Required!")
	@Pattern(regexp = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$", message = "Please enter a valid email address")
	private String email;
	@Pattern(regexp = ".{6,}", message = "Password must be at least 6 characters long")
	private String password;

	private String role;
	private boolean enabled;
	private String imageURL;
	@Column(length = 500)
	private String about;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user" , orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageURL=" + imageURL + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

//	public User(int id, String name, String email, String password, String role, boolean enabled, String imageURL,
//			String about) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.email = email;
//		this.password = password;
//		this.role = role;
//		this.enabled = enabled;
//		this.imageURL = imageURL;
//		this.about = about;
//	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public User(int id,
//		@NotBlank(message = "Name Field is Required!") @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters long") String name,
//		@NotBlank(message = "Email Field is Required!") @Pattern(regexp = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$", message = "Please enter a valid email address") String email,
//		@NotBlank(message = "Password Field is Required!") @Pattern(regexp = "/^(?=[^a-z]*[a-z])(?=[^A-Z]*[A-Z])(?=\\D*\\d)(?=[^!#%]*[!#%])[A-Za-z0-9!#%]{8,32}$/", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character and be at least 8 characters long") String password,
//		String role, boolean enabled, String imageURL, String about, List<Contact> contacts) {
//	super();
//	this.id = id;
//	this.name = name;
//	this.email = email;
//	this.password = password;
//	this.role = role;
//	this.enabled = enabled;
//	this.imageURL = imageURL;
//	this.about = about;
//	this.contacts = contacts;
//}

	public List<Contact> getContacts() {
		return contacts;
	}

	public User(int id,
		@NotBlank(message = "Name Field is Required!") @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters long") String name,
		@NotBlank(message = "Email Field is Required!") @Pattern(regexp = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$", message = "Please enter a valid email address") String email,
		@Pattern(regexp = ".{6,}", message = "Password must be at least 6 characters long") String password,
		String role, boolean enabled, String imageURL, String about, List<Contact> contacts) {
	super();
	this.id = id;
	this.name = name;
	this.email = email;
	this.password = password;
	this.role = role;
	this.enabled = enabled;
	this.imageURL = imageURL;
	this.about = about;
	this.contacts = contacts;
}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

}
