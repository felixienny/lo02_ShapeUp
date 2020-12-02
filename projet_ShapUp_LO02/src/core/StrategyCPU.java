package core;

import java.util.concurrent.atomic.AtomicInteger;

public class StrategyCPU implements Strategy {//decision tree
//Results
	boolean moveFirst;
		StringBuffer whereToWhereMove;
	int bestVCardToUse;
		StringBuffer WhereToSetCard;
	boolean moveAtAll;
	
//Results getters
	public boolean isMoveFirst() {return moveFirst;}
	public String getWhereToWhereMove() {return whereToWhereMove.toString();}
	public int getbestVCardToUse() {	return bestVCardToUse;}
	public String getWhereToSetCard() {return WhereToSetCard.toString();}
	public boolean isMoveAtAll() {return moveAtAll;}

//Resources
	Grid actualGrid;
	
//main
	public void computeBestMove(Grid currentGrid, Card[] victoryCards)//split V Card
	{
		int[] bestVCard = new int[victoryCards.length];
		
		for(int i=0;i<bestVCard.length;i++)
		{
			bestVCard[i]=computeBestSequence(victoryCards[i]);
		}
		
		//find max value
		
		this.bestVCardToUse=findIndexMaxValue(bestVCard);
		computeBestSequence(victoryCards[bestVCardToUse]);
	}
	
//possibility computers
	private int computeBestSequence(Card chosenVCard)//split engagement sequence
	{
		int[] bestSequence = new int[3];
		/**	  moveFirst moveAtAll
		 * 0 : false false
		 * 1 : false true
		 * 2 : true false
		 * 3 : true true
		 */
		
		bestSequence[0]=computeSequencesPossibilities(chosenVCard,false,false);
		bestSequence[1]=computeSequencesPossibilities(chosenVCard,false,true);
		bestSequence[2]=computeSequencesPossibilities(chosenVCard,true,false);
		bestSequence[3]=computeSequencesPossibilities(chosenVCard,true,true);
		
		int result=findIndexMaxValue(bestSequence);
	
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
		
		return result;
	}
	
	private int computeSequencesPossibilities(Card chosenVCard, boolean moveFirst, boolean moveAtAll)
	{
		//TODO ça
		int[] bestScore = new int[3];
		
		if(moveFirst)
		{
			Grid T=this.actualGrid.clone();

			AtomicInteger TPossBScore = new AtomicInteger();
			StringBuffer answerT = findBestCardMove(T, chosenVCard, TPossBScore);
			bestScore[0]=TPossBScore.get();
			
			if(moveAtAll)
			{
				Grid TT=T.clone();
				
				AtomicInteger TTPossBScore = new AtomicInteger();
				StringBuffer answerTT = findBestCardMove(TT, chosenVCard, TTPossBScore);
				bestScore[1]=TPossBScore.get();
			}
		}
		else
		{
			
		}
		
		int bestI=0;
		
		return bestScore[bestI];
	}
	
	private StringBuffer findBestCardMove(Grid currentGrid, Card chosenCard, AtomicInteger bestScoreReturn)
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
								currentScore=newPoss.calculateScore(chosenCard);
								
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
		bestScoreReturn.set(bestScore);
		return answer;
	}
	
	private StringBuffer findBestCardSet(Grid currentGrid, Card cardToPlace, Card chosenCard, AtomicInteger bestScoreReturn)
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
					currentScore=newPoss.calculateScore(chosenCard);
					
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
		bestScoreReturn.set(bestScore);
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
