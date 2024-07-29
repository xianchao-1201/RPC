package com.it.client;

import com.it.common.message.RPCRequest;
import com.it.common.message.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {
	//这里负责底层与服务端的通信，发送request，返回response
	public static RPCResponse sendRequest(String host, int port, RPCRequest request){
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
