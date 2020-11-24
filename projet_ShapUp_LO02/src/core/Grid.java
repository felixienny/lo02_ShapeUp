package core;
import java.util.ArrayList;


import java.lang.Cloneable;

public class Grid implements Cloneable {
	private int width;
	private int height;
	private Card[][] gridTiles;
	private Boolean[][] gridAliveTiles;

	public Grid(int width, int height){
		this.height=height;
		this.width=width;
		
		this.gridTiles = new Card[width][height];
		this.gridAliveTiles = new Boolean[width][height];
		
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
		this.gridAliveTiles = new Boolean[width][height];
		
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
	
	public int getWidth() {return width;}
	public int getHeight() {return height;}

	public boolean checkBounds(int x, int y){
		if (x>=0 || x<=this.width || y>=0 || y>=this.height) return false;
		else return true;

		//TODO java.lang.ArrayIndexOutOfBoundsException
	}

	public boolean isAlive(int x, int y){
		if (this.checkBounds(x, y)) {
			return this.gridAliveTiles[x][y];
		}
		return false;
	}


	public boolean canContainACard(int x, int y){
		if (this.checkBounds(x, y)) {
			return this.gridAliveTiles[x][y] && this.gridTiles[x][y] == null;
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


	public int getNumberOfPlacedCards(){
		int numberOfCards=0;
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				if(this.containsACard(x,y)) { numberOfCards++; }
			}
		}	
		return numberOfCards;
	}

	public boolean isEmpty(){
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(this.gridTiles[x][y] != null) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isFull(){
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
				if (Math.abs(i+j)==1 && this.canContainACard(i, j)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean testSettingTile(int x, int y) {
		return this.isPlayable(x, y);
	}


	public boolean setTile(int x, int y, Card cardToPlace) {
		if(this.testSettingTile(x, y)){
			this.gridTiles[x][y] = cardToPlace;
			return true;
		}
		return false;
	}

	public boolean testMovingTile(int xSrc, int ySrc, int xDest, int yDest) {
		return (this.containsACard(xSrc, ySrc) && this.canContainACard(xDest, yDest) && this.isPlayable(xDest, yDest));
	}

	public boolean moveTile(int xSrc, int ySrc, int xDest, int yDest){
		if (this.testMovingTile(xSrc, ySrc, xDest, yDest)) {
			this.gridTiles[xDest][yDest] = this.gridTiles[xSrc][ySrc].clone();
			this.gridTiles[xSrc][ySrc] = null;
			return true;
		}
		return false;
	}


	public Card getTile(int x, int y) {
		return this.gridTiles[x][y];
	}

	
	public Grid clone(){
		Grid clonedGrid = new Grid(this.width,this.height);
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				clonedGrid.gridAliveTiles[i][j] = this.gridAliveTiles[i][j];
				clonedGrid.gridTiles[i][j] = this.gridTiles[i][j].clone();
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
				
				if (currentCard == null) {
					shapeCombo=0;
					hollowCombo=0;
					colorCombo=0;
				}

				if (currentCard.getShape() == victoryCard.getShape() && currentCard.getShape() == lastCard.getShape()) shapeCombo++;
				else if (currentCard.getShape() == victoryCard.getShape()) {
					if (shapeCombo>=2) currentScore+=(shapeCombo-1);
					shapeCombo=0;
				}
				else shapeCombo=0;

				if (currentCard.getHollow() == victoryCard.getHollow() && currentCard.getHollow() == lastCard.getHollow()) hollowCombo++;
				else if (currentCard.getHollow() == victoryCard.getHollow()) {
					if (hollowCombo>=3) currentScore+=hollowCombo;
					hollowCombo=0;
				}
				else hollowCombo=0;

				if (currentCard.getColor() == victoryCard.getColor() && currentCard.getColor() == lastCard.getColor()) colorCombo++;
				else if (currentCard.getColor() == victoryCard.getColor()) {
					if (colorCombo>=3) currentScore+=(colorCombo+1);
					colorCombo=0;
				}
				else colorCombo=0;

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
				
				if (currentCard == null) {
					shapeCombo=0;
					hollowCombo=0;
					colorCombo=0;
				}

				if (currentCard.getShape() == victoryCard.getShape() && currentCard.getShape() == lastCard.getShape()) shapeCombo++;
				else if (currentCard.getShape() == victoryCard.getShape()) {
					if (shapeCombo>=2) currentScore+=(shapeCombo-1);
					shapeCombo=0;
				}
				else shapeCombo=0;

				if (currentCard.getHollow() == victoryCard.getHollow() && currentCard.getHollow() == lastCard.getHollow()) hollowCombo++;
				else if (currentCard.getHollow() == victoryCard.getHollow()) {
					if (hollowCombo>=3) currentScore+=hollowCombo;
					hollowCombo=0;
				}
				else hollowCombo=0;

				if (currentCard.getColor() == victoryCard.getColor() && currentCard.getColor() == lastCard.getColor()) colorCombo++;
				else if (currentCard.getColor() == victoryCard.getColor()) {
					if (colorCombo>=3) currentScore+=(colorCombo+1);
					colorCombo=0;
				}
				else colorCombo=0;

				lastCard = currentCard;
			}
		}

		return currentScore;
	}


	public void displayCard(Card card) {
		StringBuffer sb = new StringBuffer();

		if (card.getHollow()) { sb.append("H"); }
		else { sb.append("H"); }

		switch (card.getColor()) {
			case RED: sb.append("R"); break;
			case GREEN: sb.append("G"); break;
			case BLUE: sb.append("B"); break;
			default: break;
		}

		switch (card.getShape()) {
			case CIRCLE: sb.append("C"); break;
			case TRIANGLE: sb.append("T"); break;
			case SQUARE: sb.append("S"); break;
			default: break;
		}

		System.out.print(sb.toString());
	}

	public void displayTile(int x, int y) {
		if (this.isAlive(x, y)) {
			if (this.containsACard(x, y)) {
				this.displayCard(this.getTile(x, y));
			}
			else {
				System.out.print("XXX");
			}
		}
		else {
			System.out.print("   ");
		}
		System.out.print(" | ");
	}

	public void display() {
		for (int x=0; x<height; x++) {
			for (int y=0; y<width; y++) {
				displayTile(x, y);
				System.out.println("");
			}
			for (int y=0; y<width; y++) {
				System.out.print("------");
				System.out.println("");
			}
		}
	}
}
