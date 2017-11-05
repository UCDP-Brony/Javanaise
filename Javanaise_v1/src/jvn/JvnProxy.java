package jvn;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JvnProxy implements InvocationHandler{

	private JvnObject JObject;
	
	public JvnProxy(Serializable o, String objectName) throws JvnException{
		
		// initialize JVN
		JvnServerImpl js = JvnServerImpl.jvnGetServer();
				
		// look up the IRC object in the JVN server
		// if not found, create it, and register it in the JVN server
		JObject = js.jvnLookupObject(objectName);
		if (JObject == null) {
			JObject = js.jvnCreateObject(o);
			// after creation, I have a write lock on the object
			JObject.jvnUnLock();
			js.jvnRegisterObject(objectName, JObject);
		}
	}
	
	public static Object newInstance(Serializable o, String objectName) throws IllegalArgumentException, JvnException {
		return Proxy.newProxyInstance(o.getClass().getClassLoader(),o.getClass().getInterfaces(),new JvnProxy(o, objectName));
	}
	
	public Object invoke(Object o, Method m, Object[] argList) throws Throwable {
		if(m.isAnnotationPresent(SentenceInterceptor.class)) {
			if(m.getAnnotation(SentenceInterceptor.class).type().equals("read"))
				JObject.jvnLockRead();
			else if(m.getAnnotation(SentenceInterceptor.class).type().equals("write"))
				JObject.jvnLockWrite();
			else
				throw new JvnException("Unknown method");
		}
		Object temp = m.invoke(JObject.jvnGetObjectState(), argList);
		JObject.jvnUnLock();
		return temp;
	}
	
}
