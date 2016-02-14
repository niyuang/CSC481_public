package event;

import java.io.Serializable;

import NetWorking.Player;

public class ReplayEvent  extends Event implements Serializable{
	private static final long serialVersionUID = -5081922609034388561L;
	private int replayLength; 
	private int startsOn;

	public ReplayEvent(Player caller, int replayLength, int startsOn) {
		super(caller);
		this.replayLength = replayLength;
		this.startsOn = startsOn;
	}
	
	public int getReplayLength() {
		return replayLength;
	}

	public int getStartsOn() {
		return startsOn;
	}
	
	public String toString() {
		return "replay " + " handled in " + getHandleInWorldFrame();
	}
}
