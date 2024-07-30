package com.it.server.netty.handler;

import com.it.common.message.RPCRequest;
import com.it.common.message.RPCResponse;
import com.it.server.provider.ServiceProvider;
import com.it.server.rateLimit.RateLimit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
	private ServiceProvider serviceProvider;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RPCRequest request) throws Exception {
		//接收request，读取并调用服务
		RPCResponse response = getResponse(request);
		ctx.writeAndFlush(response);
		ctx.close();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	private RPCResponse getResponse(RPCRequest rpcRequest){
		//得到服务名
		String interfaceName=rpcRequest.getInterfaceName();
		//接口限流降级
		RateLimit rateLimit=serviceProvider.getRateLimitProvider().getRateLimit(interfaceName);
		if(!rateLimit.getToken()){
			//如果获取令牌失败，进行限流降级，快速返回结果
			System.out.println("服务限流！！");
			return RPCResponse.fail();
		}

		//得到服务端相应服务实现类
		Object service = serviceProvider.getService(interfaceName);
		//反射调用方法
		Method method=null;
		try {
			method= service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
			Object invoke=method.invoke(service,rpcRequest.getParams());
			return RPCResponse.sussess(invoke);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			System.out.println("方法执行错误");
			return RPCResponse.fail();
		}
	}
}
