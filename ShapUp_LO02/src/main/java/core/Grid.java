package projet_ShapUp_LO02;
import java.util.ArrayList;
import java.lang.Cloneable;

public class Grid implements Cloneable {
	public Grid(int width, int height)
	{
		grid = new Tile[width][height];
		this.height=height;
		this.width=width;
	}
	
	private Tile grid[][];
	
	private int width;
	private int height;
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
	//public void setNewGrid(Tile[][] newGrid) {grid=newGrid;}
	//public Tile[][] getGrid() {return grid;}
	public boolean setTile(int x, int y, Card cardToPlace)
	{
		boolean success=true;
		if(x>=width || y>=height) success=false;
		else
		{
			if(grid[x][y] == null) success=false;
			else
			{
				success=grid[x][y].setCard(cardToPlace);
			}
		}
		return success;
	}
	public Card getTile(int x, int y) {return grid[x][y].getCardReference();}
	public boolean isFreeToPlaceACardOn(int x, int y)
	{
		return (grid[x][y].isDead()
				&& ! grid[x][y].currentlyContainsACard());
	}
	public void setTileDead(int x, int y) {grid[x][y].setTileDead();}//XXX no check on success
	
	public Grid clone()
	{
		Grid clonedGrid = new Grid(this.width,this.height);
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				clonedGrid.setTile(i, j, this.getTile(i, j).clone());
			
		return clonedGrid;
	}
	public int calculateScore(Card victoryCard)
	{
		ArrayList<ArrayList<Card>> lines = new ArrayList<ArrayList<Card>>();
		
		int listPosition=0;
		for(int i=0;i<this.width;i++)//horizontal
		{
			for(int j=0;j<grid[i].length;j++)//vertical
			{
				boolean skipped=false;
				if(grid[i][j].currentlyContainsACard())
				{
					lines.get(listPosition).add(grid[i][j].getCardReference());
					skipped=false;
				}
				else
				{
					listPosition++;
					if(!skipped) skipped=true;
				}
				if(!skipped) listPosition++;
			}
			
		}
		
		for(int i=0;i<this.height;i++)//vertical
		{
			for(int j=0;j<this.width;j++)//horizontal
			{
					boolean skipped=false;
					if(grid[j][i].currentlyContainsACard())
					{
						lines.get(listPosition).add(grid[j][i].getCardReference());
						skipped=false;
					}
					else
					{
						listPosition++;
						if(!skipped) skipped=true;
					}
					if(!skipped) listPosition++;
			}
		}
		
		return findScoreOnIndividualLines(lines,victoryCard);
	}
	private int findScoreOnIndividualLines(ArrayList<ArrayList<Card>> lines, Card victoryCard)//TODO calcul score selon victory card
	{
		int currentScore=0;
		
		for(int i=0;i<lines.size();i++)//line
		{
			ArrayList<Card> currentLine=lines.get(i);
			
			Shape winningShape=victoryCard.getShape();
			boolean winningHollow=victoryCard.getHollow();
			Color winningColor=victoryCard.getColor();
			
			int shapeCombo=1;
			int hollowCombo=1;
			int colorCombo=1;
			Color lastColor=currentLine.get(0).getColor();
			Shape lastShape=currentLine.get(0).getShape();
			boolean lastHollow=currentLine.get(0).getHollow();
			
			for(int j=1;j<currentLine.size();j++)//card
			{
				Card currentCard=currentLine.get(j);
				
				if(currentCard.getShape()==lastShape && currentCard.getShape()==winningShape ) shapeCombo++;
				else
				{
					if(shapeCombo>=2) currentScore+=shapeCombo-1;
					shapeCombo=0;
				}
				
				
				if(currentCard.getHollow()==lastHollow && currentCard.getHollow()==winningHollow) hollowCombo++;
				else
				{
					if(hollowCombo>=3) currentScore+=hollowCombo;
					hollowCombo=0;
				}
				
				
				if(currentCard.getColor()==lastColor && currentCard.getColor()==winningColor) colorCombo++;
				else
				{	
					if(colorCombo>=3) currentScore+=colorCombo+1;
					colorCombo=0;
				}
			}
		}
		return currentScore;
	}
}
