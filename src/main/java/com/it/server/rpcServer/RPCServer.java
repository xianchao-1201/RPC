package com.it.server.rpcServer;

public interface RPCServer {
	//开启监听
	void start(int port);
	void stop();
}
