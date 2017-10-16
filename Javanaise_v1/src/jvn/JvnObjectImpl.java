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
		case RC:{
			state = JvnObjectState.R;
			break;
		}

		case WC:{
			state = JvnObjectState.RWC;
			break;
		}
		
		case NL:{
			object = JvnServerImpl.jvnGetServer().jvnLockRead(ID);
			state = JvnObjectState.R;
			break;
		}
		default:{
			throw new JvnException("Unreachable code reached.");
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
		case RC:{
			object =JvnServerImpl.jvnGetServer().jvnLockWrite(this.ID);
			state = JvnObjectState.W;
			break;
		}
		case W: {
			//Already okay
			break;
		}
		case RWC:{
			state = JvnObjectState.W;
			break;
		}
		case WC:{
			state = JvnObjectState.W;
			break;
		}
		default:{
			throw new JvnException("Unreachable code reached.");
		}
		}
	}

	synchronized public void jvnUnLock() throws JvnException {
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
			state = JvnObjectState.WC;
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

	synchronized public void jvnInvalidateReader() throws JvnException {
		switch(state){
		case R:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			state = JvnObjectState.NL;
			break;
		case RWC:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			state = JvnObjectState.NL;
			break;
		case RC:
			state = JvnObjectState.NL;
			break;
		default:
			break;
		
		}
		

	}

	synchronized public Serializable jvnInvalidateWriter() throws JvnException {
		switch(state){
		case RWC:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			state = JvnObjectState.NL;
			break;
		case W:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			state = JvnObjectState.NL;
			break;
		case WC:
			state = JvnObjectState.NL;
			break;
		default:
			break;
		
		}
		return object;
	}

	synchronized public Serializable jvnInvalidateWriterForReader() throws JvnException {
		switch(state){
		case RWC:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			state = JvnObjectState.R;

			break;
		case W:
			try{
				this.wait();
			}catch(InterruptedException e){
				
			}
			state = JvnObjectState.RC;

			break;
		case WC:
			state = JvnObjectState.RC;

		default:
			break;
		
		}
		return object;
	}

	public void setSerializable(Serializable s) throws JvnException {
		this.object = s;
	}

	public void jvnSetToNoLock() throws JvnException {
		state = JvnObjectState.NL;
	}

}
