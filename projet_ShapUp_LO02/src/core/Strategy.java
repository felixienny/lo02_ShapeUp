package core;

import java.util.ArrayList;

public abstract class Strategy {

	protected Grid actualGrid;

	
	public abstract void makeBestMove(Card VictoryCard, Card cardToPlace);//advanced
	public abstract void makeBestMove(ArrayList<Card> victoryCards);//classic

	public void setGrid(Grid actualGrid) {
		this.actualGrid=actualGrid;
	}

	public void forgetGrid() {
		this.actualGrid=null;
	}	
}
