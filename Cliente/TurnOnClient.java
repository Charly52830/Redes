import java.net.*;
import java.io.*;
 
//declaramos la clase udp
public class TurnOnClient {
	protected final int PUERTO_SERVER;
	protected final String SERVER;
	
	public TurnOnClient(String servidor, int puertoS) {
		PUERTO_SERVER=puertoS;
		SERVER=servidor;
	}
	
	public void inicia()throws Exception{
		DatagramSocket socket=new DatagramSocket(52830);
		//System.out.println(socket.getLocalPort());
		
		//ClienteEscuchaUDP clienteEnvUDP=new ClienteEscuchaUDP(socket);
		//ClienteEnviaUDP clienteEscUDP=new ClienteEnviaUDP(socket, SERVER, PUERTO_SERVER);
		
		//clienteEnvUDP.start();
		//clienteEscUDP.start();
		SenderClient senderClient=new SenderClient(socket,SERVER,PUERTO_SERVER);
		ListeningClient listeningClient=new ListeningClient(52831);
		
		senderClient.start();
		listeningClient.start();
		
		
	}
}
