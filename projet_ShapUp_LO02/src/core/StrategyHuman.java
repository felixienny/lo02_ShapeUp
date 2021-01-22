package core;

import java.util.ArrayList;

/**
 * Class which is for the strategy of the human players.
 */
class StrategyHuman extends Strategy {

	/**
	 * The constructor of the class takes a reference to the GameController.
	 * @param gameController
	 */
	public StrategyHuman(GameController gameController) {
		this.gameController = gameController;
	}

	/**
	 * Attributes to keep a reference on the GameController for each player.
	 */
	private GameController gameController;

	/**
	 * Function that called the function {@link core.GameController#makeTurnOfPlayer(ArrayList)} to make the human player turn.
	 */
	public void makeBestMove(ArrayList<Card> playerHand) {
		this.gameController.makeTurnOfPlayer(playerHand);
	}
}