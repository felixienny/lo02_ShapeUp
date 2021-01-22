package core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Representation of an actual player in the game, real or not. A player handles the score keeping for itself. A player can be assigned a {@link core.StrategyHuman} if
 * it's going to be used by an actual human, otherwise it will resort a predefined algorithm defining a "computer strategy" with {@link core.StrategyCPU}.
 *
 */
public class Player {
	private final String name;
	private int currentScore;
	private Strategy strategyType;
	private Grid grid;
	private ArrayList<Integer> scores = new ArrayList<>();
	private ArrayList<Card> playerHand = new ArrayList<Card>();
	protected enum StrategyType {
		HUMAN,
		CPU,
	}
	/**
	 * Puts in place the necessary reference to the other objects necessary to operation of the class.
	 * @param name Final name for the Player.
	 * @param strategyType Human if played by a actual user, CPU otherwise.
	 * @param gameController Controller object in the MVC design pattern, necessary for the corrent operation of the graphic interface.
	 */
	public Player(String name, StrategyType strategyType, GameController gameController) {
		this.name=name;
		if(strategyType == StrategyType.HUMAN) {
			this.strategyType = new StrategyHuman(gameController);
		}
		else this.strategyType = new StrategyCPU();
	}
	/**
	 * Asks the strategy for the player move and updates the score of the player accordingly.
	 */
	public void askMove() {
		this.strategyType.makeBestMove(playerHand);
		
		this.currentScore = 0;
		Iterator<Card> cardIterator = playerHand.iterator();
		while (cardIterator.hasNext()) {
			this.currentScore += this.grid.calculateScore(cardIterator.next());
		}

	}
	/**
	 * Gives a new card to the player. 
	 * @param pickedCard card to give the player
	 */
	public void giveCard(Card pickedCard) {
		this.playerHand.add(pickedCard);
		if(this.grid.isAdvancedGame() && playerHand.size() > 3) throw new RuntimeException("Player has too many cards : Advanced");
		if(!this.grid.isAdvancedGame() && playerHand.size() > 2) throw new RuntimeException("Player has too many cards : Classic");
	}
	/**
	 * updates the attribute containing the reference to the grid. Called when a new game is started the grid is new.
	 * @param newGrid Grid to now work on
	 */
	public void gameStarts(Grid newGrid) {
		this.grid = newGrid;
		this.strategyType.actualGrid = this.grid;
	}
	/**
	 * Sets some attributes to null for the time in between two games.
	 */
	public void gameEnds() {
		scores.add(this.currentScore);
		this.currentScore=0;
		
		this.grid = null;
		this.strategyType.actualGrid = null;

		this.playerHand.clear();
	}
	/**
	 * Checks if {@link #strategyType} is {@link core.StrategyHuman} and so if it is controlled by a real user.
	 * @return if {@link #strategyType} is {@link core.StrategyHuman}
	 */
	public boolean isHuman() {
		if (StrategyHuman.class.isInstance(this.strategyType)) return true;
		else return false;
	}
	/**
	 * Getter of the String containing the given name of the player.
	 * @return the name of the player
	 */
	public String getName() { return name; }
	/**
	 * Getter of the score the player has now.
	 * @return the score the player has
	 */
	public int getCurrentScore() { return currentScore; }
	/**
	 * Getter of the total score of the player for all the tournament.
	 * @return the total score of the player.
	 */
	public int getFinalScore() { 
		int total=0;
		for (Integer score : this.scores) {
			total += score;
		}
		return total;
	}
	/**
	 * Getter of the {@link java.util.ArrayList} of scores during the tournament.
	 * @return the list of scores of the player
	 */
	public ArrayList<Integer> getScores() { return this.scores; }
	/**
	 * Getter of the {@link java.util.ArrayList} of {@link core.Card} hand of the player.
	 * @return
	 */
	public ArrayList<Card> getPlayerHand() { return this.playerHand; }
	/**
	 * In a basic game the card to put down is kept in the index n°1 of the player hand. Getter to retrieve it. Actually removes it from the {@link java.util.ArrayList}.
	 * @return the {@link core.Card} the player has to play
	 */
	public Card getCardToPlace() { return this.playerHand.remove(1); }

}