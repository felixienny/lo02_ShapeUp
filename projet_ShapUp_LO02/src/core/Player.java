package core;

import java.util.ArrayList;

public abstract class Player {
//methods
	public Player(String name) {this.name=name;}
	
	//setter
	
	
	//getter
	public String getName() {return name;};
	public int getCurrentScore() {return currentScore;}
	
	
	//job specific
	public abstract void askMove(Card pickedCard);
	public void gameStarts(Grid newPlayingGridAdress, Card newVictoryCard)
	{
		this.playingGridAdress=newPlayingGridAdress;
		this.victoryCard=newVictoryCard;
	}
	public void gameEnds()
	{
		scores.add(this.currentScore);
		currentScore=0;
		playingGridAdress=null;
	}

//attributes
	//private
	private final String name;
	private ArrayList<Integer> scores = new ArrayList<Integer>();
	
	//protected
	protected int currentScore;
	protected Grid playingGridAdress;
	protected Card victoryCard;

}
