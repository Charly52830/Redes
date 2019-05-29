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
	
	private final int MAX_MESSAGE_SIZE=50;
	
	public MessageHandler(boolean send,Message message,Map<String, Queue<Message> > messageBuffer) {
		this.send=send;
		this.message=message;
		this.messageBuffer=messageBuffer;
	}
	
	public void run() {
		//Si va a agregar o sacar elementos del buffer.
		if(send) {
			enqueueMessage(message);
		}
		else {
			
		} 
			
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
	
	private void dequeueMessages(String destinationAddress) {
		
	}
	
}
