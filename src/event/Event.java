package event;

import java.io.Serializable;
import java.util.ArrayList;

import NetWorking.Player;
import eventListener.EventListener;
import processing.core.PApplet;

public class Event implements Serializable{
	private static final long serialVersionUID = -7743154542929760845L;

	private ArrayList<EventListener> listener;
	private Player caller;

	private boolean handled = false;
	private int handleInWorldFrame = 0;
	private boolean replayed = false;

	public boolean isReplayed() {
		return replayed;
	}

	public void setReplayed(boolean replayed) {
		this.replayed = replayed;
	}

	public Event(Player caller) {
		listener = new ArrayList<EventListener>();
		setCaller(caller);
	}
	
	public ArrayList<EventListener> getListener() {
		return listener;
	}

	public void addListener(EventListener e) {
		listener.add(e);
	}
	
	public boolean isHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}
	
	public void handle() {
		handled = false;
	}
	
	public int getHandleInWorldFrame() {
		return handleInWorldFrame;
	}

	public void setHandleInWorldFrame(int handleInWorldFrame) {
		this.handleInWorldFrame = handleInWorldFrame;
	}
	
	public void update(PApplet p) {
		for(EventListener eventListener: listener) {
			eventListener.handle(this, p);
		}
		
		if(!(this instanceof ReplayEvent)) {
			// Replay Event as a special case. 
			// It does not just trigger and run but run in a time interval
			setHandled(true);
		} 
	}
	
	public void replay(PApplet p) {
		if(isReplayed() == false) {
			for(EventListener eventListener: listener) {
				eventListener.handle(this, p);
			}
			setReplayed(true);
		}
	}
	
	public Player getCaller() {
		return caller;
	}

	public void setCaller(Player caller) {
		this.caller = caller;
	}
}
