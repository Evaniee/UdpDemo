package pack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoServer extends Thread {

	private DatagramSocket _socket;
	private boolean _running;
	private byte[] _buffer = new byte[256];
	
	public EchoServer(int port) {
		try {
			_socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		_running = true;
		
		while (_running) {
			DatagramPacket packet = new DatagramPacket(_buffer, _buffer.length);
			try {
				_socket.receive(packet);														// Receive Packet
				InetAddress address = packet.getAddress();										// Find Address from packet
				int port = packet.getPort();													// Find Port from packet
				packet = new DatagramPacket(_buffer, _buffer.length, address, port);			// Create a new packet with sender's data, address and port
				
				String recieved = new String(packet.getData(), 0, packet.getLength()).trim();	// Find data from packet
				System.out.println("Recieved " + recieved);
				_socket.send(packet);															// Send packet back
				if(recieved.trim().equals("end")) {												// Exit if data was "end". Trim required to avoid whitespace
					_running = false;
					continue;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			_buffer = new byte[256];															// Empty Buffer
		}
		System.out.println("Server closing");
		_socket.close();																		// Close packet
	}
	
	public static void main(String[] args) {

		int port = 4445;
		
		// Make server
		EchoServer server = new EchoServer(port);
		server.start();
		
		// Make client
		EchoClient client = new EchoClient(port);
		for(int i = 0; i < 10; i++)
		{
			String echo = client.sendEcho("Hello World! " + i);
			System.out.println(echo);
		}
		
		// Close Threads
		client.sendEcho("end");
	}

}