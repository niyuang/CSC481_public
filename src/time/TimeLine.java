package time;

import NetWorking.Client;
import NetWorking.Server;
import processing.core.PApplet;

public class TimeLine {
	private static TimeLine instance;
	private int frameCounter = 0;
	private boolean counterLock = false;
	
	public TimeLine() {
		
	}
	
	public static TimeLine getInstance() {
		if(instance == null) {
			instance = new TimeLine();
		}
		return instance;
	}
	
	public float getLocalTime(Client p) {
		return p.frameCount / p.frameRate;
	}
	
	public float getWorldTime(Server s) {
		return s.frameCount / s.frameRate;
	}
	
	public int getFrameCounter() {
		return frameCounter;
	}

	public void startCount(PApplet p) {
		frameCounter = p.frameCount;
		counterLock = true;
	}
	
	public int endCounter(PApplet p) {
		if(counterLock == true) {
			counterLock = false;
			return p.frameCount - frameCounter;
		}
		
		return 0;
	}
	
}
