public class PruebaClienteUDP{
	public static void main(String args[]) throws Exception{
		TurnOnClient client=new TurnOnClient("127.0.0.1",50000);
		client.inicia();
	}
}
