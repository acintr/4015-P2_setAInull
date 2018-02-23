package rbadia.voidspace.model;

import java.awt.Rectangle;

//import rbadia.voidspace.main.Boss;
import rbadia.voidspace.main.GameScreen;

/**
 * Represents a ship/space craft.
 *
 */
public class SuperBoss extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED3 = 4;
	private static final int Y_OFFSET = 325; // initial y distance of the ship from the bottom of the screen 
	
	private int bossWidth = 75;
	private int bossHeight = 83;
	private int bossWidth2 = 110;
	private int bossHeight2 = 100;
	private int speed3 = DEFAULT_SPEED3;
	private int superBossHealth = 100;
	private boolean isAlive = true;
	public boolean newSuperBoss;
	
	/**
	 * Creates a new ship at the default initial location. 
	 * @param screen the game screen
	 */
	public SuperBoss(GameScreen screen){
		this.setLocation((screen.getWidth() - bossWidth)/2,
				screen.getHeight() - bossHeight - Y_OFFSET);
		this.setSize(bossWidth, bossHeight);
	}
	
	/**
	 * Get the default ship width
	 * @return the default ship width
	 */
	public int getSuperBossWidth() {
		return bossWidth;
	}
	
	/**
	 * Get the default ship height
	 * @return the default ship height
	 */
	public int getSuperBossHeight2() {
		return bossHeight2;
	}
	
	/**
	 * Get the default ship width
	 * @return the default ship width
	 */
	public int getSuperBossWidth2() {
		return bossWidth2;
	}
	
	/**
	 * Get the default ship height
	 * @return the default ship height
	 */
	public int getSuperBossHeight() {
		return bossHeight;
	}
	
	/**
	 * Returns the current ship speed
	 * @return the current ship speed
	 */
	public int getSpeed_SB() {
		return speed3;
	}
	
	/**
	 * Set the current ship speed
	 * @param speed the speed to set
	 */
	public void setSpeed_SB(int speed) {
		this.speed3 = speed;
	}
	
	/**
	 * Returns the default ship speed.
	 * @return the default ship speed
	 */
	public int getDefaultSpeed(){
		return DEFAULT_SPEED3;
	}
	
	
	public synchronized boolean isNewSuperBoss() {
		return newSuperBoss;
	}

	public synchronized void setNewSuperBoss(boolean newBoss) {
		this.newSuperBoss = newBoss;
	}

	public int getSuperBossHealth() {
		return superBossHealth;
	}

	public void setSuperBossHealth(int bossHealth) {
		this.superBossHealth = bossHealth;
	}
	
	public boolean isAlive(){
		 return isAlive;
	}
	
	public void setAlive(boolean alive){
		isAlive = alive;
	}
	 
	 
	
	
}