package event;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import NetWorking.Movable;
import NetWorking.Player;
import NetWorking.Renderable;
import NetWorking.Server;
import event.InputEvent.Type;
import eventListener.EventListener;
import processing.core.PApplet;

public class EventManager {
	private static EventManager instance;
	private ConcurrentLinkedQueue<Event> EventQueue;
	private ConcurrentLinkedQueue<Event> RecordingQueue;

	//private static boolean recordingStarted = false;
	public State state = State.Normal;

	private EventManager() {
		EventQueue = new ConcurrentLinkedQueue<Event>();
		RecordingQueue = new ConcurrentLinkedQueue<Event>();
	} 
	
	public static synchronized EventManager getInstance() {
		
		if(instance == null) {
			synchronized(EventManager.class) {
				if(instance == null) {
					instance = new EventManager();
				}
			}
		} 
		return instance;
	}
	
	public void addEvent(Event e) {
		EventQueue.add(e);
		if(state == State.Recording) {
			RecordingQueue.add(e);
		}
	}
	
	public void removeEvent(EventListener p) {
		EventQueue.remove(p);
	}
	
	public void attach(Event e, EventListener p) {
		if(getState() == State.Replaying && e instanceof InputEvent) {
			// only in these three circumstance can an input event be proceeded
			if(((InputEvent) e).getType() == Type.doubleReplay
				|| 	((InputEvent) e).getType() == Type.normalReplay
				|| ((InputEvent) e).getType() == Type.slowReplay
				) {
				addEvent(e);
				e.addListener(p);
			}
		} else {
			addEvent(e);
			e.addListener(p);
		}
	}
	
	public synchronized void update(PApplet p) {
		Iterator<Event> event = EventQueue.iterator();
		while(event.hasNext()) {
			Event nextEvent = event.next();
			if(nextEvent.isHandled() == false) {
				nextEvent.update(p);
	
				if(p instanceof Server ) {
					nextEvent.setHandleInWorldFrame(p.frameCount);
				}
			}
		} 
	}
	
	public String toString() {
		return EventQueue.toString();
	}

	public ConcurrentLinkedQueue<Event> getEventQueue() {
		return EventQueue;
	}
	
	public ConcurrentLinkedQueue<Event> getRecordingQueue() {
		return RecordingQueue;
	}
	
	public void resetRecordingQueue() {
		RecordingQueue = new ConcurrentLinkedQueue<Event>();
	}
	
	static public enum State {
		Normal, Replaying, Recording
	}
	
	public synchronized State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public void savePosAndSpeedAll(BlockingQueue<Renderable> clientList) {
		Iterator<Renderable> clientListIterator = clientList.iterator();
		while(clientListIterator.hasNext()) {
			Renderable r = clientListIterator.next();
			if(r instanceof Movable) {
				((Movable) r).savePosAndSpeed();
			}
		}
	}
	
	public void recoverPosAndSpeedAll(BlockingQueue<Renderable> clientList) {
		Iterator<Renderable> clientListIterator = clientList.iterator();
		while(clientListIterator.hasNext()) {
			Renderable r = clientListIterator.next();
			if(r instanceof Movable) {
				((Movable) r).recoverPosAndSpeed();
			}
		}
	}
	
	public void setModeForAllPlayer(Player.Mode m, BlockingQueue<Renderable> clientList) {
		Iterator<Renderable> clientListIterator = clientList.iterator();
		while(clientListIterator.hasNext()) {
			Renderable r = clientListIterator.next();
			if(r instanceof Player) {
				((Player) r).setMode(m);
			}
		}
	}
	
	public void setReplaySpeedForAllPlayer(int frameRate, BlockingQueue<Renderable> clientList) {
		Iterator<Renderable> clientListIterator = clientList.iterator();
		while(clientListIterator.hasNext()) {
			Renderable r = clientListIterator.next();
			if(r instanceof Player) {
				((Player) r).setPlayInFrame(frameRate);
			}
		}
	}
}
