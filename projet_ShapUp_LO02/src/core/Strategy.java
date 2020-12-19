package core;

import java.util.ArrayList;

public abstract class Strategy {
	protected Grid actualGrid;
	public abstract void makeBestMove(ArrayList<Card> playerHand);
}
