public class TurnOnUDPServer {

	public final int serverPort;
	
	public TurnOnUDPServer(int serverPort){
		this.serverPort=serverPort;
	}
	
    public void turnOn() throws Exception{
        UDPServer server=new UDPServer(serverPort);
		server.start();
	}
}
