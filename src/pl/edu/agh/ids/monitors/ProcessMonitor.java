package pl.edu.agh.ids.monitors;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;

public class ProcessMonitor extends Monitor {

	public static final String FILENAME = "proc.csv";
	public static final String HEADER = "timestamp,pid,uid,name\n";
	private final Timer timer;
	private List<ActivityManager.RunningAppProcessInfo> procInfo;
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
		timer.scheduleAtFixedRate(new procTimer(), 0L, MONITOR_INTERVAL);
	}

	@Override
	public void stop() {
		timer.cancel();
		timer.purge();
		super.stop();
	}

	protected class procTimer extends TimerTask {

		@Override
		public void run() {
			long timestamp = System.currentTimeMillis();
			procInfo = activityManager.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo proc : ProcessMonitor.this.procInfo) {
				buffer.append(timestamp + "," + proc.pid + "," + proc.uid + "," + proc.processName + "\n");
			}
		}

	}

}
