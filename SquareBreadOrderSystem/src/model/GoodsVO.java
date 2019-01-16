package model;

public class GoodsVO {
	private int no;
	private String g_name;
	private String g_unit;
	private String g_count;
	private String g_price;
	private String g_day;

	public GoodsVO() {
		super();
	}

	public GoodsVO(String g_name, String g_unit, String g_count, String g_price, String g_day) {
		super();
		this.g_name = g_name;
		this.g_unit = g_unit;
		this.g_count = g_count;
		this.g_price = g_price;
		this.g_day = g_day;
	}

	public GoodsVO(int no, String g_name, String g_unit, String g_count, String g_price, String g_day) {
		super();
		this.no = no;
		this.g_name = g_name;
		this.g_unit = g_unit;
		this.g_count = g_count;
		this.g_price = g_price;
		this.g_day = g_day;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getG_name() {
		return g_name;
	}

	public void setG_name(String g_name) {
		this.g_name = g_name;
	}

	public String getG_unit() {
		return g_unit;
	}

	public void setG_unit(String g_unit) {
		this.g_unit = g_unit;
	}

	public String getG_count() {
		return g_count;
	}

	public void setG_count(String g_count) {
		this.g_count = g_count;
	}

	public String getG_price() {
		return g_price;
	}

	public void setG_price(String g_price) {
		this.g_price = g_price;
	}

	public String getG_day() {
		return g_day;
	}

	public void setG_day(String g_day) {
		this.g_day = g_day;
	}

}
