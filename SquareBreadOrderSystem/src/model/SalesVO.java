package model;

public class SalesVO {
	private int no;
	private String p_count;
	private String p_price;
	private String p_day;

	public SalesVO() {
		super();
	}

	public SalesVO(String p_count, String p_price, String p_day) {
		super();
		this.p_count = p_count;
		this.p_price = p_price;
		this.p_day = p_day;
	}

	public SalesVO(int no, String p_count, String p_price, String p_day) {
		super();
		this.no = no;
		this.p_count = p_count;
		this.p_price = p_price;
		this.p_day = p_day;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getP_count() {
		return p_count;
	}

	public void setP_count(String p_count) {
		this.p_count = p_count;
	}

	public String getP_price() {
		return p_price;
	}

	public void setP_price(String p_price) {
		this.p_price = p_price;
	}

	public String getP_day() {
		return p_day;
	}

	public void setP_day(String p_day) {
		this.p_day = p_day;
	}

}
