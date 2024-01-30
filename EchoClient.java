package pack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
    private DatagramSocket _socket;
    private InetAddress _address;

    private byte[] _buffer;
    private int _port;

    public EchoClient(int port) {
    	_port = port;
        try {
			_socket = new DatagramSocket();
			_address = InetAddress.getByName("localhost");
		}
        catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

    public String sendEcho(String msg) {
        _buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(_buffer, _buffer.length, _address, _port);
        try {
        	
        	// Send Packet
			_socket.send(packet);
	        packet = new DatagramPacket(_buffer, _buffer.length);
	        
	        // Receive packet
	        _socket.receive(packet);
	        String received = new String(
	        packet.getData(), 0, packet.getLength()).trim();
	        
	        // Handle special messages
	        if(received.equals("end"))
	        	close();
	        
	        return received;
        }
        catch (IOException e) {
			e.printStackTrace();
			close();
			return null;
		}
    }

    public void close() {
    	System.out.println("Client closing");
        _socket.close();
    }
}