package core;

import java.lang.Cloneable;
import java.util.ArrayList;
import java.util.HashSet;

public class Grid implements Cloneable{
//attributes
	//private
	private final boolean shiftable;
	private int width;
	private int height;
	private ArrayList<ArrayList<Tile>> gridTiles;
	
//intern class
	private class Tile {
	//methods
        private Tile(Card card, boolean alive) {
            this.card = card;
            this.alive = alive;
        }
		private Card getAndRemoveCard() { 
			Card cardToRemove = this.card.clone();
			this.card = null;
			return cardToRemove;
		}
    //attributes
        private boolean alive;
        private Card card;
    }
//constructors
	public Grid(int width, int height){
		this.shiftable=true;
		this.height=height;
		this.width=width;
		
		this.gridTiles = new ArrayList<ArrayList<Tile>>(height);
		
		for(int x=0;x<width;x++)
			for(int y=0;y<height;y++)
				this.gridTiles.get(x).add(new Tile(null,true));
		
	}
	public Grid(int width, int height, String[] gridDeadTiles){
		this.shiftable=false;
		this.height=height;
		this.width=width;
		
		this.gridTiles = new ArrayList<ArrayList<Tile>>(width);		
		
		for(int x=0;x<width;x++)
			for(int y=0;y<height;y++)
				this.gridTiles.get(x).add(new Tile(null,true));

		for(int n=0;n<gridDeadTiles.length;n++)
		{
			int x = Integer.valueOf(gridDeadTiles[n].split(",")[0]);
			int y = Integer.valueOf(gridDeadTiles[n].split(",")[1]);
			this.gridTiles.get(x).get(y).alive=false;
		}
	}

//methods
	//setter
	public boolean setTile(int x, int y, Card cardToPlace) {
		
		if(!this.shiftable) {//static
			if(this.cardCanBePlacedHere(x, y)){
				this.gridTiles.get(x).get(y).card=cardToPlace;
				return true;
			}
			else return false;
		}
		else
		{//dynamic
			Direction whereToPlace;
			whereToPlace=checkBoundsDynamic(x, y);
			if(whereToPlace==Direction.NONE) return false;
			else
			{
				resizeWiden(whereToPlace);
				this.gridTiles.get(x).get(y).card=cardToPlace;
				return true;
			}
		}
		
	}
	public boolean moveTile(int xSrc, int ySrc, int xDest, int yDest){
		if (this.cardCanBeMovedHere(xSrc, ySrc, xDest, yDest)) {
			this.gridTiles.get(xDest).get(yDest).card=this.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard();
			this.gridTiles.get(xSrc).set(ySrc,null);//inutile ?
			return true;
		}
		else return false;
	}
	
	//getter
	public int getWidth() {return width;}
	public int getHeight() {return height;}

	
//big tests
	private void resizeNarrow()
	{
		//under
		int x=0;
		boolean rowIsEmpty=true;
		
		do
		{
			if(this.gridTiles.get(x).get(0).card!=null) rowIsEmpty=false;
			x++;
		}while(rowIsEmpty && x<this.width);
		
		if(rowIsEmpty) 
			for(int i=0;i<this.width;i++)
			{
				if(this.gridTiles.get(i).get(0).card!=null) System.out.println("FAIL1");
				this.gridTiles.get(i).remove(0);
				
			}
		
		
		//left
		int y=0;
		boolean columnIsEmpty=true;
		
		do
		{
			if(this.gridTiles.get(0).get(y).card!=null) columnIsEmpty=false;
			y++;
		}while(columnIsEmpty && y<this.height);
		
		if(columnIsEmpty) 
			for(int i=0;i<this.height;i++)
			{
				if(this.gridTiles.get(0).get(i).card!=null) System.out.println("FAIL2");
				this.gridTiles.get(0).remove(i);
				
			}
		
		
		//up
		int z=0;
		boolean upRowIsEmpty=true;
		
		do
		{
			if(this.gridTiles.get(z).get(this.height).card!=null) upRowIsEmpty=false;
			z++;
		}while(upRowIsEmpty && z<this.width);
		
		if(upRowIsEmpty) 
			for(int i=0;i<this.width;i++)
			{
				if(this.gridTiles.get(i).get(this.height).card!=null) System.out.println("FAIL3");
				this.gridTiles.get(i).remove(this.height);
				
			}
		
		
		//right
		int t=0;
		boolean rightColumnIsEmpty=true;
		
		do
		{
			if(this.gridTiles.get(this.width).get(t).card!=null) rightColumnIsEmpty=false;
			t++;
		}while(rightColumnIsEmpty && t<this.height);
		
		if(rightColumnIsEmpty) 
			for(int i=0;i<this.height;i++)
			{
				if(this.gridTiles.get(this.width).get(i).card!=null) System.out.println("FAIL4");
				this.gridTiles.get(this.width).remove(i);
				
			}
	}
	private void resizeWiden(Direction whereToExpand)
	{
		switch(whereToExpand)
		{
		case RIGHT:
			this.gridTiles.add(new ArrayList<Tile>(this.height));
			break;
			
		case UNDER:
			for(int i=0;i<this.width;i++) this.gridTiles.get(0).add(0, new Tile(null,true));
			break;
			
		case LEFT:
			this.gridTiles.add(0, new ArrayList<Tile>(this.height));
			break;
			
		case UP:
			for(int i=0;i<this.width;i++) this.gridTiles.get(i).add(new Tile(null,true));
			break;
			
		default://mandatory for Direction.NONE
			break;
		}
	}
	
	public boolean cardCanBePlacedHere(int x, int y){
		boolean conditionsSatisfied=true;
		
		if(!this.checkBoundsStatic(x,y)) conditionsSatisfied=false;
		else
		{
			if(this.isEmpty()) conditionsSatisfied=true;//if is empty then we don't care of the rest
			else
			{
				if(this.canContainACard(x, y))//alive and contains no card
				{
					if(conditionsSatisfied)//check if has a neighbor
					{
						conditionsSatisfied=this.hasANeighbor(x, y);
					}
					
				}
				//exit here if dead
			}
			//exit here if empty
		}
		//exit here if out of bound
		return conditionsSatisfied;
	}
	
	public boolean cardCanBeMovedHere(int xSrc, int ySrc, int xDest, int yDest)
	{
		boolean conditionsSatisfied=true;
		
		if( !(this.checkBoundsStatic(xSrc, ySrc) && this.checkBoundsStatic(xDest, yDest))) conditionsSatisfied=false;//coord are within
		else
		{
			if(!this.containsACard(xSrc, ySrc)) conditionsSatisfied=false;//src don't contain a card
			else
			{
				if(!this.cardCanBePlacedHere(xDest, yDest)) conditionsSatisfied=false;//card can't be placed on dest
				else
				{
					//graph doesn't disconnect ?
					conditionsSatisfied=this.disconnectsGraphByRemovingCard(xSrc, ySrc);
				}
			}
		}
		
		return conditionsSatisfied;
	}
	
	public boolean isFull(){
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(this.gridTiles.get(x).get(y).alive && this.gridTiles.get(x).get(y).card == null) return false;
			}
		}
		return true;
	}
	public boolean isEmpty(){
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(this.gridTiles.get(x).get(y).alive && this.containsACard(x,y)) return false;
			}
		}
		return true;
	}
	
//medium tests
	private boolean containsACard(int x, int y){
		if(this.checkBoundsStatic(x, y)) return (this.gridTiles.get(x).get(y).alive && this.gridTiles.get(x).get(y).card != null);//check bounds bc of check of adjacency
		else return false;
	}
	
//little tests
	private enum Direction{
		UP,
		RIGHT,
		UNDER,
		LEFT,
		NONE
	}
	
	private int getNumberOfPlacedCards(){
		int numberOfCards=0;
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				if(this.containsACard(x,y)) {numberOfCards++;}
			}
		}	
		return numberOfCards;
	}
	
	private boolean canContainACard(int x, int y){
			return (this.gridTiles.get(x).get(y).alive && this.gridTiles.get(x).get(y).card == null);
	}
	
	private boolean hasANeighbor(int x, int y)
	{
		boolean hasANeighbor=false;
		
		if (this.containsACard(x-1, y)) hasANeighbor=true;
		else if (this.containsACard(x, y-1)) hasANeighbor=true;
		else if (this.containsACard(x+1, y)) hasANeighbor=true;
		else if (this.containsACard(x, y+1)) hasANeighbor=true;
		
		return hasANeighbor;
	}
	
	/**
	 * *celle ci à utiliser
	 * @param xRemove
	 * @param yRemove
	 * @return true if it's ok
	 */
	private boolean disconnectsGraphByRemovingCard(int xRemove, int yRemove)
	{
		Tile startingTile=null;
		int xPosition=0;
		int yPosition=0;
		Integer countedCards=0;
		
		while(startingTile==null)
		{
			while(startingTile==null && yPosition<this.width)
			{
				startingTile=this.gridTiles.get(xPosition).get(yPosition);
				if(startingTile==null) yPosition++;
			}
			xPosition++;
		}
		
		disconnectsGraphByRemovingCard(xPosition, yPosition, xRemove, yRemove, Direction.NONE, countedCards, new HashSet<Tile>());
		
		return countedCards==this.getNumberOfPlacedCards();
	}
	/**
	 * *pas elle
	 * @param x
	 * @param y
	 * @param xIgnore
	 * @param yIgnore
	 * @param arrivingDirection
	 * @param countedCards
	 * @param tilesSeen
	 */
	private void disconnectsGraphByRemovingCard(
		int x, int y, int xIgnore, int yIgnore, Direction arrivingDirection, Integer countedCards, HashSet<Tile> tilesSeen)
	{
		if( !((x==xIgnore && y==yIgnore) || this.gridTiles.get(x).get(y).card==null))
		{
			if( !tilesSeen.contains(this.gridTiles.get(x).get(y)));//card seen
			{
				tilesSeen.add(this.gridTiles.get(x).get(y));
				countedCards++;
				
				if(arrivingDirection != Direction.UP)
					disconnectsGraphByRemovingCard(x, y+1, xIgnore, yIgnore, Direction.UNDER, countedCards, tilesSeen);
				
				if(arrivingDirection != Direction.RIGHT)
					disconnectsGraphByRemovingCard(x+1, y, xIgnore, yIgnore, Direction.LEFT, countedCards, tilesSeen);
				
				if(arrivingDirection != Direction.UNDER)
					disconnectsGraphByRemovingCard(x, y-1, xIgnore, yIgnore, Direction.UP, countedCards, tilesSeen);
				
				if(arrivingDirection != Direction.LEFT)
					disconnectsGraphByRemovingCard(x-1, y, xIgnore, yIgnore, Direction.RIGHT, countedCards, tilesSeen);
			}
		}
	}
	
	//job specific
	private boolean checkBoundsStatic(int x, int y){
		if (x<0 || x>=this.width || y<0 || y>=this.height) return false;
		else return true;
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @return Direction if expandable
	 * NONE if inside or outside diagonally
	 */
	private Direction checkBoundsDynamic(int x, int y)
	{
		Direction answer=Direction.NONE;
		if(checkBoundsStatic(x, y))//in bounds 
		{
			
		}
		else//outside somewhere
		{
			
			if(
				(x>this.width
				&&
					(y<0
						||
					y>this.height)
				)
			||
				(x<0
				&&
					(y<0
						||
					y>this.height)
				)
			)			
				
			{
				
			}
			else
			{
				
				if(x<0) answer=Direction.LEFT;
				else if(y<0) answer=Direction.UNDER;
				else if(x>this.width) answer=Direction.RIGHT;
				else if(y>this.height) answer=Direction.UP;
			}
		}
		return answer;
	}
	
	public Grid clone(){
		Grid clonedGrid = new Grid(this.width,this.height);
		for(int x=0;x<height;x++){
			for(int y=0;y<width;y++){
				if(this.containsACard(x,y)) {
					clonedGrid.gridTiles.get(x).get(y).alive = this.gridTiles.get(x).get(y).alive;
					clonedGrid.gridTiles.get(x).get(y).card=this.gridTiles.get(x).get(y).card.clone();
				}
			}
		}
		return clonedGrid;
	}
	public int calculateScore(Card victoryCard){
		int currentScore = 0;
		int shapeCombo,hollowCombo,colorCombo;		

		for(int y=0;y<this.width;y++){
			shapeCombo=0;
			hollowCombo=0;
			colorCombo=0;

			Card lastCard = this.gridTiles.get(0).get(y).card;
			for(int x=1;x<this.height;x++){   
				Card currentCard = this.gridTiles.get(x).get(y).card;
				
				if (currentCard == null || lastCard == null) {
					shapeCombo=0;
					hollowCombo=0;
					colorCombo=0;
					continue;
				}
				else {
					if (currentCard.getShape() == victoryCard.getShape() && currentCard.getShape() == lastCard.getShape()) shapeCombo++;
					else if (currentCard.getShape() == victoryCard.getShape()) {
						if (shapeCombo>=2) currentScore+=(shapeCombo-1);
						shapeCombo=1;
					}
					else shapeCombo=0;

					if (currentCard.getHollow() == victoryCard.getHollow() && currentCard.getHollow() == lastCard.getHollow()) hollowCombo++;
					else if (currentCard.getHollow() == victoryCard.getHollow()) {
						if (hollowCombo>=3) currentScore+=hollowCombo;
						hollowCombo=1;
					}
					else hollowCombo=0;

					if (currentCard.getColor() == victoryCard.getColor() && currentCard.getColor() == lastCard.getColor()) colorCombo++;
					else if (currentCard.getColor() == victoryCard.getColor()) {
						if (colorCombo>=3) currentScore+=(colorCombo+1);
						colorCombo=1;
					}
					else colorCombo=0;
				}

				lastCard = currentCard;
			}
		}
		
		for(int x=0;x<this.height;x++){
			shapeCombo=0;
			hollowCombo=0;
			colorCombo=0;

			Card lastCard = this.gridTiles.get(x).get(0).card;
			for(int y=1;y<this.width;y++){   
				Card currentCard = this.gridTiles.get(x).get(y).card;
				
				if (currentCard == null || lastCard == null) {
					shapeCombo=0;
					hollowCombo=0;
					colorCombo=0;
					continue;
				}
				else {
					if (currentCard.getShape() == victoryCard.getShape() && currentCard.getShape() == lastCard.getShape()) shapeCombo++;
					else if (currentCard.getShape() == victoryCard.getShape()) {
						if (shapeCombo>=2) currentScore+=(shapeCombo-1);
						shapeCombo=1;
					}
					else shapeCombo=0;

					if (currentCard.getHollow() == victoryCard.getHollow() && currentCard.getHollow() == lastCard.getHollow()) hollowCombo++;
					else if (currentCard.getHollow() == victoryCard.getHollow()) {
						if (hollowCombo>=3) currentScore+=hollowCombo;
						hollowCombo=1;
					}
					else hollowCombo=0;

					if (currentCard.getColor() == victoryCard.getColor() && currentCard.getColor() == lastCard.getColor()) colorCombo++;
					else if (currentCard.getColor() == victoryCard.getColor()) {
						if (colorCombo>=3) currentScore+=(colorCombo+1);
						colorCombo=1;
					}
					else colorCombo=0;
				}
				
				lastCard = currentCard;
			}
		}

		return currentScore;
	}
	private String tileToString(int x, int y) {
		//TODO check quel mots selon état
		String returnString = new String();
		if (this.gridTiles.get(x).get(y).alive)
		{
			if (this.containsACard(x, y)) returnString+=this.gridTiles.get(x).get(y).card.toString();
			else returnString+="---";
		}
		else returnString+="   ";
		return returnString;
	}
	public String toString() {
		String returnString = new String("\n");
		
		for(int i=0;i<(3*width)+(2*(width-1))+2;i++) returnString+=("_");
		returnString+="\n";
		
		for (int x=0; x<height; x++)
		{
			returnString+="|";
			for (int y=0; y<width; y++)
			{
				returnString+=this.tileToString(x, y);
				returnString+=" ";
			}
			returnString+="|\n";
		}
			
		for(int i=0;i<(3*width)+(2*(width-1))+2;i++) returnString+=("_");
		
		return returnString;
	}
	//public void display() {System.out.println(this.toString());}

}
