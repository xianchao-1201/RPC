package com.it.server;

import com.it.common.service.UserService;
import com.it.common.service.impl.UserServiceImpl;
import com.it.server.provider.ServiceProvider;
import com.it.server.rpcServer.RPCServer;
import com.it.server.rpcServer.impl.SimpleRPCServer;

public class TestServer {
	public static void main(String[] args) {
		UserService userService=new UserServiceImpl();

		ServiceProvider serviceProvider=new ServiceProvider();
		serviceProvider.provideServiceInterface(userService);

		RPCServer rpcServer=new SimpleRPCServer(serviceProvider);
		rpcServer.start(9999);
	}
}
