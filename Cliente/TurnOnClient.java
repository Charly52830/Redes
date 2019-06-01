import java.net.*;
import java.io.*;

public class TurnOnClient {

	protected final int serverPort;
	protected final String server;
	
	public TurnOnClient(String server, int serverPort) {
		this.server=server;
		this.serverPort=serverPort;
	}

	public void inicia()throws Exception{
		//El cliente manda mensajes por el puerto 52830.
		DatagramSocket socket=new DatagramSocket(52830);
		
		//El cliente escucha mensajes por el puerto 52831
		ListeningClient listeningClient=new ListeningClient(52831);
		
		SenderClient senderClient=new SenderClient(socket,server,serverPort);
		senderClient.start();
		listeningClient.start();		
	}
}
