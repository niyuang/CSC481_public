package eventListener;
import event.Event;
import processing.core.PApplet;

public interface EventListener {
	boolean handle(Event event, PApplet p);
}
