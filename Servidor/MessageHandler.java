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
	protected Socket socket=null;
	
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
		return true;
	}
	
	private void dequeueMessages(String hostAddress,int port) {
		port=52831;
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
				socket.close();
				socket=null;
			}
			catch(UnknownHostException e) {
				System.err.println("Host desconocido "+e.getMessage());
			}
			catch(IOException e) {
				System.err.printf("Error de entrada/salida %s\n",e.getMessage());
			}
		}
		else {
			System.out.printf("%s no está escuchando\n",hostAddress);
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
            	if(socket == null)
            		socket=s;
				}
				catch(Exception e){
				}
			}
	}	
}
