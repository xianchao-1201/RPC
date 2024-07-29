package com.it.server;

import com.it.common.service.UserService;
import com.it.common.service.impl.UserServiceImpl;
import com.it.server.provider.ServiceProvider;
import com.it.server.rpcServer.RPCServer;
import com.it.server.rpcServer.impl.NettyRPCServer;
import com.it.server.rpcServer.impl.SimpleRPCServer;

public class TestServer {
	public static void main(String[] args) {
		UserService userService=new UserServiceImpl();

		ServiceProvider serviceProvider=new ServiceProvider("127.0.0.1",8888);
		serviceProvider.provideServiceInterface(userService);

		RPCServer rpcServer=new NettyRPCServer(serviceProvider);
		rpcServer.start(8888);
	}
}
