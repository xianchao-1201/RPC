package com.it.server.provider;

import com.it.server.rateLimit.provider.RateLimitProvider;
import com.it.server.serviceRegister.ServiceRegister;
import com.it.server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

//本地服务存放器
public class ServiceProvider {
	private Map<String,Object> interfaceProvider;

	private int port;
	private String host;
	//注册服务类
	private ServiceRegister serviceRegister;
	//限流器
	private RateLimitProvider rateLimitProvider;
	public ServiceProvider(String host,int port){
		//需要传入服务端自身的网络地址
		this.host=host;
		this.port=port;
		this.interfaceProvider=new HashMap<>();
		this.serviceRegister=new ZKServiceRegister();
		this.rateLimitProvider=new RateLimitProvider();
	}

	public void provideServiceInterface(Object service,boolean canRetry){
		String serviceName=service.getClass().getName();
		Class<?>[] interfaceName=service.getClass().getInterfaces();

		for (Class<?> clazz:interfaceName){
			//本机的映射表
			interfaceProvider.put(clazz.getName(),service);
			//在注册中心注册服务
			serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port),canRetry);
		}
	}

	public Object getService(String interfaceName){
		return interfaceProvider.get(interfaceName);
	}

	public RateLimitProvider getRateLimitProvider(){
		return rateLimitProvider;
	}

}
