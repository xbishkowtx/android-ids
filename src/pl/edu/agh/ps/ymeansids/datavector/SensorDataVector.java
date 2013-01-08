package pl.edu.agh.ps.ymeansids.datavector;

public abstract class SensorDataVector implements DataVector {

	public static double minX;
	public static double minY;
	public static double minZ;
	public static double maxX;
	public static double maxY;
	public static double maxZ;

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

		if (v instanceof AccelerometerDataVector) {
			SensorDataVector s = (SensorDataVector) v;

			double normX = Math.abs(minX) + Math.abs(maxX);
			double normY = Math.abs(minY) + Math.abs(maxY);
			double normZ = Math.abs(minZ) + Math.abs(maxZ);

			double dx = (x - s.x) / normX;
			double dy = (y - s.y) / normY;
			double dz = (z - s.z) / normZ;

			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		} else {
			throw new IllegalArgumentException();
		}
	}
}
