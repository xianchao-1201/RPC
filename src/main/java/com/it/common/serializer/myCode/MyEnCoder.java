package com.it.common.serializer.myCode;

import com.it.common.message.MessageType;
import com.it.common.message.RPCRequest;
import com.it.common.message.RPCResponse;
import com.it.common.serializer.mySerializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;


import java.awt.*;

@AllArgsConstructor
public class MyEnCoder extends MessageToByteEncoder {
	private Serializer serializer;
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		System.out.println(msg.getClass());
		//1.写入消息类型
		if(msg instanceof RPCRequest){
			out.writeShort(MessageType.REQUEST.getCode());
		}
		else if(msg instanceof RPCResponse){
			out.writeShort(MessageType.RESPONSE.getCode());
		}
		//2.写入序列化方式
		out.writeShort(serializer.getType());
		//得到序列化数组
		byte[] serializeBytes = serializer.serialize(msg);
		//3.写入长度
		out.writeInt(serializeBytes.length);
		//4.写入序列化数组
		out.writeBytes(serializeBytes);
	}
}
