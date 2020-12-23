package core;

import java.util.ArrayList;

class StrategyHuman extends Strategy {

	public StrategyHuman(GameController gameController) {
		this.gameController = gameController;
	}

	private GameController gameController;

	public void makeBestMove(ArrayList<Card> playerHand) {
		this.gameController.makeTurnOfPlayer(playerHand);
	}
}