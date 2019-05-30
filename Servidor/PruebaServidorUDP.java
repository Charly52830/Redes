public class PruebaServidorUDP{
    public static void main(String args[]) throws Exception{
        //ServidorUDP servidorUDP=new ServidorUDP(50000);
        TurnOnUDPServer server=new TurnOnUDPServer(50000);
        
        server.inicia();
        
    }
}
