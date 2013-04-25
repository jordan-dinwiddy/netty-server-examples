package com.dinwiddy.examples.netty_server.basic.handlers;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicServerHandler.class);
	
	private int messageCount = 0; 

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)  {

		String clientHostStr = getClientHostStr(e);
		LOGGER.info("Client connected: {}", clientHostStr);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)  {

		String clientHostStr = getClientHostStr(e);
		LOGGER.info("Client disconnected: {}", clientHostStr);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)  {

		messageCount++; 

		String clientHostStr = getClientHostStr(e);

		ChannelBuffer cb = (ChannelBuffer) e.getMessage();
		String msgStr = cb.toString(Charset.forName("UTF-8"));

		LOGGER.info("Message (#{}) from {}: {}", messageCount, clientHostStr, msgStr.trim());
	}

	private String getClientHostStr(ChannelEvent e) {

		InetSocketAddress remoteAddress = (InetSocketAddress) e.getChannel().getRemoteAddress();
		return remoteAddress.getHostName();
	}
}
