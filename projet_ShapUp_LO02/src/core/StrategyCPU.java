package core;

import java.util.ArrayList;
/**
 * Given a hand of cards, computes the best move to do and does it.
 * This is the mother function, each function goes down in the tree of possibilities, and then goes back up evaluating the move that we bring the 
 * most points. A move is :
 * 	if advanced game, which victory card to use
 * 	then if at first/after/at all move a car
 * 	which card to use
 * 	where to put
 */
class StrategyCPU extends Strategy {
	/**
	 * Mother function called to compute the best move possible
	 */
	public void makeBestMove(ArrayList<Card> playerHand) {
		String bestMove, bestSet;
		if (this.actualGrid.isAdvancedGame()) {
			int bestScore=-1,bestSolution=-1, bestI=-1, bestJ=-1, jTemp=-1,maxIndex=0,maxValue=-1;
			for(int i=0;i<playerHand.size();i++) {
				int[] scores = new int[3];
				for(int j=0;j<playerHand.size();j++) {
					if(j != i) {
						scores[0] += this.getScoreSet(playerHand.get(i), playerHand.get(j));
						scores[1] += this.getScoreSetFirst(playerHand.get(i), playerHand.get(j));
						scores[2] += this.getScoreMoveFirst(playerHand.get(i), playerHand.get(j));
						jTemp=j;
					}
				}
				for(int k=0; k<scores.length; k++) {
					if(scores[k]>maxValue) {
					  maxIndex = k;
				   }
				}
				if (scores[maxIndex] > bestScore) {
					bestScore = scores[maxIndex];
					bestSolution = maxIndex;
					bestI = i;
					bestJ = jTemp;
				}
			}

			if (bestSolution==0) {
				bestSet = this.doBestCardSet(this.actualGrid, playerHand.get(bestI), playerHand.get(bestJ));
				this.actualGrid.setTile(bestSet, playerHand.remove(bestI));
			}
			else if (bestSolution==1) {
				bestSet = this.doBestCardSet(this.actualGrid, playerHand.get(bestI), playerHand.get(bestJ));
				this.actualGrid.setTile(bestSet, playerHand.remove(bestI));
				bestMove = this.doBestCardMove(this.actualGrid, playerHand.get(bestJ));
				this.actualGrid.moveTile(bestMove);
			}
			else if (bestSolution==2) {
				bestMove = this.doBestCardMove(this.actualGrid, playerHand.get(bestJ));
				this.actualGrid.moveTile(bestMove);
				bestSet = this.doBestCardSet(this.actualGrid, playerHand.get(bestI), playerHand.get(bestJ));
				this.actualGrid.setTile(bestSet, playerHand.remove(bestI));
			}
		}
		else {
			if(this.getScoreSet(playerHand.get(1), playerHand.get(0)) >= Math.max(this.getScoreSetFirst(playerHand.get(1), playerHand.get(0)),this.getScoreMoveFirst(playerHand.get(1), playerHand.get(0)))) {
				bestSet = this.doBestCardSet(this.actualGrid, playerHand.get(1), playerHand.get(0));
				this.actualGrid.setTile(bestSet, playerHand.remove(1));
			}
			else if(this.getScoreSetFirst(playerHand.get(1), playerHand.get(0)) >= Math.max(this.getScoreSet(playerHand.get(1), playerHand.get(0)),this.getScoreMoveFirst(playerHand.get(1), playerHand.get(0)))) {
				bestSet = this.doBestCardSet(this.actualGrid, playerHand.get(1), playerHand.get(0));
				this.actualGrid.setTile(bestSet, playerHand.remove(1));
				bestMove = this.doBestCardMove(this.actualGrid, playerHand.get(0));
				this.actualGrid.moveTile(bestMove);
			
			}
			else if(this.getScoreMoveFirst(playerHand.get(1), playerHand.get(0)) >= Math.max(this.getScoreSet(playerHand.get(1), playerHand.get(0)),this.getScoreSetFirst(playerHand.get(1), playerHand.get(0)))) {
				bestMove = this.doBestCardMove(this.actualGrid, playerHand.get(0));
				this.actualGrid.moveTile(bestMove);
				bestSet = this.doBestCardSet(this.actualGrid, playerHand.get(1), playerHand.get(0));
				this.actualGrid.setTile(bestSet, playerHand.remove(1));
			}
		}

	}
	/**
	 * See {@link #makeBestMove(ArrayList)}
	 * @param cardToPlace card to put down to consider for computation
	 * @param victoryCard victory card to consider for computation
	 * @return the associated score of the best move given the card and victory card given
	 */
	private int getScoreSet(Card cardToPlace, Card victoryCard) {
		Grid computer = this.actualGrid.clone();
		computer.setTile(this.doBestCardSet(computer, cardToPlace, victoryCard), cardToPlace);
		return computer.calculateScore(victoryCard);
	}
	/**
	 * Gives back the score when the card is placed first
	 * @param cardToPlace card to put down to consider for computation
	 * @param victoryCard victory card to consider for computation
	 * @return the associated score of the best move given the card and victory card given
	 */
	private int getScoreSetFirst(Card cardToPlace, Card victoryCard) {
		Grid computer = this.actualGrid.clone();
		computer.setTile(doBestCardSet(computer, cardToPlace, victoryCard), cardToPlace);
		computer.moveTile(doBestCardMove(computer, victoryCard));
		return computer.calculateScore(victoryCard);
	}
	/**
	 * Gives back the score when the card is moved first
	 * @param cardToPlace card to put down to consider for computation
	 * @param victoryCard victory card to consider for computation
	 * @return the associated score of the best move given the card and victory card given
	 */
	private int getScoreMoveFirst(Card cardToPlace, Card victoryCard) {
		Grid computer = this.actualGrid.clone();
		computer.moveTile(this.doBestCardMove(computer, victoryCard));
		computer.setTile(this.doBestCardSet(computer, cardToPlace, victoryCard), cardToPlace);
		return computer.calculateScore(victoryCard);
	}
  /**
   * Returns the best card set possible in a String
   * @param grid grid to make the best set on
   * @param cardToPlace card to put down to consider for computation
   * @param victoryCard victory card to consider for computation
   * @return the String containing the best card set to do
   */
	private String doBestCardSet(Grid grid, Card cardToPlace, Card victoryCard) {
		StringBuffer answer = new StringBuffer();

		int bestX=-1;
		int bestY=-1;
		int bestScore=-1;
		int currentScore;

		int xIndex;
		for(int x=-1;x<=grid.getHeight();x++) {
			if (x==-1) xIndex=0;
			else xIndex=x;

			for(int y=-1;y<=grid.getWidthOnSpecificLine(xIndex);y++) {

				if (grid.testSettingTile(x, y)) {
					Grid newPoss = grid.clone();
					newPoss.setTile(x, y, cardToPlace);
					currentScore=newPoss.calculateScore(victoryCard);
					
					if(currentScore>bestScore) {
						bestX=x;
						bestY=y;
						bestScore=currentScore;
					}
				}

			}
		}

		answer.append(bestX);
		answer.append(',');
		answer.append(bestY);

		return answer.toString();
	}
	/**
	   * Returns the best card move possible in a String
	   * @param grid grid to make the best move on
	   * @param victoryCard victory card to consider for computation
	   * @return the String containing the best card move to do
	   */
	private String doBestCardMove(Grid grid, Card victoryCard) {
		StringBuffer answer = new StringBuffer();
		
		int bestXSrc=-1;
		int bestYSrc=-1;
		int bestXDest=-1;
		int bestYDest=-1;
		
		int bestScore=-1;
		int currentScore=0;
		
		int xDestIndex;
		
		for(int xSrc=0;xSrc<grid.getHeight();xSrc++) {
			for(int ySrc=0;ySrc<grid.getWidthOnSpecificLine(xSrc);ySrc++) {

				if(grid.containsACard(xSrc, ySrc)) {

					for(int xDest=-1;xDest<=grid.getHeight();xDest++) {
						if (xDest==-1) xDestIndex=0;
						else xDestIndex=xDest;

						for(int yDest=-1;yDest<=grid.getWidthOnSpecificLine(xDestIndex);yDest++) {

							if(grid.testMovingTile(xSrc, ySrc, xDest, yDest)) {
								Grid newPoss = grid.clone();
								newPoss.moveTile(xSrc, ySrc, xDest, yDest);
								currentScore=newPoss.calculateScore(victoryCard);
								if(currentScore>bestScore) {
									bestXSrc=xSrc;
									bestYSrc=ySrc;
									bestXDest=xDest;
									bestYDest=yDest;
									
									bestScore=currentScore;
								}
							}
						}
					}

				}
			}
		}

		answer.append(bestXSrc);
		answer.append(',');
		answer.append(bestYSrc);
		answer.append(':');
		answer.append(bestXDest);
		answer.append(',');
		answer.append(bestYDest);
		
		return answer.toString();
	}
}