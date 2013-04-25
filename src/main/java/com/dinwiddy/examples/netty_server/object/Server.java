package com.dinwiddy.examples.netty_server.object;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dinwiddy.examples.netty_server.object.handlers.ObjectServerHandler;

public class Server {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class); 
	
	private final int port;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void run() {
		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()
						));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {

				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
						
						// I wanted to use these guys so that the client can use the standard Java input/output
						// object streams. But the below encoders are buggy so I'm just gonna use Netty's
						// native object encoder/decoders.
						//new CompatibleObjectEncoder(),			// Instead of ObjectEncoder
						//new CompatibleObjectDecoder(),			// Instead of ObjectDecoder
						new ObjectServerHandler());
			}
		});

		LOGGER.info("Binding to port {}...", port);
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port));

		// This thread is now done...
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new Server(port).run();
	}
}
