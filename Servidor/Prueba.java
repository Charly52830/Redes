import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;

public class Prueba {
	
	public static void main(String []args) {
		
		//Queue<Message> q =new LinkedList<>();
		Map<String, Queue<Message> > map=new HashMap<String, Queue<Message> >(); 
		map.put("192.168.1.64",new LinkedList<>() );
		map.get("192.168.1.64").add(new Message("192.168.1.65",60000,"Hola como estas?"));
		
		//Message m=new Message("1",1,"1");
	
	}
	
}

