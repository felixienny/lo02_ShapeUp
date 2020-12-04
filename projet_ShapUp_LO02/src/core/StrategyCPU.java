package core;

import java.util.ArrayList;

class StrategyCPU extends Strategy {//decision tree
	
	public void computeBestMove(Card VictoryCard, Card cardToPlace)//classic
	{
		computeBestSequence(cardToPlace, VictoryCard);
	}
	public void computeBestMove(ArrayList<Card> playerCards)//advanced
	{
		int bestScore=0;
		int bestI=-1;
		int bestJ=-1;
		
		int currentScore;
		
		for(int i=0;i<playerCards.size();i++)//card to place
		{
			for(int j=0;j<playerCards.size();j++)//VCard
			{
				if(i != j)
				{
					currentScore = computeBestSequence(playerCards.get(i), playerCards.get(j));
					if(currentScore>bestScore)
					{
						bestI=i;
						bestJ=j;
					}
				}
			}
		}
		
		if(bestI == -1 || bestJ == -1) throw new RuntimeException("Pas de BestMove trouv�");
		
		int result=computeBestSequence(playerCards.get(bestI), playerCards.get(bestJ));
		if(result==0)
		{
			this.moveFirst=false;
			this.moveAtAll=false;
		}
		else if(result==1)
		{
			this.moveFirst=false;
			this.moveAtAll=true;
		}
		else if(result==2)
		{
			this.moveFirst=true;
			this.moveAtAll=false;
		}
		else 
		{
			this.moveFirst=true;
			this.moveAtAll=true;
		}
	}
	
//possibility computers
	private int computeBestSequence(Card cardToPlace, Card VCard)//split engagement sequence
	{
		int[] bestSequence = new int[3];
		/**	  moveFirst moveAtAll
		 * 0 : false false
		 * 1 : false true
		 * 2 : true false
		 * 3 : true true
		 */
		
		bestSequence[0]=computeSequencesPossibilities(cardToPlace,VCard,false,false);
		bestSequence[1]=computeSequencesPossibilities(cardToPlace,VCard,false,true);
		bestSequence[2]=computeSequencesPossibilities(cardToPlace,VCard,true,false);
		bestSequence[3]=computeSequencesPossibilities(cardToPlace,VCard,true,true);
		
		int result=findIndexMaxValue(bestSequence);
		
		return result;
	}
	
	private int computeSequencesPossibilities(Card cardToPlace, Card VCard, boolean moveFirst, boolean moveAtAll)
	{
		StringBuffer answerBestPlacement = new StringBuffer();
		StringBuffer answerBestMove = new StringBuffer();
		Grid computer = this.actualGrid.clone();
		
		if(moveFirst)
		{
			this.doBestCardMove(computer, VCard);
			this.doBestCardSet(computer, cardToPlace, VCard);
		}
		
		
		return computer.calculateScore(VCard);
	}
	
	private StringBuffer doBestCardMove(Grid currentGrid, Card VCard)
	{
		StringBuffer answer = new StringBuffer();
		
		int bestXSrc=-1;
		int bestYSrc=-1;
		
		int bestXDest=-1;
		int bestYDest=-1;
		
		int bestScore=0;
		int currentScore=0;
		
		for(int xSrc=0;xSrc<currentGrid.getHeight();xSrc++)
		{
			for(int ySrc=0;ySrc<currentGrid.getWidth();ySrc++)
			{
				if(currentGrid.containsACard(xSrc, ySrc))
				{
					for(int xDest=0;xDest<currentGrid.getHeight();xDest++)
						for(int yDest=0;yDest<currentGrid.getWidth();yDest++)
						{
							if(currentGrid.testMovingTile(xSrc, ySrc, xDest, yDest))
							{
								Grid newPoss = currentGrid.clone();
								newPoss.moveTile(xSrc, ySrc, xDest, yDest);
								currentScore=newPoss.calculateScore(VCard);
								
								if(currentScore>bestScore)
								{
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
		if(bestXSrc > 0 && bestYSrc > 0 && bestXDest > 0 && bestYDest > 0)
		{
			answer.append(bestXSrc);
			answer.append(',');
			answer.append(bestYSrc);
			
			answer.append(':');
			
			answer.append(bestXDest);
			answer.append(',');
			answer.append(bestYDest);
		}

		return answer;
	}
	
	private StringBuffer doBestCardSet(Grid currentGrid, Card cardToPlace, Card VCard)
	{
		StringBuffer answer = new StringBuffer();

		int bestX=-1;
		int bestY=-1;
		int bestScore=0;
		int currentScore=0;
		
		for(int x=0;x<currentGrid.getHeight();x++)
		{
			for(int y=0;y<currentGrid.getWidth();y++)
			{
				Grid newPoss = currentGrid.clone();
				if(newPoss.testSettingTile(x, y))
				{
					newPoss.setTile(x, y, cardToPlace);
					currentScore=newPoss.calculateScore(VCard);
					
					if(currentScore>bestScore)
					{
						bestX=x;
						bestY=y;
					}
				}
			}
		}
		if(bestX >= 0 && bestY >= 0)
		{
			answer.append(bestX);
			answer.append(',');
			answer.append(bestY);			
		}

		return answer;
	}
	
	private int findIndexMaxValue(int[] values)
	{
		int maxValue = values[0];
		int index = 0;
		for(int a = 0; a < values.length; a++) 
		{
		   if(maxValue < values[a]) 
		   {
		      maxValue = values[a];
		      index = a;
		   }
		}
		return index;
	}
}
