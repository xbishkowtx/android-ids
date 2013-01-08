package pl.edu.agh.ps.ymeansids.datavector;

public class CallDataVector implements DataVector {

	public static int max = 0;

	public final long timestamp;
	public final String number;

	public CallDataVector(long timestamp, String number) {
		this.timestamp = timestamp;
		this.number = number;
	}

	/**
	 * @return Levenshtein distance between two strings (phone numbers) / max
	 *         number length
	 */
	@Override
	public double compare(DataVector v) {
		if (v instanceof CallDataVector) {
			CallDataVector c = (CallDataVector) v;

			String s1 = number;
			String s2 = c.number;

			int[] costs = new int[s2.length() + 1];
			for (int i = 0; i <= s1.length(); i++) {
				int lastValue = i;
				for (int j = 0; j <= s2.length(); j++) {
					if (i == 0) {
						costs[j] = j;
					} else {
						if (j > 0) {
							int newValue = costs[j - 1];
							if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
								newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
							}
							costs[j - 1] = lastValue;
							lastValue = newValue;
						}
					}
				}
				if (i > 0) {
					costs[s2.length()] = lastValue;
				}
			}
			return costs[s2.length()] / max;

		} else {
			throw new IllegalArgumentException();
		}
	}

}
