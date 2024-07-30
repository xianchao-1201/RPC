package com.it.client.proxy;

import com.it.client.IOClient;
import com.it.client.rpcClient.RPCClient;
import com.it.client.rpcClient.impl.NettyRPCClient;
import com.it.client.rpcClient.impl.SimpleSocketRPCClient;
import com.it.common.message.RPCRequest;
import com.it.common.message.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
	//传入参数service接口的class对象，反射封装成一个request

	private RPCClient rpcClient;
	public ClientProxy() throws InterruptedException {
		rpcClient = new NettyRPCClient();
	}
	//jdk动态代理，每一次代理对象调用方法，都会经过此方法增强（反射获取request对象，socket发送到服务端）
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//构建request
		RPCRequest request=RPCRequest.builder()
				.interfaceName(method.getDeclaringClass().getName())
				.methodName(method.getName())
				.params(args).paramsType(method.getParameterTypes()).build();
		//数据传输
		RPCResponse response= rpcClient.sendRequest(request);
		return response.getData();
	}
	public <T>T getProxy(Class<T> clazz){
		Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
		return (T)o;
	}
}
