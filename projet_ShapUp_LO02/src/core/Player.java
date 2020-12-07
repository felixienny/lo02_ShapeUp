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
			this.strategyType.computeBestMove(playerHand.get(0), playerHand.remove(1));
		}
<<<<<<< HEAD
		
		if(this.strategyType.getMoveFirst())
		{
			this.playingGridAdress.moveTile(this.strategyType.getWhereToWhereMove());
			if(isAdvancedGame) this.playingGridAdress.setTile(this.strategyType.getWhereToSetCard(), this.playerHand.remove(this.strategyType.getbestVCardToUse()));
			else this.playingGridAdress.setTile(this.strategyType.getWhereToSetCard(), this.playerHand.remove(1));
		}
		else
		{
			if(isAdvancedGame) this.playingGridAdress.setTile(this.strategyType.getWhereToSetCard(), this.playerHand.remove(this.strategyType.getbestVCardToUse()));
			else this.playingGridAdress.setTile(this.strategyType.getWhereToSetCard(), this.playerHand.remove(1));
			
			if(this.strategyType.getMoveAtAll()) this.playingGridAdress.moveTile(this.strategyType.getWhereToWhereMove());
		}
=======

		// if(this.strategyType.getMoveFirst())
		// {
		// 	this.playingGridAdress.moveTile(this.strategyType.getWhereToWhereMove());
		// 	if(isAdvancedGame) this.playingGridAdress.setTile(this.strategyType.getWhereToSetCard(), this.playerHand.remove(this.strategyType.getbestVCardToUse()));
		// 	else this.playingGridAdress.setTile(this.strategyType.getWhereToSetCard(), this.playerHand.remove(1));
		// }
		// else
		// {
		// 	if(isAdvancedGame) this.playingGridAdress.setTile(this.strategyType.getWhereToSetCard(), this.playerHand.remove(this.strategyType.getbestVCardToUse()));
		// 	else this.playingGridAdress.setTile(this.strategyType.getWhereToSetCard(), this.playerHand.remove(1));
			
		// 	if(this.strategyType.getMoveAtAll()) this.playingGridAdress.moveTile(this.strategyType.getWhereToWhereMove());
		// }
>>>>>>> felixienny
	}
	
	public void giveCard(Card pickedCard)
	{
		this.playerHand.add(pickedCard);
		if(isAdvancedGame && playerHand.size() > 3) throw new RuntimeException("Player has too many cards : Classic");
		if(!isAdvancedGame && playerHand.size() > 2) throw new RuntimeException("Player has too many cards : Advanced");
	}
	public void gameStarts(Grid newPlayingGridAdress)
	{
		this.playingGridAdress=newPlayingGridAdress;
		this.strategyType.setGrid(this.playingGridAdress);
		this.isAdvancedGame=this.playingGridAdress.isAdvancedGame();
	}
	public void gameEnds()
	{
		scores.add(this.currentScore);
		currentScore=0;
		
		playingGridAdress=null;
		
		this.strategyType.forgetGrid();
	}
}