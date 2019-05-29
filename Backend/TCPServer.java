import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;
import java.net.*;
import java.io.*;

public class TCPServer extends Thread {
	
	/**
	*	Buffer de mensajes.
	*	Buffer de mensajes que se guarda en el caché. Utiliza un mapa donde la
	*	llave es un string con la IP del destino de los mensajes que guarda una
	*	cola de los mensajes entrantes para dicha IP.
	*/
	protected Map<String, Queue<Message> > messageBuffer;
	
	private final int MAX_MESSAGE_SIZE=50;
	
	protected ServerSocket socket;
	protected DataInputStream in;
	protected Socket socket_cli;
	protected final int serverPort;
	
	public TCPServer(int serverPort) throws Exception {
		messageBuffer=new HashMap<String, Queue<Message> >(); 
		this.serverPort=serverPort;
		socket = new ServerSocket(serverPort);
	}
	
	public void run() {
		try {
			socket_cli = socket.accept();
			in =new DataInputStream(socket_cli.getInputStream());
			while(true) {
				String mensaje ="";
                mensaje = in.readUTF();
                System.out.println(mensaje);
			}
		}
		catch(Exception e) {
			System.err.println("Ocurrió un error en el servidor: "+e.getMessage());
		}
	}
	
}	
