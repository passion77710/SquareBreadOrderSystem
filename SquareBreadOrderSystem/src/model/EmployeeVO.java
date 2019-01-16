package model;

public class EmployeeVO {
	private int no;
	private String j_name;
	private String j_gender;
	private String j_age;
	private String j_phone;
	private String j_addr;
	private String j_day;
	private String j_deleteday;

	public EmployeeVO() {
		super();
	}

	public EmployeeVO(String j_name, String j_gender, String j_age, String j_phone, String j_addr, String j_day,
			String j_deleteday) {
		super();
		this.j_name = j_name;
		this.j_gender = j_gender;
		this.j_age = j_age;
		this.j_phone = j_phone;
		this.j_addr = j_addr;
		this.j_day = j_day;
		this.j_deleteday = j_deleteday;
	}

	public EmployeeVO(int no, String j_name, String j_gender, String j_age, String j_phone, String j_addr, String j_day,
			String j_deleteday) {
		super();
		this.no = no;
		this.j_name = j_name;
		this.j_gender = j_gender;
		this.j_age = j_age;
		this.j_phone = j_phone;
		this.j_addr = j_addr;
		this.j_day = j_day;
		this.j_deleteday = j_deleteday;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getJ_name() {
		return j_name;
	}

	public void setJ_name(String j_name) {
		this.j_name = j_name;
	}

	public String getJ_gender() {
		return j_gender;
	}

	public void setJ_gender(String j_gender) {
		this.j_gender = j_gender;
	}

	public String getJ_age() {
		return j_age;
	}

	public void setJ_age(String j_age) {
		this.j_age = j_age;
	}

	public String getJ_phone() {
		return j_phone;
	}

	public void setJ_phone(String j_phone) {
		this.j_phone = j_phone;
	}

	public String getJ_addr() {
		return j_addr;
	}

	public void setJ_addr(String j_addr) {
		this.j_addr = j_addr;
	}

	public String getJ_day() {
		return j_day;
	}

	public void setJ_day(String j_day) {
		this.j_day = j_day;
	}

	public String getJ_deleteday() {
		return j_deleteday;
	}

	public void setJ_deleteday(String j_deleteday) {
		this.j_deleteday = j_deleteday;
	}

}
