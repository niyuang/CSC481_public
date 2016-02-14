package NetWorking;

import java.util.concurrent.BlockingQueue;

import processing.core.PApplet;
import processing.core.PImage;

public interface Renderable {
	public int getPosX();

	public void setPosX(int PosX);

	public int getPosY() ;

	public void setPosY(int PosY);
	
	public int getWidth();

	public int getHeight();
	
	public void render(PApplet p, BlockingQueue<Renderable> objectList, PImage image);

	public void updatePosition() ;
	
}
