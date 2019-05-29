
public class Message {
	
	private final String sourceAddress;
	private final String destinationAddress;
	private final int port;
	private String message;
	
	public Message(String sourceAddress,String destinationAddress,int port,String message) {
		this.sourceAddress=sourceAddress;
		this.destinationAddress=destinationAddress;
		this.port=port;
		this.message=message;
	}
	
	protected String getSourceAddress() {
		return sourceAddress;
	}
	
	protected String getDestinationAddress() {
		return destinationAddress;
	}
	
	protected int getPort() {
		return port;
	}
	
	protected String getMessage() {
		return message;
	}
	
	public String toString() {
		return "SourceAddress: "+sourceAddress+"\nDestinationAddress: "+destinationAddress+"\nPort: "+String.valueOf(port)+"\nMessage: "+message;
	}
	
}
