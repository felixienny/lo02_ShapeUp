package core;

import java.lang.Cloneable;
import java.util.ArrayList;
import java.util.HashSet;

public class Grid implements Cloneable {

	
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		String[] gridDeadTiles = {"0,0","1,1","2,2","3,3","4,4","5,5"};
		Grid g = new Grid(8,4);
		// Grid g = new Grid(11,11,gridDeadTiles);
		// Grid g = new Grid(11,11,"CIRCLE");
		// Grid g = new Grid(10,10,"TRIANGLE"); 
		Deck d = new Deck(35);
		g.setTile(1, 0, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.setTile(0, -1, d.pickNextCard());
		g.moveTile(0,1,1,0);
		g.display();
	}

	private class Tile {
        private Card card;
        private boolean alive;
        public Tile(Card card, boolean alive) {
            this.card = card;
            this.alive = alive;
        }
		public Card getAndRemoveCard(){ 
			Card cardToRemove = this.card.clone();
			this.card = null;
			return cardToRemove; 
		}
    }

	public Grid(int height, int width){
		this.height=height;
		this.width=width;
		this.shiftable=true;
		this.gridTiles = new ArrayList<ArrayList<Tile>>();
	}

	public Grid(int width, int height, String[] gridDeadTiles){
		this.height=height;
		this.width=width;
		this.shiftable=false;
		
		this.gridTiles = new ArrayList<ArrayList<Tile>>(height);		
		for(int x=0;x<this.height;x++){
			this.gridTiles.add(new ArrayList<Tile>(width));
			for(int y=0;y<this.width;y++){
				this.gridTiles.get(x).add(new Tile(null,true));
			}
		}
		int x,y;
		for(int n=0;n<gridDeadTiles.length;n++){
			if (gridDeadTiles[n].matches("^[0-9],[0-9]$")) {
				x = Integer.valueOf(gridDeadTiles[n].split(",")[0]);
				y = Integer.valueOf(gridDeadTiles[n].split(",")[1]);
				//TODO check disconnects graph
				if (checkBounds(x,y)) this.gridTiles.get(x).get(y).alive = false;
			}
		}
	}

	public Grid(int width, int height, String predefined){
		this.height=height;
		this.width=width;
		this.shiftable=false;
		
		this.gridTiles = new ArrayList<ArrayList<Tile>>(height);		
		for(int x=0;x<this.height;x++){
			this.gridTiles.add(new ArrayList<Tile>(width));
			for(int y=0;y<this.width;y++){
				this.gridTiles.get(x).add(new Tile(null,true));
			}
		}
		switch (predefined) {
			case "CIRCLE":
				for(int x=0;x<this.height;x++){
					for(int y=0;y<this.width;y++){
						if((Math.pow((x-(this.height/2)),2)+Math.pow((y-(this.width/2)),2)) >= Math.min(this.height,this.width)+1) {
							this.gridTiles.get(x).get(y).alive = false;
						}
					}
				}
				break;
			case "TRIANGLE":
				for(int x=0;x<this.height;x++){
					for(int y=0;y<this.width;y++){
						if(x-y<(this.height-this.width)/2) {
							this.gridTiles.get(x).get(y).alive = false;
						}
					}
				}
				break;
			case "WRAP":
				for(int x=0;x<this.height;x++){
					for(int y=0;y<this.width;y++){
						if(y<=this.width/2) {
							if (x-y>(this.height-this.width)/2)
								this.gridTiles.get(x).get(y).alive = false;
						}
						else {
							if (x-y<(this.height-this.width)/2)
								this.gridTiles.get(x).get(y).alive = false;
						}
					}
				}
				break;
			default:
				this.shiftable = true;
				this.gridTiles = null;
				this.gridTiles = new ArrayList<ArrayList<Tile>>();
				break;
		}
	}

//methods
	//setter

	/** 
	 * @param x
	 * @param y
	 * @param cardToPlace
	 * @return boolean
	 */
	public boolean setTile(int x, int y, Card cardToPlace) {
		if(this.testSettingTile(x, y)){
			if(this.shiftable) {
				if (this.isEmpty()){
					x=0;
					y=0;
				} 
				this.allocateTileGrid(x,y);
				if(x==-1) x=0;
				if(y==-1) y=0;
				this.gridTiles.get(x).get(y).card = cardToPlace;
				return true;
			}
			else{
				this.gridTiles.get(x).get(y).card = cardToPlace;
				return true;
			}
		}
		return false;
	}
	
	/** 
	 * @param xSrc
	 * @param ySrc
	 * @param xDest
	 * @param yDest
	 * @return boolean
	 */
	public boolean moveTile(int xSrc, int ySrc, int xDest, int yDest){
		if (this.testMovingTile(xSrc, ySrc, xDest, yDest)) {
			if (this.shiftable) {
				if(xSrc==-1) xSrc=0;
				if(ySrc==-1) ySrc=0;
				Card cardToMove = this.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard();
				this.checkForDeallocating();
				this.setTile(xDest, yDest, cardToMove);
				return true;
			} 
			else {
				this.gridTiles.get(xDest).get(yDest).card = this.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard();
				this.gridTiles.get(xSrc).set(ySrc,null);
				return true;
			}
		}
		return false;
	}
	
	/** 
	 * @return int
	 */
	//getter
	public int getWidth() {
		int width = 0;
		for (int i=0; i<this.gridTiles.size(); i++) {
			if (this.gridTiles.get(i).size() >= width) width = this.gridTiles.get(i).size();
		}
		return width;
	}
	
	/** 
	 * @return int
	 */
	public int getHeight() {
		return this.gridTiles.size();
	}
	
	/** 
	 * @return int
	 */
	public int getNumberOfPlacedCards(){
		int numberOfCards=0;
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				if(this.containsACard(x,y)) { numberOfCards++; }
			}
		}	
		return numberOfCards;
	}

	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean isAlive(int x, int y){
		if (this.checkBounds(x, y)) {
			return this.gridTiles.get(x).get(y).alive;
		}
		return false;
	}

	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean canContainACard(int x, int y){
		if (this.shiftable){
			if (this.checkBounds(x, y)) {
				boolean result = false;
				if (x>=0 && x<=this.getHeight() && this.getHeight()<=this.height) result = true;
				else if (x==-1 && this.getHeight()<this.height) result = true;

				if (result && y>=0 && y<=this.getWidth() && this.getWidth()<=this.width) result = true;
				else if (result && y==-1 && this.gridTiles.get(x).size()<this.width) result = true;

				if (result && this.gridTiles.get(x).get(y).card == null) return true;
			}
		}
		else {
			if (this.checkBounds(x, y)) {
				return (this.isAlive(x, y) && this.gridTiles.get(x).get(y).card == null);
			}
		}
		return false;
	}
	
	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean containsACard(int x, int y){
		if(!this.shiftable){
			if (this.checkBounds(x, y))
				if (this.isAlive(x, y) && this.gridTiles.get(x).get(y).card != null)
					return true;
		}
		else {
			if (this.checkBounds(x, y))
				if (this.gridTiles.get(x).get(y).card != null) 
					return true;
		}
		return false;
	}

	/** 
	 * @return boolean
	 */
	public boolean isFull(){
		if (this.getHeight() != this.height) return false;
		for(int x=0;x<this.gridTiles.size();x++) {
			if (this.gridTiles.get(x).size() != this.width)
				return false;
			for(int y=0;y<this.gridTiles.get(x).size();y++) {
				if (this.shiftable) {
					if(this.gridTiles.get(x).get(y).card == null) 
						return false;
				}
				else {
					if(this.isAlive(x, y) && this.gridTiles.get(x).get(y).card == null) 
						return false;
				}
			}
		}
		return true;
	}
	
	/** 
	 * @return boolean
	 */
	public boolean isEmpty(){
		if (this.shiftable) {
			this.checkForDeallocating();
			if(this.gridTiles.size() == 0) return true;
			else return false;
		}
		else {
			for(int x=0;x<this.height;x++) {
				for(int y=0;y<this.width;y++) {
					if(this.isAlive(x,y) && this.containsACard(x,y)) return false;
				}
			}
			return true;
		}
	}
	
	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean isPlayable(int x, int y){ //todo change usage in playercpu
		if(this.shiftable) {
			if (this.isEmpty()) return true;
			for(int i=x-1 ; i<=x+1 ; i++){
				for (int j=y-1; j<=y+1; j++) {
					if ((i==x || j==y) && !(i==x && j==y) && this.containsACard(i,j)){
						return true;
					}
				}
			}
		}
		else {
			if (this.isEmpty() && this.checkBounds(x, y)) return true;
			if (checkBounds(x, y)){
				for(int i=x-1 ; i<=x+1 ; i++){
					for (int j=y-1; j<=y+1; j++) {
						if ((i==x || j==y) && !(i==x && j==y) && this.containsACard(i,j)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/** 
	 * @param xPos
	 * @param yPos
	 */
	private void allocateTileGrid(int xPos, int yPos){
		if (xPos==-1) {
			this.gridTiles.add(0, new ArrayList<Tile>());
			xPos=0;
			for(int y=0;y<=yPos;y++)
				this.gridTiles.get(xPos).add(new Tile(null,true)); }
		if (yPos==-1)
			for (int x=0;x<this.getHeight();x++)
				this.gridTiles.get(x).add(0,new Tile(null,true));
		if (xPos>=this.gridTiles.size() && xPos<this.height)
			for (int x=this.gridTiles.size(); x<=xPos; x++)
				this.gridTiles.add(new ArrayList<Tile>());
		if (yPos>=this.gridTiles.get(xPos).size() && yPos<this.width)
			for (int y=this.gridTiles.get(xPos).size(); y<=yPos; y++)
				this.gridTiles.get(xPos).add(new Tile(null,true));
	}

	private void checkForDeallocating() {
		boolean cardAfter;
		for (int x=this.gridTiles.size()-1; x>=0; x--) {
			cardAfter=false;
			for (int y=this.gridTiles.get(x).size()-1; y>=0; y--) {
				if (!cardAfter && this.gridTiles.get(x).get(y).card==null) this.gridTiles.get(x).remove(y);
				else cardAfter=true;
			}
			if (!cardAfter) this.gridTiles.remove(x);
		}
	}

	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean testSettingTile(int x, int y) {
		boolean result=false;
		if (this.shiftable) {
			if (!this.checkBounds(x,y)){
				boolean shift=false;
				if (x>=-1 && x<Math.max(this.height,this.width) && x<=this.gridTiles.size()) {
					if (x>=0 && x<this.height && this.getHeight()<=this.height) {
						result = true;
					}
					else if (x>=0 && this.isShiftable() && x<this.width && this.getHeight()<=this.width) {
						shift = true;
						result = true;
						this.shift();
					}
					else if (x==-1 && this.getHeight()<this.height) result = true;
					else if (x==-1 && this.isShiftable() && this.getHeight()<this.width) {
						shift = true;
						result = true;
						this.shift();
					}
					else result=false;
					
					if (result && y>=-1 && y<Math.max(this.height,this.width) && y<=this.getWidth()) {
						if (y>=0 && y<this.width && this.getWidth()<=this.width) result = true;
						else if (y>=0 && !shift && this.isShiftable() && y<this.height && this.getWidth()<=this.height) {
							shift = true;
							result = true;
							this.shift();
						}
						else if (y==-1 && this.getWidth()<this.width) result = true;
						else if (y==-1 && !shift && this.isShiftable() && this.getWidth()<this.height) {
							shift = true;
							result = true;
							this.shift();
						}
						else result = false;
					}
				}
				else result = false;
				result = result && this.isPlayable(x,y);
			}
			else result = this.isPlayable(x,y);
		}
		else result = this.isPlayable(x,y);
		return result;
	}

	/** 
	 * @param xSrc
	 * @param ySrc
	 * @param xDest
	 * @param yDest
	 * @return boolean
	 */
	public boolean testMovingTile(int xSrc, int ySrc, int xDest, int yDest) {
		boolean result = false;
		if(!this.shiftable){
			result = (this.containsACard(xSrc, ySrc) && this.canContainACard(xDest, yDest) && this.isPlayable(xDest, yDest));
		}
		else result = (this.containsACard(xSrc, ySrc) && this.testSettingTile(xDest, yDest));
		
		return result;
	}

	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	//job specific
	private boolean checkBounds(int x, int y){
		if (x<0 || x>=this.gridTiles.size()) return false; //|| x>=this.height
		if (y<0 || y>=this.gridTiles.get(x).size()) return false; //|| y>=this.width
		return true;
	}

	private void shift() {
		int temp = this.height;
		this.height = this.width;
		this.width = temp;
	}

	/** 
	 * @return boolean
	 */
	private boolean isShiftable() {
		if (this.getHeight()<=Math.min(this.height,this.width) && this.getWidth()<=Math.min(this.height,this.width))
			return true;
		return false;
	}

	/** 
	 * @return Grid
	 */
	public Grid clone(){
		Grid clonedGrid = new Grid(this.height,this.width);
		for(int x=0;x<height;x++){
			for(int y=0;y<width;y++){
				if(this.containsACard(x,y)) {
					clonedGrid.gridTiles.get(x).get(y).alive = this.gridTiles.get(x).get(y).alive;
					clonedGrid.gridTiles.get(x).get(y).card = this.gridTiles.get(x).get(y).card.clone();
				}
			}
		}
		return clonedGrid;
	}
	
	/** 
	 * @param victoryCard
	 * @return int
	 */
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
				
				if (currentCard == null) {
					shapeCombo=0;
					hollowCombo=0;
					colorCombo=0;
					continue;
				}
				else if (lastCard == null) {
					if (currentCard.getShape() == victoryCard.getShape()) shapeCombo=1;
					else shapeCombo=0;
					if (currentCard.getColor() == victoryCard.getColor()) colorCombo=1;
					else colorCombo=0;
					if (currentCard.getHollow() == victoryCard.getHollow()) hollowCombo=1;
					else hollowCombo=0;
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
				
				if (currentCard == null) {
					shapeCombo=0;
					hollowCombo=0;
					colorCombo=0;
					continue;
				}
				else if (lastCard == null) {
					if (currentCard.getShape() == victoryCard.getShape()) shapeCombo=1;
					else shapeCombo=0;
					if (currentCard.getColor() == victoryCard.getColor()) colorCombo=1;
					else colorCombo=0;
					if (currentCard.getHollow() == victoryCard.getHollow()) hollowCombo=1;
					else hollowCombo=0;
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
	
	/** 
	 * @param x
	 * @param y
	 * @return String
	 */
	public String tileToString(int x, int y) {
		StringBuffer cardToString = new StringBuffer();
		if (this.shiftable) {
			if (this.containsACard(x, y)) cardToString.append(this.gridTiles.get(x).get(y).card.toString());
			else cardToString.append("---");
		}
		else {
			if (this.isAlive(x, y)) {
				if (this.containsACard(x, y)) cardToString.append(this.gridTiles.get(x).get(y).card.toString());
				else cardToString.append("---");
			}
			else cardToString.append("XXX");
		}
		return cardToString.toString();
	}
	
	/** 
	 * @return String
	 */
	public String toString() {
		StringBuffer gridToString = new StringBuffer();
		
		for(int i=0;i<((3*width)+(2*width-1)+2);i++) gridToString.append("_");
		gridToString.append("\n");
		
		for (int x=0; x<this.gridTiles.size(); x++) {
			gridToString.append("|");
			for (int y=0; y<this.gridTiles.get(x).size(); y++) {
				gridToString.append(" ");
				gridToString.append(this.tileToString(x, y));
				gridToString.append(" ");
			}
			gridToString.append("|\n");
		}
		
		for(int i=0;i<((3*width)+(2*width-1)+2);i++) gridToString.append("_");
		// for(int i=0;i<(3*width)+(2*(width-1))+2;i++) returnString+=("_");
		
		return gridToString.toString();
	}
	public void display() {System.out.println(this.toString());}
	
//attributes
	//private
	private int width;
	private int height;
	private boolean shiftable;
	private ArrayList<ArrayList<Tile>> gridTiles;
}
