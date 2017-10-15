package jvn;

import java.io.Serializable;

public class JvnObjectImpl implements JvnObject  {
	
	private Serializable object;
	private JvnObjectState state;
	private int ID;
	
	
	public JvnObjectImpl(Serializable o, int ID){
		object = o;
		state = JvnObjectState.W;
		this.ID = ID;
	}

	public void jvnLockRead() throws JvnException {
		switch(state){
		case RWC:{
			break;
		}
		case W:{
			//Nothing to be done
			break;
		}
		case WC:{
			state = JvnObjectState.RWC;
			break;
		}
		default:{
			object = JvnServerImpl.jvnGetServer().jvnLockRead(ID);
			state = JvnObjectState.R;
			break;
		}
		}
	}

	public void jvnLockWrite() throws JvnException {
		switch(state){
		
		case NL:
			object =JvnServerImpl.jvnGetServer().jvnLockWrite(this.ID);
			state = JvnObjectState.W;
			break;
		case R:{
			object =JvnServerImpl.jvnGetServer().jvnLockWrite(this.ID);
			state = JvnObjectState.W;
			break;
		}
		case W: {
			//Already okay
			break;
		}
		case RWC:{
			//Already okay
			break;
		}
		default:
			state = JvnObjectState.W;
			break;
		}
	}

	public void jvnUnLock() throws JvnException {
		switch(state){
		case NL:{
			throw new JvnException("No lock to unlock.");
		}
		case RC: {
			throw new JvnException("Read already unlocked.");
		}
		case WC:{
			throw new JvnException("Write already unlocked.");
		}
		case R:{
			state = JvnObjectState.RC;
			break;
		}
		case W:{
			state = JvnObjectState.WC;
			break;
		}
		case RWC:{
			break;
		}
		default:{
			throw new JvnException("Unreachable code.");
		}
		}
		
		this.notify();
	}

	public int jvnGetObjectId() throws JvnException {
		return this.ID;
	}

	public Serializable jvnGetObjectState() throws JvnException {
		return object;
	}

	public void jvnInvalidateReader() throws JvnException {
		switch(state){
		case R:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			break;
		case RC:
			break;
		case RWC:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			break;
		default:
			break;
		
		}
		state = JvnObjectState.NL;

	}

	public Serializable jvnInvalidateWriter() throws JvnException {
		switch(state){
		case RWC:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			break;
		case W:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			break;
		default:
			break;
		
		}
		state = JvnObjectState.NL;
		return object;
	}

	public Serializable jvnInvalidateWriterForReader() throws JvnException {
		switch(state){
		case RWC:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			break;
		case W:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			break;
		default:
			break;
		
		}
		state = JvnObjectState.NL;
		return object;
	}

	public void setSerializable(Serializable s) throws JvnException {
		this.object = s;
	}

}
