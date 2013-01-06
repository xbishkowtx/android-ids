package pl.edu.agh.ids.monitors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;

public abstract class Monitor {

	protected final long INTERVAL = 60L * 1000L;

	protected final File csv;
	protected final Service parentService;
	protected final StringBuffer buffer;

	public Monitor(Service parentService, File dir) {
		this.parentService = parentService;
		this.csv = new File(dir, getFileName());
		if (!this.csv.exists()) {
			writeHeader();
		}
		buffer = new StringBuffer();
	}

	protected abstract String getFileName();

	protected abstract String getHeader();

	public abstract void start();

	public abstract void stop();

	protected void writeHeader() {
		FileWriter writer = null;
		try {
			csv.createNewFile();
			writer = new FileWriter(csv);
			writer.append("test\n");
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
			writer = new FileWriter(csv);
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
		Timer timer = new Timer();
		TimerTask task = new FlushTask();
		timer.scheduleAtFixedRate(task, INTERVAL, INTERVAL);
	}

	protected class FlushTask extends TimerTask {

		@Override
		public void run() {
			Monitor.this.flush();
		}

	}
}