package NetWorking;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import event.CharacterCollisionEvent;
import event.DeathEvent;
import event.EventManager;
import event.InputEvent;
import event.SpawnEvent;
import eventListener.CharacterCollisionListener;
import eventListener.DeathListener;
import eventListener.InputListener;
import eventListener.ReplayListener;
import eventListener.SpawnListener;
import processing.core.PApplet;
import processing.core.PImage;

public class Player extends networkingObject implements Serializable, Renderable, Movable {
	private static final long serialVersionUID = -7261541182714362022L;

	private int speedX;
	private int speedY;
	
	private int height = 20;
	private int width = 20;
	
	private int jumpHeight = 150;
	
	private State state;
	private Mode mode = Mode.Normal;
	
	public Mode getMode() {
		return mode;
	}

	public int getPlayInFrame() {
		return playInFrame;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	private int previousStep;
	private int horizontal;
	private int playInFrame = 60;

	public void setPlayInFrame(int playInFrame) {
		this.playInFrame = playInFrame;
	}

	public Renderable supportBy;
	
	private InputListener inputListener = new InputListener(this, getObjectList());
	private DeathListener deathListener = new DeathListener(this);
	private SpawnListener spawnListener = new SpawnListener(this);
	private ReplayListener replayListener = new ReplayListener(this, getObjectList());
	private CharacterCollisionListener characterCollisionListener = new CharacterCollisionListener(this, getObjectList());
	
	private int savedPosX = 0;
	private int savedPosY = 0;
	private int savedSpeedX = 0;
	private int savedSpeedY = 0;
	
	static public enum State {
		Walking, Rising, Falling, FallingCollide, Dead
	}
	
	static public enum Mode{
		Normal, Replaying, Recording
	}
	
	public State getPlayerState() {
		return state;
	}

	public void setPlayerState(State state) {
		this.state = state;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
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

	private void log(String message) {
	   System.out.println(message);
	}  
	
	public Player(Socket socket, int clientNumber, BlockingQueue<Renderable> objectList) {
	    super(socket, clientNumber, objectList);
	    setPosX(Server.spawnPoint.getPosX());
	    setPosY(Server.spawnPoint.getPosY());

		state = State.Walking;
		this.previousStep = getPosY();
		this.horizontal = getPosY();
	 }

	@Override
	public void run() {
	    try{   
		    while (true) {
		      String input = in.readLine();
		      if(input == null) {
		    	  break;
		      } else {
		    	  if(state != State.Dead) {
			    	  EventManager.getInstance().attach(new InputEvent(this, input), inputListener);
		    	  }
		      }
		    }
	    } catch (IOException e) {
	      log("Error handling client# " + getClientNumber() + ": " + e);
	    } finally {
	      try {
	    	  Server.removePlayer(this);
			  socket.close();
	      } catch(IOException e) {
	        log("Error closing client#" + getClientNumber() + ": " + e);
	      }
	      log("Connection with client# " + getClientNumber() + " closed");     
	    }
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((socket == null) ? 0 : socket.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (socket == null) {
			if (other.socket != null)
				return false;
		} else if (!socket.equals(other.socket))
			return false;
		return true;
	}
	
	public void collideWithWall() {
		  // collide with walls horizontally
		  if(getPosX() < 0 && getSpeedX() < 0) {
			  setPosX(0);
			  setSpeedX(0);
		  }
		  
		  if(getPosX() > 640 - getWidth() && getSpeedX() > 0) {
			  setPosX(640 - getWidth() );
			  setSpeedX(0);
		  } 
		  
		  if(getPosY() < 0 && getSpeedY() < 0) {
			  setPosY(0);
			  // setSpeedY(0);
			  state = State.Falling;
		  }
	}
	
	public void collideDeathZone() {
		if(getPosX() < Server.deathZone.getWidth() + Server.deathZone.getPosX()
			&& getPosX() + getWidth() > Server.deathZone.getPosX()
			&& getPosY() < Server.deathZone.getPosY() + Server.deathZone.getHeight()
			&& getPosY() + getHeight() > Server.deathZone.getPosY()) {

			EventManager.getInstance().attach(new DeathEvent(this), deathListener);	
		}
	}
	
	public void collideObject() {
		Iterator<Renderable> iter = getObjectList().iterator();
		while(iter.hasNext()) {
			Renderable pl = iter.next();
			if(!pl.equals(this)) {				
				// Collision in between players
				if(pl instanceof Player) {
					
					if(getPosX() + getWidth() - pl.getPosX() < getWidth() 
							  && getPosX() + getWidth() - pl.getPosX() >= 0
							  && getSpeedX() > 0
							  && pl.getPosY() - getPosY() < getHeight()) {
							  EventManager.getInstance().attach(new CharacterCollisionEvent(this, "left", pl), characterCollisionListener);
					  } 
					  
					  if(getPosX() - pl.getPosX() - pl.getWidth() > (-1) * getWidth()
							  && getPosX() - pl.getPosX() - pl.getWidth() <= 0
							  && getSpeedX() < 0
							  && pl.getPosY() - getPosY() < getHeight()) {
						  	EventManager.getInstance().attach(new CharacterCollisionEvent(this, "right", pl), characterCollisionListener);
					  }
				}
				
				if(! (pl instanceof Deathzone) ) {
					if(getPosX() < pl.getPosX() + pl.getWidth() 
						&& getPosX() + getWidth() > pl.getPosX()) {
						if(getPosY() == pl.getPosY() + pl.getHeight()) {
							if(state == State.Rising) {
								state = State.Falling;
							}
						} else if (getPosY() + getHeight() == pl.getPosY()) {
							if(state == State.Falling) {
								state = State.FallingCollide;
								supportBy = (Renderable) pl;
								if(pl instanceof Player) {
									sendObjects();
								}
							}
						}
					}
					
					if(getPosY() != horizontal
						&& state == State.Walking
						&& supportBy != null
						&& ( supportBy.getPosX() - getPosX() > getWidth() 
						|| getPosX() - supportBy.getPosX() > getWidth()) ) {
							if(supportBy.getPosX() > getPosX() && supportBy.getPosX() - getPosX() > getWidth()) {
						  		state = State.Falling;
						  	} else if(getPosX() > supportBy.getPosX() + supportBy.getWidth() && getPosX() - supportBy.getPosX() > getWidth()) {
						  		state = State.Falling;
						  	}
					  } 
				}	
			}
		}
		
	}

	public void render(PApplet p, BlockingQueue<Renderable> objectLis, PImage image) {
		p.frameRate(getPlayInFrame());
		if(state != State.Dead) {
			updatePosition();
			collideWithWall();
			collideObject();
			collideDeathZone();
			
			  if(state == State.Rising) {
				  if(getPosY() == previousStep - jumpHeight) {
					  state = State.Falling;
				  }
			  } else if(state == State.Falling) {
				  setSpeedY(2);
				  if(getPosY() == horizontal) {
					  state = State.Walking;
					  setSpeedY(0);
				  }
			  } else if(state == State.FallingCollide) {
				  setSpeedY(0);
				  state = State.Walking;
				  previousStep = getPosY();
			  } 
			
			p.image(image, getPosX(), getPosY());
		} else if (state == State.Dead){
			EventManager.getInstance().attach(new SpawnEvent(this), spawnListener);
		} 
		
		EventManager.getInstance().update(p);
		p.text(getMode().toString(), 450, 125);
	}
	
	public void updatePosition() {
		  setPosX(getPosX() + getSpeedX());
		  setPosY(getPosY() + getSpeedY());
	}
	
	public void jump() {
		if(state == State.Walking) {
			state = State.Rising;
			setSpeedY(-2);
			previousStep = getPosY();
		}
	}
	
	public String toString() {
		return "Player " + getClientNumber();
	}
	
	public void reset() {
		setPlayerState(Player.State.Walking);
		setPosX(Server.spawnPoint.getPosX());
		setPosY(Server.spawnPoint.getPosY());
		setSpeedX(0);
		setSpeedY(0);
	}

	public ReplayListener getReplayListener() {
		return replayListener;
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
	
	public State getDeadState() {
		return State.Dead;
	}
}
