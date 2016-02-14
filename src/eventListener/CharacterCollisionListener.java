package eventListener;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import NetWorking.Player;
import NetWorking.Renderable;
import event.CharacterCollisionEvent;
import event.Event;
import processing.core.PApplet;
import scriptManager.ScriptManager;

public class CharacterCollisionListener implements EventListener, Serializable{
	private static final long serialVersionUID = 648992709159566406L;
	private Player caller;
	
	public CharacterCollisionListener(Player caller, BlockingQueue<Renderable> objectList) {
		this.caller = caller;
	}

	@Override
	public boolean handle(Event event, PApplet p) {
		if(event instanceof CharacterCollisionEvent) {
			if(((CharacterCollisionEvent) event).getSignal().equals("left")) {
				
				ScriptManager.loadScript("script/collisionLeft.js");
				ScriptManager.bindArgument("game_object", (Player) caller);
				ScriptManager.executeScript((CharacterCollisionEvent)event);
				
			} else if(((CharacterCollisionEvent) event).getSignal().equals("right")) {

				ScriptManager.loadScript("script/collisionRight.js");
				ScriptManager.bindArgument("game_object", (Player) caller);
				ScriptManager.executeScript((CharacterCollisionEvent)event);
			}
			return true;
		} else {
			return false;	
		}
	}

}
