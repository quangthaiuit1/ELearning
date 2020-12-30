package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "plan")
public class Plan extends AbstractEntity {
	private String employee_code;
	private String department_code;
	private int year;

	private String employee_name;
	private String department_name;

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getEmployee_code() {
		return employee_code;
	}

	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDepartment_code() {
		return department_code;
	}

	public void setDepartment_code(String department_code) {
		this.department_code = department_code;
	}
}
