package core;

import java.util.ArrayList;

public abstract class Strategy {
	protected boolean moveFirst;
	protected 	StringBuffer whereToWhereMove = new StringBuffer();
	protected int bestVCardToUse;
	protected 	StringBuffer WhereToSetCard = new StringBuffer();
	protected boolean moveAtAll;
	protected Grid actualGrid;
	
	public boolean getMoveFirst() {return moveFirst;}
	public String getWhereToWhereMove() {return whereToWhereMove.toString();}
	public int getbestVCardToUse() {	return bestVCardToUse;}
	public String getWhereToSetCard() {return WhereToSetCard.toString();}
	public boolean getMoveAtAll() {return moveAtAll;}
	
	public abstract void computeBestMove(Card VictoryCard, Card cardToPlace);//classic
	public abstract void computeBestMove(ArrayList<Card> victoryCards);//advanced
	public void setGrid(Grid actualGrid)
	{
		this.actualGrid=actualGrid;
	}
	public void forgetGrid()
	{
		this.actualGrid=null;
	}	
}
