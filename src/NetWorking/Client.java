package NetWorking;

import processing.core.*;
import time.TimeLine;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends PApplet {
	private static final long serialVersionUID = -3258661466640458703L;
	private static BlockingQueue<Renderable> objectList = new LinkedBlockingQueue<Renderable>();
	private static ObjectInputStream in;
	private static PrintWriter out;
	private Socket socket;
	public static PImage background;
	public static PImage platformer_texture;
	public static PImage character;
	public static PImage deathzone_texture;

	public static PFont pixelFont;
	@Override
	public void setup() {
		size(640, 480);
		noStroke();
	    try { 
	        socket = new Socket("127.0.0.1", 9890);
	        
	        in = new ObjectInputStream(socket.getInputStream());
	        out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
	        
	      } catch(IOException e) {
	        print("Error connecting to server: " + e);
	      } 

	      background = loadImage("MountainDew.png");
	      platformer_texture = loadImage("platformer.png");
	      character = loadImage("green.png");
	      deathzone_texture = loadImage("Flame.png");
	      
	      (new Receiver()).start();   

	      pixelFont = createFont("alterebro-pixel-font.ttf", 30);
	}
	
	@Override
	public void draw() {
		background(background);
		
		Iterator<Renderable> iter = objectList.iterator();
		while(iter.hasNext()) {
			Object p = iter.next();
			if(p instanceof Renderable) {
				if(p instanceof Deathzone) {
					((Renderable) p).render(this, objectList, deathzone_texture);
				}else if(p instanceof Player) {
					((Renderable) p).render(this, objectList, character);
				} else if(p instanceof SceneComponentMovableObject || p instanceof  SceneComponentObject){
					((Renderable) p).render(this, objectList, platformer_texture);
				}
			}
		}
	    textFont(pixelFont);
		fill(82, 145, 204);
		text( String.format("Local Time: %.2f seconds",TimeLine.getInstance().getLocalTime(this)), 440, 35);
		text( "FPS: " + frameRate, 445, 75);
		
		textSize((float) 15.0);
		text(Server.notice, 15, 15);
		
		textSize((float) 30.0);
  	}
	
	@Override
	public void keyPressed() {
		  if(key == CODED) {
		    if(keyCode == LEFT) {
		      out.println("left");
		    } else if(keyCode == RIGHT) {
		      out.println("right");
		    } else if(keyCode == 27) {
		      // ESC
		      out.println("Disconnect");
		      try {
		        socket.close();
		      } catch(IOException e) {
		        System.out.println("Error closing socket by client" + e);
		      }
		    }
		  } else if(key == ' ') {
			  out.println("jump");
		  } else if(key == 'r') {
			  out.println("startRecording");
		  } else if(key == 's') {
			  out.println("stopRecording");
		  } else if(key == '1') {
			  out.println("slowReplay");
		  } else if(key == '2') {
			  out.println("normalReplay");
		  } else if(key == '3') {
			  out.println("doubleReplay");
		  }
		  out.flush();
	}	  
	
	  class Receiver extends Thread {
		@Override
		@SuppressWarnings("unchecked")
		public void run() {
				try {
					while(true) {
						objectList = (LinkedBlockingQueue<Renderable>) in.readObject();
					}
				} catch (Exception e) {
					 System.out.println("Error Receiving objects by client" + e);
			         e.printStackTrace();
				}
		  }
	  }
}

