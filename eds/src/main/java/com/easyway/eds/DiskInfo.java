package com.easyway.eds;

public class DiskInfo {
	private String serialNumber;
	private long totalSpace;
	private long freeSpace;

	public DiskInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DiskInfo(String serialNumber, long totalSpace, long freeSpace) {
		super();
		this.serialNumber = serialNumber;
		this.totalSpace = totalSpace;
		this.freeSpace = freeSpace;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public long getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}

	public long getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(long freeSpace) {
		this.freeSpace = freeSpace;
	}

	@Override
	public String toString() {
		return "DiskInfo [serialNumber=" + serialNumber + ", totalSpace=" + totalSpace + ", freeSpace=" + freeSpace
				+ "]";
	}

}
