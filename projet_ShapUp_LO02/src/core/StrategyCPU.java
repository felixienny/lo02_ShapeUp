package core;

import java.util.ArrayList;

class StrategyCPU extends Strategy {//decision tree

	public void makeBestMove(ArrayList<Card> victoryCards) {		
		int bestScore=-1,bestSolution=-1, bestI=-1, bestJ=-1, jTemp=-1,maxIndex=0,maxValue=-1;
		String bestMove, bestSet;

		for(int i=0;i<victoryCards.size();i++) {
			int[] scores = new int[3];

			for(int j=0;j<victoryCards.size();j++) {
				if(j != i) {
					scores[0] += this.getScoreSet(victoryCards.get(i), victoryCards.get(j));
					scores[1] += this.getScoreSetFirst(victoryCards.get(i), victoryCards.get(j));
					scores[2] += this.getScoreMoveFirst(victoryCards.get(i), victoryCards.get(j));
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
			bestSet = this.doBestCardSet(this.actualGrid, victoryCards.get(bestI), victoryCards.get(bestJ));
			System.out.println(bestSet);
			this.actualGrid.setTile(bestSet, victoryCards.remove(bestI));
		}
		else if (bestSolution==1) {
			bestSet = this.doBestCardSet(this.actualGrid, victoryCards.get(bestI), victoryCards.get(bestJ));
			System.out.println(bestSet);
			this.actualGrid.setTile(bestSet, victoryCards.remove(bestI));

			this.actualGrid.display();
			
			bestMove = this.doBestCardMove(this.actualGrid, victoryCards.get(bestJ));
			System.out.println(bestMove);			
			this.actualGrid.moveTile(bestMove);
		}
		else if (bestSolution==2) {
			bestMove = this.doBestCardMove(this.actualGrid, victoryCards.get(bestJ));
			System.out.println(bestMove);			
			this.actualGrid.moveTile(bestMove);

			this.actualGrid.display();
			
			bestSet = this.doBestCardSet(this.actualGrid, victoryCards.get(bestI), victoryCards.get(bestJ));
			System.out.println(bestSet);
			this.actualGrid.setTile(bestSet, victoryCards.remove(bestI));
		}

	}
	
	public void makeBestMove(Card victoryCard, Card cardToPlace) {
		String bestMove, bestSet;

		if(this.getScoreSet(cardToPlace, victoryCard) >= Math.max(this.getScoreSetFirst(cardToPlace, victoryCard),this.getScoreMoveFirst(cardToPlace, victoryCard))) {
			bestSet = this.doBestCardSet(this.actualGrid, victoryCard, cardToPlace);
			System.out.println(bestSet);
			this.actualGrid.setTile(bestSet, cardToPlace);
		}
		else if(this.getScoreSetFirst(cardToPlace, victoryCard) >= Math.max(this.getScoreSet(cardToPlace, victoryCard),this.getScoreMoveFirst(cardToPlace, victoryCard))) {
			bestSet = this.doBestCardSet(this.actualGrid, victoryCard, cardToPlace);
			System.out.println(bestSet);
			this.actualGrid.setTile(bestSet, cardToPlace);

			this.actualGrid.display();

			bestMove = this.doBestCardMove(this.actualGrid, victoryCard);
			System.out.println(bestMove);			
			this.actualGrid.moveTile(bestMove);

		}
		else if(this.getScoreMoveFirst(cardToPlace, victoryCard) >= Math.max(this.getScoreSet(cardToPlace, victoryCard),this.getScoreSetFirst(cardToPlace, victoryCard))) {
			bestMove = this.doBestCardMove(this.actualGrid, victoryCard);
			System.out.println(bestMove);			
			this.actualGrid.moveTile(bestMove);

			this.actualGrid.display();

			bestSet = this.doBestCardSet(this.actualGrid, victoryCard, cardToPlace);
			System.out.println(bestSet);
			this.actualGrid.setTile(bestSet, cardToPlace);
		}
	}
	

	private int getScoreSet(Card cardToPlace, Card victoryCard) {
		Grid computer = this.actualGrid.clone();
		computer.setTile(this.doBestCardSet(computer, cardToPlace, victoryCard), cardToPlace);
		return computer.calculateScore(victoryCard);
	}
	
	private int getScoreSetFirst(Card cardToPlace, Card victoryCard) {
		Grid computer = this.actualGrid.clone();
		computer.setTile(doBestCardSet(computer, cardToPlace, victoryCard), cardToPlace);
		computer.moveTile(doBestCardMove(computer, victoryCard));
		return computer.calculateScore(victoryCard);
	}

	private int getScoreMoveFirst(Card cardToPlace, Card victoryCard) {
		Grid computer = this.actualGrid.clone();
		computer.moveTile(this.doBestCardMove(computer, victoryCard));
		computer.setTile(this.doBestCardSet(computer, cardToPlace, victoryCard), cardToPlace);
		return computer.calculateScore(victoryCard);
	}
  

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