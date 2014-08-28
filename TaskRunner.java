import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TaskRunner {

	static final int MAX_RANDOM_NUM = 10;
	
	public static void main(String[] args) throws ParseException {
		
		Options options = new Options();
		options.addOption("n", true, "Dimensions of the matrix");
		options.addOption("t", "tasks", true, "Thread count");
		options.addOption("q", false, "Quiet mode");
		CommandLineParser commandLineParser = new GnuParser();
		CommandLine commandLine = commandLineParser.parse(options, args);
		
		int el_count = Integer.parseInt(commandLine.getOptionValue("n"));
		int thread_count = Integer.parseInt(commandLine.getOptionValue("t"));
		boolean quiet = commandLine.hasOption("q");
		
		PrintData.initialize(quiet);
		
		int i, j, criticalSectionsCount;
		
		int criticalSectionSize = el_count / thread_count;
	
		if(el_count % thread_count == 0)
			criticalSectionsCount = thread_count;
		else
			criticalSectionsCount = thread_count + 1;
				
		ReentrantLock[] rl = new ReentrantLock[criticalSectionsCount];
	 	for(i = 0; i < criticalSectionsCount; i++)
	 		rl[i] = new ReentrantLock();
		
		double m[][] = new double[el_count][el_count];
		
		ThreadPool.tr = new Thread[thread_count];
		
		for(i = 0; i < el_count; i++)
			for(j = 0; j < el_count; j++)
				m[i][j] = 2 * (Math.random() - 0.5) * MAX_RANDOM_NUM;
		
		/*for(i = 0; i < el_count - 1; i++)
			for(j = i + 1; j < el_count; j++) {
				double c = -m[j][i] / m[i][i];
				m[j][i] = 0;
				for(int k = i + 1; k < el_count; k++)
					m[j][k] += c * m[i][k];
			}
		double det = 1;
		for(i = 0; i < el_count; i++)
			det *= m[i][i];
		System.out.println("Determinant: " + det);*/
		
		PrintData.printInitialMatrix(m, el_count);
		
		PrintData.getInitialTime();
		
		GaussRunnable r = new GaussRunnable(m, el_count, 0, rl, criticalSectionSize, thread_count);
		ThreadPool.tr[0] = new Thread(r);
		ThreadPool.tr[0].start();
	}
}
