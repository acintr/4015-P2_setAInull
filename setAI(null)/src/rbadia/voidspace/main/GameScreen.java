package rbadia.voidspace.main;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.BulletBoss;
import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.BulletSuperBoss;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.SuperBoss;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Main game screen. Handles all game graphics updates and some of the game logic.
 */
public class GameScreen extends BaseScreen{
	private static final long serialVersionUID = 1L;

	private BufferedImage backBuffer;
	private Graphics2D g2d;

	private static final int NEW_SHIP_DELAY = 500;
	private static final int NEW_ASTEROID_DELAY = 500;
	//	private static final int NEW_ASTEROID_2_DELAY = 500;
	//	private static final int NEW_BIG_ASTEROID_DELAY = 500;
	private static final int NEW_BOSS_DELAY = 500;

	//	private long lastShipTime;
	private long lastAsteroidTime;
	private long lastAsteroid2Time;
	private long lastBigAsteroidTime;
	private long lastBossTime;
	private long lastBulletTime;
	private long lastBulletTime_SB;
	private long lastBossMMCollision;

	private Rectangle asteroidExplosion;
	//	private Rectangle bigAsteroidExplosion;
	private Rectangle shipExplosion;
	private Rectangle bossExplosion;

	private JLabel shipsValueLabel;
	private JLabel destroyedValueLabel;
	private JLabel levelValueLabel;

	private Random rand;

	private Font originalFont;
	private Font bigFont;
	private Font biggestFont;

	private GameStatus status;
	private SoundManager soundMan;
	private GraphicsManager graphicsMan;
	private GameLogic gameLogic;
	//private InputHandler input;
	//private InputHandler input;
	//private Platform[] platforms;

	private int boom=0;
	private int boomBoss=0;
	private boolean boomSuperBoss = false;
	
	private int LIMIT_LEVEL1 = 10;
	private int LIMIT_LEVEL2 = LIMIT_LEVEL1 + 25;
	private int LIMIT_LEVEL3 = LIMIT_LEVEL2 + 50;
	private int LIMIT_LEVEL4 = LIMIT_LEVEL3 + 75;
	private int LIMIT_LEVEL5 = LIMIT_LEVEL4 + 100;
	
	//private int damage=0;
	//	private int scroll=0;
	//	private int bossHealth=0;
	//	private int delay=0;

	private long currentTime;
	
	/**
	 * This method initializes 
	 * 
	 */
	public GameScreen() {
		super();
		// initialize random number generator
		rand = new Random();

		initialize();

		// init graphics manager
		graphicsMan = new GraphicsManager();

		// init back buffer image
		backBuffer = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB);
		g2d = backBuffer.createGraphics();
	}

	/**
	 * Initialization method (for VE compatibility).
	 */
	protected void initialize() {
		// set panel properties
		this.setSize(new Dimension(500, 400));
		this.setPreferredSize(new Dimension(500, 400));
		this.setBackground(Color.BLACK);
	}

	/**
	 * Update the game screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw current backbuffer to the actual game screen
		g.drawImage(backBuffer, 0, 0, this);
	}
//---------------UpdateScreen------------------------------------------------------------------------------------------------
/////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/**
	 * Update the game screen's backbuffer image.
	 */
	public void updateScreen(){
		MegaMan megaMan = gameLogic.getMegaMan();
		Floor[] floor = gameLogic.getFloor();
		Platform[] numPlatforms = gameLogic.getNumPlatforms();
		List<Bullet> bullets = gameLogic.getBullets();
		Asteroid asteroid = gameLogic.getAsteroid();
		List<BigBullet> bigBullets = gameLogic.getBigBullets();
		Asteroid asteroid2 = gameLogic.getAsteroid2();
		//		BigAsteroid bigAsteroid = gameLogic.getBigAsteroid();
		List<BulletBoss> bulletsBoss = gameLogic.getBulletBoss();
		List<BulletBoss2> bulletsBoss2 = gameLogic.getBulletBoss2();
		List<BulletSuperBoss> bulletSuperBoss = gameLogic.getBulletSuperBoss();
		Boss boss = gameLogic.getBoss();
		Boss boss2 = gameLogic.getBoss2();
		Boss boss3 = gameLogic.getBoss3();
		SuperBoss superBoss = gameLogic.getSuperBoss();


		// set orignal font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		// erase screen
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		
		
		// draw 50 random stars
		//drawStars(50);

		
		
		// if the game is starting, draw "Get Ready" message
		if(status.isGameStarting()){ 
			drawYouPass();
			return;
		}

		// if the game is over, draw the "Game Over" message
		if(status.isGameOver()){
			// draw the message
			drawGameOver();

			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if((currentTime - lastBossTime) < NEW_BOSS_DELAY){
				graphicsMan.drawBossExplosion(bossExplosion, g2d, this);
			}
			return;
		}
		
		nextLevelChecker();
		
		
		//if the game is won, draw the "You Win!!!" message
		if(status.isLevelWon()){
			// draw the message
			drawYouPass();

			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if((currentTime - lastBossTime) < NEW_BOSS_DELAY){
				graphicsMan.drawBossExplosion(bossExplosion, g2d, this);
			}
			return;
		}
		
		if(status.isGameWon()){
			// draw the message
			drawYouWin();

			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if((currentTime - lastBossTime) < NEW_BOSS_DELAY){
				graphicsMan.drawBossExplosion(bossExplosion, g2d, this);
			}
			return;
		}

		drawBG();
		// the game has not started yet
		if(!status.isGameStarted()){
			// draw game title screen
			
			initialMessage();
			return;
		}
		
		
		
		//draw Floor
		for(int i=0; i<9; i++){
			graphicsMan.drawFloor(floor[i], g2d, this, i);	
		}


		for(int i=0; i<8; i++){
			graphicsMan.drawPlatform(numPlatforms[i], g2d, this, i);
			//			}
		}

		//draw MegaMan
		if(!status.isNewMegaMan()){
			if((Gravity() == true) || ((Gravity() == true) && (Fire() == true || Fire2() == true))){
				graphicsMan.drawMegaFallR(megaMan, g2d, this);
			}
		}

		if((Fire() == true || Fire2()== true) && (Gravity()==false)){
			graphicsMan.drawMegaFireR(megaMan, g2d, this);
		}

		if((Gravity()==false) && (Fire()==false) && (Fire2()==false)){
			graphicsMan.drawMegaMan(megaMan, g2d, this);
		}
		
		drawAsteroid(asteroid, status.getLevel());
		drawAsteroid(asteroid2, status.getLevel());
		asteroidCollisionChecker(asteroid, megaMan, floor, bullets, bigBullets);
		asteroidCollisionChecker(asteroid2, megaMan, floor, bullets, bigBullets);
		
		drawBoss(boss, status.getLevel());
		bossCollisionChecker(boss, megaMan, floor, bullets, bigBullets, bulletsBoss);
		
		if(status.getLevel()>2){
			drawBoss(boss2, status.getLevel());
			bossCollisionChecker(boss2, megaMan, floor, bullets, bigBullets, bulletsBoss);
		}
		
		if(status.getLevel()>3){
			drawBoss(boss3, status.getLevel());
			bossCollisionChecker(boss3, megaMan, floor, bullets, bigBullets, bulletsBoss);
		}
		
		if(status.getLevel()==5){
			drawSuperBoss(superBoss);
			superBossCollisionChecker(superBoss, megaMan, floor, bullets, bigBullets, bulletSuperBoss);
		}
		
		
		// draw bullets   
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			graphicsMan.drawBullet(bullet, g2d, this);

			boolean remove = gameLogic.moveBullet(bullet);
			if(remove){
				bullets.remove(i);
				i--;
			}
		}

		// draw big bullets
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			graphicsMan.drawBigBullet(bigBullet, g2d, this);

			boolean remove = gameLogic.moveBigBullet(bigBullet);
			if(remove){
				bigBullets.remove(i);
				i--;
			}
		}
		
		for(int i=0; i<bulletsBoss.size(); i++){
			BulletBoss bulletBoss = bulletsBoss.get(i);
			graphicsMan.drawBulletBoss(bulletBoss, g2d, this);

			boolean remove = gameLogic.moveBulletBoss(bulletBoss);
			if(remove){
				bulletsBoss.remove(i);
				i--;
			}
		}
		
		for(int i=0; i<bulletsBoss2.size(); i++){
			BulletBoss bulletBoss2 = bulletsBoss.get(i);
			graphicsMan.drawBulletBoss(bulletBoss2, g2d, this);

			boolean remove = gameLogic.moveBulletBoss(bulletBoss2);
			if(remove){
				bulletsBoss2.remove(i);
				i--;
			}
		}
		for(int i=0; i<bulletSuperBoss.size(); i++){
			BulletSuperBoss BulletSuperBoss = bulletSuperBoss.get(i);
			graphicsMan.drawBulletSuperBoss(BulletSuperBoss, g2d, this);
			
			boolean remove = gameLogic.moveBulletSuperBoss(BulletSuperBoss);
			if(remove){
				bulletSuperBoss.remove(i);
				i--;
			}
		}

		
		
		
		updateHUD();
	}  
	
	


//------------------------------Methods------------------------------------------------------------------------------------
//////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\	
	/**
	 * Draws the proper background image.
	 */
	private void drawBG(){
		
		if(!status.isGameStarted()&&!status.isGameStarting()&&!status.isGameWon()&&!status.isGameOver()){
			graphicsMan.drawInitialBG(g2d, this);
			return;
		}
		//draws black
		else if (status.isGameOver() || status.isGameStarting() || status.isGameWon() ||status.isLevelWon()){
			graphicsMan.drawBG( g2d, this, 0);
			return;	
		}
		//draws the assigned level background
		else{
				graphicsMan.drawBG( g2d, this, status.getLevel());
				return;
			}
		}
	
	/**
	 * Detects collisions between asteroids, MegaMan, bullets, and the floor.
	 * @param asteroid
	 * @param megaMan
	 * @param floor
	 * @param bullets
	 * @param bigBullets
	 */
	private void asteroidCollisionChecker(Asteroid asteroid,MegaMan megaMan,Floor[] floor,List<Bullet> bullets, List<BigBullet> bigBullets  ){
		// check bullet-asteroid1 collisions
				for(int i=0; i<bullets.size(); i++){
					Bullet bullet = bullets.get(i);
					if(asteroid.intersects(bullet)){
						// increase asteroids destroyed count
						status.setScore(status.getScore() + 100);
						removeAsteroid(asteroid);
						boom=boom + 1;
						damage=0;
						bullets.remove(i);
						break;
					}
				}

				// check big bullet-asteroid collisions
				for(int i=0; i<bigBullets.size(); i++){
					BigBullet bigBullet = bigBullets.get(i);
					if(asteroid.intersects(bigBullet)){
						// increase asteroids destroyed count
						status.setScore(status.getScore() + 100);
						removeAsteroid(asteroid);
						boom=boom + 1;
						damage=0;
					}
				}
				
				//MM-Asteroid collision
				if(asteroid.intersects(megaMan)){
					if(!status.isImmortal()){
						status.setShipsLeft(status.getShipsLeft() - 1);
					}
					removeAsteroid(asteroid);
				}

				//Asteroid-Floor collision
				for(int i=0; i<9; i++){
					if(asteroid.intersects(floor[i])){
						removeAsteroid(asteroid);

					}
				}
	}
	
	private void bossCollisionChecker(Boss boss,MegaMan megaMan,Floor[] floor,
			List<Bullet> bullets, List<BigBullet> bigBullets,
			List<BulletBoss> bulletsBoss){
		
		// check bullet-boss collisions
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(boss.intersects(bullet)){
				boss.setBossHealth(boss.getBossHealth()-1);
				if(boss.getBossHealth()<=0){
					status.setScore(status.getScore() + 500);
					removeBoss(boss);
					boomBoss=boomBoss + 1;
					damage=0;
				}
				break;
			}
		}
		// check big bullet-boss collisions
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(boss.intersects(bigBullet)){
				boss.setBossHealth(boss.getBossHealth()-1);
				if(boss.getBossHealth()<=0){
					status.setScore(status.getScore() + 500);
					removeBoss(boss);
					boomBoss=boomBoss + 1;
					damage=0;
				}
				break;
			}
		}
		//MM-Boss collision
		if(boss.intersects(megaMan)){
			if(!status.isImmortal()){
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastBossMMCollision)>1000){
					lastBossMMCollision = currentTime;
					status.setShipsLeft(status.getShipsLeft() - 2);
				}
			}
//			removeBoss(boss);
		}
		//Boss-Floor collision. Very unlikely to happen.
		for(int i=0; i<9; i++){
			if(boss.intersects(floor[i])){
				removeBoss(boss);
			}
		}
		
//		BulletsBoss-MM collision
		for(int i=0; i<bulletsBoss.size(); i++){
			BulletBoss bulletBoss = bulletsBoss.get(i);
			if(bulletBoss.intersects(megaMan)){
				if(!status.isImmortal()){
					status.setShipsLeft(status.getShipsLeft() - 1);
					
				}
				bulletsBoss.remove(i);
			}
		}
		
		
	}
	
	private void superBossCollisionChecker(SuperBoss superBoss,MegaMan megaMan,Floor[] floor,
			List<Bullet> bullets, List<BigBullet> bigBullets,
			List<BulletSuperBoss> bulletSuperBoss){
		
		// check bullet-boss collisions
				for(int i=0; i<bullets.size(); i++){
					Bullet bullet = bullets.get(i);
					if(superBoss.intersects(bullet)){
						
						superBoss.setSuperBossHealth(superBoss.getSuperBossHealth()-1);
						
						if(superBoss.getSuperBossHealth()<=0){
							status.setScore(status.getScore() + 10000);
							status.setDeadBoss(true);
							boomBoss=boomBoss + 1;
							damage=0;
							removeSuperBoss(superBoss);
						}
						bullets.remove(bullet);
						graphicsMan.drawSuperBossBulletExplosion(superBoss, g2d, this);
						
						break;
					}
				}
				// check big bullet-boss collisions
				for(int i=0; i<bigBullets.size(); i++){
					BigBullet bigBullet = bigBullets.get(i);
					if(superBoss.intersects(bigBullet)){
						superBoss.setSuperBossHealth(superBoss.getSuperBossHealth()-1);
						if(superBoss.getSuperBossHealth()<=0){
							status.setScore(status.getScore() + 10000);
							status.setDeadBoss(true);
							boomBoss=boomBoss + 1;
							boomSuperBoss = true;
							damage=0;
							bullets.remove(bigBullet);
						}
						break;
					}
				}
				
				
//				BulletsBoss-MM collision
				for(int i=0; i<bulletSuperBoss.size(); i++){
					BulletSuperBoss bulletBoss = bulletSuperBoss.get(i);
					if(bulletBoss.intersects(megaMan)){
						if(!status.isImmortal()){
							status.setShipsLeft(status.getShipsLeft() - 1);
						}
							bulletSuperBoss.remove(i);
					}
				}
		
		
	}
	
	
	private void drawAsteroid(Asteroid asteroid, int level){
		// draw first asteroid
		
		switch (level){
		case 1:
				if(!asteroid.isNewAsteroid() && boom < LIMIT_LEVEL1){
					// draw the asteroid until it reaches the bottom of the screen

					//LEVEL 1
					if((asteroid.getX() + asteroid.getAsteroidWidth() >  0)/*&& (boom <= 5 || boom == 15)*/){
						asteroid.translate(-asteroid.getSpeed(), 0);
						graphicsMan.drawAsteroid(asteroid, g2d, this);	
					}
					else if (boom < LIMIT_LEVEL1){
						asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
								rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
						asteroid.setSpeed(rand.nextInt(8)+1);
					}	
				}
				break;

		case 2:
				if(!asteroid.isNewAsteroid() && (boomBoss < LIMIT_LEVEL2 && boom >= LIMIT_LEVEL1)){
					// draw the asteroid until it reaches the bottom of the screen
					//LEVEL 2
					if((asteroid.getX() + asteroid.getAsteroidWidth() >  0)){
						asteroid.translate(-asteroid.getSpeed(), yPathPattern(asteroid.getX(),asteroid.getSpeed(),4));
						graphicsMan.drawAsteroid(asteroid, g2d, this);	
					}
					else if (boomBoss < LIMIT_LEVEL2){
						asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
								rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
						asteroid.setSpeed(rand.nextInt(8)+1);
					}	
				}
				break;
				
		case 3:
				//LEVEL 3 
				 if(!asteroid.isNewAsteroid() && (boomBoss < LIMIT_LEVEL3 && boomBoss >= LIMIT_LEVEL2)){
					if((asteroid.getX() + asteroid.getAsteroidWidth() >  0)){
				
						asteroid.translate(-asteroid.getSpeed()*2, yPathPattern(asteroid.getX(),asteroid.getSpeed(),asteroid.getAsteroidPath()));
						graphicsMan.drawAsteroid(asteroid, g2d, this);	
					}
					else if (boomBoss < LIMIT_LEVEL3){
						asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
								rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
						asteroid.setSpeed(rand.nextInt(8)+1);
					}	
				}
				 break;
				
		case 4:
				//LEVEL 4
				if(!asteroid.isNewAsteroid() && (boomBoss < LIMIT_LEVEL4 && boomBoss >= LIMIT_LEVEL3)){
					if((asteroid.getX() + asteroid.getAsteroidWidth() >  0)){
						asteroid.translate(-asteroid.getSpeed()*4, yPathPattern(asteroid.getX(),asteroid.getSpeed(),asteroid.getAsteroidPath()));
						graphicsMan.drawAsteroid(asteroid, g2d, this);	
					}
					else if (boomBoss < LIMIT_LEVEL4){
						asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
								rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
						asteroid.setSpeed(rand.nextInt(8)+1);
					}	
				}
				break;
			
		case 5:
				//LEVEL 5
				 if(!asteroid.isNewAsteroid() && (boomBoss < LIMIT_LEVEL5 && boomBoss >= LIMIT_LEVEL4)){
					if((asteroid.getX() + asteroid.getAsteroidWidth() >  0)){
						asteroid.translate(-asteroid.getSpeed()*3, yPathPattern(asteroid.getX(),asteroid.getSpeed(),asteroid.getAsteroidPath()));
						graphicsMan.drawAsteroid(asteroid, g2d, this);	
					}
					else if (boomBoss < LIMIT_LEVEL5){
						asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
								rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
						asteroid.setSpeed(rand.nextInt(8)+1);
					}	
				}
				 break;
		}
		
			if(asteroid.isNewAsteroid()){ 
				asteroid.setSpeed(rand.nextInt(8)+1);
				asteroid.setAsteroidPath(rand.nextInt(4));
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
					// draw a new asteroid
					lastAsteroidTime = currentTime;
					asteroid.setNewAsteroid(false);
					asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
							rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
				}
				else{
					// draw explosion
					graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
				}
			}
		
	}
	
	private void drawBoss(Boss boss, int level){
		switch(level){
		case 1:
			//NO BOSS AT LEVEL 1
			break;
		case 2:
			
			if(!boss.isNewBoss() && (boomBoss < LIMIT_LEVEL2 && boom >= LIMIT_LEVEL1)){

				if((boss.getX() + boss.getBossWidth() >  0) /*&& (boom <= 5 || boom == 15)*/){
					boss.translate(-boss.getSpeed3(), 0);
					graphicsMan.drawBoss(boss, g2d, this);	
				}
				else if (boomBoss < LIMIT_LEVEL2){
					boss.setLocation(this.getWidth() - boss.getBossWidth(),
							rand.nextInt(this.getHeight() - boss.getBossHeight() - 32));
					boss.setSpeed(rand.nextInt(8)+1);
				}	
			}
						
				if(!boss.isNewBoss()){
					currentTime = System.currentTimeMillis();
					if((currentTime - lastBulletTime)>1000/2){
						lastBulletTime = currentTime;
						gameLogic.BulletBoss(boss);
//						gameLogic.BulletBoss2(boss);
					}
				}
			
			
			break;
		case 3:	
			
			if(!boss.isNewBoss() && (boomBoss < LIMIT_LEVEL3 && boomBoss >= LIMIT_LEVEL2)){

				if((boss.getX() + boss.getBossWidth() >  0) /*&& (boom <= 5 || boom == 15)*/){
					boss.translate(-boss.getSpeed3(), 0);
					graphicsMan.drawBoss(boss, g2d, this);	
				}
				else if (boomBoss < LIMIT_LEVEL3){
					boss.setLocation(this.getWidth() - boss.getBossWidth(),
							rand.nextInt(this.getHeight() - boss.getBossHeight() - 32));
					boss.setSpeed(rand.nextInt(8)+1);
				}	
			}
						
				if(!boss.isNewBoss()){
					currentTime = System.currentTimeMillis();
					if((currentTime - lastBulletTime)>1000/3){
						lastBulletTime = currentTime;
						gameLogic.BulletBoss(boss);
						gameLogic.BulletBoss2(boss);
					}
				}
			
			break;
		case 4:
			
			if(!boss.isNewBoss() && (boomBoss < LIMIT_LEVEL4 && boomBoss >= LIMIT_LEVEL3)){

				if((boss.getX() + boss.getBossWidth() >  0)/*&& (boom <= 5 || boom == 15)*/){
					boss.translate(-boss.getSpeed3(), 0);
					graphicsMan.drawBoss(boss, g2d, this);	
				}
				else if (boomBoss < LIMIT_LEVEL4){
					boss.setLocation(this.getWidth() - boss.getBossWidth(),
							rand.nextInt(this.getHeight() - boss.getBossHeight() - 32));
					boss.setSpeed(rand.nextInt(8)+1);
				}	
			}
						
				if(!boss.isNewBoss()){
					currentTime = System.currentTimeMillis();
					if((currentTime - lastBulletTime)>1000/4){
						lastBulletTime = currentTime;
						gameLogic.BulletBoss(boss);
						gameLogic.BulletBoss2(boss);
					}
				}
			
			break;
		case 5:
			
			if(!boss.isNewBoss() && (boomBoss < LIMIT_LEVEL5 && boomBoss >= LIMIT_LEVEL4)){

				if((boss.getX() + boss.getBossWidth() >  0) /*&& (boom <= 5 || boom == 15)*/){
					boss.translate(-boss.getSpeed3(), 0);
					graphicsMan.drawBoss(boss, g2d, this);	
				}
				else if (boomBoss < LIMIT_LEVEL5){
					boss.setLocation(this.getWidth() - boss.getBossWidth(),
							rand.nextInt(this.getHeight() - boss.getBossHeight() - 32));
					boss.setSpeed(rand.nextInt(8)+1);
				}	
			}
						
				if(!boss.isNewBoss()){
					currentTime = System.currentTimeMillis();
					if((currentTime - lastBulletTime)>1000/5){
						lastBulletTime = currentTime;
						gameLogic.BulletBoss(boss);
						gameLogic.BulletBoss2(boss);

					}
				}
			
			break;
		}
		
		if(boss.isNewBoss()){
			boss.setSpeed(rand.nextInt(8)+1);
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastBossTime) > NEW_BOSS_DELAY){
				// draw a new asteroid
				lastBossTime = currentTime;
				boss.setNewBoss(false);
				boss.setLocation(this.getWidth() + 50,
						rand.nextInt(this.getHeight() - boss.getBossHeight() - 32));
				boss.setBossHealth(3);
			}
			else{
				// draw explosion
				graphicsMan.drawSuperBossExplosion(bossExplosion, g2d, this);
			}
		}
		
	}
	
	
	public void drawSuperBoss(SuperBoss superBoss){
		
		if(!superBoss.isNewSuperBoss() && (boomBoss < LIMIT_LEVEL5 && boomBoss >= LIMIT_LEVEL4)){

			if((superBoss.getX() <= 75)){
				superBoss.setSpeed_SB(-superBoss.getSpeed_SB());
			}
			if((superBoss.getX() + superBoss.getWidth() >= this.getWidth() - 100)){
				superBoss.setSpeed_SB(-superBoss.getSpeed_SB());
			}
			
			currentTime = System.currentTimeMillis();
			if((currentTime - lastBulletTime_SB)>1000/5){
				lastBulletTime_SB = currentTime;
				gameLogic.BulletSuperBoss(superBoss);
			}
			
				superBoss.translate(superBoss.getSpeed_SB(), 0);
				graphicsMan.drawSuperBoss(superBoss, g2d, this);
		} else if (boomBoss < LIMIT_LEVEL5){
				superBoss.setLocation(this.getWidth()/2 - superBoss.getSuperBossWidth(),
							this.getHeight()/2 - superBoss.getSuperBossHeight());
				superBoss.setSpeed_SB(rand.nextInt(8)+1);
			}	
			
			if(superBoss.isNewSuperBoss()){
				superBoss.setSpeed_SB(rand.nextInt(8)+1);
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastBossTime) > NEW_BOSS_DELAY){
					// draw a new asteroid
					lastBossTime = currentTime;
					superBoss.setNewSuperBoss(false);
					
					superBoss.setLocation(this.getWidth()/2 - superBoss.getSuperBossWidth(),
							this.getHeight()/2 - superBoss.getSuperBossHeight());
					superBoss.setSuperBossHealth(50);
				}
				else{
					// draw explosion
//					if(superBoss.getSuperBossHealth() <= 0){
					if(status.isDeadBoss()){		
						graphicsMan.drawSuperBossExplosion(superBoss, g2d, this);
					}
				}
			}				
	}
	
	
	
	private void nextLevelChecker(){
		switch(status.getLevel()){
		case 1:
			if(boom == LIMIT_LEVEL1)
				nextLevel(2); 
			break;
		case 2: 
			if(boomBoss == LIMIT_LEVEL2)
				nextLevel(3);
			break;
		case 3: 
			if(boomBoss == LIMIT_LEVEL3)
				nextLevel(4);
			break;
		case 4: 
			if(boomBoss == LIMIT_LEVEL4)
				nextLevel(5);
			break;
		case 5: if(boomBoss == LIMIT_LEVEL5 && status.isDeadBoss())
				nextLevel(6);
			break;
		}
	}
	
	/**
	 * Returns the desired dy values for the translate method to use for certain paths.
	 * @param x - the x coordinate
	 * @param speed
	 * @param pathType -0=V, 1=Z, 2=N, 3=Off-Centered Parabola, 4= Sine wave.
	 * @return dy
	 */
	private int yPathPattern(double x, int speed, int pathType ){
		/*
		 * Path Type:
		 * 0=v
		 * 1=z
		 * 2=N
		 * 3=parabola a(how sudden the curve)(x-b(center of curve))*2
		 * 4=sine
		 */
		
		int dy = 0;
		switch(pathType){
		case 0:
			if(x<=250){
				dy=-speed;
			}
			else{
				dy=speed;
			}
			break;
			
		case 1: 
			if(x==100||x==300){
			dy=speed*11;
		}
		else if (x==200||x==400){
			dy=-speed*18;
		}
			break;
			
		case 2: 
			if(x<200&&x>=100||x<400&&x>=300){
			dy=speed;
		}
		else if (x<300&&x>=200||x>=400||x<100){
			dy=-speed;
		}
			break;	
		case 3:
			dy= (int) (0.03*(x-300)*2);
			break;
			
		case 4:
			dy= (int) ((int) 5*Math.sin(x*0.02));
			
			
		}
		return dy;
	}
	/**
	 * Draws the "Game Over" message.
	 */
	protected void drawGameOver() {
		String gameOverStr = "FATAL ERROR: SERVER DESTROYED";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameOverStr);
		if(strWidth == this.getWidth() - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(gameOverStr);
		}
		else if (strWidth>this.getWidth()-10){
			bigFont = currentFont.deriveFont(fontSize-1);
			biggestFont = currentFont.deriveFont(fontSize-1);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.RED);
		g2d.drawString(gameOverStr, strX, strY);

		boomReset();
		healthReset();
		delayReset();
	}

	/**
	 * Draws the "Next Level" message.
	 */
	protected void drawYouPass() {
		drawBienveDialogue(status.getLevel());
	}
	
	/**
	 * Draws the "Game Won" message.
	 */
	protected void drawYouWin() {
		drawBienveDialogue(6);
	}

	/**
	 * Draws the initial "Get Ready!" message.
	 */
	protected void drawGetReady() {/*NOT USED*/}

	/**
	 * Draws the specified number of stars randomly on the game screen.
	 * @param numberOfStars the number of stars to draw
	 */
	protected void drawStars(int numberOfStars) {
		g2d.setColor(Color.WHITE);
		for(int i=0; i<numberOfStars; i++){
			int x = (int)(Math.random() * this.getWidth());
			int y = (int)(Math.random() * this.getHeight());
			g2d.drawLine(x, y, x, y);
		}
	}

	/**
	 * Display initial game title screen.
	 */
	protected void initialMessage() {
		String gameTitleStr = "set_AI(Null);";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize+1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameTitleStr);
		if(strWidth == this.getWidth() - 10){
			bigFont = currentFont;
			biggestFont = currentFont;
			System.out.println(currentFont);
			fm = g2d.getFontMetrics(currentFont);
			strWidth = fm.stringWidth(gameTitleStr);
		}
		else if (strWidth>this.getWidth()-10){
			bigFont = currentFont.deriveFont(fontSize-1);
			biggestFont = currentFont.deriveFont(fontSize-1);
		}
		g2d.setFont(bigFont);
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2 - ascent;
		g2d.setPaint(new Color(127,0,255));
		g2d.drawString(gameTitleStr, strX, strY);

		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Press <Space> to Start a New Game.";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(newGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String itemGameStr = "Press <I> for Item Menu.";
		strWidth = fm.stringWidth(itemGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(itemGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String shopGameStr = "Press <S> for Shop Menu.";
		strWidth = fm.stringWidth(shopGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(shopGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String exitGameStr = "Press <Esc> to Exit the Game.";
		strWidth = fm.stringWidth(exitGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(exitGameStr, strX, strY);
	}
	
	private void drawBienveDialogue(int level) {
		 graphicsMan.drawBienve(g2d, this);
		 graphicsMan.drawText(g2d, this, level);
		return;	
	}
	
	
	
	

	/**
	 * Prepare screen for game over.
	 */
	public void doGameOver(){
		shipsValueLabel.setForeground(new Color(128, 0, 0));
		boomReset();
	}

	/**
	 * Prepare screen for a new game.
	 */
	public void doNewGame(){		
		lastAsteroidTime = -NEW_ASTEROID_DELAY;
		//lastBigAsteroidTime = -NEW_BIG_ASTEROID_DELAY;
		lastShipTime = -NEW_SHIP_DELAY;

		bigFont = originalFont;
		biggestFont = null;

		// set labels' text
		shipsValueLabel.setForeground(Color.BLACK);
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));
		destroyedValueLabel.setText(Long.toString(status.getScore()));
		levelValueLabel.setText(Long.toString(status.getLevel()));
	}

	/**
	 * Sets the game graphics manager.
	 * @param graphicsMan the graphics manager
	 */
	public void setGraphicsMan(GraphicsManager graphicsMan) {
		this.graphicsMan = graphicsMan;
	}

	/**
	 * Sets the game logic handler
	 * @param gameLogic the game logic handler
	 */
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.status = gameLogic.getStatus();
		this.soundMan = gameLogic.getSoundMan();
	}

	/**
	 * Sets the label that displays the value for asteroids destroyed.
	 * @param destroyedValueLabel the label to set
	 */
	public void setDestroyedValueLabel(JLabel destroyedValueLabel) {
		this.destroyedValueLabel = destroyedValueLabel;
	}

	/**
	 * Sets the label that displays the value for ship (lives) left
	 * @param shipsValueLabel the label to set
	 */
	public void setShipsValueLabel(JLabel shipsValueLabel) {
		this.shipsValueLabel = shipsValueLabel;
	}

	public void setLevelValueLabel(JLabel levelValueLabel){
		this.levelValueLabel = levelValueLabel;
	}
	/**
	 * Returns the current value of Boom.
	 */
	public int getBoom(){
		return boom;
	}
	/**
	 * Sets Boom to a certain int value.
	 * @param x - desired value for Boom.
	 */
	public void setBoom(int x){
		boom=x;
	}
	public int boomReset(){
		boom = 0;
		boomBoss = 0;
		return boom;
	}
	public long healthReset(){
		boom = 0;
		return boom;
	}
	public long delayReset(){
		boom = 0;
		return boom;
	}

	protected boolean Gravity(){
		MegaMan megaMan = gameLogic.getMegaMan();
		Floor[] floor = gameLogic.getFloor();

		for(int i=0; i<9; i++){
			if((megaMan.getY() + megaMan.getMegaManHeight() -17 < this.getHeight() - floor[i].getFloorHeight()/2) 
					&& Fall() == true){

				megaMan.translate(0 , 2);
				return true;

			}
		}
		return false;
	}
	//Bullet fire pose
	protected boolean Fire(){
		MegaMan megaMan = gameLogic.getMegaMan();
		List<Bullet> bullets = gameLogic.getBullets();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if((bullet.getX() > megaMan.getX() + megaMan.getMegaManWidth()) && 
					(bullet.getX() <= megaMan.getX() + megaMan.getMegaManWidth() + 60)){
				return true;
			}
		}
		return false;
	}

	//BigBullet fire pose
	protected boolean Fire2(){
		MegaMan megaMan = gameLogic.getMegaMan();
		List<BigBullet> bigBullets = gameLogic.getBigBullets();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if((bigBullet.getX() > megaMan.getX() + megaMan.getMegaManWidth()) && 
					(bigBullet.getX() <= megaMan.getX() + megaMan.getMegaManWidth() + 60)){
				return true;
			}
		}
		return false;
	}

	//Platform Gravity
	public boolean Fall(){
		MegaMan megaMan = gameLogic.getMegaMan(); 
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<8; i++){
			if((((platform[i].getX() < megaMan.getX()) && (megaMan.getX()< platform[i].getX() + platform[i].getPlatformWidth()))
					|| ((platform[i].getX() < megaMan.getX() + megaMan.getMegaManWidth()) 
							&& (megaMan.getX() + megaMan.getMegaManWidth()< platform[i].getX() + platform[i].getPlatformWidth())))
					&& megaMan.getY() + megaMan.getMegaManHeight() == platform[i].getY()
					){
				return false;
			}
		}
		return true;
	}

	/**
	 * Reorganizes the platform locations based on the recieved level, then adds 1 to the current level label.
	 * @param level - The level platform pattern desired.
	 */
	public void restructure(int level){
		Platform[] platform = gameLogic.getNumPlatforms();
		switch(level){
		case 1:return;
		
		case 2:	
			for(int i=0; i<8; i++){
			if(i<4)	platform[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			if(i==4) platform[i].setLocation(50 +i*50, getHeight()/2 + 140 - 3*40);
			if(i>4){	
				int n=4;
				platform[i].setLocation(50 + i*50, getHeight()/2 + 20 + (i-n)*40 );
				n=n+2;
				}
			this.setBackground(Color.BLACK);
			}break;
		case 3:
			for(int i=0; i<8; i++){
				if(i>4)	platform[i].setLocation( i*50-60, getHeight()/2 + 140 - i*40);
				if(i==4) platform[i].setLocation(20 +2*50, getHeight()/2 -10);
				if(i<4){	
					int n=4;
					platform[i].setLocation(50 + i*50, getHeight() - (n-i)*40 );//TODO set this level 
					n=n+2;
					}
				} break;
		case 4:for(int i=0; i<8; i++){
			if(i>4)	platform[i].setLocation( i*50-70, getHeight()/2 -10);
			if(i==4) platform[i].setLocation(getWidth()/2-20, getHeight()/4+40);
			if(i<4){	
				int n=4;
				platform[i].setLocation(getWidth()/2-20, getHeight() - (n-i)*40 );//TODO set this level 
				n=n+2;
				}
			}  break;
		case 5:for(int i=0; i<8; i++){
			if(i>=1)	platform[i].setLocation(40+ i*50, getHeight()-110);
			if(i==0) platform[i].setLocation(40, getHeight()-60);
			}  break;
		}
		status.setLevel(status.getLevel() + 1);
	}

	public void removeAsteroid(Asteroid asteroid){
		// "remove" asteroid
		asteroidExplosion = new Rectangle(
				asteroid.x,
				asteroid.y,
				asteroid.width,
				asteroid.height);
		asteroid.setLocation(-asteroid.width, -asteroid.height);
		asteroid.setNewAsteroid(true); //!!!!!
		lastAsteroidTime = System.currentTimeMillis();

		// play asteroid explosion sound
		soundMan.playAsteroidExplosionSound();
	}
	
	public void removeBoss(Boss boss){
		bossExplosion = new Rectangle(
				boss.x,
				boss.y,
				boss.width,
				boss.height);
		boss.setLocation(1000, 1000);
		boss.setNewBoss(true); //!!!!!
		lastBossTime = System.currentTimeMillis();

		// play asteroid explosion sound
		soundMan.playAsteroidExplosionSound();
		
	}
	
	public void removeSuperBoss(SuperBoss superBoss){
		bossExplosion = new Rectangle(
				superBoss.x,
				superBoss.y,
				superBoss.width,
				superBoss.height);
		superBoss.setLocation(1000, 1000);
		superBoss.setNewSuperBoss(true); //!!!!!
		lastBossTime = System.currentTimeMillis();

		// play asteroid explosion sound
		soundMan.playAsteroidExplosionSound();
		
	}
	
	
	
	/**
	 * Sets Boom to the appropriate level value, checks to win game, calls Restructure(), and updates HUD.
	 * @param level
	 */
	public void nextLevel(int level){
		switch(level){
		case 1: 
			return;
		case 2: 
			boom = LIMIT_LEVEL1; 
			break;
		case 3: 
			boomBoss = LIMIT_LEVEL2;
			break;
		case 4:
			boomBoss = LIMIT_LEVEL3;
			break;
		case 5:
			boomBoss = LIMIT_LEVEL4;
			break;
		case 6: 
		gameLogic.gameWon(); 
		return;
		}
		
		
		gameLogic.levelWon();
		restructure(level);

		status.getScore();
		status.getShipsLeft();
		status.getLevel();

		updateHUD();
	}
	
	public void updateHUD(){
		destroyedValueLabel.setText(Long.toString(status.getScore()));

		// update ships left label
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));

		//update level label
		levelValueLabel.setText(Long.toString(status.getLevel()));
	}
	public Asteroid getAsteroid(Asteroid asteroid){
		return asteroid;
	}
	
	public int yAlternatingPath(int speed){
		return -speed;
		
	}
	
}
