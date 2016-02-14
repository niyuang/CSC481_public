package NetWorking;

public interface Movable {
	public int getSpeedX();

	public void setSpeedX(int speedX);

	public int getSpeedY();

	public void setSpeedY(int speedY);
	
	public void savePosAndSpeed();
	
	public void recoverPosAndSpeed();
}
