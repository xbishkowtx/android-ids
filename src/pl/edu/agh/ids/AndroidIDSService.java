package pl.edu.agh.ids;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.ids.monitors.AccelerometerMonitor;
import pl.edu.agh.ids.monitors.CallMonitor;
import pl.edu.agh.ids.monitors.Monitor;
import pl.edu.agh.ids.monitors.OrientationMonitor;
import pl.edu.agh.ids.monitors.ProcessMonitor;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

public class AndroidIDSService extends Service {

	private PowerManager.WakeLock wakeLock;
	private File dir;
	private List<Monitor> monitors;

	@Override
	public void onCreate() {

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AndroidIDS");
			wakeLock.acquire();

			dir = new File(Environment.getExternalStorageDirectory(), "androidIDS");
			dir.mkdirs();

			initMonitors();
			for (Monitor monitor : monitors) {
				monitor.start();
			}

			Toast.makeText(this, R.string.remote_service_started, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, R.string.storage_unavailable, Toast.LENGTH_SHORT).show();
		}

	}

	private void initMonitors() {
		monitors = new ArrayList<Monitor>();
		monitors.add(new OrientationMonitor(this, dir));
		monitors.add(new AccelerometerMonitor(this, dir));
		monitors.add(new ProcessMonitor(this, dir));
		monitors.add(new CallMonitor(this, dir));
	}

	@Override
	public void onDestroy() {
		for (Monitor monitor : monitors) {
			monitor.flush();
			monitor.stop();
		}
		wakeLock.release();
		Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
