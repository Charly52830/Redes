public class Message {
	
	private final String sourceAddress;
	private final String destinationAddress;
	private String message;
	private byte[] file;
	private String formatFile;
	
	public Message(String sourceAddress,String destinationAddress,String message) {
		this.sourceAddress=sourceAddress;
		this.destinationAddress=destinationAddress;
		this.message=message;
		file=null;
	}
	
	public Message(String sourceAddress,String destinationAddress,String formatFile,byte[] file) {
		this.sourceAddress=sourceAddress;
		this.destinationAddress=destinationAddress;
		this.formatFile=formatFile;
		message=null;
		this.file=file;
	}
	
	protected String getSourceAddress() {
		return sourceAddress;
	}
	
	protected String getDestinationAddress() {
		return destinationAddress;
	}
		
	protected String getMessage() {
		return message;
	}
	
	protected boolean hasFile() {
		return file!=null;
	}
	
	protected byte[] getFile() {
		return file;
	}
	
	protected String getFileFormat() {
		return formatFile;
	}
	
	public String toString() {
		if(hasFile())
			return sourceAddress+" has sent a file";
		else
			return sourceAddress+": "+message;
	}
	
}
