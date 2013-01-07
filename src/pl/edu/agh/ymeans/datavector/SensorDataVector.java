package pl.edu.agh.ymeans.datavector;

public class SensorDataVector implements DataVector {

	public final long timestamp;
	public final double x;
	public final double y;
	public final double z;

	public SensorDataVector(long timestamp, double x, double y, double z) {
		this.timestamp = timestamp;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public double compare(DataVector v) {

		if (v instanceof SensorDataVector) {
			SensorDataVector s = (SensorDataVector) v;

			double dx = x - s.x;
			double dy = y - s.y;
			double dz = z - s.z;

			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		} else {
			throw new IllegalArgumentException();
		}
	}

}
