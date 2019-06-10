package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Listen implements Runnable {

	private ObjectInputStream istream;
	
	Listen(Socket sock){
		try {
			this.istream = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
    		System.out.println(e.getMessage());
		}
	}
	
	public void end(){
		try {
			this.istream.close();
		} catch (IOException e) {
    		System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void run() {
		try {
			while(true){
				System.out.println("Received Message: "+(String)istream.readObject());
			}
		} catch (IOException e) {
    		System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
    		System.out.println(e.getMessage());
		}
	}

}
