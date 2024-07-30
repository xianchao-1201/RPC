package com.it.server.netty.nettyInitializer;

import com.it.common.serializer.myCode.MyDeCoder;
import com.it.common.serializer.myCode.MyEnCoder;
import com.it.common.serializer.mySerializer.JsonSerializer;
import com.it.server.netty.handler.NettyRPCServerHandler;
import com.it.server.provider.ServiceProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
	private ServiceProvider serviceProvider;
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//使用自定义的编/解码器
		pipeline.addLast(new MyEnCoder(new JsonSerializer()));
		pipeline.addLast(new MyDeCoder());
		pipeline.addLast(new NettyRPCServerHandler(serviceProvider));
	}
}
