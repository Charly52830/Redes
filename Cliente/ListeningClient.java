import java.net.*;
import java.io.*;

/**
*	Clase que corre un hilo donde se crea una conexión TCP para escuchar.
*/
public class ListeningClient extends Thread {
	
	protected ServerSocket socket;
	protected DataInputStream in;
	protected Socket socketCliente;
	protected final int serverPort;
	
	public ListeningClient(int serverPort)throws Exception{
		this.serverPort=serverPort;
	}
	
	public void run() { 
		try {
			while(true) {
				socket = new ServerSocket(serverPort);
				socketCliente = socket.accept();
				in =new DataInputStream(socketCliente.getInputStream());
				while(true) {
					String mensaje = in.readUTF();
					if(mensaje.startsWith("END"))
						break;
					System.out.printf("Mensaje recibido:\n%s\n",mensaje);
				}				
				socket.close();
				socket=null;
			}
		}
		catch (IOException e) {
			System.err.printf("Ocurrió un error: %s\n",e.getMessage());
		}
    }
}
