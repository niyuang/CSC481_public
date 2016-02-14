package event;

import java.io.Serializable;

import NetWorking.Player;

public class SpawnEvent extends Event implements Serializable {
	public SpawnEvent(Player caller) {
		super(caller);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -1102175309917044162L;

	public String toString() {
		return "spawn by" + getCaller() + " handled in " + getHandleInWorldFrame();
	}	
}
