package pl.edu.agh.ymeans.datavector;

public class ProcDataVector implements DataVector {

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

			int dx = procCount - p.procCount;
			double dy = availMem - p.availMem;

			return Math.sqrt(dx * dx + dy * dy);
		} else {
			throw new IllegalArgumentException();
		}
	}

}
