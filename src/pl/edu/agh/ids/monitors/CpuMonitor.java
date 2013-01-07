package pl.edu.agh.ids.monitors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;

public class CpuMonitor extends Monitor {

	public static final String FILENAME = "cpu.csv";
	public static final String HEADER = "timestamp,user,system\n";
	private final Timer timer;

	public CpuMonitor(Service parentService, File dir) {
		super(parentService, dir);
		timer = new Timer();
	}

	@Override
	protected String getFileName() {
		return FILENAME;
	}

	@Override
	protected String getHeader() {
		return HEADER;
	}

	@Override
	public void start() {
		timer.scheduleAtFixedRate(new CpuTimer(), 0L, MONITOR_INTERVAL);
	}

	@Override
	public void stop() {
		timer.cancel();
		timer.purge();
		super.stop();
	}

	/**
	 * 
	 * @return integer Array with 4 elements: user, system, idle and other cpu
	 *         usage %.
	 */
	private int[] getCpuUsageStats() {

		String top = executeTop();

		top = top.replaceAll(",", "");
		top = top.replaceAll("User", "");
		top = top.replaceAll("System", "");
		top = top.replaceAll("IOW", "");
		top = top.replaceAll("IRQ", "");
		top = top.replaceAll("%", "");
		for (int i = 0; i < 10; i++) {
			top = top.replaceAll("  ", " ");
		}
		top = top.trim();
		String[] tmp = top.split(" ");
		int[] cpuUsageAsInt = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = tmp[i].trim();
			cpuUsageAsInt[i] = Integer.parseInt(tmp[i]);
		}
		return cpuUsageAsInt;
	}

	private String executeTop() {
		Process proc = null;
		BufferedReader in = null;
		String top = null;
		try {
			proc = Runtime.getRuntime().exec("top -n 1");
			in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while (top == null || top.contentEquals("")) {
				top = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				proc.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return top;
	}

	protected class CpuTimer extends TimerTask {

		@Override
		public void run() {
			long timestamp = System.currentTimeMillis();
			int[] cpustats = getCpuUsageStats();
			buffer.append(timestamp + "," + cpustats[0] + "," + cpustats[1] + "\n");
		}

	}

}
