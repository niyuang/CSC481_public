package eventListener;

import java.io.Serializable;

import NetWorking.Player;
import NetWorking.Renderable;
import event.CharacterCollisionEvent;
import event.Event;
import event.SpawnEvent;
import processing.core.PApplet;
import scriptManager.ScriptManager;

public class SpawnListener implements EventListener, Serializable {
	private static final long serialVersionUID = 8793062300880593993L;
	private Renderable caller;
	
	public SpawnListener(Renderable caller) {
		this.caller = caller;
	}

	@Override
	public boolean handle(Event event, PApplet p) {
		if(caller instanceof Player && event instanceof SpawnEvent) {
			ScriptManager.loadScript("script/spawn.js");
			ScriptManager.bindArgument("game_object", (Player) caller);
			ScriptManager.executeScript();
			return true;
		} else {
			return false;
		}
	}
}
