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
	protected String sourceAddress;
	//Puerto donde los clientes escuchan 
	protected int port=52831;
	protected int imagePort=13085;
	protected Socket socket=null;
	protected boolean sendFile=false;
	protected int imageSendPort=52838;
	
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
	
	public MessageHandler(String hostAddress,String sourceAddress,Map<String, Queue<Message> > messageBuffer) {
		send=true;
		sendFile=true;
		this.sourceAddress=sourceAddress;
		this.messageBuffer=messageBuffer;
		this.hostAddress=hostAddress;
	}
	
	/**
	*	Método que corre el thread. Encola o manda mensajes al host indicado.
	*/
	public void run() {
		if(sendFile) {
			this.message=downloadFile(sourceAddress,imagePort);
		}
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
						out.writeUTF(message.toString());
						System.out.println("Mensaje enviado al host "+hostAddress+": "+message.getMessage());						
						if(message.hasFile()) {
							out.writeUTF("FILE "+message.getSourceAddress());
							Thread.sleep(100);
							sendNewFile(message.getDestinationAddress(),imageSendPort,message);
						}
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
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.printf("El host %s no está escuchando\n",hostAddress);
		}
		
	}
	
	/**
	*	Método que verifica si un host está escuchando en un puerto especifico.
	*/
	private boolean isListening(String host,int port) {
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
	
	private Message downloadFile(String host,int port) {
		Message message=null;
		try {
			Socket socket = new Socket(host, port);
			DataInputStream dis=new DataInputStream(socket.getInputStream());
			InputStream socketInput=socket.getInputStream();
			// Get size & format
			String[] msj=dis.readUTF().split(" ");
			int size=Integer.valueOf(msj[0]);
			String format=msj[1];
			// Get byte array && calcule time.
			byte[] byteArray=new byte[size];
			long startTime=System.currentTimeMillis();
			for(int i=0;i<size;i+=4) {
				byte[] tmpBytes=new byte[4];
				socketInput.read(tmpBytes);
				long totalTime=System.currentTimeMillis() - startTime;
				double remainingTime=(totalTime*(size-i))/(double)(i + Math.min(4,size-i) );
				System.out.printf("Remaining time: %f second\n",remainingTime);
				for(int j=0;j< Math.min(4,size-i);j++)
					byteArray[j+i]=tmpBytes[j];
			}
			// Cerramos el socket.
			socket.close();
			// Escribimos el archivo.
			
			File newFile=new File("newTest."+format);
			FileOutputStream fos=new FileOutputStream(newFile);
			fos.write(byteArray);
			fos.close();	
			
			message=new Message(sourceAddress,hostAddress,format,byteArray);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return message;
	}
	
	private void sendNewFile(String host,int port,Message message) {
		try {
			// Get byte array
			byte[] byteArray = message.getFile();
			// Get size
			int size=byteArray.length;
			// Get format
			String format=message.getFileFormat();
			// Open TCP connection
			Socket socket = new Socket(host, port);
			OutputStream socketOutput = socket.getOutputStream();
			DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
			// Send size & format
			dos.writeUTF(""+String.valueOf(size)+" "+format);
			// Send byte array.
			for(int i=0;i<size;i+=4) {
				byte[] tmpBytes=new byte[4];
				for(int j=0;j< Math.min(4,size-i);j++)
					tmpBytes[j]=byteArray[j+i];
				socketOutput.write(tmpBytes);
			}
			// Cerramos sockets.
			socket.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
