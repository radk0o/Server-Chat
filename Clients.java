package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Clients implements Runnable {
    private static final HashMap<String, ObjectOutputStream> connectedClients = new HashMap<String, ObjectOutputStream>(); 
    private Socket sock;
    private ObjectInputStream istream;
    private ObjectOutputStream ostream;
    
	public Clients(Socket sock){
		this.sock = sock;
	}
	@Override
	public void run() {
		try {
    		ostream = new ObjectOutputStream(sock.getOutputStream());
    		istream = new ObjectInputStream(sock.getInputStream());
    	} catch (IOException e) {
    		System.err.println(e.getMessage());
		}
		while(!sock.isClosed()) {
	    	try {
	    		System.out.println("Awaiting command");
	    		String command = (String) istream.readObject();
	    		
	    		if(command.equals("Exit")) {
	    			System.out.println("closing sockets");
	    			ostream.close();
					istream.close();
					sock.close();
					break;
	    		} else if(command.equals("add")) {
	    			String identifier = (String) istream.readObject();
	    			if(Clients.connectedClients.get(identifier) == null){
	    				Clients.connectedClients.put(identifier, this.ostream);
	    				ostream.writeObject("added");
	    				ostream.flush();
	    			}	    			
	    		} else if(command.equals("message")) {
	    			String identifier = (String) istream.readObject();
	    			if(Clients.connectedClients.get(identifier) != null){
	    				String toPerson = (String) istream.readObject();
	    				if(Clients.connectedClients.get(toPerson) != null) {
	    					String message = (String) istream.readObject();
		    				ObjectOutputStream friendStream = Clients.connectedClients.get(toPerson);
		    				friendStream.writeObject(message);
		    				friendStream.flush();
		    				ostream.writeObject("completed");
		    				ostream.flush();
		    			}
	    			}  
	    		}
	    	} catch (ClassNotFoundException e)  {
	    		System.out.println(e.getMessage());
	    	} catch (IOException e) {
	    		System.out.println(e.getMessage());
			}
	    }
	}

}
