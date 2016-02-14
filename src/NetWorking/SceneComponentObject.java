package NetWorking;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import processing.core.PApplet;
import processing.core.PImage;

public class SceneComponentObject extends SceneComponentPoint implements Serializable, Renderable{
	private static final long serialVersionUID = 8307820399801860385L;

	private int speedX;
	private int speedY;
	
	private int height;
	private int width;
	private PImage texture;

	public SceneComponentObject(int posX, int posY, PImage image) {
		super(posX, posY);
		texture = image;
		setHeight(image.height);
		setWidth(image.width);
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public SceneComponentObject(int posX, int posY, int height, int width) {
		super(posX, posY);
		setHeight(height);
		setWidth(width);
	}
	
	public void updatePosition() {
		  setPosX(getPosX() + getSpeedX());
		  setPosY(getPosY() + getSpeedY());
	}
	
	public void render(PApplet p, BlockingQueue<Renderable> objectList, PImage image) {
		p.image(image, getPosX(), getPosY());
	}
	
	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

}
