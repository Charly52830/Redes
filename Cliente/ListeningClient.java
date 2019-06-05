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
	protected int imageDownloadPort=52838;
	
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
					if(mensaje.startsWith("FILE")) {
						String sourceHost=mensaje.split(" ")[1];
						System.out.printf("Downloading file from %s\n",sourceHost);
						downloadFile();
						
					}
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
 
    private void downloadFile() {
    	try {
    		ServerSocket serverSocket = new ServerSocket(imageDownloadPort);
    		Socket socket = serverSocket.accept();
    		
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
    		serverSocket.close();
			// Escribimos el archivo.
			File newFile=new File("newTest."+format);
			FileOutputStream fos=new FileOutputStream(newFile);
			fos.write(byteArray);
			fos.close();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
}
