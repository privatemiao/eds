package com.easyway.eds;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;

import com.sun.management.OperatingSystemMXBean;

public class Service {
	private static Service service = new Service();
	OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	private Service() {
	};

	public static Service getInstance() {
		return service;
	}

	public MemoryInfo getMemoryInfo() {
		double m = 1024 * 1024;
		long totalMemory = osmxb.getTotalPhysicalMemorySize();
		long freeMemory = osmxb.getFreePhysicalMemorySize();
		int rate = (int) (((new Double(totalMemory) - new Double(freeMemory)) / new Double(totalMemory)) * 100);
		int total = (int) (totalMemory / m);
		int free = (int) (freeMemory / m);
		return new MemoryInfo(total, free, rate);
	}

	public CPUInfo getCPUInfo() {
		CPUInfo info = new CPUInfo();
		info.setUsedrate(getCpuRatio());
		info.setQuantity(osmxb.getAvailableProcessors());
		return info;
	}

	private int getCpuRatio() {
		String[] cpuRate = new String[2];
		int CPUTIME = 500;
		int PERCENT = 100;
		try {
			String procCmd = "wmic process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
			long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
			if (c0 != null && c1 != null) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				cpuRate[1] = Double.valueOf(PERCENT * (busytime) * 1.0 / (busytime + idletime)).intValue() + "";
				return Double.valueOf(PERCENT * (busytime) * 1.0 / (busytime + idletime)).intValue();
			} else {
				return 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	private long[] readCpu(final Process proc) {
		int FAULTLENGTH = 10;
		long[] retn = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < FAULTLENGTH) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0;
			long kneltime = 0;
			long usertime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}
				String caption = substring(line, capidx, cmdidx - 1).trim();
				String cmd = substring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}
				String s1 = substring(line, kmtidx, rocidx - 1).trim();
				String s2 = substring(line, umtidx, wocidx - 1).trim();
				if (caption.equals("System Idle Process") || caption.equals("System")) {
					if (s1.length() > 0)
						idletime += Long.valueOf(s1).longValue();
					if (s2.length() > 0)
						idletime += Long.valueOf(s2).longValue();
					continue;
				}
				if (s1.length() > 0)
					kneltime += Long.valueOf(s1).longValue();
				if (s2.length() > 0)
					s2 = s2.replace(" ", "");
				usertime += Long.valueOf(s2).longValue();
			}
			retn[0] = idletime;
			retn[1] = kneltime + usertime;
			return retn;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String substring(String src, int start_idx, int end_idx) {
		byte[] b = src.getBytes();
		String tgt = "";
		for (int i = start_idx; i <= end_idx; i++) {
			tgt += (char) b[i];
		}
		return tgt;
	}

	public DiskInfo getDiskInfo() {
		File[] roots = File.listRoots();
		int m = 1024 * 1024;
		long totalSpace = 0;
		long freeSpace = 0;
		for (File file : roots) {
			totalSpace += file.getTotalSpace();
			freeSpace += file.getFreeSpace();
		}
		totalSpace = totalSpace / m / 1024;
		freeSpace = freeSpace / m / 1024;
		return new DiskInfo(getDiskSerialNumber(), totalSpace, freeSpace);
	}

	private String getDiskSerialNumber() {
		Process process = null;
		BufferedReader reader = null;
		try {
			process = Runtime.getRuntime().exec("wmic diskdrive get serialnumber");
			reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
			String line = null;
			while (null != (line = reader.readLine())) {
				line = line.trim();
				if (line.length() == 0 || "SerialNumber".equals(line)) {
					continue;
				}
				return line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
			try {
				if (process != null) {
					process.destroy();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}

	public HardwareInfoDTO getHardwareInfo() {
		return new HardwareInfoDTO(Service.getInstance().getCPUInfo(), Service.getInstance().getDiskInfo(),
				Service.getInstance().getMemoryInfo());
	}

}
