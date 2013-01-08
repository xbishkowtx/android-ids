package pl.edu.agh.ps.ymeansids.datavector;

public class ProcDataVector implements DataVector {

	public static int maxProcCount = 0;
	public static long maxAvailMem = 0L;

	public final long timestamp;
	public final int procCount;
	public final long availMem;

	public ProcDataVector(long timestamp, int procCount, long availMem) {
		this.timestamp = timestamp;
		this.procCount = procCount;
		this.availMem = availMem;
	}

	@Override
	public double compare(DataVector v) {

		if (v instanceof ProcDataVector) {
			ProcDataVector p = (ProcDataVector) v;

			double dx = (procCount - p.procCount) / maxProcCount;
			double dy = (availMem - p.availMem) / maxAvailMem;

			return Math.sqrt(dx * dx + dy * dy);
		} else {
			throw new IllegalArgumentException();
		}
	}

}
