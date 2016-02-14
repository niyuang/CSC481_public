package eventListener;

import java.io.Serializable;

import NetWorking.Player;
import NetWorking.Renderable;
import event.DeathEvent;
import event.Event;
import processing.core.PApplet;
import scriptManager.ScriptManager;

public class DeathListener implements EventListener, Serializable{
	private static final long serialVersionUID = -5419299807458557388L;
	private Renderable caller;
	
	public DeathListener(Renderable caller) {
		this.caller = caller;
	}
	
	@Override
	public boolean handle(Event event, PApplet p) {
		if(caller instanceof Player && event instanceof DeathEvent) {
			ScriptManager.loadScript("script/death.js");
			ScriptManager.bindArgument("game_object", (Player) caller);
			ScriptManager.executeScript();
			return true;
		} else {
			return false;
		}
	}

}
