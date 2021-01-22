package core;

import java.util.ArrayList;
/**
 * Insures the implementation of the method that will be used by the {@link core.Player} object to asks for the move to do.
 * The {@link core.StrategyCPU} will calculate it and {@link core.StrategyHuman} will ask it to the user.
 *
 */
public abstract class Strategy {
	protected Grid actualGrid;
	public abstract void makeBestMove(ArrayList<Card> playerHand);
}
