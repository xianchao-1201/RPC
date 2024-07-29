package com.it.server.rpcServer.work;

import com.it.common.message.RPCRequest;
import com.it.common.message.RPCResponse;
import com.it.server.provider.ServiceProvider;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable{
	private Socket socket;
	private ServiceProvider serviceProvide;
	@Override
	public void run() {
		try {
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
			//读取客户端传过来的request
			RPCRequest rpcRequest = (RPCRequest) ois.readObject();
			//反射调用服务方法获取返回值
			RPCResponse rpcResponse=getResponse(rpcRequest);
			//向客户端写入response
			oos.writeObject(rpcResponse);
			oos.flush();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private RPCResponse getResponse(RPCRequest rpcRequest){
		//得到服务名
		String interfaceName=rpcRequest.getInterfaceName();
		//得到服务端相应服务实现类
		Object service = serviceProvide.getService(interfaceName);
		//反射调用方法
		Method method=null;
		try {
			method= service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
			Object invoke=method.invoke(service,rpcRequest.getParams());
			return RPCResponse.sussess(invoke);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			System.out.println("方法执行错误");
			return RPCResponse.fail();
		}
	}
}
