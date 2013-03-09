package com.dinwiddy.examples.netty_server.basic.handlers;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class BasicServerHandler extends SimpleChannelUpstreamHandler {

	private int messageCount = 0; 

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)  {

		String clientHostStr = getClientHostStr(e);
		System.out.printf("Client connected: %s\n", clientHostStr);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)  {

		String clientHostStr = getClientHostStr(e);
		System.out.printf("Client disconnected: %s\n", clientHostStr);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)  {

		messageCount++; 

		String clientHostStr = getClientHostStr(e);

		ChannelBuffer cb = (ChannelBuffer) e.getMessage();
		String msgStr = cb.toString(Charset.forName("UTF-8"));

		System.out.printf("Message (#%d) from %s: %s\n", messageCount, clientHostStr, msgStr.trim());
	}

	private String getClientHostStr(ChannelEvent e) {

		InetSocketAddress remoteAddress = (InetSocketAddress) e.getChannel().getRemoteAddress();
		return remoteAddress.getHostName();
	}
}
