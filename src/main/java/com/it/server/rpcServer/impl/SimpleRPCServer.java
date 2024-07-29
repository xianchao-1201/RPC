package com.it.server.rpcServer.impl;

import com.it.server.provider.ServiceProvider;
import com.it.server.rpcServer.RPCServer;
import com.it.server.rpcServer.work.WorkThread;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRPCServer implements RPCServer {
	private ServiceProvider serviceProvide;
	@Override
	public void start(int port) {
		try {
			ServerSocket serverSocket=new ServerSocket(port);
			System.out.println("服务器启动了");
			while (true) {
				//如果没有连接，会堵塞在这里
				Socket socket = serverSocket.accept();
				//有连接，创建一个新的线程执行处理
				new Thread(new WorkThread(socket,serviceProvide)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
	}
}
