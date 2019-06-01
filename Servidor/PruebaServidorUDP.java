public class PruebaServidorUDP{
	public static void main(String args[]) throws Exception{
		TurnOnUDPServer server=new TurnOnUDPServer(50000);
		server.turnOn();
	}
}
