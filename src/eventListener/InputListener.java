package eventListener;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import NetWorking.Player;
import NetWorking.Player.Mode;
import NetWorking.Renderable;
import NetWorking.networkingObject;
import event.Event;
import event.EventManager;
import event.InputEvent;
import event.ReplayEvent;
import event.EventManager.State;
import processing.core.PApplet;
import scriptManager.ScriptManager;
import time.TimeLine;

public class InputListener implements EventListener, Serializable{

	private static final long serialVersionUID = -3411337932650435294L;
	private Renderable caller;
	private boolean listening;
	private BlockingQueue<Renderable> objectList;
	private ScriptManager sm = new ScriptManager();
	
	public InputListener(Renderable caller, BlockingQueue<Renderable> objectList) {
		this.caller = caller;
		listening = true;
		this.objectList = objectList;
	}
	
	@Override
	public boolean handle(Event event, PApplet p) {
		if(caller instanceof Player && event instanceof InputEvent) {
			switch (((InputEvent) event).getType()) {
			case jump:  
				ScriptManager.loadScript("script/jump.js");
				ScriptManager.bindArgument("game_object", (Player) caller);
				ScriptManager.executeScript();
				
				break;
			case left:
				ScriptManager.loadScript("script/moveLeft.js");
				ScriptManager.bindArgument("game_object", (Player) caller);
				ScriptManager.executeScript();
				
				break;
			case right:
				ScriptManager.loadScript("script/moveRight.js");
				ScriptManager.bindArgument("game_object", (Player) caller);
				ScriptManager.executeScript();
				break;
			case startRecording:
				EventManager.getInstance().setState(State.Recording);
				EventManager.getInstance().setModeForAllPlayer(Mode.Recording, objectList);
				
				TimeLine.getInstance().startCount(p);
				EventManager.getInstance().savePosAndSpeedAll(objectList);
				
				break;
			case stopRecording:
				if(EventManager.getInstance().getState() == EventManager.State.Recording) {
					int endCounter = TimeLine.getInstance().endCounter(p);
					EventManager.getInstance().attach(new ReplayEvent(((Player) caller), endCounter, p.frameCount), ((Player) caller).getReplayListener());
				}
				
				break;
			case slowReplay:
				if(EventManager.getInstance().getState() == EventManager.State.Replaying) {
					EventManager.getInstance().setReplaySpeedForAllPlayer(15, objectList);
				}
				break;
			case normalReplay:
				if(EventManager.getInstance().getState() == EventManager.State.Replaying) {
					// change all player
					// ((Player) caller).setPlayInFrame(60);
					EventManager.getInstance().setReplaySpeedForAllPlayer(30, objectList);
				}
				break;
			case doubleReplay:
				if(EventManager.getInstance().getState() == EventManager.State.Replaying) {
					EventManager.getInstance().setReplaySpeedForAllPlayer(60, objectList);
				}
				break;
			case disconnect:
				break;
		} 
			return true;
		} else {
			return false;
		}
	}

	public boolean getListening() {
		return listening;
	}
	
	
	public void setListening(boolean listening) {
		this.listening = listening;
	} 
}
