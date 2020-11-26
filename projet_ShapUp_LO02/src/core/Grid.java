package core;

import java.lang.Cloneable;
import java.util.ArrayList;
import java.util.Collections;

public class Grid implements Cloneable {

	private class Tile {
        private Card card;
        private boolean alive;
        public Tile(Card card, boolean alive) {
            this.card = card;
            this.alive = alive;
        }
        public Card getCard(){ return this.card; }
		public boolean isAlive(){ return this.alive; }
		public Card getAndRemoveCard(){ 
			Card cardToRemove = this.card.clone();
			this.card = null;
			return cardToRemove; 
		}
        public void setCard(Card card){ this.card = card; }
        public void setAlive(boolean alive){ this.alive = alive; }
    }

	public Grid(int width, int height){
		this.height=height;
		this.width=width;
		this.shiftable=true;
		
		this.gridTiles = new ArrayList<ArrayList<Tile>>(height);
		for(int x=0;x<height;x++){
			this.gridTiles.add(new ArrayList<Tile>(width));
			for(int y=0;y<width;y++){
				this.gridTiles.get(x).add(new Tile(null,true));
			}
		}
	}

	public Grid(int width, int height, String[] gridDeadTiles){
		this.height=height;
		this.width=width;
		this.shiftable=true;
		
		this.gridTiles = new ArrayList<ArrayList<Tile>>(height);		
		for(int x=0;x<height;x++){
			for(int y=0;y<width;y++){
				this.gridTiles.get(x).add(new Tile(null,true));
			}
		}

		for(int n=0;n<gridDeadTiles.length;n++){
			this.shiftable=false;
			int x = Integer.valueOf(gridDeadTiles[n].split(",")[0]);
			int y = Integer.valueOf(gridDeadTiles[n].split(",")[1]);
			this.gridTiles.get(x).get(y).setAlive(false);
		}		
	}

//methods
	//setter
	public boolean setTile(int x, int y, Card cardToPlace) {
		if(this.testSettingTile(x, y)){
			this.gridTiles.get(x).get(y).setCard(cardToPlace);
			return true;
		}
		return false;
	}
	public boolean moveTile(int xSrc, int ySrc, int xDest, int yDest){
		if (this.testMovingTile(xSrc, ySrc, xDest, yDest)) {
			this.gridTiles.get(xDest).get(yDest).setCard(this.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard());
			this.gridTiles.get(xSrc).set(ySrc,null);
			return true;
		}
		return false;
	}
	
	//getter
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getNumberOfPlacedCards(){
		int numberOfCards=0;
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				if(this.containsACard(x,y)) { numberOfCards++; }
			}
		}	
		return numberOfCards;
	}
	public boolean isAlive(int x, int y){
		if (this.checkBounds(x, y)) {
			return this.gridTiles.get(x).get(y).alive;
		}
		return false;
	}
	public boolean canContainACard(int x, int y){
		if (this.checkBounds(x, y)) {
			return (this.isAlive(x, y) && this.gridTiles.get(x).get(y).getCard()== null);
		}
		return false;
	}
	public boolean containsACard(int x, int y){
		if (this.checkBounds(x, y)) {
			if (this.isAlive(x, y) && this.gridTiles.get(x).get(y).getCard() != null) {
				return true;
			}
		}
		return false;
	}
	public boolean isFull(){
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(this.gridTiles.get(x).get(y) == null) {
					return false;
				}
			}
		}
		return true;
	}
	public boolean isEmpty(){
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(this.isAlive(x,y) && this.containsACard(x,y)) return false;
			}
		}
		return true;
	}
	public boolean isPlayable(int x, int y){
		if (this.isEmpty() && this.checkBounds(x,y)) return true;
		
		for(int i=x-1 ; i<=x+1 ; i++){
			for (int j=y-1; j<=y+1; j++) {
				if ((i==x || j==y) && !(i==x && j==y) && this.containsACard(i, j)){
					return true;
				}
			}
		}
		return false;
	}
	public boolean testSettingTile(int x, int y) {
		return this.isPlayable(x, y);
	}
	public boolean testMovingTile(int xSrc, int ySrc, int xDest, int yDest) {
		return (this.containsACard(xSrc, ySrc) && this.canContainACard(xDest, yDest) && this.isPlayable(xDest, yDest));
	}
	
	//job specific
	public boolean checkBounds(int x, int y){
		if (x<0 || x>=this.width || y<0 || y>=this.height) return false;
		else return true;

		//TODO java.lang.ArrayIndexOutOfBoundsException
	}
	public Grid clone(){
		Grid clonedGrid = new Grid(this.width,this.height);
		for(int x=0;x<height;x++){
			for(int y=0;y<width;y++){
				if(this.containsACard(x,y)) {
					clonedGrid.gridTiles.get(x).get(y).alive = this.gridTiles.get(x).get(y).alive;
					clonedGrid.gridTiles.get(x).get(y).setCard(this.gridTiles.get(x).get(y).getCard().clone());
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

			Card lastCard = this.gridTiles.get(0).get(y).getCard();
			for(int x=1;x<this.height;x++){   
				Card currentCard = this.gridTiles.get(x).get(y).getCard();
				
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

			Card lastCard = this.gridTiles.get(x).get(0).getCard();
			for(int y=1;y<this.width;y++){   
				Card currentCard = this.gridTiles.get(x).get(y).getCard();
				
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
	public String tileToString(int x, int y) {
		//TODO check quel mots selon état
		String returnString = new String();
		if (this.isAlive(x, y))
		{
			if (this.containsACard(x, y)) returnString+=this.gridTiles.get(x).get(y).getCard().toString();
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
	public void display() {System.out.println(this.toString());}
	
//attributes
	//private
	private int width;
	private int height;
	private boolean shiftable;
	private ArrayList<ArrayList<Tile>> gridTiles;
}
