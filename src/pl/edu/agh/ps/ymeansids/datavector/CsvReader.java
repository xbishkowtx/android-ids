package pl.edu.agh.ps.ymeansids.datavector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

	public static final String ACCELEROMETER_FILENAME = "accelerometer.csv";
	public static final String ORIENTATION_FILENAME = "orientation.csv";
	public static final String PROC_FILENAME = "proc.csv";
	public static final String CPU_FILENAME = "cpu.csv";
	public static final String CALLS_FILENAME = "calls.csv";
	private static File dir;

	public static void setDirectory(String dir) {
		File file = new File(dir);
		if (file.exists() && file.isDirectory() && file.canRead()) {
			CsvReader.dir = file;
		}
	}

	public static List<DataVector> getAccelerometerData() {

		List<DataVector> data = new ArrayList<DataVector>();

		File file = new File(dir, ACCELEROMETER_FILENAME);

		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(file));
			in.readLine(); // header
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.trim().length() < 1) {
					continue;
				}
				String[] values = line.split(",");
				long timestamp = Long.parseLong(values[0]);
				double x = Double.parseDouble(values[1]);
				double y = Double.parseDouble(values[2]);
				double z = Double.parseDouble(values[3]);

				if (x < AccelerometerDataVector.minX) {
					AccelerometerDataVector.minX = x;
				} else if (x > AccelerometerDataVector.maxX) {
					AccelerometerDataVector.maxX = x;
				}

				if (y < AccelerometerDataVector.minY) {
					AccelerometerDataVector.minY = y;
				} else if (y > AccelerometerDataVector.maxY) {
					AccelerometerDataVector.maxY = y;
				}

				if (z < AccelerometerDataVector.minZ) {
					AccelerometerDataVector.minZ = z;
				} else if (z > AccelerometerDataVector.maxZ) {
					AccelerometerDataVector.maxZ = z;
				}

				data.add(new AccelerometerDataVector(timestamp, x, y, z));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;

	}

	public static List<DataVector> getOrientationData() {
		List<DataVector> data = new ArrayList<DataVector>();

		File file = new File(dir, ORIENTATION_FILENAME);

		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(file));
			in.readLine(); // header
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.trim().length() < 1) {
					continue;
				}
				String[] values = line.split(",");
				long timestamp = Long.parseLong(values[0]);
				double x = Double.parseDouble(values[1]);
				double y = Double.parseDouble(values[2]);
				double z = Double.parseDouble(values[3]);

				if (x < OrientationDataVector.minX) {
					OrientationDataVector.minX = x;
				} else if (x > OrientationDataVector.maxX) {
					OrientationDataVector.maxX = x;
				}

				if (y < OrientationDataVector.minY) {
					OrientationDataVector.minY = y;
				} else if (y > OrientationDataVector.maxY) {
					OrientationDataVector.maxY = y;
				}

				if (z < OrientationDataVector.minZ) {
					OrientationDataVector.minZ = z;
				} else if (z > OrientationDataVector.maxZ) {
					OrientationDataVector.maxZ = z;
				}

				data.add(new OrientationDataVector(timestamp, x, y, z));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	public static List<DataVector> getProcData() {
		List<DataVector> data = new ArrayList<DataVector>();

		File file = new File(dir, PROC_FILENAME);

		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(file));
			in.readLine(); // header
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.trim().length() < 1) {
					continue;
				}
				String[] values = line.split(",");
				long timestamp = Long.parseLong(values[0]);
				int procCount = Integer.parseInt(values[1]);
				if (procCount > ProcDataVector.maxProcCount) {
					ProcDataVector.maxProcCount = procCount;
				}
				long availMem = Long.parseLong(values[2]);
				if (availMem > ProcDataVector.maxAvailMem) {
					ProcDataVector.maxAvailMem = availMem;
				}
				data.add(new ProcDataVector(timestamp, procCount, availMem));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;

	}

	public static List<DataVector> getCpuData() {

		List<DataVector> data = new ArrayList<DataVector>();

		File file = new File(dir, CPU_FILENAME);

		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(file));
			in.readLine(); // header
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.trim().length() < 1) {
					continue;
				}
				String[] values = line.split(",");
				long timestamp = Long.parseLong(values[0]);
				int user = Integer.parseInt(values[1]);
				int system = Integer.parseInt(values[2]);

				data.add(new CpuDataVector(timestamp, user, system));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;

	}

	public static List<DataVector> getCallData() {

		List<DataVector> data = new ArrayList<DataVector>();

		File file = new File(dir, CALLS_FILENAME);

		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(file));
			in.readLine(); // header
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.trim().length() < 1) {
					continue;
				}
				String[] values = line.split(",");
				long timestamp = Long.parseLong(values[0]);

				String number = values[1];

				if (number.length() > CallDataVector.max) {
					CallDataVector.max = number.length();
				}

				data.add(new CallDataVector(timestamp, number));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;

	}

	public static void main(String[] args) {
		System.out.println(getAccelerometerData().size());
		System.out.println(getOrientationData().size());
		System.out.println(getProcData().size());
		System.out.println(getCpuData().size());
		System.out.println(getCallData().size());
	}

}
