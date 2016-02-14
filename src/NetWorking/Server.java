package NetWorking;

import processing.core.*;
import time.TimeLine;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.Serializable;

public class Server extends PApplet implements Serializable{
	private static final long serialVersionUID = 6981036013080425788L;
	
	private static int clientNumber = 0;
	private static ServerSocket listener;
	
	private static BlockingQueue<Renderable> objectList = new LinkedBlockingQueue<Renderable>();

	public static PImage background;
	public static PImage platformer_texture;
	public static PImage character;
	public static PImage deathzone_texture;
	
	public SceneComponentMovableObject plat;
	public SceneComponentMovableObject Upperplat;
	public SceneComponentObject Lowplat;
	
	public static Deathzone deathZone = new Deathzone(530, 362, 98, 67); 
	public static SceneComponentPoint spawnPoint = new SceneComponentPoint(200, 400);
	// scene settings 
	private final static int screenWidth = 640;
	private final static int screenHeight = 480;
	
	public static PFont pixelFont;
	public static String notice = " Press key r to record, \n Press key s to stop and replay. \n When replaying, use key 1, 2 and 3 to select replaying speed\n";
	
	
  @Override
public void setup() {
      size(screenWidth, screenHeight);
      noStroke();
      
      System.out.println("The click server is running.");  
      
      (new Acceptor()).start();
      
      background = loadImage("MountainDew.png");
      platformer_texture = loadImage("platformer.png");
      character = loadImage("green.png");
      deathzone_texture = loadImage("Flame.png");
      
      plat = new SceneComponentMovableObject(150, 200, 50, 95);
      Lowplat = new SceneComponentObject(100, 300, 50, 95);
      Upperplat = new SceneComponentMovableObject(230, 130, 50, 95);
      
      objectList.add(plat);
      objectList.add(Lowplat);
      objectList.add(deathZone);
      objectList.add(Upperplat);
      pixelFont = createFont("alterebro-pixel-font.ttf", 30);
  }
  
  public synchronized static void addPlayer(Player server) {
	  objectList.add(server);
	  clientNumber ++;
  }

  public synchronized static void removePlayer(Player s) {
	  objectList.remove(s);
	  clientNumber --;
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
				} else if(p instanceof SceneComponentMovableObject || p instanceof SceneComponentObject){
					((Renderable) p).render(this, objectList, platformer_texture);
				}
			} 
		}
		
		textFont(pixelFont);
		fill(82, 145, 204);
		text(String.format("World Time: %.2f seconds", TimeLine.getInstance().getWorldTime(this)), 440, 35);

		// only works for server
		/*
		if(EventManager.getInstance().getState() == EventManager.State.Recording) {
			text( String.format("Recording"), 445, 95);
		} else if (EventManager.getInstance().getState() == EventManager.State.Replaying) {
			text( String.format("Replaying"), 445, 95);
		} */
		
		text( "FPS: " + frameRate, 445, 75);
		
		textSize((float) 15.0);
		text(notice, 15, 15);
		
		textSize((float) 30.0);
	}
  
  public static BlockingQueue<Renderable> getObjectList() {
		return objectList;
	}

class Acceptor extends Thread {	
		@Override
		public void run() {
		      try {
		          listener = new ServerSocket(9890);
		          
		          while(true) {
		        		Socket client = listener.accept();
		                Player server = new Player(client, clientNumber++, objectList);
		                server.start();
		                
		                addPlayer(server);
		                server.sendObjects();
		          }
		         
		        } catch(IOException e) {
		          System.out.println(e);
		        }
		  }
	}
}