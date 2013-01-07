package pl.edu.agh.ymeans.datavector;

public class CpuDataVector implements DataVector {

	public final long timestamp;
	public final int user;
	public final int system;

	public CpuDataVector(long timestamp, int user, int system) {
		this.timestamp = timestamp;
		this.user = user;
		this.system = system;
	}

	@Override
	public double compare(DataVector v) {

		if (v instanceof CpuDataVector) {
			CpuDataVector c = (CpuDataVector) v;

			int dx = user - c.user;
			int dy = system - c.system;

			return Math.sqrt(dx * dx + dy * dy);
		} else {
			throw new IllegalArgumentException();
		}
	}

}
