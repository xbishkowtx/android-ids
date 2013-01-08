package pl.edu.agh.ps.ymeansids.datavector;

public class CpuDataVector implements DataVector {

	private static int maxUser = 0;
	private static int maxSystem = 0;

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

			double dx = (user - c.user) / maxUser;
			double dy = (system - c.system) / maxSystem;

			return Math.sqrt(dx * dx + dy * dy);
		} else {
			throw new IllegalArgumentException();
		}
	}

}
