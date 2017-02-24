package com.easyway.eds;

public class MemoryInfo {
	private int totalMemory;
	private int freeMemory;
	private int usedrate;

	public MemoryInfo() {
	}

	public MemoryInfo(int totalMemory, int freeMemory, int usedrate) {
		super();
		this.totalMemory = totalMemory;
		this.freeMemory = freeMemory;
		this.usedrate = usedrate;
	}

	public int getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(int totalMemory) {
		this.totalMemory = totalMemory;
	}

	public int getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(int freeMemory) {
		this.freeMemory = freeMemory;
	}

	public int getUsedrate() {
		return usedrate;
	}

	public void setUsedrate(int usedrate) {
		this.usedrate = usedrate;
	}

	@Override
	public String toString() {
		return "MemoryInfo [totalMemory=" + totalMemory + ", freeMemory=" + freeMemory + ", usedrate=" + usedrate + "]";
	}

}
