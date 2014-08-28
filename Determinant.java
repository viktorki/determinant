public class Determinant {
	
	public static double calculate(double m[][], int n) {
		
		int i;
		double determinant = 1;
		
		for(i = 0; i < n; i++)
			determinant *= m[i][i];
		
		return determinant;
		
	}
}
