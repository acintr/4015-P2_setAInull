package rbadia.voidspace.main;

/**
 * Container for game flags and/or status variables.
 */
public class GameStatus {
	// game flags
	private boolean gameStarted = false;
	private boolean gameStarting = false;
	private boolean gameOver = false;
	private boolean gameWon = false;
	private boolean levelWon = false;
	
	private boolean up;
	
	// status variables
	private boolean newMegaMan;
	private boolean newBoss;
	private boolean newAsteroid;
	private boolean newAsteroid2;
	private boolean newBigAsteroid;
	private boolean newFloor;
	private boolean newPlatform;
	private int shipsLeft;
	private int level = 1;
	private int maxLevels =2;
	private boolean immortal = false;
	private boolean deadBoss;
	private long score = 0;
	
	public long getScore(){
		return score;
	}
	
	public void setScore(long newScore){
		score = newScore;
	}

	public boolean isDeadBoss() {
		return deadBoss;
	}

	public void setDeadBoss(boolean deadBoss) {
		this.deadBoss = deadBoss;
	}

	public GameStatus(){
		
	}
	
	/**
	 * Indicates if the game has already started or not.
	 * @return if the game has already started or not
	 */
	public synchronized boolean isGameStarted() {
		return gameStarted;
	}
	
	public synchronized void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}
	
	/**
	 * Indicates if the game is starting ("Get Ready" message is displaying) or not.
	 * @return if the game is starting or not.
	 */
	public synchronized boolean isGameStarting() {
		return gameStarting;
	}
	
	public synchronized void setGameStarting(boolean gameStarting) {
		this.gameStarting = gameStarting;
	}
	
	/**
	 * Indicates if the game has ended and the "Game Over" message is displaying.
	 * @return if the game has ended and the "Game Over" message is displaying.
	 */
	public synchronized boolean isGameOver() {
		return gameOver;
	}
	
	public synchronized void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	/**
	 * Indicates if the game has and the "You Win!!!" message is displaying.
	 * @return if the game has ended and the "You Win!!!" message is displaying.
	 */
	public synchronized boolean isGameWon() {
		return gameWon;
	}
	
	public boolean getUp(){
		return up;
	}
	
	public void setUp(boolean x){
		up=x;
	}
	
	public synchronized void setGameWon(boolean gameWon) {
		this.gameWon = gameWon;
	}
	
	public synchronized boolean isLevelWon() {
		return levelWon;
	}
	
	public synchronized void setLevelWon(boolean levelWon) {
		this.levelWon = levelWon;
	}
	
	/**
	 * Indicates if a new ship should be created/drawn.
	 * @return if a new ship should be created/drawn
	 */
	public synchronized boolean isNewMegaMan() {
		return newMegaMan;
	}

	public synchronized void setNewMegaMan(boolean newMegaMan) {
		this.newMegaMan = newMegaMan;
	}
	
//	public synchronized boolean isNewFloor(){
//		return newFloor;
//	}
//	
//	public synchronized void setNewFloor(boolean newFloor) {
//		this.newFloor = newFloor;
//	}
	
//	public synchronized boolean isNewPlatform(){
//		return newPlatform;
//	}
//	
//	public synchronized void setNewPlatform(boolean newPlatform){
//		this.newPlatform = newPlatform;
//	}
	
	public synchronized boolean isNewBoss() {
		return newBoss;
	}

	public synchronized void setNewBoss(boolean newBoss) {
		this.newBoss = newBoss;
	}

	public synchronized boolean isNewBoss2() {
		return newBoss;
	}

	public synchronized void setNewBoss2(boolean newBoss) {
		this.newBoss = newBoss;
	}

	

	
	
	
	
	/**
	 * Indicates if a new asteroid should be created/drawn.
	 * @return if a new asteroid should be created/drawn
	 */
	public synchronized boolean isNewAsteroid() {
		return newAsteroid;
	}

	public synchronized void setNewAsteroid(boolean newAsteroid) {
		this.newAsteroid = newAsteroid;
	}
	
	public synchronized boolean isNewAsteroid2() {
		return newAsteroid2;
	}
	
	public synchronized void setNewAsteroid2(boolean newAsteroid2) {
		this.newAsteroid2 = newAsteroid2;
	}
	
	/**
	 * Indicates if a new big asteroid should be created/drawn.
	 * @return if a new big asteroid should be created/drawn
	 */
	public synchronized boolean isNewBigAsteroid() {
		return newBigAsteroid;
	}

	public synchronized void setNewBigAsteroid(boolean newBigAsteroid) {
		this.newBigAsteroid = newBigAsteroid;
	}

	/**
	 * Returns the number of asteroid destroyed. 
	 * @return the number of asteroid destroyed
	 */

	/**
	 * Returns the number ships/lives left.
	 * @return the number ships left
	 */
	public synchronized int getShipsLeft() {
		return shipsLeft;
	}

	public synchronized void setShipsLeft(int shipsLeft) {
		this.shipsLeft = shipsLeft;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	public int getMaxLevels(){
		return maxLevels;
	}
	
	/**
	 * Returns the current game level.
	 * @return the game level
	 */
	
	
	public boolean isImmortal() {
		return immortal;
	}

	public void setImmortal(boolean immortal) {
		this.immortal = immortal;
	}
	
	
	
}
