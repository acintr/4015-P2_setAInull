package rbadia.voidspace.model;
import java.awt.Rectangle;

/**
 * Represents a bullet fired by a ship.
 */
public class BulletSuperBoss extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	private int bulletSuperBossWidth = 8;
	private int bulletSuperBossHeight = 8;
	private int speed = 12;

	
	/**
	 * Creates a new bullet above the ship, centered on it
	 * @param ship
	 */	
	public BulletSuperBoss(SuperBoss superBoss){
		this.setLocation(superBoss.x + superBoss.width/2 - bulletSuperBossWidth/2,
				superBoss.y + superBoss.height);
		this.setSize(bulletSuperBossWidth, bulletSuperBossHeight);
	}
	

	/**
	 * Return the bullet's speed.
	 * @return the bullet's speed.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Set the bullet's speed
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}