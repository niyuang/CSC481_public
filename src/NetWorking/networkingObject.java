package NetWorking;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import processing.core.PApplet;
import processing.core.PImage;

public class networkingObject extends Thread implements Serializable, Networking {
	private static final long serialVersionUID = 1884609149385710474L;
	transient Socket socket;
	private int clientNumber;
	private BlockingQueue<Renderable> objectList;
	transient BufferedReader in;
	transient ObjectOutputStream out;
	
	private int posX;
	private int posY;
	
	public networkingObject(Socket socket, int clientNumber, BlockingQueue<Renderable> objectList) {
	    this.socket = socket;
	    this.clientNumber = clientNumber;
	    this.objectList = objectList;
	    
	    log("New connection with user " + clientNumber + " at " + socket);
	    
	    try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Error Creating Stream: " + e);
		} 
	  }
	
	private void log(String message) {
		   System.out.println(message);
	}  
	
	public void sendObjects() {
		 Iterator<Renderable> iter = objectList.iterator();
		 while(iter.hasNext()) {
			 Object p = iter.next();
			 
				if(p instanceof Networking) {
					try {
						((Networking) p).getOut().writeObject(objectList);
						((Networking) p).getOut().reset();
						((Networking) p).getOut().flush();
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
		 }
	 }
	
	public int getClientNumber() {
		return clientNumber;
	}
	
	public ObjectOutputStream getOut() {
		return out;
	}

	public Socket getSocket() {
		return socket;
	}
	
	public void render(PApplet p, BlockingQueue<Renderable> objectList, PImage image) {
		p.image(image, getPosX(), getPosY());
	}
	
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}


	public BlockingQueue<Renderable> getObjectList() {
		return objectList;
	}
}
