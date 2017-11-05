package irc;

import jvn.SentenceInterceptor;

public interface SentenceInterface {
	@SentenceInterceptor(type="write")
	public void write(String text);
	
	@SentenceInterceptor(type="read")
	public String read();
}
