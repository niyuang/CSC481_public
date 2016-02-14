package event;

import java.io.Serializable;

import NetWorking.Player;

public class InputEvent extends Event implements Serializable {
	private static final long serialVersionUID = -3354127945719715164L;
	private Type type;
	
	public InputEvent(Player caller, String signal) {
		super(caller);
		if(signal.equals("jump")) {
			type = Type.jump;
		} else if(signal.equals("left")) {
			type = Type.left;
		} else if(signal.equals("right")) {
			type = Type.right;
		} else if(signal.equals("disconnect")) {
			type = Type.disconnect;
		} else if(signal.equals("stopRecording")) {
			type = Type.stopRecording;
		} else if(signal.equals("startRecording")) {
			type = Type.startRecording;
		}else if(signal.equals("slowReplay")) {
			type = Type.slowReplay;
		}else if(signal.equals("normalReplay")) {
			type = Type.normalReplay;
		}else if(signal.equals("doubleReplay")) {
			type = Type.doubleReplay;
		}
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String toString() {
		return "Input: by " + getCaller() + type.toString() + " handled in " + getHandleInWorldFrame();
	}
	
	static public enum Type {
		jump,
		left,
		right,
		disconnect,
		startRecording,
		stopRecording,
		slowReplay,
		normalReplay,
		doubleReplay
	}
}
