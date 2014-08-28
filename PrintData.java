import java.util.Calendar;

public class PrintData {

	private static long ts_b, ts_e;
	
	private static boolean quiet;
	
	public static void initialize(boolean quiet) {
		PrintData.quiet = quiet;
	}
	
	public static void getInitialTime()
	{
		ts_b = Calendar.getInstance().getTimeInMillis();
	}
	
	public static void getFinalTime()
	{
		ts_e = Calendar.getInstance().getTimeInMillis();
	}
	
	public static void printInitialMatrix(double m[][], int n)
	{
		if(!quiet) {
			int i, j;
			System.out.println("n = " + n);
			System.out.println();
			System.out.println("Initial matrix:");
			for(i = 0; i < n; i++) {
				for(j = 0; j < n; j++)
					System.out.print(m[i][j] + " ");
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public static void printResult(double m[][], int n, double determinant)
	{
		int i, j;
		if(!quiet) {
			System.out.println("Matrix in triangular form:");
			for(i = 0; i < n; i++) {
				for(j = 0; j < n; j++)
					System.out.print(m[i][j] + " ");
				System.out.println();
			}
			System.out.println();
			System.out.println("Determinant: " + determinant);
			System.out.println();
			System.out.print("Time elapsed (in ms): ");
		}
		System.out.println(ts_e - ts_b);
	}
}
