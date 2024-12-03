package it.airdesk.airdesk_app.model.auth;

import java.time.LocalDate;
import java.util.Objects;

import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.dataTypes.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

@Entity
@Table(name = "host")
public class Host {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name field must not be blank")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "surname field must not be blank")
    @Column(nullable = false)
    private String surname;

    @NotBlank(message = "email field must not be blank")
    @Email(message = "email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @Past(message = "birth date must be in the past")
    private LocalDate birthDate;
    
    @Embedded
    @Column(nullable = true)
    private Address address;

    @ManyToOne(cascade = CascadeType.MERGE) // associazione unidirezionale, per cui non è necessario il mapping
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;
    
    /* COSTRUTTORE PUBLIC VUOTO */
    public Host() {
		super();
	}

    /* GETTERS, SETTERS, TOSTRING, EQUALS AND HASHCODE METHODS */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, birthDate, company, email, id, name, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Host other = (Host) obj;
		return Objects.equals(address, other.address) && Objects.equals(birthDate, other.birthDate)
				&& Objects.equals(company, other.company) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(surname, other.surname);
	}

	@Override
	public String toString() {
		return "Host [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", birthDate="
				+ birthDate + ", address=" + address + ", company=" + company + "]";
	}
	
}
