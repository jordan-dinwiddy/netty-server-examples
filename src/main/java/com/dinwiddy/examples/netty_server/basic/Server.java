package com.dinwiddy.examples.netty_server.basic;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;

import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		bootstrap.setPipelineFactory(new BasicServerPipelineFactory());

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
