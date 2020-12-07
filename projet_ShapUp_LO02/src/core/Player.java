package core;

import java.util.ArrayList;
import java.util.Iterator;

public class Player {
//attributes
	//private
	//common
	boolean isAdvancedGame;
	private int currentScore;
	private final String name;
	private Strategy strategyType;
	private Grid playingGridAdress;
	
	//advanced
	private ArrayList<Integer> scores = new ArrayList<Integer>();
	private ArrayList<Card> playerHand = new ArrayList<Card>();
	
//methods
	public Player(String name, String type)
	{
		this.name=name;
		
		if(type.equals("Human")) this.strategyType = new StrategyHuman();
		else this.strategyType = new StrategyCPU();
	}
	
	//getter
	public String getName() {return name;};
	public int getCurrentScore() { return currentScore; }
	
	//job specific
	public void askMove() {
		if(isAdvancedGame) {
			this.strategyType.makeBestMove(playerHand);
			this.currentScore = 0;
			Iterator<Card> VCardIterator = playerHand.iterator();
			while (VCardIterator.hasNext()) {
				this.currentScore += this.playingGridAdress.calculateScore(VCardIterator.next());
			}
		}
		else {
			this.strategyType.makeBestMove(playerHand.get(0), playerHand.remove(1));
			this.currentScore = this.playingGridAdress.calculateScore(playerHand.get(0));
		}
	}
	
	public void giveCard(Card pickedCard) {
		this.playerHand.add(pickedCard);
		if(isAdvancedGame && playerHand.size() > 3) throw new RuntimeException("Player has too many cards : Classic");
		if(!isAdvancedGame && playerHand.size() > 2) throw new RuntimeException("Player has too many cards : Advanced");
	}

	public void gameStarts(Grid newPlayingGridAdress) {
		this.playingGridAdress=newPlayingGridAdress;
		this.strategyType.setGrid(this.playingGridAdress);
		this.isAdvancedGame=this.playingGridAdress.isAdvancedGame();
	}

	public void gameEnds() {
		scores.add(this.currentScore);
		currentScore=0;
		
		playingGridAdress=null;
		
		this.strategyType.forgetGrid();
	}
}