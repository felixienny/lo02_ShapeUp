package core;

import java.util.ArrayList;
import java.util.Iterator;

public class Player {
	private int currentScore;
	private final String name;
	private Strategy strategyType;
	protected Grid grid;
	private ArrayList<Integer> scores = new ArrayList<>();
	private ArrayList<Card> playerHand = new ArrayList<Card>();

	protected enum StrategyType {
		HUMAN,
		CPU,
	}
	
	public Player(String name, StrategyType strategyType, GameController gameController) {
		this.name=name;
		if(strategyType == StrategyType.HUMAN) {
			this.strategyType = new StrategyHuman(gameController);
		}
		else this.strategyType = new StrategyCPU();
	}

	
	public void askMove() {
		this.strategyType.makeBestMove(playerHand);
		
		this.currentScore = 0;
		Iterator<Card> cardIterator = playerHand.iterator();
		while (cardIterator.hasNext()) {
			this.currentScore += this.grid.calculateScore(cardIterator.next());
		}

	}
	
	public void giveCard(Card pickedCard) {
		this.playerHand.add(pickedCard);
		if(this.grid.isAdvancedGame() && playerHand.size() > 3) throw new RuntimeException("Player has too many cards : Advanced");
		if(!this.grid.isAdvancedGame() && playerHand.size() > 2) throw new RuntimeException("Player has too many cards : Classic");
	}





	public void gameStarts(Grid newGrid) {
		this.grid = newGrid;
		this.strategyType.actualGrid = this.grid;
	}

	public void gameEnds() {
		scores.add(this.currentScore);
		this.currentScore=0;
		
		this.grid = null;
		this.strategyType.actualGrid = null;

		this.playerHand.clear();
	}


	public boolean isHuman() {
		if (StrategyHuman.class.isInstance(this.strategyType)) return true;
		else return false;
	}
	public String getName() { return name; }
	public int getCurrentScore() { return currentScore; }
	public int getFinalScore() { 
		int total=0;
		for (Integer score : this.scores) {
			total += score;
		}
		return total;
	}
	public ArrayList<Integer> getScores() { return this.scores; }
	public ArrayList<Card> getPlayerHand() { return this.playerHand; }
	public Card getCardToPlace() { return this.playerHand.remove(1); }

}