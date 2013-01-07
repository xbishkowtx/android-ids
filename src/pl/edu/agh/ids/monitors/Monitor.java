package pl.edu.agh.ids.monitors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.util.Log;

public abstract class Monitor {

	public static final long FLUSH_INTERVAL = 60L * 1000L;
	public static final long MONITOR_INTERVAL = 10L * 1000L;

	protected final File csv;
	protected final Service parentService;
	protected final StringBuffer buffer;
	protected final Timer flushTimer;

	public Monitor(Service parentService, File dir) {
		this.parentService = parentService;
		csv = new File(dir, getFileName());
		if (!csv.exists()) {
			Log.i("androidIDS", "CSV nonexistent");
			writeHeader();
		}
		buffer = new StringBuffer();
		flushTimer = new Timer();
	}

	protected abstract String getFileName();

	protected abstract String getHeader();

	public abstract void start();

	public void stop() {
		flushTimer.cancel();
		flushTimer.purge();
	}

	protected void writeHeader() {
		FileWriter writer = null;
		try {
			csv.createNewFile();
			writer = new FileWriter(csv);
			writer.append(getHeader());
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public synchronized void flush() {
		FileWriter writer = null;
		try {
			byte[] rawData = buffer.toString().getBytes("UTF-8");
			String data = new String(rawData, "UTF-8");
			buffer.setLength(0);
			writer = new FileWriter(csv, true);
			writer.append(data);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	protected void scheduleFlushTask() {
		flushTimer.scheduleAtFixedRate(new FlushTask(), FLUSH_INTERVAL, FLUSH_INTERVAL);
	}

	protected class FlushTask extends TimerTask {

		@Override
		public void run() {
			Monitor.this.flush();
		}

	}
}