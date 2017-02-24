package com.easyway.eds;

public class HardwareInfoDTO {
	private CPUInfo cpuInfo;
	private DiskInfo diskInfo;
	private MemoryInfo memoryInfo;

	public HardwareInfoDTO() {
	}

	public HardwareInfoDTO(CPUInfo cpuInfo, DiskInfo diskInfo, MemoryInfo memoryInfo) {
		this.cpuInfo = cpuInfo;
		this.diskInfo = diskInfo;
		this.memoryInfo = memoryInfo;
	}

	public CPUInfo getCpuInfo() {
		return cpuInfo;
	}

	public void setCpuInfo(CPUInfo cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	public DiskInfo getDiskInfo() {
		return diskInfo;
	}

	public void setDiskInfo(DiskInfo diskInfo) {
		this.diskInfo = diskInfo;
	}

	public MemoryInfo getMemoryInfo() {
		return memoryInfo;
	}

	public void setMemoryInfo(MemoryInfo memoryInfo) {
		this.memoryInfo = memoryInfo;
	}

}
