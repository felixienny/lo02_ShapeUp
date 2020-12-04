package core;

import java.lang.Cloneable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class Grid implements Cloneable, Iterable<Tile> {

	
	/** 
	 * @return Iterator<Tile>
	 */
	public Iterator<Tile> iterator() {
		return new GridIterator(this);
	}
	
	/** 
	 * @param column
	 * @return Iterator<Tile>
	 */
	public Iterator<Tile> columnIterator(int column) {
		return new GridIterator(this, true, false, column);
	}
	
	/** 
	 * @param row
	 * @return Iterator<Tile>
	 */
	public Iterator<Tile> rowIterator(int row) {
		return new GridIterator(this, false, true, row);
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
				if (checkBounds(x,y) && this.checkForDiconnectingGraphByAddingDeadTile(x,y)) this.gridTiles.get(x).get(y).setAlive(false);;
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
						if((Math.pow((x-((this.height)/2)),2)+Math.pow((y-((this.width)/2)),2)) >= Math.min(this.height,this.width)+1) {
							this.gridTiles.get(x).get(y).setAlive(false);;
						}
					}
				}
				break;
			case "DIAMOND":
				int ca = this.width/2;
				int cb = this.width/2;
				for(int x=0;x<this.height/2;x++){
					for (int y=0; y<ca; y++) {
						this.gridTiles.get(x).get(y).setAlive(false);
					}
					for (int y=this.width-1; y>cb; y--) {
						this.gridTiles.get(x).get(y).setAlive(false);
					}
					ca--;
					cb++;
				}
				for(int x=this.height/2;x<this.height;x++){
					for (int y=0; y<ca; y++) {
						this.gridTiles.get(x).get(y).setAlive(false);
					}
					for (int y=this.width-1; y>cb; y--) {
						this.gridTiles.get(x).get(y).setAlive(false);
					}
					ca++;
					cb--;
				}
				break;
			case "TRIANGLE":
				for(int x=0;x<this.height;x++){
					for(int y=0;y<this.width;y++){
						if(x-y<=(this.height-this.width)/2)
							this.gridTiles.get(x).get(y).setAlive(false);;
					}
				}
				break;
			case "WRAP":
				int c1 = 0;
				int c2 = this.width;
				for(int x=0;x<this.height;x++){
					if (c2<=c1) break;
					for (int y=c1; y<c2; y++) {
						this.gridTiles.get(x).get(y).setAlive(false);
					}
					c1++;
					c2--;
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
	public boolean isAdvancedGame() {return this.isAdvancedGame; }
	/** 
	 * @return int
	 */
	public int getHeight() {
		return this.gridTiles.size();
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
	 * @param x
	 * @return int
	 */
	public int getWidthOnSpecificLine(int x) {
		if (x<this.gridTiles.size()) return this.gridTiles.get(x).size();
		else return 0;
	}
	

	
	/** 
	 * @param x
	 * @param y
	 * @return Tile
	 */
	public Tile getTile(int x, int y) {
		if (this.checkBounds(x, y)) return this.gridTiles.get(x).get(y);
		return null;
	}

	
	/** 
	 * @return int
	 */
	public int getNumberOfPlacedCards(){
		int numberOfPlacedCards=0;
		Iterator<Tile> iterator = this.iterator();
		while (iterator.hasNext()){
			if(iterator.next().getCard() != null) numberOfPlacedCards++;
		}
		return numberOfPlacedCards;
	}

	
	/** 
	 * @return int
	 */
	public int getNumberOfDeadTiles(){
		int numberOfDeadTiles=0;
		Iterator<Tile> iterator = this.iterator();
		while (iterator.hasNext()){
			if(!iterator.next().getAlive()) numberOfDeadTiles++;
		}
		return numberOfDeadTiles;
	}

	/** 
	 * @return boolean
	 */
	public boolean isFull(){
		return ((this.height)*(this.width) == this.getNumberOfPlacedCards()+this.getNumberOfDeadTiles());
	}
	
	/** 
	 * @return boolean
	 */
	public boolean isEmpty(){
		return (this.getNumberOfPlacedCards() == 0);
	}
	
	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean isOnEdge(int x, int y) {
		if (this.checkBounds(x,y)) {
			if (x==this.gridTiles.size()-1 || y==this.gridTiles.get(x).size()-1) return true;
			else return false;
		}
		else return false;
	}

	/** 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean isAlive(int x, int y){
		if (this.checkBounds(x, y)) {
			return this.gridTiles.get(x).get(y).getAlive();
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
			else return this.gridTiles.get(x).get(y).getCard() == null;
		}
		else {
			if (this.checkBounds(x, y)) {
				return (this.isAlive(x, y) && this.gridTiles.get(x).get(y).getCard() == null);
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
				if (this.isAlive(x, y) && this.gridTiles.get(x).get(y).getCard() != null)
					return true;
		}
		else {
			if (this.checkBounds(x, y))
				if (this.gridTiles.get(x).get(y).getCard() != null) 
					return true;
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
				if (!cardAfter && this.gridTiles.get(x).get(y).getCard() == null) this.gridTiles.get(x).remove(y);
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
	private boolean isPlayable(int x, int y){
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
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean testSettingTile(int x, int y) {
		boolean result=false;
		if(this.isEmpty()) return true;
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
			}
			if (this.checkBounds(x, y)) {
				this.gridTiles.get(x).get(y).setCard(cardToPlace);
				return true;
			} 
		}
		return false;
	}

	
	/** 
	 * @param coordinates
	 * @return boolean
	 */
	public boolean testSettingTile(String coordinates) {
		if (coordinates.matches("^(-1|[0-9]),(-1|[0-9])$")) {
			int x = Integer.valueOf(coordinates.split(",")[0]);
			int y = Integer.valueOf(coordinates.split(",")[1]);
			return testSettingTile(x,y);
		}
		return false;
	}

	
	/** 
	 * @param coordinates
	 * @param card
	 * @return boolean
	 */
	public boolean setTile(String coordinates, Card card) {
		if (coordinates.matches("^(-1|[0-9]),(-1|[0-9])$")) {
			int x = Integer.valueOf(coordinates.split(",")[0]);
			int y = Integer.valueOf(coordinates.split(",")[1]);
			return this.setTile(x, y, card);
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
	public boolean testMovingTile(int xSrc, int ySrc, int xDest, int yDest) {
		return (this.containsACard(xSrc, ySrc) && this.checkForDiconnectingGraphByRemovingCard(xSrc,ySrc) && this.testSettingTile(xDest, yDest));
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
				this.gridTiles.get(xDest).get(yDest).setCard(this.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard());
				return true;
			}
		}
		return false;
	}

	
	/** 
	 * @param coordinates
	 * @return boolean
	 */
	public boolean testMovingTile(String coordinates) {
		if (coordinates.matches("^(-1|[0-9]),(-1|[0-9]):(-1|[0-9]),(-1|[0-9])$")) {
			int xSrc = Integer.valueOf(coordinates.split(":")[0].split(",")[0]);
			int ySrc = Integer.valueOf(coordinates.split(":")[0].split(",")[1]);
			int xDest = Integer.valueOf(coordinates.split(":")[1].split(",")[0]);
			int yDest = Integer.valueOf(coordinates.split(":")[1].split(",")[1]);
			return testMovingTile(xSrc,ySrc,xDest,yDest);
		}
		return false;
	}

	
	/** 
	 * @param coordinates
	 * @return boolean
	 */
	public boolean moveTile(String coordinates) {
		if (coordinates.matches("^(-1|[0-9]),(-1|[0-9]):(-1|[0-9]),(-1|[0-9])$")) {
			int xSrc = Integer.valueOf(coordinates.split(":")[0].split(",")[0]);
			int ySrc = Integer.valueOf(coordinates.split(":")[0].split(",")[1]);
			int xDest = Integer.valueOf(coordinates.split(":")[1].split(",")[0]);
			int yDest = Integer.valueOf(coordinates.split(":")[1].split(",")[1]);
			return moveTile(xSrc,ySrc,xDest,yDest);
		}
		return false;
	}


	
	/** 
	 * @param xRemove
	 * @param yRemove
	 * @return boolean
	 */
	private boolean checkForDiconnectingGraphByRemovingCard(int xRemove, int yRemove) {
		int xStart=0, yStart=0;
		AtomicInteger countedCards = new AtomicInteger(0);
		/*
		if((xStart==xRemove && yStart==yRemove) || !this.containsACard(xStart,yStart)){
			do {
				yStart++;
				while (!this.containsACard(xStart,yStart) && xStart<this.gridTiles.size()) xStart++;
			} while (!this.containsACard(xStart,yStart) && yStart<this.gridTiles.get(xStart).size());
		}
		*/
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
					clonedGrid.gridTiles.get(x).get(y).setAlive(false);gridTiles.get(x).get(y).getAlive();
					clonedGrid.gridTiles.get(x).get(y).setCard(this.gridTiles.get(x).get(y).getCard().clone());
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
		int score=0;
		
		ScoreCounter scoreCounter = new ScoreCounter(victoryCard);

		for(int x=0; x<this.getHeight(); x++) {
			scoreCounter.reset();
			Iterator<Tile> iterator = this.rowIterator(x);
			while (iterator.hasNext()) {
				scoreCounter.visit(iterator.next());
			}
			score += scoreCounter.getScore();
		}

		for(int y=0; y<this.getWidth(); y++) {
			scoreCounter.reset();
			Iterator<Tile> iterator = this.columnIterator(y);
			while (iterator.hasNext()) {
				scoreCounter.visit(iterator.next());
			}
			score += scoreCounter.getScore();
		}

		return score;
	}
	
	/** 
	 * @param x
	 * @param y
	 * @return String
	 */
	public String tileToString(int x, int y) {
		StringBuffer cardToString = new StringBuffer();
		if (this.shiftable) {
			if (this.containsACard(x, y)) cardToString.append(this.gridTiles.get(x).get(y).getCard().toString());
			else cardToString.append("---");
		}
		else {
			if (this.isAlive(x, y)) {
				if (this.containsACard(x, y)) cardToString.append(this.gridTiles.get(x).get(y).getCard().toString());
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
		
		for(int i=0;i<(5*getWidth()+2);i++) gridToString.append("_");
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

		for(int i=0;i<(5*getWidth()+2);i++) gridToString.append("_");
		
		return gridToString.toString();
	}
	public void display() {System.out.println(this.toString());}
	
//attributes
	//private
	private int width;
	private int height;
	private boolean shiftable;
	private boolean isAdvancedGame;
	private ArrayList<ArrayList<Tile>> gridTiles;
}
