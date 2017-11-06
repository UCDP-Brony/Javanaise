package test;

import java.util.Random;

import irc.Sentence;
import irc.SentenceInterface;
import jvn.JvnException;
import jvn.JvnProxy;
import jvn.JvnServerImpl;

public class DummyClient implements Runnable{
	
	private final static int NBREQUEST = 1000000;
	
	public void run(){
		SentenceInterface s = null;
		try {
			s = (SentenceInterface) JvnProxy.newInstance(new Sentence(), "IRC");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (JvnException e) {
			e.printStackTrace();
		}
		
		Random random = new Random();
		for(int i = 0; i <NBREQUEST; i++){

		    if(random.nextBoolean()){
		    	s.write("test");
		    }
		    else{
		    	s.read();
		    }
		}
		try {
			JvnServerImpl.jvnGetServer().jvnTerminate();
		} catch (JvnException e) {
			System.out.println("An error occured");
			e.printStackTrace();
		}
		
	}

}
