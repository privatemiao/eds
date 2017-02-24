package com.easyway.eds;

public class CPUInfo {
	private int usedrate;
	private int quantity;

	public CPUInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CPUInfo(int usedrate, int quantity) {
		super();
		this.usedrate = usedrate;
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getUsedrate() {
		return usedrate;
	}

	public void setUsedrate(int usedrate) {
		this.usedrate = usedrate;
	}

	@Override
	public String toString() {
		return "CPUInfo [usedrate=" + usedrate + ", quantity=" + quantity + "]";
	}

}
