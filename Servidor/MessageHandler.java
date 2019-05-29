import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;

import java.net.*;
import java.io.*;

public class MessageHandler extends Thread {
	
	protected boolean send;
	protected boolean successfulEnqueue;
	protected Map<String, Queue<Message> > messageBuffer;
	protected Message message;
	protected String hostAddress;
	protected int port;
	
	private final int MAX_MESSAGE_SIZE=50;
	
	public MessageHandler(Message message,Map<String, Queue<Message> > messageBuffer) {
		send=true;
		this.messageBuffer=messageBuffer;
		
		this.message=message;
	}
	
	public MessageHandler(String hostAddress,int port,Map<String, Queue<Message> > messageBuffer) {
		send=false;
		this.messageBuffer=messageBuffer;
		
		this.hostAddress=hostAddress;
		this.port=port;
	}
	
	public void run() {
		//Si va a agregar o sacar elementos del buffer.
		if(send) {
			enqueueMessage(message);
			dequeueMessages(message.getDestinationAddress(),message.getPort());
		}
		else 
			dequeueMessages(hostAddress,port);
	}
	
	private boolean enqueueMessage(Message message) {
		//TO DO: sustituir el boolean por excepciones.
		Queue<Message> q=messageBuffer.get(message.getDestinationAddress());
		if(q == null) {
			q=new LinkedList<>();
			messageBuffer.put(message.getDestinationAddress(),q);
		}
		if(q.size() == MAX_MESSAGE_SIZE) 
			return false;
		else
			q.add(message);
		
		System.out.println("Tamanio de la cola "+q.size());
		
		return true;
	}
	
	private void dequeueMessages(String hostAddress,int port) {
		if(isListening(hostAddress,port)) {
			try {
				Socket socket=new Socket(hostAddress,port);
				DataOutputStream out =new DataOutputStream(socket.getOutputStream());
				Queue<Message> q=messageBuffer.get(hostAddress);
				while(q.size() > 0) {
					Message message=q.remove();
					out.writeUTF(message.toString());
				}
				messageBuffer.remove(hostAddress);
				socket.close();
			}
			catch(UnknownHostException e) {
				System.err.println("Host desconocido "+e.getMessage());
			}
			catch(IOException e) {
				System.err.println("Error de entrada/salida "+e.getMessage());
			}
		}
		
	}
	
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
			if(s != null)
            try {
            	s.close();
            }
            catch(Exception e){}
		}
	}
	
}
