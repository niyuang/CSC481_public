package NetWorking;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import processing.core.PApplet;
import processing.core.PImage;

public class SceneComponentMovableObject extends SceneComponentObject implements Serializable, Renderable, Movable {
	private static final long serialVersionUID = 5050441320506257434L;

	public SceneComponentMovableObject(int posX, int posY, int height, int width) {
		super(posX, posY, height, width);
		startingPoint = getPosX();
		setHeight(height);
		setWidth(width);
	}

	private int speedX;
	private int speedY;
	
	private int height;
	private int width;
	
	private int savedPosX = 0;
	private int savedPosY = 0;
	private int savedSpeedX = 0;
	private int savedSpeedY = 0;
	
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

	private int startingPoint;
	
	
	public void updatePosition() {
		  setPosX(getPosX() + getSpeedX());
		  setPosY(getPosY() + getSpeedY());
	}
	
	public void render(PApplet p, BlockingQueue<Renderable> objectList, PImage image) {
		updatePosition();
		p.image(image, getPosX(), getPosY());
		moveLeftAndRight(370);
		
	}
	
	public void moveLeftAndRight(int posX) {
		if( getPosX() == startingPoint) {
			setSpeedX(1);
		} 
		
		if( getPosX() == posX) {
			setSpeedX(-1);
		}
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
	
	@Override
	public void savePosAndSpeed() {
		savedPosX = getPosX();
		savedPosY = getPosY();
		savedSpeedX = getSpeedX();
		savedSpeedY = getSpeedY();
	}

	@Override
	public void recoverPosAndSpeed() {
		setPosX(savedPosX);
		setPosY(savedPosY);
		setSpeedX(savedSpeedX);
		setSpeedY(savedSpeedY);
	}
}
