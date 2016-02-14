package NetWorking;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import processing.core.PApplet;
import processing.core.PImage;

public class SceneComponentPoint implements Serializable, Renderable{
	private static final long serialVersionUID = -6857045829634142145L;
	private int posX;
	private int posY;
	
	public SceneComponentPoint(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}


	@Override
	public void updatePosition() {
		
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void render(PApplet p, BlockingQueue<Renderable> objectList, PImage image) {
		// TODO Auto-generated method stub
		
	}

}
