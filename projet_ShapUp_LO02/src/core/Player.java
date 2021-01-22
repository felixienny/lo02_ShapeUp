package core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Representation of an actual player in the game, real or not. A player handles the score keeping for itself. A player can be assigned a HumanStrategy if
 * it's going to be used by an actual human, otherwise it will resort a predefined algorithm defining a "computer strategy".
 *
 */
public class Player {
	/**
	 * Cannot be changed. Necessary for all players.
	 */
	private final String name;
	/**
	 * Score of the player for this specific game at a specific turn.
	 */
	private int currentScore;
	/**
	 * Keeps a link to the chosen strategy for the object.
	 */
	private Strategy strategyType;
	/**
	 * Keeps a link to the Grid used for the current game.
	 */
	private Grid grid;
	/**
	 * List of the scores for the current game in a tournament. Added in the list at the end of a game, otherwise kept in the currentScore variable.
	 */
	private ArrayList<Integer> scores = new ArrayList<>();
	/**
	 * List of the cards "in the hands" of the Player. In a basic game, the card n�0 is the victory cards and the n�1 is the pickedCard ; in a advanced game, 
	 * all the cards are in the hand of the Player.
	 */
	private ArrayList<Card> playerHand = new ArrayList<Card>();

	protected enum StrategyType {
		HUMAN,
		CPU,
	}
	/**
	 * Puts in place the necessaryr reference to the other objects necessary to operation of the class.
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
	 * @param pickedCard
	 */
	public void giveCard(Card pickedCard) {
		this.playerHand.add(pickedCard);
		if(this.grid.isAdvancedGame() && playerHand.size() > 3) throw new RuntimeException("Player has too many cards : Advanced");
		if(!this.grid.isAdvancedGame() && playerHand.size() > 2) throw new RuntimeException("Player has too many cards : Classic");
	}

	
	/**
	 * Called to give to a player the address of the grid for the new game.
	 * @param newGrid
	 */
	public void gameStarts(Grid newGrid) {
		this.grid = newGrid;
		this.strategyType.actualGrid = this.grid;
	}

	/**
	 * Called to end a game, by adding player's score at the current match and reset his score after.
	 * The grid is pointing to null and the player hand is set to empty, because the game is finished.
	 */
	public void gameEnds() {
		scores.add(this.currentScore);
		this.currentScore=0;
		
		this.grid = null;
		this.strategyType.actualGrid = null;

		this.playerHand.clear();
	}

	/**
	 * Return true if the strategy type of the player is human.
	 */
	public boolean isHuman() {
		if (StrategyHuman.class.isInstance(this.strategyType)) return true;
		else return false;
	}

	/**
	 * @return name the player's name
	 */
	public String getName() { return name; }
	/**
	 * @return score the current score of the player, in the current match.
	 */
	public int getCurrentScore() { return currentScore; }
	/**
	 * @return finalscore the final score of the player, at all the matches.
	 */
	public int getFinalScore() { 
		int total=0;
		for (Integer score : this.scores) {
			total += score;
		}
		return total;
	}
	/**
	 * @return scores an arraylist of all scores ordered by match.
	 */
	public ArrayList<Integer> getScores() { return this.scores; }

	/**
	 * @return playerHand an arraylist of all cards in the player's hand.
	 */
	public ArrayList<Card> getPlayerHand() { return this.playerHand; }

	
	/**
	 * @return Card the card to place in non-advanced game.
	 */
	public Card getCardToPlace() { return this.playerHand.remove(1); }

}