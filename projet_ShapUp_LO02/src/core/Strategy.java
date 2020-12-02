package core;

public interface Strategy {
	public void computeBestMove(Grid currentGrid, Card[] victoryCards);
}
