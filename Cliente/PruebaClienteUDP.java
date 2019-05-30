public class PruebaClienteUDP{
    public static void main(String args[]) throws Exception{
        //ClienteUDP clienteUDP =new ClienteUDP("127.0.0.1",50000);
        TurnOnClient client=new TurnOnClient("127.0.0.1",50000);
        
        client.inicia();
        
    }
}
