package com.dinwiddy.examples.netty_server.basic;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.dinwiddy.examples.netty_server.basic.handlers.BasicServerHandler;

public class BasicServerPipelineFactory implements ChannelPipelineFactory {

	/**
	 * This get's called every time a new channel is created. So if you 
	 * instantiate handlers in this method, there will be one handler
	 * per channel/client connection. That's useful for storing client 
	 * state.
	 */
	public ChannelPipeline getPipeline() throws Exception {

		return Channels.pipeline(new BasicServerHandler());
	}

}
