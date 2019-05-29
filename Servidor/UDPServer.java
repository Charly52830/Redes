import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;

import java.net.*;
import java.io.*;

public class UDPServer extends Thread {
	
	/**
	*	Buffer de mensajes.
	*	Buffer de mensajes que funciona como memoria caché utilizado para encolar 
	*	mensajes en espera de ser enviados. Utiliza un mapa donde la llave es un 
	*	string con la IP del destino, y una cola que le corresponde a cada dirección.
	*/
	protected Map<String, Queue<Message> > messageBuffer;
	
	protected DatagramSocket socket;
	protected final int serverPort;
	
	protected InetAddress addressCliente;
    protected byte[] mensaje2_bytes;
    protected final int MAX_BUFFER=256;
    protected DatagramPacket paquete;
    protected byte[] mensaje_bytes;
    protected DatagramPacket envPaquete;
	
	public UDPServer(int serverPort) throws SocketException {
		//Creamos el socket.
		this.serverPort=serverPort;
		socket = new DatagramSocket(serverPort);
		//Inicializamos el caché.
		messageBuffer=new HashMap<String, Queue<Message> >(); 
	}
	
	public void run() {
	
		try {
			while(true) {
				//Recibimos el paquete.
				mensaje_bytes=new byte[MAX_BUFFER];
				paquete = new DatagramPacket(mensaje_bytes,MAX_BUFFER);
				socket.receive(paquete);
				
				// Lo formateamos
				mensaje_bytes=new byte[paquete.getLength()];
				mensaje_bytes=paquete.getData();
				String mensaje = new String(mensaje_bytes,0,paquete.getLength()).trim();
				
				if(mensaje.startsWith("SEND")) {
					String ipDestino="";
					int i;
					for(i=5;i < mensaje.length() && mensaje.charAt(i) != 32;i++) 
						ipDestino+=String.valueOf(mensaje.charAt(i));
						
					Message message=new Message(paquete.getAddress().toString().substring(1), ipDestino, paquete.getPort(), mensaje.substring(i+1) );
					MessageHandler handler=new MessageHandler(message,messageBuffer );
					handler.start();
				}
				else if(mensaje.startsWith("RECEIVE")) {
					MessageHandler handler=new MessageHandler(paquete.getAddress().toString().substring(1),paquete.getPort(),messageBuffer);
				}

				// Lo mostramos por pantalla
				System.out.println("Mensaje recibido \""+mensaje+"\" del cliente "+ paquete.getAddress()+"#"+paquete.getPort());               
			}
			
		}
		catch(Exception e) {
			System.err.println("Ocurrió un error "+e.getMessage());
		}
		
	}
	
}
