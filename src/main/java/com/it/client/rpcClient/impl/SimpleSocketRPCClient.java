package com.it.client.rpcClient.impl;

import com.it.client.rpcClient.RPCClient;
import com.it.common.message.RPCRequest;
import com.it.common.message.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleSocketRPCClient implements RPCClient {
	private String host;
	private int port;
	public SimpleSocketRPCClient(String host,int port){
		this.host=host;
		this.port=port;
	}
	@Override
	public RPCResponse sendRequest(RPCRequest request) {
		try {
			Socket socket=new Socket(host, port);
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());

			oos.writeObject(request);
			oos.flush();

			RPCResponse response=(RPCResponse) ois.readObject();
			return response;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
