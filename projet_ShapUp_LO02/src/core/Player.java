package core;

import java.util.ArrayList;

public class Player {
//attributes
	//private
	//common
	private int currentScore;
	private ArrayList<Integer> scores = new ArrayList<Integer>();
	private final String name;
	private Strategy strategyType;
	private Grid playingGridAdress;
	boolean isAdvancedGame;
	
	//advanced
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
	public int getCurrentScore() {return currentScore;}
	
	//job specific
	public void askMove()
	{
		if(isAdvancedGame)
		{
			this.strategyType.computeBestMove(playerHand);
		}
		else
		{
			this.strategyType.computeBestMove(playerHand.get(1), playerHand.get(0));
		}
		
		if(this.strategyType.getMoveFirst())
		{
			this.playingGridAdress.moveTile(xSrc, ySrc, xDest, yDest);
			this.playingGridAdress.setTile(x, y, cardToPlace);
		}
		else
		{
			this.playingGridAdress.setTile(x, y, cardToPlace);
			if(this.strategyType.getMoveAtAll()) this.playingGridAdress.moveTile(xSrc, ySrc, xDest, yDest);
		}
	}
	
	public void giveCard(Card pickedCard)
	{
		this.playerHand.add(pickedCard);
		if(isAdvancedGame && playerHand.size() > 4) throw new RuntimeException("Player has too many cards : Classic");
		if(!isAdvancedGame && playerHand.size() > 2) throw new RuntimeException("Player has too many cards : Advanced");
	}
	public void gameStarts(Grid newPlayingGridAdress, Card newVictoryCard)
	{
		this.playingGridAdress=newPlayingGridAdress;
		this.strategyType.setGrid(this.playingGridAdress);
		this.isAdvancedGame=this.playingGridAdress.isAdvancedGame();
		
		this.victoryCard=newVictoryCard;
		
		
	}
	public void gameEnds()
	{
		scores.add(this.currentScore);
		currentScore=0;
		
		playingGridAdress=null;
		
		this.strategyType.forgetGrid();
	}
}
