package event;

import java.io.Serializable;

import NetWorking.Player;

public class DeathEvent extends Event implements Serializable {
	public DeathEvent(Player caller) {
		super(caller);
	}

	private static final long serialVersionUID = -5870572417358169408L;

	public String toString() {
		return "death by " + getCaller() + " handled in " + getHandleInWorldFrame();
	}	
}	
