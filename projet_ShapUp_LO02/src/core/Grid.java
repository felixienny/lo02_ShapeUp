package core;

import java.lang.Cloneable;

public class Grid implements Cloneable {
	public Grid(int width, int height){
		this.height=height;
		this.width=width;
		
		this.gridTiles = new Card[width][height];
		this.gridAliveTiles = new boolean[width][height];
		
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				gridAliveTiles[x][y] = true;
			}
		}
		
	}
	public Grid(int width, int height, String[] gridDeadTiles){
		this.height=height;
		this.width=width;
		
		this.gridTiles = new Card[width][height];
		this.gridAliveTiles = new boolean[width][height];
		
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				gridAliveTiles[x][y] = true;
			}
		}

		for(int n=0;n<gridDeadTiles.length;n++){
			int x = Integer.valueOf(gridDeadTiles[n].split(",")[0]);
			int y = Integer.valueOf(gridDeadTiles[n].split(",")[1]);
			gridAliveTiles[x][y] = false;
		}
		
		
	}
//methods
	//setter
	public boolean setTile(int x, int y, Card cardToPlace) {
		if(this.testSettingTile(x, y)){
			this.gridTiles[x][y] = cardToPlace;
			return true;
		}
		return false;
	}
	public boolean moveTile(int xSrc, int ySrc, int xDest, int yDest){
		if (this.testMovingTile(xSrc, ySrc, xDest, yDest)) {
			this.gridTiles[xDest][yDest] = this.gridTiles[xSrc][ySrc].clone();//TODO clonage useless
			this.gridTiles[xSrc][ySrc] = null;
			return true;
		}
		return false;
	}
	
	//getter
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public Card getTile(int x, int y) {return this.gridTiles[x][y];}
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
			return this.gridAliveTiles[x][y];
		}
		return false;
	}
	public boolean canContainACard(int x, int y){
		if (this.checkBounds(x, y)) {
			return (this.gridAliveTiles[x][y] && this.gridTiles[x][y] == null);
		}
		return false;
	}
	public boolean containsACard(int x, int y){
		if (this.checkBounds(x, y)) {
			if (this.isAlive(x, y) && this.gridTiles[x][y] != null) {
				return true;
			}
		}
		return false;
	}
	public boolean isFull(){
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(this.gridTiles[x][y] == null) {
					return false;
				}
			}
		}
		return true;
	}
	public boolean isEmpty(){
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(this.isAlive(x,y) && !this.containsACard(x,y)) return false;
			}
		}
		return true;
	}
	public boolean isPlayable(int x, int y){
		if (this.isEmpty()) return true;
		
		for(int i=x-1 ; i<=x+1 ; i++){
			for (int j=y-1; j<=y+1; j++) {
				if (Math.abs(i+j)==1 && this.containsACard(i, j)){
					return true;
				}
			}
		}
		return false;
	}
	public boolean testSettingTile(int x, int y) {
		return this.isPlayable(x, y);
	}
	public boolean testMovingTile(int xSrc, int ySrc, int xDest, int yDest) {return (this.containsACard(xSrc, ySrc) && this.isPlayable(xDest, yDest));}
	
	//job specific
	public boolean checkBounds(int x, int y){
		if (x<0 || x>=this.width || y<0 || y>=this.height) return false;
		else return true;

		//TODO java.lang.ArrayIndexOutOfBoundsException
	}
	public Grid clone(){
		Grid clonedGrid = new Grid(this.width,this.height);
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				clonedGrid.gridAliveTiles[x][y] = this.gridAliveTiles[x][y];
				if(this.containsACard(x,y)) clonedGrid.gridTiles[x][y] = this.gridTiles[x][y].clone();
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

			Card lastCard = this.gridTiles[0][y];
			for(int x=1;x<this.height;x++){   
				Card currentCard = this.gridTiles[x][y];
				
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

			Card lastCard = this.gridTiles[x][0];
			for(int y=1;y<this.width;y++){   
				Card currentCard = this.gridTiles[x][y];
				
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
			if (this.containsACard(x, y)) returnString+=this.gridTiles[x][y].toString();
			else returnString+="---";
		}
		else returnString+="   ";
		return returnString;
	}
	public String toString()
	{
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
	private Card[][] gridTiles;
	private boolean[][] gridAliveTiles;
}
