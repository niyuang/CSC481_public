package eventListener;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import NetWorking.Player;
import NetWorking.Player.Mode;
import NetWorking.Renderable;
import event.Event;
import event.EventManager;
import event.ReplayEvent;
import event.EventManager.State;
import processing.core.PApplet;

public class ReplayListener implements EventListener, Serializable{
	private static final long serialVersionUID = -2066781134850877975L;
	private Renderable caller;
	private boolean playerReset = false;
	private BlockingQueue<Renderable> objectList;
	
	public ReplayListener(Renderable caller, BlockingQueue<Renderable> objectList) {
		this.caller = caller;
		this.objectList = objectList;
	}

	@Override
	public boolean handle(Event event, PApplet p) {
		if(caller instanceof Player && event instanceof ReplayEvent) {
			EventManager.getInstance().setState(State.Replaying);
			EventManager.getInstance().setModeForAllPlayer(Mode.Replaying, objectList);
			
			
			if(playerReset == false) {	
				EventManager.getInstance().recoverPosAndSpeedAll(objectList);
				playerReset = true;
			}
			
			// When replaying finishes...
			if(p.frameCount == (((ReplayEvent) event).getStartsOn() + ((ReplayEvent) event).getReplayLength())) {
				EventManager.getInstance().setState(State.Normal);
				EventManager.getInstance().setModeForAllPlayer(Mode.Normal, objectList);
				
				((Player) caller).sendObjects();

				System.out.println("" + EventManager.getInstance().getRecordingQueue().toString());
				
				event.setHandled(true);
				EventManager.getInstance().resetRecordingQueue();
				playerReset = false;
				// need to recover frame rate after replay
				((Player) caller).setPlayInFrame(60);
				
			} 

			Event firstEvent = EventManager.getInstance().getRecordingQueue().peek();
			
			Iterator<Event> recordingEventIterator = EventManager.getInstance().getRecordingQueue().iterator();
			
			while(recordingEventIterator.hasNext()) {
				Event nextEvent = recordingEventIterator.next();
				
					if(p.frameCount - 1 == (((ReplayEvent) event).getStartsOn() + nextEvent.getHandleInWorldFrame() - firstEvent.getHandleInWorldFrame())) {
						
						if(!(nextEvent instanceof ReplayEvent)){
							nextEvent.replay(p);
						} 
					}  
			}
					
			return true;
		} else {
			return false;
		}
	}
}
