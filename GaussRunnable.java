import java.util.concurrent.locks.ReentrantLock;

public class GaussRunnable implements Runnable {
	
	double m[][];
	int n, currentRow, k, thread_count;
	ReentrantLock[] rl;
	
	public GaussRunnable(double m[][], int n, int currentRow, ReentrantLock[] rl, int csSize, int thread_count) {
		
		this.m = m;
		this.n = n;
		this.currentRow = currentRow;
		this.rl = rl;
		this.k = csSize;
		this.thread_count = thread_count;
	}
	
	public void run() {
		
		final int STEP = 4;
		int i, j;
		double c;
		
		if(currentRow == n)
		{
			double determinant = Determinant.calculate(m, n);
			PrintData.getFinalTime();
			PrintData.printResult(m, n, determinant);
			return;
		}
		
		rl[currentRow / k].lock();
		for(i = currentRow + 1; i < n; i++) {
			
			if(i % k == 0) {
				rl[i / k - 1].unlock();
				rl[i / k].lock();
			}
			
			c = -m[i][currentRow] / m[currentRow][currentRow];
			m[i][currentRow] = 0;
			for(j = currentRow + 1; j < n; j++)
				m[i][j] += c * m[currentRow][j];
			
			if(i == currentRow + STEP) {
				if(currentRow + 1 >= thread_count && thread_count > 1)
					try {
						ThreadPool.tr[(currentRow + 1) % thread_count].join();
					} catch (InterruptedException e) {
					}
				GaussRunnable r = new GaussRunnable(m, n, currentRow + 1, rl, k, thread_count);
				ThreadPool.tr[(currentRow + 1) % thread_count] = new Thread(r);
				ThreadPool.tr[(currentRow + 1) % thread_count].start();
			}
			
		}
		
		rl[(n - 1) / k].unlock();

		if(n - 1 < currentRow + STEP) {
			if(currentRow + 1 >= thread_count && thread_count > 1)
				try {
					ThreadPool.tr[(currentRow + 1) % thread_count].join();
				} catch (InterruptedException e) {
				}
			GaussRunnable r = new GaussRunnable(m, n, currentRow + 1, rl, k, thread_count);
			ThreadPool.tr[(currentRow + 1) % thread_count] = new Thread(r);
			ThreadPool.tr[(currentRow + 1) % thread_count].start();
		}
	}
}
