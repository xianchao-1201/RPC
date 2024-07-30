package com.it.client.netty.nettyInitializer;

import com.it.client.netty.handler.NettyClientHandler;
import com.it.common.serializer.myCode.MyDeCoder;
import com.it.common.serializer.myCode.MyEnCoder;
import com.it.common.serializer.mySerializer.JsonSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//使用自定义的编/解码器
		pipeline.addLast(new MyDeCoder());
		pipeline.addLast(new MyEnCoder(new JsonSerializer()));
		pipeline.addLast(new NettyClientHandler());
	}
}
