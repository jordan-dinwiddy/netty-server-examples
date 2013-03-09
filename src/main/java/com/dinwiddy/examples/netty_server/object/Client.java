package com.dinwiddy.examples.netty_server.object;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.jboss.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import com.dinwiddy.examples.netty_server.object.domain.ExampleMessage;

/**
 * Establishes a connection with a server and then periodically sends a
 * message to it. 
 * 
 */
public class Client {

	private static final long THREAD_SLEEP_TIME = 5000;
	private Socket clientSocket;
	private String host;
	private int port;
	private ObjectEncoderOutputStream objectOut;
	private int messageCount = 0;				// Count of the messages we've sent

	public Client(String host, int port) {

		this.host = host; 
		this.port = port; 
	}

	private void run() {

		while(true) {

			messageCount++;

			try {
				System.out.printf("Sending test message... ");

				ExampleMessage msg = new ExampleMessage();
				msg.setFrom("testClient");
				msg.setSubject("Message " + messageCount);
				msg.setMessage("This is the message body");

				objectOut.writeObject(msg);
				System.out.printf("DONE, sleeping for %d ms\n", THREAD_SLEEP_TIME);
			} catch (IOException e) {
				throw new RuntimeException("Failed to send message " + messageCount);
			}

			sleep(THREAD_SLEEP_TIME);
		}
	}

	private void connect() {

		try {
			clientSocket = new Socket(host, port);

			// The server side has Netty and although Netty does have deprecated support for clients
			// using standard java Object input/output streams, it has a bunch of bugs. So I'm just
			// going to use Netty's native object encoder/decoder streams. They work just the same 
			// way.
			// objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
			// objectIn = new ObjectInputStream(clientSocket.getInputStream());

			objectOut = new ObjectEncoderOutputStream(clientSocket.getOutputStream());
			// objectIn = new ObjectDecoderInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e) {
			throw new RuntimeException("Unknown Host", e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to open connection to host", e);
		}
	}

	private void sleep(long threadSleepTime) {

		try {
			Thread.sleep(threadSleepTime);
		} catch (InterruptedException e) {

		}
	}

	public static void main(String[] args) {

		String serverHost;
		int serverPort;

		if(args.length != 2) {
			System.out.println("Missing host and port params");
			System.out.println("Assuming defaults of localhost:8088");
			serverHost = "localhost";
			serverPort = 8080;
		} else {
			serverHost = args[0];
			serverPort = Integer.parseInt(args[1]);
		}

		System.out.printf("Creating new client connection to %s:%d...\n", serverHost, serverPort);
		Client c = new Client(serverHost, serverPort); 
		c.connect();

		System.out.println("Running client demo...");
		c.run();
	}
}
