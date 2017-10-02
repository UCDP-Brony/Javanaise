package jvn;

import java.io.Serializable;

public class JvnObjectImpl implements JvnObject {
	
	private Serializable object;
	private JvnObjectState state;
	
	
	public JvnObjectImpl(Serializable o){
		object = o;
		state = JvnObjectState.W;
	}

	public void jvnLockRead() throws JvnException {
		switch(state){
		case RWC:{
			break;
		}
		case W:{
			//TODO
			break;
		}
		case WC:{
			state = JvnObjectState.RWC;
		}
		default:{
			state = JvnObjectState.R;
			break;
		}
		}
	}

	public void jvnLockWrite() throws JvnException {
		switch(state){
		case R:{
			//TODO
			break;
		}
		case W: {
			//TODO
			break;
		}
		case RWC:{
			//TODO
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
	}

	public int jvnGetObjectId() throws JvnException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Serializable jvnGetObjectState() throws JvnException {
		return object;
	}

	public void jvnInvalidateReader() throws JvnException {
		// TODO Auto-generated method stub

	}

	public Serializable jvnInvalidateWriter() throws JvnException {
		// TODO Auto-generated method stub
		return null;
	}

	public Serializable jvnInvalidateWriterForReader() throws JvnException {
		// TODO Auto-generated method stub
		return null;
	}

}
