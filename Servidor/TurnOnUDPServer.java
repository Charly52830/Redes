import java.net.*;
import java.io.*;

public class TurnOnUDPServer {
    
    public final int PUERTO_SERVER;
    
    public TurnOnUDPServer(int puertoS){
        PUERTO_SERVER=puertoS;
    }
    public void inicia()throws Exception{
        //ServidorEscuchaUDP servidorUDP=new ServidorEscuchaUDP(PUERTO_SERVER);
        UDPServer servidor=new UDPServer(PUERTO_SERVER);
        
        servidor.start();
    }
}
