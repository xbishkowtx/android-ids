package pl.edu.agh.ids.monitors;

import java.io.File;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationMonitor extends Monitor implements SensorEventListener {

	public static final String FILENAME = "orientation.csv";
	public static final String HEADER = "timestamp,x,y,z\n";

	private SensorManager sensorManager;
	private Sensor orientation;

	public OrientationMonitor(Service parentService, File dir) {
		super(parentService, dir);
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
		sensorManager = (SensorManager) parentService.getSystemService(Context.SENSOR_SERVICE);
		orientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, orientation, SensorManager.SENSOR_DELAY_NORMAL);
		scheduleFlushTask();

	}

	@Override
	public void stop() {
		sensorManager.unregisterListener(this);
		super.stop();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		buffer.append(System.currentTimeMillis() + "," + x + "," + y + "," + z + "\n");

	}

}
