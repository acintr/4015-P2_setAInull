package rbadia.voidspace.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigAsteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.BulletBoss;
import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.BulletSuperBoss;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.SuperBoss;

/**
 * Manages and draws game graphics and images.
 */
public class GraphicsManager {
	private BufferedImage megaManImg;
	private BufferedImage megaFallRImg;
	private BufferedImage megaFireRImg;
	private BufferedImage floorImg;
	private BufferedImage platformImg;
	private BufferedImage bulletImg;
	private BufferedImage bigBulletImg;
	private BufferedImage asteroidImg;
	private BufferedImage asteroidExplosionImg;
	private BufferedImage megaManExplosionImg;
	private BufferedImage bossImg;
	private BufferedImage superBossImg;
	private BufferedImage bossExplosionImg;
	private BufferedImage bigAsteroidImg;
	private BufferedImage bigAsteroidExplosionImg;
	private BufferedImage galaxyImg;
	private BufferedImage porticoImg;
	private BufferedImage bienveBGImg;
	private BufferedImage bitCityImg;
	private BufferedImage bitFactoryImg;
	private BufferedImage errorImg;
	private BufferedImage initialBG;
	
	private BufferedImage bienveDialog1;
	private BufferedImage bienveDialog2;
	private BufferedImage bienveDialog3;
	private BufferedImage bienveDialog4;
	private BufferedImage bienveDialog5;
	private BufferedImage bienveDialog6;


	/**
	 * Creates a new graphics manager and loads the game images.
	 */
	public GraphicsManager(){
		// load images
		try {
			this.megaManImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaMan3.png"));
			this.megaFallRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFallRight.png"));
			this.megaFireRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireRight.png"));
			this.floorImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFloor.png"));
			this.platformImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/platform3.png"));
			this.bossImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/boss1.png"));
			this.superBossImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/boss2.png"));
			this.bossExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/shipExplosion.png"));
			this.asteroidImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroid.png"));
			this.asteroidExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroidExplosion.png"));
//			this.megaManExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaManExplosion.png"));
			this.bulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bullet.png"));
			this.bigBulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigBullet.png"));
			//			this.bigAsteroidImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/BigAsteroid.png"));
			//			this.bigAsteroidExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigAsteroidExplosion.png"));
			this.galaxyImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/galaxy.jpeg"));
			this.porticoImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/portico.jpeg"));
			this.bitCityImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/8bitcity.jpg"));
			this.bitFactoryImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/factory.png"));
			this.errorImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/error.jpg"));
			this.initialBG = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/initialBG.jpg"));
			
			this.bienveBGImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/BienveBG.png"));
			this.bienveDialog1 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/text1.png"));
			this.bienveDialog2 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/text2.png"));
			this.bienveDialog3 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/text3.png"));
			this.bienveDialog4 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/text4.png"));
			this.bienveDialog5 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/text5.png"));
			this.bienveDialog6 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/text6.png"));
			
			

//			this.bigAsteroidImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/BigAsteroid.png"));
//			this.bigAsteroidExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigAsteroidExplosion.png"));


		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"VoidSpace - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Draws a ship image to the specified graphics canvas.
	 * @param ship the ship to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBG ( Graphics2D g2d, ImageObserver observer, int level){
		BufferedImage background;
		int x=0;
		int y=0;
		switch(level){
		default:
			//background=mainMenuImage;
			g2d.setPaint(Color.BLACK);
			g2d.fillRect(0, 0, 500, 400);
			return;
		
		case 1:
			background = galaxyImg;
			x=-500;
			y=-300;
			break;
			
		case 2:
			background = porticoImg;
			x=-160;
			y=-50;
			break;
			
		case 3:
			background =bitCityImg;
			y=-200;
			break;
		case 4:
			background = bitFactoryImg;
			break;
		case 5:
			background = errorImg;
			y=15;
			break;
		}
		g2d.drawImage(background, x, y, observer);	//TODO Add various backgrounds.
	}
	public void drawInitialBG( Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(initialBG,-15,-60, observer);
	}
	
	public void drawBienve ( Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(bienveBGImg,-15,-60, observer);
	}
	public void drawText ( Graphics2D g2d, ImageObserver observer, int text){
		int x = 400;
		switch(text){
		case 1:
			g2d.drawImage(bienveDialog1, 120, 20, observer);
			g2d.drawImage(bossImg, x ,150, observer);
			g2d.drawImage(bossImg, x-100 ,150, observer);
			g2d.drawImage(superBossImg, x-75 ,200, observer);
			
			break;
		case 2:
			g2d.drawImage(bienveDialog2, 120, 20, observer);
			break;
		case 3:
			g2d.drawImage(bienveDialog3, 120, 20, observer);
			break;
		case 4:
			g2d.drawImage(bienveDialog4, 120, 20, observer);
			break;
		case 5:
			g2d.drawImage(bienveDialog5, 120, 20, observer);
			g2d.drawImage(superBossImg, x-75 ,200, observer);
			break;
		case 6:
			g2d.drawImage(bienveDialog6, 120, 20, observer);
			break;
		}
	}
	
	
	
	
	
	
	
	
	public void drawMegaMan (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaManImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawMegaFallR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFallRImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawMegaFireR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFireRImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawFloor (Floor floor, Graphics2D g2d, ImageObserver observer, int i){
			g2d.drawImage(floorImg, floor.x, floor.y, observer);				
	}
	public void drawPlatform(Platform platform, Graphics2D g2d, ImageObserver observer, int i){
			g2d.drawImage(platformImg, platform.x , platform.y, observer);	
	}
	
	public void drawPlatform2 (Platform platform, Graphics2D g2d, ImageObserver observer, int i){
		g2d.drawImage(platformImg, platform.x , platform.y, observer);	
}

	/**
	 * Draws a bullet image to the specified graphics canvas.
	 * @param bullet the bullet to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBullet(Bullet bullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bulletImg, bullet.x, bullet.y, observer);
	}
	
	public void drawBulletBoss(BulletBoss bulletBoss, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bulletImg, bulletBoss.x, bulletBoss.y, observer);
	}
	
	public void drawBulletSuperBoss(BulletSuperBoss bulletSuperBoss, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bulletImg, bulletSuperBoss.x, bulletSuperBoss.y, observer);
	}

	/**
	 * Draws a bullet image to the specified graphics canvas.
	 * @param bigBullet the bullet to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBigBullet(BigBullet bigBullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigBulletImg, bigBullet.x, bigBullet.y, observer);
	}

	/**
	 * Draws an asteroid image to the specified graphics canvas.
	 * @param asteroid the asteroid to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawAsteroid(Asteroid asteroid, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidImg, asteroid.x, asteroid.y, observer);
	}
	
	public void drawBoss(Boss boss, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bossImg, boss.x, boss.y, observer);
	}
	
	public void drawSuperBoss(SuperBoss boss, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(superBossImg, boss.x, boss.y, observer);
	}

	/**
	 * Draws a ship explosion image to the specified graphics canvas.
	 * @param shipExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawMegaManExplosion(Rectangle megaManExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(megaManExplosionImg, megaManExplosion.x, megaManExplosion.y, observer);
	}

	/**
	 * Draws an asteroid explosion image to the specified graphics canvas.
	 * @param asteroidExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawAsteroidExplosion(Rectangle asteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidExplosionImg, asteroidExplosion.x, asteroidExplosion.y, observer);
	}

	public void drawBigAsteroidExplosion(Rectangle bigAsteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigAsteroidExplosionImg, bigAsteroidExplosion.x, bigAsteroidExplosion.y, observer);
	}

	public void drawBossExplosion(Rectangle bossExplosion, Graphics2D g2d, ImageObserver observer) {
//		g2d.drawImage(bossExplosionImg, bossExplosion.x, bossExplosion.y, observer);
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x, bossExplosion.y+15, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+15, bossExplosion.y, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-15, bossExplosion.y, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x, bossExplosion.y+15, observer);
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x+20, bossExplosion.y+20, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+20, bossExplosion.y-20, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-20, bossExplosion.y-20, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-20, bossExplosion.y+20, observer);
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x+15, bossExplosion.y+15, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+15, bossExplosion.y-15, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-15, bossExplosion.y-15, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-15, bossExplosion.y+15, observer);	
	}
	
	

	public void drawSuperBossExplosion(Rectangle bossExplosion, Graphics2D g2d, ImageObserver observer) {
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x, bossExplosion.y+15, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+15, bossExplosion.y, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-15, bossExplosion.y, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x, bossExplosion.y+15, observer);
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x+15, bossExplosion.y+15, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+15, bossExplosion.y-15, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-15, bossExplosion.y-15, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-15, bossExplosion.y+15, observer);
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x+20, bossExplosion.y+20, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+20, bossExplosion.y-20, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-20, bossExplosion.y-20, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-20, bossExplosion.y+20, observer);
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x, bossExplosion.y+25, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+25, bossExplosion.y, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-25, bossExplosion.y, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x, bossExplosion.y+25, observer);
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x+25, bossExplosion.y+25, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+25, bossExplosion.y-25, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-25, bossExplosion.y-25, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-25, bossExplosion.y+25, observer);
		
		g2d.drawImage(bossExplosionImg, bossExplosion.x+30, bossExplosion.y+30, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x+30, bossExplosion.y-30, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-30, bossExplosion.y-30, observer);
		g2d.drawImage(bossExplosionImg, bossExplosion.x-30, bossExplosion.y+30, observer);
		
	}
	
	public void drawSuperBossBulletExplosion(Rectangle bossExplosion, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(asteroidExplosionImg, bossExplosion.x+50, bossExplosion.y+50, observer);
	}
	

}
