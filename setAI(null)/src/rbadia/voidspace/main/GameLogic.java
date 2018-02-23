package rbadia.voidspace.main;

import java.awt.event.ActionEvent;



import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigAsteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.BulletBoss;
import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.BulletSuperBoss;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.SuperBoss;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.sounds.SoundManager;


/**
 * Handles general game logic and status.
 */
public class GameLogic {
	private GameScreen gameScreen;
	private GameScreen2 gameScreen2;
	private GameStatus status;
	private SoundManager soundMan;

	private MegaMan megaMan;

	private Boss boss;
	private Boss boss2;
	private Boss boss3;
	private SuperBoss superBoss;
	private Asteroid asteroid;
	private Asteroid asteroid2;
	private BigAsteroid bigAsteroid;
	private List<Bullet> bullets;
	
	private List<BigBullet> bigBullets;
	private List<BulletBoss> bulletsBoss;
	private List<BulletBoss2> bulletsBoss2;
	private List<BulletSuperBoss> bulletSuperBoss;

	private Platform[] numPlatforms;
	private Floor[] floor;
	private InputHandler input;

	private long lastBulletTime;

	/**
	 * Create a new game logic handler
	 * @param gameScreen the game screen
	 */
	public GameLogic(GameScreen gameScreen){
		this.gameScreen = gameScreen;

		// initialize game status information
		status = new GameStatus();
		// initialize the sound manager
		soundMan = new SoundManager();

		// init some variables
		bullets = new ArrayList<Bullet>();
		bigBullets = new ArrayList<BigBullet>();
		bulletsBoss = new ArrayList<BulletBoss>();
		bulletsBoss2 = new ArrayList<BulletBoss2>();
		bulletSuperBoss = new ArrayList<BulletSuperBoss>();
		
	}

	/**
	 * Returns the game status
	 * @return the game status 
	 */
	public GameStatus getStatus() {
		return status;
	}

	public SoundManager getSoundMan() {
		return soundMan;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	/**
	 * Prepare for a new game.
	 */
	public void newGame(){
		status.setGameStarting(true);

		// init game variables
		bullets = new ArrayList<Bullet>();
		bulletsBoss = new ArrayList<BulletBoss>();
		bulletsBoss2 = new ArrayList<BulletBoss2>();
		bulletSuperBoss = new ArrayList<BulletSuperBoss>();
		bigBullets = new ArrayList<BigBullet>();
		
		//numPlatforms = new Platform[5];

		status.setShipsLeft(20);
		status.setLevel(1);
		status.setGameOver(false);
		status.setScore(0);
		status.setNewAsteroid(false);
		status.setNewAsteroid2(false);
		status.setNewBigAsteroid(false);
		//status.setNewFloor(false);

		// init the ship and the asteroid
		newMegaMan(gameScreen);
		newFloor(gameScreen, 9);

		newNumPlatforms(gameScreen, 8);

		//        newPlatform(gameScreen/*, 1*/);
		//        newPlatform1(gameScreen);
		newBoss(gameScreen);
		newBoss2(gameScreen);
		newBoss3(gameScreen);
		newSuperBoss(gameScreen);
		newAsteroid(gameScreen);
		newAsteroid2(gameScreen);
		newBigAsteroid(gameScreen);

		// prepare game screen
		gameScreen.doNewGame();

		// delay to display "Get Ready" message for 1.5 seconds
		Timer timer = new Timer(5000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameStarting(false);
				status.setGameStarted(true);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	/**
	 * Check game or level ending conditions.
	 */
	public void checkConditions(){
		// check game over conditions
		if(!status.isGameOver() && status.isGameStarted()){
			if(status.getShipsLeft() <= 0){
				gameOver();
			}
		}

		if(!status.isGameWon()){
			if(status.isLevelWon())
				levelWon();
		}
	}

	/**
	 * Actions to take when the game is over.
	 */
	public void gameOver(){
		status.setGameStarted(false);
		status.setGameOver(true);
		gameScreen.doGameOver();

		// delay to display "Game Over" message for 3 seconds
		Timer timer = new Timer(5000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameOver(false);
			}
		});
		timer.setRepeats(false);
		timer.start();

		//Change music back to menu screen music
		VoidSpaceMain.audioClip.close();
		VoidSpaceMain.audioFile = new File("audio/menuScreen.wav");
		try {
			VoidSpaceMain.audioStream = AudioSystem.getAudioInputStream(VoidSpaceMain.audioFile);
			VoidSpaceMain.audioClip.open(VoidSpaceMain.audioStream);
			VoidSpaceMain.audioClip.start();
			VoidSpaceMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Actions to take if game is won.
	 */

	//GAME LOOPS ON THE FIRST GAMESCREEN AND RESETS ALL VARIABLE COUNTERS
	public void gameWon(){
		status.setGameStarted(false);  //SENDS TO MAIN SCREEN/ IF COMMENTED OUT LOOPS THE GAME
		status.setGameWon(true);
		gameScreen.doGameOver();

		// delay to display "Game Won" message for 5 seconds
		Timer timer = new Timer(10000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameWon(false);
			}
		});
		timer.setRepeats(false);
		timer.start();

		//Change music back to menu screen music
		VoidSpaceMain.audioClip.close();
		VoidSpaceMain.audioFile = new File("audio/menuScreen.wav");
		try {
			VoidSpaceMain.audioStream = AudioSystem.getAudioInputStream(VoidSpaceMain.audioFile);
			VoidSpaceMain.audioClip.open(VoidSpaceMain.audioStream);
			VoidSpaceMain.audioClip.start();
			VoidSpaceMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Sets LevelWon to true to so UpdateScreen can run the drawYouPass() method.
	 */
	public void levelWon(){
		status.setLevelWon(true);
		// delay to display "Level Won" message for 7 seconds. Cannot display it until 3 seconds have passed from showing it.
		Timer timer = new Timer(7000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setLevelWon(false);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * Fire a bullet from ship.
	 */
	public void fireBullet(){
		Bullet bullet = new Bullet(megaMan);
		bullets.add(bullet);
		soundMan.playBulletSound();
	}

	/**
	 * Fire the "Power Shot" bullet
	 */
	public void fireBigBullet(){
		BigBullet bigBullet = new BigBullet(megaMan);
		bigBullets.add(bigBullet);
		soundMan.playBulletSound();
	}

	/**
	 * Move a bullet once fired from the ship.
	 * @param bullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBullet(Bullet bullet){
		if(bullet.getY() - bullet.getSpeed() >= 0){
		if (status.getUp()){
			bullet.translate(0, -bullet.getSpeed());
			return false;
		}
		else{
			bullet.translate(bullet.getSpeed(), 0);
			return false;
		}
		}
		else{
			return true;
		}
	}
	
	public void BulletBoss(Boss boss){
		BulletBoss bulletBoss = new BulletBoss(boss);
		bulletsBoss.add(bulletBoss);
		soundMan.playBulletSound();
	}

	public void BulletBoss2(Boss boss){
		BulletBoss bulletBoss2 = new BulletBoss(boss2);
		bulletsBoss.add(bulletBoss2);
		soundMan.playBulletSound();
	}
	
	public void BulletBoss3(){
		BulletBoss bulletBoss3 = new BulletBoss(boss3);
		bulletsBoss.add(bulletBoss3);
		soundMan.playBulletSound();
	}
	
	public void BulletSuperBoss(SuperBoss superBoss){
		BulletSuperBoss bulletSuperBoss = new BulletSuperBoss(this.superBoss);
		this.bulletSuperBoss.add(bulletSuperBoss);
		soundMan.playBulletSound();
		}
	
	

	/**
	 * Move a bullet once fired from the boss.
	 * @param bulletBoss the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBulletBoss(BulletBoss bulletBoss){
		if(bulletBoss.getY() - bulletBoss.getSpeed() >= 0){
			bulletBoss.translate(0, bulletBoss.getSpeed());
			
			return false;
		}
		else{
			return true;
		}
	}

	/** Move a bullet once fired from the second boss.
	 * @param bulletBoss2 the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBulletBoss2(BulletBoss2 bulletBoss2){
		if(bulletBoss2.getY() - bulletBoss2.getSpeed() >= 0){
			bulletBoss2.translate(0, bulletBoss2.getSpeed());
			return false;
		}
		else{
			return true;
		}
	}
	private int direction = 1;
	public boolean moveBulletSuperBoss(BulletSuperBoss bulletSuperBoss){
		if(!status.isDeadBoss()){
			int speedX = 0;
			switch(this.direction){
			case 1:
				direction = 2;
				break;
			case 2:
				speedX = -bulletSuperBoss.getSpeed();
				direction = 3;
				break;
			case 3:
				speedX = 2*speedX;
				direction = 4;
				break;
			case 4:
				speedX = -speedX;
				direction = 5;
				break;
			case 5:
				speedX = 2*speedX;
				direction = 1;
				break;
			}
			bulletSuperBoss.translate(speedX, bulletSuperBoss.getSpeed());
			
			return false;
		}
		else{
			return true;
		}
	}

	/** Move a "Power Shot" bullet once fired from the ship.
	 * @param bulletBoss2 the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBigBullet(BigBullet bigBullet){
		if(bigBullet.getY() - bigBullet.getBigSpeed() >= 0){
			bigBullet.translate(bigBullet.getBigSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Create a new ship (and replace current one).
	 */
	public MegaMan newMegaMan(GameScreen screen){
		this.megaMan = new MegaMan(screen);
		return megaMan;
	}

	public Floor[] newFloor(GameScreen screen, int n){
		floor = new Floor[n];
		for(int i=0; i<n; i++){
			this.floor[i] = new Floor(screen, i);
		}

		return floor;
	}

	public Platform[] newNumPlatforms(GameScreen screen, int n){
		numPlatforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.numPlatforms[i] = new Platform(screen, i);
		}
		return numPlatforms;

	}


	/**
	 * Create the first boss.
	 */
	public Boss newBoss(GameScreen screen){
		this.boss = new Boss(screen);
		return boss;
	}

	/**
	 * Create the second boss.
	 */
	public Boss newBoss2(GameScreen screen){
		this.boss2 = new Boss(screen);
		return boss2;
	}
	
	//Third boss
	public Boss newBoss3(GameScreen screen){
		this.boss3 = new Boss(screen);
		return boss3;
	}
	
	//Create SuperBoss
	public SuperBoss newSuperBoss(GameScreen screen){
		this.superBoss = new SuperBoss(screen);
		return superBoss;
	}
	
	
	

	/**
	 * Create a new asteroid.
	 */
	public Asteroid newAsteroid(GameScreen screen){
		this.asteroid = new Asteroid(screen);
		return asteroid;
	}

	/**
	 * Create a second asteroid.
	 */
	public Asteroid newAsteroid2(GameScreen screen){
		this.asteroid2 = new Asteroid(screen);
		return asteroid2;
	}

	/**
	 * Create a new big asteroid.
	 */
	public BigAsteroid newBigAsteroid(GameScreen screen){
		this.bigAsteroid = new BigAsteroid(screen);
		return bigAsteroid;
	}

	/**
	 * Returns the ship.
	 * @return the ship
	 */
	public MegaMan getMegaMan() {
		return megaMan;
	}

	public Floor[] getFloor(){
		return floor;	
	}

	public Platform[] getNumPlatforms(){
		return numPlatforms;
	}

	public Boss getBoss() {
		return boss;
	}

	public Boss getBoss2() {
		return boss2;
	}
	public Boss getBoss3() {
		return boss3;
	}
	
	public SuperBoss getSuperBoss(){
		return superBoss;
	}

	/**
	 * Returns the asteroid.
	 * @return the asteroid
	 */
	public Asteroid getAsteroid() {
		return asteroid;
	}

	public Asteroid getAsteroid2() {
		return asteroid2;
	}

	public BigAsteroid getBigAsteroid() {
		return bigAsteroid;
	}

	/**
	 * Returns the list of bullets.
	 * @return the list of bullets
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}

	/**
	 * Returns the list of the boss's bullets.
	 * @return the list of the boss's bullets
	 */
	public List<BulletBoss> getBulletBoss() {
		return bulletsBoss;
	}

	/**
	 * Returns the list of the second boss's bullets.
	 * @return the list of the second boss's bullets
	 */
	public List<BulletBoss2> getBulletBoss2() {
		return bulletsBoss2;
	}

	/**
	 * Returns the list of "Power Shot" bullets.
	 * @return the list of "Power Shot" bullets
	 */
	public List<BigBullet> getBigBullets(){
		return bigBullets;
	}
	
	public List<BulletSuperBoss> getBulletSuperBoss() {
		return bulletSuperBoss;
	}
}
