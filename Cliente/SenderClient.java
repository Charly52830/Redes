import java.net.*;
import java.io.*;

/**
*	Clase que corre un hilo encargado de enviar mensajes al servidor a través de
*	una conexión UDP.
*/
public class SenderClient extends Thread{
	
	protected BufferedReader in;
	protected final int MAX_BUFFER=256;
	protected final int serverPort;
	protected DatagramSocket socket;
	protected InetAddress address;
	protected DatagramPacket paquete;
	protected String server;
	protected int imagePort=13085;
	
	public SenderClient(DatagramSocket socket, String server, int serverPort) {
		this.socket = socket;
		this.server=server;
		this.serverPort=serverPort;
	}
	
	public void run() {

		in = new BufferedReader(new InputStreamReader(System.in));
		byte[] mensaje_bytes;
		String mensaje="";
		mensaje_bytes=mensaje.getBytes();
		try {
			address=InetAddress.getByName(server);
			while(true)	{
				mensaje = in.readLine();
				if(mensaje.startsWith("FIN"))
					break;
				if(mensaje.startsWith("FILE")) {
					//Separamos el mensaje	FILE host path
					String[] msj=mensaje.split(" ");
					//Enviamos el mensaje al servidor
					mensaje=msj[0]+" "+msj[1];
					mensaje_bytes=new byte[mensaje.length()];
					mensaje_bytes = mensaje.getBytes();
					paquete = new DatagramPacket(mensaje_bytes,mensaje.length(),address,serverPort);
					socket.send(paquete);
					sendFile(msj[2]);
				}
				else {
					mensaje_bytes=new byte[mensaje.length()];
					mensaje_bytes = mensaje.getBytes();
					paquete = new DatagramPacket(mensaje_bytes,mensaje.length(),address,serverPort);
					socket.send(paquete);
				}
				String mensajeMandado=new String(paquete.getData(),0,paquete.getLength()).trim();
				System.out.printf("Mensaje \"%s\" enviado a %s:%s\n",mensajeMandado,paquete.getAddress(),String.valueOf(paquete.getPort()));
			}
		}
		catch (IOException e) {
			System.err.printf("Ocurrió un error de entrada/salida: %s\n",e.getMessage());
		}
	}
	
	public void sendFile(String filePath) {
		try {
			System.out.printf("Enviando archivo %s\n",filePath);
			File file=new File(filePath);			
			// Get size
			int size=(int)file.length();
			// Get format
			StringBuilder builder=new StringBuilder();
			for(int i=filePath.length()-1; i>=0 && filePath.charAt(i)!='.';i-- )
				builder.append(filePath.charAt(i));
			builder.reverse();
			String format=builder.toString();
			// Get byte array
			byte[] byteArray = new byte[size];
			FileInputStream fis=new FileInputStream(file);
			fis.read(byteArray);
			fis.close();
			// Open TCP connection
			ServerSocket serverSocket = new ServerSocket(13085);
			Socket socket = serverSocket.accept();
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
			serverSocket.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
