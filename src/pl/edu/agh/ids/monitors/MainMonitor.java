package pl.edu.agh.ids.monitors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.DetailedState;
import android.net.TrafficStats;

public class MainMonitor extends Monitor {

	public static final String FILENAME = "data.csv";
	public static final String HEADER = "timestamp,procCount,availableMem,cpuUser,cpuSystem,mobileRxBytes,mobileRxPackets,mobileTxBytes,mobileTxPackets,allRxBytes,allRxPackets,allTxBytes,allTxPackets,wifiState\n";
	private final Timer timer;
	private ActivityManager activityManager;
	ConnectivityManager connectivityManager;

	public MainMonitor(Service parentService, File dir) {
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
		activityManager = (ActivityManager) parentService.getSystemService(Context.ACTIVITY_SERVICE);
		connectivityManager = (ConnectivityManager) parentService.getSystemService(Context.CONNECTIVITY_SERVICE);

		timer.scheduleAtFixedRate(new MainTimer(), 0L, MONITOR_INTERVAL);
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

	protected class MainTimer extends TimerTask {

		@Override
		public void run() {
			long timestamp = System.currentTimeMillis();
			double procCount = activityManager.getRunningAppProcesses().size();
			MemoryInfo memInfo = new MemoryInfo();
			activityManager.getMemoryInfo(memInfo);
			double freeMem = memInfo.availMem / (1024L * 1024L);

			int[] cpustats = getCpuUsageStats();
			buffer.append(timestamp + "," + cpustats[0] + "," + cpustats[1] + "\n");

			double mobileRxBytes = TrafficStats.getMobileRxBytes();
			double mobileRxPackets = TrafficStats.getMobileRxPackets();
			double mobileTxBytes = TrafficStats.getMobileTxBytes();
			double mobileTxPackets = TrafficStats.getMobileTxPackets();
			double allRxBytes = TrafficStats.getMobileRxBytes();
			double allRxPackets = TrafficStats.getMobileRxPackets();
			double allTxBytes = TrafficStats.getMobileTxBytes();
			double allTxPackets = TrafficStats.getMobileTxPackets();

			DetailedState state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getDetailedState();
			double wifiState = state.ordinal();

			buffer.append(timestamp + "," + procCount + "," + freeMem + "," + cpustats[0] + "," + cpustats[1] + "," + mobileRxBytes + "," + mobileRxPackets + "," + mobileTxBytes + "," + mobileTxPackets + "," + allRxBytes + "," + allRxPackets + "," + allTxBytes + "," + allTxPackets + "," + wifiState + "\n");
		}
	}

}
