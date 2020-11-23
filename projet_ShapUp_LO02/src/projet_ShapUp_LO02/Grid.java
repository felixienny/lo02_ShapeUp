package projet_ShapUp_LO02;
import java.util.ArrayList;
import java.lang.Cloneable;

public class Grid implements Cloneable {
	public Grid(int width, int height)
	{
		this.height=height;
		this.width=width;
		
		grid = new Tile[width][height];
		
		for(int x=0;x<width;x++)
			for(int y=0;y<height;y++)
				grid[x][y] = new Tile();
		
	}
	
	private Tile grid[][];
	
	private int width;
	private int height;
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getNumberOfPlacedCards()
	{
		int numberOfCards=0;
		for(int x=0;x<width;x++)
			for(int y=0;y<height;y++)
				if(grid[x][y].currentlyContainsACard()) numberOfCards++;
		return numberOfCards;
	}
	public boolean isFull()
	{
		boolean hasAnEmptySpot=false;
		
		outerloop:
		for(int x=0;x<width;x++)
			for(int y=0;y<height;y++)
				if(!grid[x][y].isDead() && !grid[x][y].currentlyContainsACard())
				{
					hasAnEmptySpot=true;
					break outerloop;
				}
		
		return hasAnEmptySpot;
	}
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
	public boolean moveTile(int xSrc, int ySrc, int xDest, int yDest){
		boolean success=true;
		if(xSrc>=width || ySrc>=height || xDest>=width || yDest>=height) success=false;
		else
		{
			if(grid[xSrc][ySrc] == null) success=false;
			else
			{
				if (grid[xSrc][ySrc].getCardReference() != null && !grid[xDest][yDest].isDead()){
					success = grid[xDest][yDest].setCard(grid[xSrc][ySrc].getAndRemoveCard());
				}
				else {
					success = false;
				}
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
	public void setTileDead(int x, int y) {grid[x][y].setTileDead();}
	
	public Grid clone()
	{
		Grid clonedGrid = new Grid(this.width,this.height);
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
			{
				if(this.getTile(i, j)==null)
					clonedGrid.setTile(i, j, null);
				else
					clonedGrid.setTile(i, j, this.getTile(i, j).clone());
			}
			
		return clonedGrid;
	}
	public int calculateScore(Card victoryCard)
	{
		ArrayList<ArrayList<Card>> lines = new ArrayList<ArrayList<Card>>();
		
		for(int x=0;x<this.width;x++)//horizontal
		{
			lines.add(new ArrayList<Card>());
			ArrayList<Card> currentCardLine=lines.get(lines.size()-1);
			for(int y=0;y<grid[x].length;y++)//vertical
			{
				if(grid[x][y].getCardReference()!=null)//card à ajouter
				{
					currentCardLine.add(grid[x][y].getCardReference().clone());
				}
				else//saut à faire
				{
					if(!currentCardLine.isEmpty()) 
					{
						lines.add(new ArrayList<Card>());
						currentCardLine=lines.get(lines.size()-1);
					}
				}
			}
			
		}
		
		for(int y=0;y<this.height;y++)//vertical
		{
			lines.add(new ArrayList<Card>());
			ArrayList<Card> currentCardLine=lines.get(lines.size()-1);
			for(int x=0;x<grid[y].length;x++)//horizontal
			{
				if(grid[x][y].getCardReference()!=null)//card à ajouter
				{
					currentCardLine.add(grid[x][y].getCardReference().clone());
				}
				else//saut à faire
				{
					if(!currentCardLine.isEmpty()) 
					{
						lines.add(new ArrayList<Card>());
						currentCardLine=lines.get(lines.size()-1);
					}
				}
			}
			
		}
		
		/*
		//TODO rajoute lignes pas utiles horizontalement
		for(int x=0;x<this.width;x++)//horizontal
		{
			boolean skippedInside=false;
			if(lines.isEmpty()||lines.get(x).size()!=0) lines.add(new ArrayList<Card>());
			
			
			ArrayList<Card> currentCardLine = lines.get(lines.size()-1);
			
			for(int y=0;y<grid[x].length;y++)//vertical
			{
				if(grid[x][y].currentlyContainsACard())
				{
					currentCardLine.add(grid[x][y].getCardReference().clone());
				}
				else if(!skippedInside)
				{
					lines.add(new ArrayList<Card>());
					currentCardLine = lines.get(lines.size()-1);
					skippedInside=true;
				}
				
			}
			
		}
		
		for(int x=0;x<this.width;x++)//horizontal
		{
			boolean skippedInside=false;
			if(lines.isEmpty()||lines.get(x).size()!=0)  lines.add(new ArrayList<Card>());
			ArrayList<Card> currentCardLine = lines.get(lines.size()-1);
			
			for(int y=0;y<grid[x].length;y++)//vertical
			{
				if(grid[y][x].currentlyContainsACard())
				{
					currentCardLine.add(grid[y][x].getCardReference().clone());
				}
				else if(!skippedInside)
				{
					lines.add(new ArrayList<Card>());
					currentCardLine = lines.get(lines.size()-1);
					skippedInside=true;
				}
				
			}
			
		}
		*/
		
		return findScoreOnIndividualLines(lines,victoryCard);
	}
	private int findScoreOnIndividualLines(ArrayList<ArrayList<Card>> lines, Card victoryCard)
	{
		int currentScore=0;
		
		for(int i=0;i<lines.size();i++)//line
		{
			if(lines.size()==0) break;
			ArrayList<Card> currentLine=lines.get(i);
			if(currentLine.isEmpty()==true) continue;
			
			Shape winningShape=victoryCard.getShape();
			boolean winningHollow=victoryCard.getHollow();
			Color winningColor=victoryCard.getColor();
			
			int shapeCombo=1;
			int hollowCombo=1;
			int colorCombo=1;
			Color lastColor=currentLine.get(0).getColor();
			Shape lastShape=currentLine.get(0).getShape();
			boolean lastHollow=currentLine.get(0).getHollow();
			
			for(int j=0;j<currentLine.size();j++)//card
			{
				Card currentCard=currentLine.get(j);
				if(currentCard==null) continue;
				
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
	
	public void display() {
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				System.out.print(" ");
				if(!this.isFreeToPlaceACardOn(w, h)) {
					Card card = this.getTile(w, h);

					if(card.getHollow()) {
						System.out.print("F");
					}
					else {						
						System.out.print("H");
					}

					switch (card.getColor()) {
					case RED:
						System.out.print("R");
						break;
					case GREEN:
						System.out.print("G");
						break;
					case BLUE:
						System.out.print("B");
						break;
					default:
						break;
					}

					switch (card.getShape()) {
					case CIRCLE:
						System.out.print("C");
						break;
					case TRIANGLE:
						System.out.print("T");
						break;
					case SQUARE:
						System.out.print("S");
						break;
					default:
						break;
					}
					
				}
				else{
					System.out.print("   ");
				}

				System.out.print(/*ANSI_RESET +*/ " ");
			}		  
			System.out.println("\n");
		}
		System.out.println("________________");
	}
}
