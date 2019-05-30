import java.net.*;
import java.io.*;
 
public class ListeningClient extends Thread {
    protected ServerSocket socket;
    protected DataInputStream in;
    protected Socket socket_cli;
    protected final int PUERTO_SERVER;
    
    public ListeningClient(int puertoS)throws Exception{
        PUERTO_SERVER=puertoS;
    }
    
    public void run() { 
        try {
	        while(true) {
				socket = new ServerSocket(PUERTO_SERVER);
				socket_cli = socket.accept();
				in =new DataInputStream(socket_cli.getInputStream());
				while(true) {
					String mensaje = in.readUTF();
					if(mensaje.startsWith("END"))
						break;
					System.out.println("Mensaje recibido\n"+mensaje);
				}				
				socket.close();
				socket=null;
	        }
        }
        catch (IOException e) {
			System.err.println("Ocurrió un error: "+e.getMessage());
        }
    }
}
