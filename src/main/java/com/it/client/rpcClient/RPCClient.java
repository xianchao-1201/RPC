package com.it.client.rpcClient;

import com.it.common.message.RPCRequest;
import com.it.common.message.RPCResponse;

public interface RPCClient {
	//定义底层通信的方法
	RPCResponse sendRequest(RPCRequest request);
}
