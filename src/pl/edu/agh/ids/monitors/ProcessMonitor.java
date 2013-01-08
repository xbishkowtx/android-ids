package pl.edu.agh.ids.monitors;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Service;
import android.content.Context;

public class ProcessMonitor extends Monitor {

	public static final String FILENAME = "proc.csv";
	public static final String HEADER = "timestamp,procCount,availableMem\n";
	private final Timer timer;
	private ActivityManager activityManager;

	public ProcessMonitor(Service parentService, File dir) {
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
		timer.scheduleAtFixedRate(new ProcTimer(), 0L, MONITOR_INTERVAL);
	}

	@Override
	public void stop() {
		timer.cancel();
		timer.purge();
		super.stop();
	}

	protected class ProcTimer extends TimerTask {

		@Override
		public void run() {
			long timestamp = System.currentTimeMillis();
			int procCount = activityManager.getRunningAppProcesses().size();
			MemoryInfo memInfo = new MemoryInfo();
			activityManager.getMemoryInfo(memInfo);
			long freeMem = memInfo.availMem / (1024L * 1024L);
			buffer.append(timestamp + "," + procCount + "," + freeMem + "\n");
		}

	}

}
