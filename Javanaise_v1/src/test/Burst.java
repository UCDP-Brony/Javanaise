package test;



public class Burst {
	
	private final static int NBCLIENT = 5;
	
	public static void main(String argv[]) {
		for(int i = 0; i < NBCLIENT; i++){
			Thread t = new Thread(new DummyClient());
			t.run();
		}
	}
}
