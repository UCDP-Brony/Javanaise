package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Burst {
	
	private final static int NBCLIENT = 15;
	
	public static void main(String argv[]) {
		System.out.println("Starting burst mode, launching "+NBCLIENT+" clients");
		ExecutorService es = Executors.newCachedThreadPool();
		for(int i = 0; i < NBCLIENT; i++){
			es.execute(new DummyClient());
		}
		es.shutdown();
		try {
			boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Finished all threads successfully");
		System.exit(0);
	}
	
}
