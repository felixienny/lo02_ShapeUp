package core;

import java.lang.Cloneable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class Grid implements Cloneable, Iterable<Card> {

	public Iterator<Card> iterator() {
		return new GridIterator(this);
	}
	public Iterator<Card> columnIterator(int column) {
		return new GridIterator(this, true, false, column);
	}
	public Iterator<Card> rowIterator(int row) {
		return new GridIterator(this, false, true, row);
	}

	
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		String[] gridDeadTiles = {"0,0","1,1","2,2","3,3","4,4","6,6","7,7","5,5"};
		Grid g = new Grid(8,8);
		Deck d = new Deck(36);
		// Grid g = new Grid(11,11,gridDeadTiles);
		// Grid g = new Grid(11,11,"CIRCLE");
		// Grid g = new Grid(4,8,"TRIANGLE"); 
		// Grid g = new Grid(12,15,"WRAP"); 

		g.setTile(0, 0, d.pickNextCard());
		g.setTile(0, 1, d.pickNextCard());
		g.setTile(0, 2, d.pickNextCard());
		g.setTile(0, 3, d.pickNextCard());
		g.setTile(0, 4, d.pickNextCard());
		g.setTile(0, 5, d.pickNextCard());
		g.setTile(0, 6, d.pickNextCard());
		g.setTile(0, 7, d.pickNextCard());
		
		g.setTile(1, 1, d.pickNextCard());
		g.setTile(1, 2, d.pickNextCard());
		g.setTile(1, 3, d.pickNextCard());
		g.setTile(1, 4, d.pickNextCard());
		g.setTile(1, 5, d.pickNextCard());
		g.setTile(1, 6, d.pickNextCard());
		g.setTile(1, 7, d.pickNextCard());
		
		g.setTile(2, 1, d.pickNextCard());
		g.setTile(2, 2, d.pickNextCard());
		g.setTile(2, 3, d.pickNextCard());
		g.setTile(2, 4, d.pickNextCard());
		g.setTile(2, 5, d.pickNextCard());
		g.setTile(2, 6, d.pickNextCard());
		g.setTile(2, 7, d.pickNextCard());
		
		g.setTile(3, 1, d.pickNextCard());
		g.setTile(3, 2, d.pickNextCard());
		g.setTile(3, 3, d.pickNextCard());
		g.setTile(3, 4, d.pickNextCard());
		g.setTile(3, 5, d.pickNextCard());
		g.setTile(3, 6, d.pickNextCard());
		g.setTile(3, 7, d.pickNextCard());
		
		g.setTile(4, 1, d.pickNextCard());
		g.setTile(4, 2, d.pickNextCard());
		g.setTile(4, 3, d.pickNextCard());
		g.setTile(4, 4, d.pickNextCard());
		g.setTile(4, 5, d.pickNextCard());
		g.setTile(4, 6, d.pickNextCard());
		g.setTile(4, 7, d.pickNextCard());
		
		g.display();

		System.out.println("\nit over row");
		Iterator<Card> itr = g.rowIterator(1);
		while (itr.hasNext()){
			System.out.println(itr.next());
		}

		System.out.println("\nit over col");
		Iterator<Card> itc = g.columnIterator(1);
		while (itc.hasNext()){
			System.out.println(itc.next());
		}

		System.out.println("\nit over all");
		Iterator<Card> ita = g.iterator();
		while (ita.hasNext()){
			System.out.println(ita.next());
		}

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

	public Grid(int height, int width, String[] gridDeadTiles){
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
				if (checkBounds(x,y) && this.checkForDiconnectingGraphByAddingDeadTile(x,y)) this.gridTiles.get(x).get(y).alive = false;
			}
		}
	}

	public Grid(int height, int width, String predefined){
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
						if(x-y<=(this.height-this.width)/2)
							this.gridTiles.get(x).get(y).alive = false;
					}
				}
				break;
			case "WRAP":
				for(int x=0;x<this.height;x++){
					for(int y=0;y<=(this.width/2);y++){
						if(x-y<=0)
							this.gridTiles.get(x).get(y).alive = false;
					}
					for(int y=this.width/2;y<this.width;y++){
						if(x+y<=this.width-1)
							this.gridTiles.get(x).get(y).alive = false;
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

	public int getWidthOnSpecificLine(int x) {
		if (x<this.gridTiles.size()) return this.gridTiles.get(x).size();
		else return 0;
	}
	
	/** 
	 * @return int
	 */
	public int getHeight() {
		return this.gridTiles.size();
	}

	public Card getCard(int x, int y) {
		if (this.checkBounds(x, y) && this.containsACard(x, y)) return this.gridTiles.get(x).get(y).card;
		return null;
	}
	
	/** 
	 * @return int
	 */
	public int getNumberOfPlacedCards(){
		int numberOfCards=0;
		for(int x=0;x<this.gridTiles.size(); x++){
			for(int y=0;y<this.gridTiles.get(x).size();y++){
				if(this.containsACard(x,y)) { numberOfCards++; }
			}
		}	
		return numberOfCards;
	}

	public int getNumberOfDeadTiles(){
		int numberOfDeadTiles=0;
		for(int x=0;x<this.gridTiles.size(); x++){
			for(int y=0;y<this.gridTiles.get(x).size();y++){
				if(this.gridTiles.get(x).get(y).alive == false) { numberOfDeadTiles++; }
			}
		}	
		return numberOfDeadTiles;
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
			if (!checkBounds(x, y)){
				boolean result = false;
				if (x>=0 && x<=this.getHeight() && this.getHeight()<=this.height) result = true;
				else if (x==-1 && this.getHeight()<this.height) result = true;
				else result = false;
			
				if (result && y>=0 && y<=this.getWidth() && this.getWidth()<=this.width) result = true;
				else if (result && y==-1 && this.gridTiles.get(x).size()<this.width) result = true;

				return result;
			}
			else return this.gridTiles.get(x).get(y).card==null;
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
			if (this.checkBounds(x, y) && this.canContainACard(x,y)){
				if (this.isEmpty()) return true;
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
				result = result && this.canContainACard(x, y) && this.isPlayable(x,y);
			}
			else result = this.canContainACard(x, y) && this.isPlayable(x,y);
		}
		else result = this.canContainACard(x, y) && this.isPlayable(x,y);
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
		return (this.containsACard(xSrc, ySrc) && this.checkForDiconnectingGraphByRemovingCard(xSrc,ySrc) && this.testSettingTile(xDest, yDest));
	}

	public boolean isOnEdge(int x, int y) {
		if (this.checkBounds(x,y)) {
			if (x==this.gridTiles.size()-1 || y==this.gridTiles.get(x).size()-1) return true;
			else return false;
		}
		else return false;
	}
	
	/** 
	 * @param xRemove
	 * @param yRemove
	 * @return boolean
	 */
	private boolean checkForDiconnectingGraphByRemovingCard(int xRemove, int yRemove) {
		int xStart=0, yStart=0;

		AtomicInteger countedCards = new AtomicInteger(0);

		if((xStart==xRemove && yStart==yRemove) || !this.containsACard(xStart,yStart)){
			do {
				yStart++;
				while (!this.containsACard(xStart,yStart) && xStart<this.gridTiles.size()) {
					xStart++;
				} 
			} while (!this.containsACard(xStart,yStart) && yStart<this.gridTiles.get(xStart).size());
		}

		checkForDiconnectingGraphByRemovingCard(xStart, yStart, xRemove, yRemove, countedCards, new HashSet<Tile>());
		return (countedCards.get() == this.getNumberOfPlacedCards()-1);
	}
	
	
	/** 
	 * @param x
	 * @param y
	 * @param xIgnore
	 * @param yIgnore
	 * @param arrivingDirection
	 * @param countedCards
	 * @param tilesSeen
	 */
	private void checkForDiconnectingGraphByRemovingCard(int x, int y, int xRemove, int yRemove, AtomicInteger countedCards, HashSet<Tile> tilesSeen){
		if (!(x==xRemove && y==yRemove) && this.containsACard(x, y) && !tilesSeen.contains(this.gridTiles.get(x).get(y))) {
			tilesSeen.add(this.gridTiles.get(x).get(y));
			countedCards.incrementAndGet();
			
			checkForDiconnectingGraphByRemovingCard(x, y+1, xRemove, yRemove, countedCards, tilesSeen);
			checkForDiconnectingGraphByRemovingCard(x, y-1, xRemove, yRemove, countedCards, tilesSeen);
			checkForDiconnectingGraphByRemovingCard(x+1, y, xRemove, yRemove, countedCards, tilesSeen);
			checkForDiconnectingGraphByRemovingCard(x-1, y, xRemove, yRemove, countedCards, tilesSeen);
		}
	}

	/** 
	 * @param xRemove
	 * @param yRemove
	 * @return boolean
	 */
	private boolean checkForDiconnectingGraphByAddingDeadTile(int xAdd, int yAdd) {
		int xStart=0, yStart=0;

		AtomicInteger countedCards = new AtomicInteger(0);

		if((xStart==xAdd && yStart==yAdd) || !this.containsACard(xStart,yStart)){
			do {
				yStart++;
				while (!this.isAlive(xStart,yStart) && xStart<this.gridTiles.size()) {
					xStart++;
				} 
			} while (!this.isAlive(xStart,yStart) && yStart<this.gridTiles.get(xStart).size());
		}
		
		checkForDiconnectingGraphByAddingDeadTile(xStart, yStart, xAdd, yAdd, countedCards, new HashSet<Tile>());
		return (countedCards.get() == this.height*this.width-this.getNumberOfDeadTiles()-1);
	}
	
	
	/** 
	 * @param x
	 * @param y
	 * @param xIgnore
	 * @param yIgnore
	 * @param arrivingDirection
	 * @param countedCards
	 * @param tilesSeen
	 */
	private void checkForDiconnectingGraphByAddingDeadTile(int x, int y, int xAdd, int yAdd, AtomicInteger countedCards, HashSet<Tile> tilesSeen){
		if (!(x==xAdd && y==yAdd) && this.isAlive(x, y) && !tilesSeen.contains(this.gridTiles.get(x).get(y))) {
			tilesSeen.add(this.gridTiles.get(x).get(y));
			countedCards.incrementAndGet();
			
			checkForDiconnectingGraphByAddingDeadTile(x, y+1, xAdd, yAdd, countedCards, tilesSeen);
			checkForDiconnectingGraphByAddingDeadTile(x, y-1, xAdd, yAdd, countedCards, tilesSeen);
			checkForDiconnectingGraphByAddingDeadTile(x+1, y, xAdd, yAdd, countedCards, tilesSeen);
			checkForDiconnectingGraphByAddingDeadTile(x-1, y, xAdd, yAdd, countedCards, tilesSeen);
		}

	}




	//job specific
	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
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
