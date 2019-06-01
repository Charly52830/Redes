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
		byte[] RecogerServidor_bytes;

		try {
			address=InetAddress.getByName(server);
			while(true)	{
				mensaje = in.readLine();
				if(mensaje.startsWith("FIN"))
					break;
				mensaje_bytes=new byte[mensaje.length()];
				mensaje_bytes = mensaje.getBytes();
				paquete = new DatagramPacket(mensaje_bytes,mensaje.length(),address,serverPort);
				socket.send(paquete);

				String mensajeMandado=new String(paquete.getData(),0,paquete.getLength()).trim();
				System.out.printf("Mensaje \"%s\" enviado a %s:%s\n",mensajeMandado,paquete.getAddress(),String.valueOf(paquete.getPort()));
			}
		}
		catch (IOException e) {
			System.err.printf("Ocurrió un error de entrada/salida: %s\n",e.getMessage());
		}
	}
}
