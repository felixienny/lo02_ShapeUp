package core;

public abstract class Strategy {
	private boolean moveFirst;
	private 	StringBuffer whereToWhereMove;
	private int bestVCardToUse;
	private 	StringBuffer WhereToSetCard;
	private boolean moveAtAll;
	
	private Grid actualGrid;
	
	public boolean getMoveFirst() {return moveFirst;}
	public String getWhereToWhereMove() {return whereToWhereMove.toString();}
	public int getbestVCardToUse() {	return bestVCardToUse;}
	public String getWhereToSetCard() {return WhereToSetCard.toString();}
	public boolean getMoveAtAll() {return moveAtAll;}
	
	public abstract void computeBestMove(Card VictoryCard, Card cardToPlace);//advanced
	public abstract void computeBestMove(Card[] victoryCards);//classic
	public abstract void setGrid(Grid actualGrid);
	public void setGrid(Grid actualGrid)
	{
		this.actualGrid=actualGrid;
	}
	public void forgetGrid()
	{
		this.actualGrid=null;
	}	
}
