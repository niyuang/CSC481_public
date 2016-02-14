package event;

import java.io.Serializable;

import NetWorking.Player;
import NetWorking.Renderable;

public class CharacterCollisionEvent extends Event implements Serializable {
	private static final long serialVersionUID = 8460437012620265546L;
	private String signal;
	private Renderable collisionObject;
	
	public CharacterCollisionEvent(Player caller, String signal, Renderable collisionObject) {
		super(caller);
		this.signal = signal;
		this.collisionObject = collisionObject;
	}
	
	public String getSignal() {
		return signal;
	}	
	
	public Renderable getCollisionObject() {
		return collisionObject;
	}
}
