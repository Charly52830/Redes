import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;

import java.net.*;
import java.io.*;

public class MessageHandler extends Thread {
	
	protected boolean send;
	protected Map<String, Queue<Message> > messageBuffer;
	protected Message message;
	protected String hostAddress;
	//Puerto donde los clientes escuchan 
	protected int port=52831;	
	protected Socket socket=null;
	
	//Cantidad máxima de mensajes en la cola.
	private final int MAX_QUEUE_SIZE=50;
	
	public MessageHandler(Message message,Map<String, Queue<Message> > messageBuffer) {
		send=true;
		this.messageBuffer=messageBuffer;
		this.message=message;
	}
	
	public MessageHandler(String hostAddress,Map<String, Queue<Message> > messageBuffer) {
		send=false;
		this.messageBuffer=messageBuffer;
		this.hostAddress=hostAddress;
	}
	
	/**
	*	Método que corre el thread. Encola o manda mensajes al host indicado.
	*/
	public void run() {
		if(send) {
			enqueueMessage(message);
			dequeueMessages(message.getDestinationAddress());
		}
		else 
			dequeueMessages(hostAddress);
	}
	
	/**
	*	Método que encola un mensaje en caché.
	*/
	private boolean enqueueMessage(Message message) {
		//TO DO: sustituir el boolean por excepciones.
		Queue<Message> q=messageBuffer.get(message.getDestinationAddress());
		if(q == null) {
			q=new LinkedList<>();
			messageBuffer.put(message.getDestinationAddress(),q);
		}
		if(q.size() == MAX_QUEUE_SIZE) 
			return false;
		else
			q.add(message);
		return true;
	}
	
	/**
	*	Método que envía mensajes almacenados en el caché. 
	*/
	private void dequeueMessages(String hostAddress) {
		if(isListening(hostAddress,port)) {
			try {
				DataOutputStream out =new DataOutputStream(socket.getOutputStream());
				Queue<Message> q=messageBuffer.get(hostAddress);
				if(q != null) {
					while(q.size() > 0) {
						Message message=q.remove();
						System.out.println("Mensaje enviado al host "+hostAddress+": "+message.getMessage());
						out.writeUTF(message.toString());
					}
					messageBuffer.remove(hostAddress);
				}
				out.writeUTF("END");
				out.flush();
				socket.close();
				socket=null;
			}
			catch(UnknownHostException e) {
				System.err.printf("Host desconocido: %s\n",e.getMessage());
			}
			catch(IOException e) {
				System.err.printf("Error de entrada/salida: %s\n",e.getMessage());
			}
		}
		else {
			System.out.printf("El host %s no está escuchando\n",hostAddress);
		}
		
	}
	
	/**
	*	Método que verifica si un host está escuchando en un puerto especifico.
	*/
	public boolean isListening(String host,int port) {
		Socket s = null;
		try {
			s = new Socket(host, port);
			return true;
		}
		catch (Exception e) {
			return false;
		}
		finally {
			if(s != null && socket == null) {
				socket=s;
			}
		}
	}	
}
