package com.dinwiddy.examples.netty_server.object.handlers;

import java.net.InetSocketAddress;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.dinwiddy.examples.netty_server.object.domain.ExampleMessage;

public class ObjectServerHandler extends SimpleChannelUpstreamHandler {

	private int messageCount = 0; 

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)  {

		String clientHostStr = getClientHostStr(e);
		System.out.printf("Client connected: %s\n", clientHostStr);
		
		// This guy is necessary when using Netty's compatible object encoder/decoders
		// because the clients input/output streams don't seem to get fully established
		// until some data has been sent by the server. But the welcome message
		// isn't needed if using Netty's object codecs on both client/server.
		//		ExampleMessage welcomeMsg = new ExampleMessage();
		//		welcomeMsg.setFrom("Server");
		//		welcomeMsg.setSubject("Welcome");
		//		welcomeMsg.setMessage("Thanks for connecting");
		//		ChannelFuture cf = e.getChannel().write(welcomeMsg);
		//		cf.addListener(new ChannelFutureListener() {
		//			
		//			public void operationComplete(ChannelFuture future) throws Exception {
		//				System.out.println("Welcome message sent to client");
		//			}
		//		});
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
		
		Object inObject = e.getMessage();
		
		if(inObject instanceof ExampleMessage) {
			ExampleMessage inMsg = (ExampleMessage) inObject; 
			
			System.out.printf("Message (#%d) from %s: from=%s, subject=%s, message=%s\n",  
					messageCount, clientHostStr, inMsg.getFrom(), inMsg.getSubject(), inMsg.getMessage());
		} else {
			System.out.printf("Message (#%d) from %s: Received invalid object instance, ignoring...\n",  messageCount, clientHostStr);
		}
	}

	private String getClientHostStr(ChannelEvent e) {

		InetSocketAddress remoteAddress = (InetSocketAddress) e.getChannel().getRemoteAddress();
		return remoteAddress.getHostName();
	}
}
