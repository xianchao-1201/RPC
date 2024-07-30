package com.it.client.proxy;

import com.it.client.IOClient;
import com.it.client.retry.guavaRetry;
import com.it.client.rpcClient.RPCClient;
import com.it.client.rpcClient.impl.NettyRPCClient;
import com.it.client.rpcClient.impl.SimpleSocketRPCClient;
import com.it.client.serviceCenter.ServiceCenter;
import com.it.client.serviceCenter.impl.ZKServiceCenter;
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
	private ServiceCenter serviceCenter;
	public ClientProxy() throws InterruptedException {
		serviceCenter=new ZKServiceCenter();
		rpcClient=new NettyRPCClient(serviceCenter);
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
		RPCResponse response;
		//后续添加逻辑：为保持幂等性，只对白名单上的服务进行重试
		if (serviceCenter.checkRetry(request.getInterfaceName())){
			//调用retry框架进行重试操作
			response=new guavaRetry().sendServiceWithRetry(request,rpcClient);
		}else {
			//只调用一次
			response= rpcClient.sendRequest(request);
		}
		return response.getData();
	}
	public <T>T getProxy(Class<T> clazz){
		Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
		return (T)o;
	}
}
