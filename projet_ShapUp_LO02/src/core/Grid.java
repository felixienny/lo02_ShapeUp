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
	public Iterator<Tile> iterator() { return new GridIterator(this); }
	public Iterator<Tile> columnIterator(int column) { return new GridIterator(this, true, false, column); }
	public Iterator<Tile> rowIterator(int row) { return new GridIterator(this, false, true, row); }

	public Grid(int height, int width, boolean shiftable, boolean isAdvancedGame){
		this.height=height;
		this.width=width;
		this.shiftable=shiftable;
		this.isAdvancedGame=isAdvancedGame;
		this.gridTiles = new ArrayList<ArrayList<Tile>>();

		if (!this.shiftable) {	
			for(int x=0;x<this.height;x++) { 
				this.gridTiles.add(new ArrayList<Tile>());
				for(int y=0;y<this.width;y++) this.gridTiles.get(x).add(new Tile(null,true));
			}
		}
	}

	public Grid(int height, int width, boolean shiftable, boolean isAdvancedGame, String deadTiles){
		this.height=height;
		this.width=width;
		this.shiftable=shiftable;
		this.isAdvancedGame=isAdvancedGame;
		this.gridTiles = new ArrayList<ArrayList<Tile>>();

		if (!this.shiftable) {	
			for(int x=0;x<this.height;x++) { 
				this.gridTiles.add(new ArrayList<Tile>());
				for(int y=0;y<this.width;y++) this.gridTiles.get(x).add(new Tile(null,true));
			}

			if (deadTiles.matches("^(c|circle|C|CIRCLE)$")) {
				for(int x=0;x<this.height;x++){
					for(int y=0;y<this.width;y++){
						if((Math.pow((x-((this.height)/2)),2)+Math.pow((y-((this.width)/2)),2)) >= Math.min(this.height,this.width)+1)
							this.gridTiles.get(x).get(y).setAlive(false);
					}
				}
			}
			else if (deadTiles.matches("^(d|diamond|D|DIAMOND)$")) {
				int ca = this.width/2;
				int cb = this.width/2;
				for(int x=0;x<this.height/2;x++){
					for (int y=0; y<ca; y++) this.gridTiles.get(x).get(y).setAlive(false);
					for (int y=this.width-1; y>cb; y--) this.gridTiles.get(x).get(y).setAlive(false);
					ca--;
					cb++;
				}
				for(int x=this.height/2;x<this.height;x++){
					for (int y=0; y<ca; y++)  this.gridTiles.get(x).get(y).setAlive(false);
					for (int y=this.width-1; y>cb; y--) this.gridTiles.get(x).get(y).setAlive(false);
					ca++;
					cb--;
				}
			}
			else if (deadTiles.matches("^(t|triangle|T|TRIANGLE)$")) {
				for(int x=0;x<this.height;x++){
					for(int y=0;y<this.width;y++){
						if(x-y<=(this.height-this.width)/2) this.gridTiles.get(x).get(y).setAlive(false);
					}
				}
			}
			else if (deadTiles.matches("^(w|wrap|W|WRAP)$")) {
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
			}
			else if (deadTiles.matches("^([0-9],[0-9];)+$")) {
				int x,y;
				String[] gridDeadTiles = deadTiles.split(";");
				for(int n=0;n<gridDeadTiles.length;n++){
					if (gridDeadTiles[n].matches("^[0-9],[0-9]$")) {
						x = Integer.valueOf(gridDeadTiles[n].split(",")[0]);
						y = Integer.valueOf(gridDeadTiles[n].split(",")[1]);
						if (checkBounds(x,y) && this.checkForDiconnectingGraphByAddingDeadTile(x,y)) this.gridTiles.get(x).get(y).setAlive(false);;
					}
				}
			}
		}
	}

//methods
	public boolean isAdvancedGame() {return this.isAdvancedGame; }
	public ArrayList<ArrayList<Tile>> getTiles() {return this.gridTiles; }
	public int getRealHeight() {return this.height;}
	public int getRealWidth() {return this.width;}
	public int getHeight() { return this.gridTiles.size(); }
	public int getWidth() {
		int width = 0;
		for (int i=0; i<this.gridTiles.size(); i++) {
			if (this.gridTiles.get(i).size() >= width) width = this.gridTiles.get(i).size();
		}
		return width;
	}
	public int getWidthOnSpecificLine(int x) {
		if (x>=0 && x<this.gridTiles.size()) return this.gridTiles.get(x).size();
		else return 0;
	}
	






	public boolean isFull(){
		return ((this.height)*(this.width) == this.getNumberOfPlacedCards()+this.getNumberOfDeadTiles());
	}
	

	public boolean isEmpty(){
		return (this.getNumberOfPlacedCards() == 0);
	}

	
	private boolean isEmpty(int row, int column) {
		if (row ==-1) {
			Iterator<Tile> it = this.columnIterator(column);
			while (it.hasNext()) {
				Tile tile = it.next();
				if (tile!=null && tile.getCard()!=null) return false;
			}
		}
		else if (column == -1) {
			Iterator<Tile> it = this.rowIterator(row);
			while (it.hasNext()) {
				Tile tile = it.next();
				if (tile!=null && tile.getCard()!=null) return false;
			}
		}
		return true;
	}

	private boolean isShiftable() {
		if (this.getHeight()<=Math.min(this.height,this.width) && this.getWidth()<=Math.min(this.height,this.width))
			return true;
		return false;
	}

	private void shift() {
		int temp = this.height;
		this.height = this.width;
		this.width = temp;
	}
	
	public int getNumberOfPlacedCards(){
		int numberOfPlacedCards=0;
		Iterator<Tile> iterator = this.iterator();
		while (iterator.hasNext()){
			Tile tile = iterator.next();
			if(tile != null && tile.getCard() != null) numberOfPlacedCards++;
		}
		return numberOfPlacedCards;
	}

	public int getNumberOfDeadTiles(){
		int numberOfDeadTiles=0;
		Iterator<Tile> iterator = this.iterator();
		while (iterator.hasNext()){
			Tile tile = iterator.next();
			if(tile != null && !tile.getAlive()) numberOfDeadTiles++;
		}
		return numberOfDeadTiles;
	}

	

	private boolean checkBounds(int x, int y){
		if (x<0 || x>=this.getHeight()) return false;
		if (y<0 || y>=this.getWidthOnSpecificLine(x)) return false;
		return true;
	}

	
	
	public Tile getTile(int x, int y) {
		if (this.checkBounds(x, y)) return this.gridTiles.get(x).get(y);
		else return null;
	}

	public boolean isOnEdge(int x, int y) {
		if (this.checkBounds(x,y)) {
			if (x==this.gridTiles.size()-1 || y==this.gridTiles.get(x).size()-1) return true;
			else return false;
		}
		else return false;
	}

	public boolean isAlive(int x, int y){
		if (this.checkBounds(x, y)) return this.gridTiles.get(x).get(y).getAlive();
		else return false;
	}

	public boolean canContainACard(int x, int y){
		if (this.shiftable){
			if (!checkBounds(x, y)){
				boolean result = false;
				if (x==-1 && y==-1) return false;

				if (x>=0 && x<=this.getHeight() && this.getHeight()<=this.height) result = true;
				else if (x==-1 && this.getHeight()<this.height) result = true;
				else result = false;
			
				if (result && y>=0 && y<=this.getWidth() && this.getWidth()<=this.width) result = true;
				else if (result && y==-1 && this.getWidthOnSpecificLine(x)<this.width) result = true;
				else result = false;

				return result;
			}
			else return this.gridTiles.get(x).get(y).getCard() == null;
		}
		else return (this.isAlive(x, y) && this.gridTiles.get(x).get(y).getCard() == null);
	}

	public boolean containsACard(int x, int y){
		return (this.isAlive(x, y) && this.gridTiles.get(x).get(y).getCard() != null);
	}

	

	private void allocateTileGrid(int xPos, int yPos){
		if (xPos==-1) {
			this.gridTiles.add(0, new ArrayList<Tile>());
			xPos=0;
		}
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
		for (int x=0; x<this.getHeight(); x++) {
			if (this.isEmpty(x,-1)) this.gridTiles.remove(x);
			else break;
		}
		for (int x=this.getHeight()-1; x>=0; x--) {
			if (this.isEmpty(x,-1)) this.gridTiles.remove(x);
			else break;
		}

		for (int y=0; y<this.getWidth(); y++) {
			if (this.isEmpty(-1,y)){
				for (int x=0;x<this.getHeight();x++)
					if (this.checkBounds(x, y)) this.gridTiles.get(x).remove(y);
			}
			else break;
		}
		for (int y=this.getWidth()-1; y>=0; y--) {
			if (this.isEmpty(-1,y)){
				for (int x=0;x<this.getHeight();x++)
					if (this.checkBounds(x, y)) this.gridTiles.get(x).remove(y);
			}
			else break;
		}
	}




	private boolean isPlayable(int x, int y){
		if (this.isEmpty()) return true;
		else {
			for(int i=x-1 ; i<=x+1 ; i++)
				for (int j=y-1; j<=y+1; j++)
					if ((i==x || j==y) && !(i==x && j==y) && this.containsACard(i,j)) return true;
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
		if (this.shiftable && !this.checkBounds(x,y)) {
			boolean shift=false;
			if (x>=-1 && x<=this.getHeight() && x<Math.max(this.height,this.width)) {
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
				
				if (result && y>=-1 && y<=this.getWidth() && y<Math.max(this.height,this.width)) {
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
		}
		else result = true;

		return (result && this.canContainACard(x, y) && this.isPlayable(x,y));
	}


	public boolean setTile(int x, int y, Card cardToPlace) {
		if(this.testSettingTile(x, y)){
			if (this.shiftable && !this.checkBounds(x, y)) {
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

	
	public boolean testSettingTile(String coordinates) {
		if (coordinates.matches("^(-1|[0-9]),(-1|[0-9])$")) {
			int x = Integer.valueOf(coordinates.split(",")[0]);
			int y = Integer.valueOf(coordinates.split(",")[1]);
			return testSettingTile(x,y);
		}
		else return false;
	}

	
	public boolean setTile(String coordinates, Card card) {
		if (coordinates.matches("^(-1|[0-9]),(-1|[0-9])$")) {
			int x = Integer.valueOf(coordinates.split(",")[0]);
			int y = Integer.valueOf(coordinates.split(",")[1]);
			return this.setTile(x, y, card);
		}
		else return false;
	}




	public boolean testMovingTile(int xSrc, int ySrc, int xDest, int yDest) {
		return (this.containsACard(xSrc, ySrc) && this.testSettingTile(xDest, yDest) && this.checkForDiconnectingGraphByMovingCard(xSrc,ySrc,xDest,yDest));
	}


	public boolean moveTile(int xSrc, int ySrc, int xDest, int yDest){
		if (this.testMovingTile(xSrc, ySrc, xDest, yDest)) {
			Card cardToMove = this.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard();
			this.setTile(xDest, yDest, cardToMove);
			if (this.shiftable) this.checkForDeallocating();
			return true;
		}
		else return false;
	}


	public boolean testMovingTile(String coordinates) {
		if (coordinates.matches("^[0-9],[0-9]:(-1|[0-9]),(-1|[0-9])$")) {
			int xSrc = Integer.valueOf(coordinates.split(":")[0].split(",")[0]);
			int ySrc = Integer.valueOf(coordinates.split(":")[0].split(",")[1]);
			int xDest = Integer.valueOf(coordinates.split(":")[1].split(",")[0]);
			int yDest = Integer.valueOf(coordinates.split(":")[1].split(",")[1]);
			return testMovingTile(xSrc,ySrc,xDest,yDest);
		}
		else return false;
	}

	

	public boolean moveTile(String coordinates) {
		if (coordinates.matches("^[0-9],[0-9]:(-1|[0-9]),(-1|[0-9])$")) {
			int xSrc = Integer.valueOf(coordinates.split(":")[0].split(",")[0]);
			int ySrc = Integer.valueOf(coordinates.split(":")[0].split(",")[1]);
			int xDest = Integer.valueOf(coordinates.split(":")[1].split(",")[0]);
			int yDest = Integer.valueOf(coordinates.split(":")[1].split(",")[1]);
			return moveTile(xSrc,ySrc,xDest,yDest);
		}
		else return false;
	}


	

	private boolean checkForDiconnectingGraphByMovingCard(int xSrc, int ySrc, int xDest, int yDest) {
		AtomicInteger countedCards = new AtomicInteger(0);

		Grid computer = this.clone();
		computer.setTile(xDest, yDest, computer.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard());
		if(xDest==-1) xDest=0;
		if(yDest==-1) yDest=0;
		computer.checkForDiconnectingGraphByMovingCard(xDest, yDest, countedCards, new HashSet<Tile>());
		
		return (countedCards.get() == this.getNumberOfPlacedCards());
	}
	
	

	
	private void checkForDiconnectingGraphByMovingCard(int x, int y, AtomicInteger countedCards, HashSet<Tile> tilesSeen){
		if (this.containsACard(x, y) && !tilesSeen.contains(this.gridTiles.get(x).get(y))) {
			tilesSeen.add(this.gridTiles.get(x).get(y));
			countedCards.incrementAndGet();
			
			this.checkForDiconnectingGraphByMovingCard(x, y+1, countedCards, tilesSeen);
			this.checkForDiconnectingGraphByMovingCard(x, y-1, countedCards, tilesSeen);
			this.checkForDiconnectingGraphByMovingCard(x+1, y, countedCards, tilesSeen);
			this.checkForDiconnectingGraphByMovingCard(x-1, y, countedCards, tilesSeen);
		}
	}

	
	private boolean checkForDiconnectingGraphByAddingDeadTile(int xAdd, int yAdd) {
		int xStart=-1, yStart=-1;
		AtomicInteger countedCards = new AtomicInteger(0);

		for (int x=0; x<this.getHeight(); x++) {
			for (int y=0; y<this.getWidth(); y++) {
				if (this.isAlive(x,y)) {
					xStart=x;
					yStart=y;
				}
				if (yStart!=-1) break;
			}
			if (xStart!=-1) break;
		}
		
		checkForDiconnectingGraphByAddingDeadTile(xStart, yStart, xAdd, yAdd, countedCards, new HashSet<Tile>());
		
		return (countedCards.get() == this.height*this.width-this.getNumberOfDeadTiles()-1);
	}
	
	

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



	public Grid clone(){
		Grid clonedGrid = new Grid(this.height, this.width, this.shiftable, this.isAdvancedGame);
		
		for(int x=0;x<this.getHeight();x++){
			clonedGrid.gridTiles.add(new ArrayList<Tile>());
			for(int y=0;y<this.getWidthOnSpecificLine(x);y++){
				clonedGrid.gridTiles.get(x).add(this.gridTiles.get(x).get(y).clone());
			}
		}
		
		return clonedGrid;
	}
	

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
	
	public void display() {
		System.out.println(this.toString());
	}
	
//attributes
	//private
	private int width;
	private int height;
	private boolean shiftable;
	private boolean isAdvancedGame;
	private ArrayList<ArrayList<Tile>> gridTiles;
}
