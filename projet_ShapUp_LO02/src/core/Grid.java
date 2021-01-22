package core;

import java.lang.Cloneable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Provides the implementation of a play area composed of {@link core.Tile} containing {@link core.Card}. Checks the respect of
 * the rules of the Shape Up game.
 *
 */
public class Grid implements Cloneable, Iterable<Tile> {
	private int width;
	private int height;
	private boolean shiftable;
	private boolean isAdvancedGame;
	private ArrayList<ArrayList<Tile>> gridTiles;
	
	/** 
	 * Returns an iterator that will go over columns and rows of the Grid.
	 * @return Iterator<Tile>
	 */
	public Iterator<Tile> iterator() { return new GridIterator(this); }
	/**
	 * Returns a column iterator that will stop after having gone through each {@link core.Tile} of the given column.
	 * @param column The index of the column to go through.
	 * @return 
	 */
	public Iterator<Tile> columnIterator(int column) { return new GridIterator(this, true, false, column); }
	/**
	 * Returns a row iterator that will stop after having gone through each {@link core.Tile} of the given row.
	 * @param row The index of the row to go through.
	 * @return
	 */
	public Iterator<Tile> rowIterator(int row) { return new GridIterator(this, false, true, row); }

	/**
	 * Basic constructor of the Grid, handles the instantiation of the play area and giving the parameters to the {@link core.Tile}
	 * @param height Maximum number of Tiles vertically
	 * @param width Maximum number of Tiles horizontally
	 * @param shiftable Can the play area shift in some direction over turns.
	 * @param isAdvancedGame if it's an advanced game (player with 3 cards...)
	 */
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
	/**
	 * Constructor of the Grid that handles a String containing the list of {@link core.Tile} that should not be playable
	 * @param height Maximum number of Tiles vertically
	 * @param width Maximum number of Tiles horizontally
	 * @param shiftable Can the play area shift in some direction over turns.
	 * @param isAdvancedGame if it's an advanced game (player with 3 cards...)
	 * @param deadTiles contains the list of the Tiles that have to be disabled.
	 */
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
	/**
	 * Getter of if it's an advanced game.
	 * @return it's an advanced game
	 */
	public boolean isAdvancedGame() {return this.isAdvancedGame; }
	/**
	 * Gives the MAXIMUM possible height for this game.
	 * @return the MAXIMUM possible height
	 */
	public int getRealHeight() {return this.height;}
	/**
	 * Gives the MAXIMUM possible width for this game.
	 * @return the MAXIMUM possible width
	 */
	public int getRealWidth() {return this.width;}
	/**
	 * Gives the CURRENT height for this game.
	 * @return the CURRENT height
	 */
	public int getHeight() { return this.gridTiles.size(); }
	/**
	 * Gives the CURRENT width for this game.
	 * @return the CURRENT width
	 */
	public int getWidth() {
		int width = 0;
		for (int i=0; i<this.gridTiles.size(); i++) {
			if (this.gridTiles.get(i).size() >= width) width = this.gridTiles.get(i).size();
		}
		return width;
	}
	/**
	 * Gives the CURRENT width on a specific row for this game. Can differ if from row to row if the play area is type 1 (dynamically allocated).
	 * @param x The row to give the current width on.
	 * @return the CURRENT width
	 */
	public int getWidthOnSpecificLine(int x) {
		if (x>=0 && x<this.gridTiles.size()) return this.gridTiles.get(x).size();
		else return 0;
	}
	/**
	 * Looks at all the {@link core.Tile} of the {@link #gridTiles} to see if there is any available Tile hence a game that's not full.
	 * Used that check if the game is finished or not. 
	 * @return all the Tiles in the {@link #gridTiles} are occupied.
	 */
	public boolean isFull(){
		return ((this.height)*(this.width) == this.getNumberOfPlacedCards()+this.getNumberOfDeadTiles());
	}
	/**
	 * Checks that all the Tiles in the {@link #gridTiles} are empty.
	 * @return all the Tiles in the {@link #gridTiles} are empty.
	 */
	public boolean isEmpty(){
		return (this.getNumberOfPlacedCards() == 0);
	}
	/**
	 * Checks if any row or column is empty and so can be deallocated {@link #checkForDeallocating()}
	 * @param row Index of the row to check on
	 * @param column Index of the column to check on
	 * @return a row or column is empty and so can be deallocated
	 */
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
	/**
	 * Checks if transposing the matrix of tile would fit better. Used when a Card is asked to be put in a 3x4 play area when it is 4x3.
	 * @return transposing the matrix of tile would fit better
	 */
	private boolean isShiftable() {
		if (this.getHeight()<=Math.min(this.height,this.width) && this.getWidth()<=Math.min(this.height,this.width))
			return true;
		return false;
	}
	/**
	 * Actually performs the transposition of the matrix to change its orientation.
	 */
	private void shift() {
		int temp = this.height;
		this.height = this.width;
		this.width = temp;
	}
	/**
	 * Goes through each row and column to count the number of Tile that contains a card.
	 * @return the number of Tile that contains a card.
	 */
	public int getNumberOfPlacedCards(){
		int numberOfPlacedCards=0;
		Iterator<Tile> iterator = this.iterator();
		while (iterator.hasNext()){
			Tile tile = iterator.next();
			if(tile != null && tile.getCard() != null) numberOfPlacedCards++;
		}
		return numberOfPlacedCards;
	}
	/**
	 * Goes through the each row and column to count the number of tiles that are disabled.
	 * @return the number of tiles that are disabled.
	 */
	public int getNumberOfDeadTiles(){
		int numberOfDeadTiles=0;
		Iterator<Tile> iterator = this.iterator();
		while (iterator.hasNext()){
			Tile tile = iterator.next();
			if(tile != null && !tile.getAlive()) numberOfDeadTiles++;
		}
		return numberOfDeadTiles;
	}
	/**
	 * Verifies that given coordinates belong to the play area
	 * @param x row coordinate
	 * @param y column coordinate
	 * @return if the coordinates are not out of bound
	 */
	private boolean checkBounds(int x, int y){
		if (x<0 || x>=this.getHeight()) return false;
		if (y<0 || y>=this.getWidthOnSpecificLine(x)) return false;
		return true;
	}
	/**
	 * Gives the {@link core.Tile} at the given coordinates
	 * @param x row coordinate
	 * @param y column coordinate
	 * @return the Tile at the given coordinates
	 */
	public Tile getTile(int x, int y) {
		if (this.checkBounds(x, y)) return this.gridTiles.get(x).get(y);
		else return null;
	}
	/**
	 * Checks if the given coordinates are along an edge of the play area, on the edge of a list. Hence, if the play area si dynamically
	 * allocated and the given coordinates are "inside" the maximum size of the play area, it will still return true if it's on the end of a list.
	 * @param x the row to check
	 * @param y the column to check
	 * @return if the given coordinates are on the edge of the play area.
	 */
	public boolean isOnEdge(int x, int y) {
		if (this.checkBounds(x,y)) {
			if (x==this.gridTiles.size()-1 || y==this.gridTiles.get(x).size()-1) return true;
			else return false;
		}
		else return false;
	}
	/**
	 * Returns if a {@link core.Tile} is accepting cards, if it's not dead, disabled. Doesn't check if the Tile is available but only if the card
	 * is "alive". An occupied Tile will still return true.
	 * @param x the row coordinate
	 * @param y the column coordinate
	 * @return if the {@link core.Tile} at the given coordinates accepts cards.
	 */
	public boolean isAlive(int x, int y){
		if (this.checkBounds(x, y)) return this.gridTiles.get(x).get(y).getAlive();
		else return false;
	}
	/**
	 * Returns if a {@link core.Tile} is available to set a card on or if it's occupied or dead.
	 * @param x the row to check the tile on
	 * @param y to column to check the tile on
	 * @return if the card is available to place a card on.
	 */
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
	/**
	 * Checks if the {@link core.Tile} at the given coordinates currently contains a card. Will return false if the card is dead, disabled.
	 * @param x The row of the tile to check
	 * @param y the column of the tile to check
	 * @return if the tile currently contains a card.
	 */
	public boolean containsACard(int x, int y){
		return (this.isAlive(x, y) && this.gridTiles.get(x).get(y).getCard() != null);
	}
	/**
	 * When the {@link #gridTiles} are dynamically allocated and a card is set down inside the maximum play area but outside the list.
	 * Allocates the list to accomodate the new Card.
	 * @param xPos the row where the card will be placed
	 * @param yPos
	 */
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
	/**
	 * When the {@link #gridTiles} are dynamically allocated and a card is removed, a row or column can be empty, and so has to be
	 * deallocated. Called when we want to check that there is no empty list.
	 */
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
	/**
	 * Checks if a card will respect the adjacency rule of placed at the given coordinates. Checks for neighbors
	 * @param x the row the card could be placed on
	 * @param y the column the card could be placed on
	 * @return if a card here will respect the adjacency rule
	 */
	private boolean isPlayable(int x, int y){
		if (this.isEmpty()) return true;
		else {
			for(int i=x-1 ; i<=x+1 ; i++)
				for (int j=y-1; j<=y+1; j++)
					if ((i==x || j==y) && !(i==x && j==y) && this.containsACard(i,j)) return true;
		}
		return false;
	}
	/** Checks if a putting down a card at the given coordinates is possible for a {@link core.Player}. Verifies if all the rules of Shape Up are respected.
	 * @param x the row to place a card on
	 * @param y the column to place a card on
	 * @return boolean if placing a card here respects all the rules
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
	/**
	 * Puts down a card on given coordinates. If {@link #testSettingTile(int, int)} was not called before, the rules are still verified.
	 * @param x the row to place the card on
	 * @param y the column to place the card on
	 * @param cardToPlace the card to place at the given coordinates
	 * @return if the card was placed or not. Returns false if it didn't follow the rules and the card was not placed.
	 */
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
	/**
	 * Same as {@link #testSettingTile(int, int)} but accepts coordinates in the form of a String.
	 * @param coordinates String containing the coordinates
	 * @return if placing the rules at the coordinates respects the rules of Shape Up. Returns false if the card was not placed.
	 */
	public boolean testSettingTile(String coordinates) {
		if (coordinates.matches("^(-1|[0-9]),(-1|[0-9])$")) {
			int x = Integer.valueOf(coordinates.split(",")[0]);
			int y = Integer.valueOf(coordinates.split(",")[1]);
			return testSettingTile(x,y);
		}
		else return false;
	}
	/**
	 * Same as {@link #setTile(int, int, Card)} but accepts coordinates in the form of a String. if {@link #testSettingTile(String)} was not
	 * run before to check, the rules are still verified.
	 * @param coordinates String containing the coordinates.
	 * @param card the card to place on the coordinates
	 * @return if the card was effectively placed. Returns false if it didn't respects the rules and so was not placed.
	 */
	public boolean setTile(String coordinates, Card card) {
		if (coordinates.matches("^(-1|[0-9]),(-1|[0-9])$")) {
			int x = Integer.valueOf(coordinates.split(",")[0]);
			int y = Integer.valueOf(coordinates.split(",")[1]);
			return this.setTile(x, y, card);
		}
		else return false;
	}
	/**
	 * Checks if moving a {@link core.Card} from a place to another respects the rules of Shape Up.
	 * @param xSrc the row where the card will be taken of
	 * @param ySrc the column where the card will be taken of
	 * @param xDest the row where the card will be put on
	 * @param yDest the column where the card will be put on
	 * @return if moving a card from and to the given coordinates respects the rules of Shape Up.
	 */
	public boolean testMovingTile(int xSrc, int ySrc, int xDest, int yDest) {
		return (this.containsACard(xSrc, ySrc) && this.testSettingTile(xDest, yDest) && this.checkForDiconnectingGraphByMovingCard(xSrc,ySrc,xDest,yDest));
	}
	/**
	 * Actually moves the {@link core.Card} from coordinates to coordinates. If {@link #testMovingTile(int, int, int, int)} wasn't consulted before, 
	 * it stills checks if it follows the rules.
	 * @param xSrc the row where the card will be taken of
	 * @param ySrc the column where the card will be taken of
	 * @param xDest the row where the card will be put on
	 * @param yDest the column where the card will be put on
	 * @return if moving a card from and to the given coordinates respects the rules of Shape Up.
	 */
	public boolean moveTile(int xSrc, int ySrc, int xDest, int yDest){
		if (this.testMovingTile(xSrc, ySrc, xDest, yDest)) {
			Card cardToMove = this.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard();
			this.setTile(xDest, yDest, cardToMove);
			if (this.shiftable) this.checkForDeallocating();
			return true;
		}
		else return false;
	}
	/**
	 * Same as {@link #testMovingTile(int, int, int, int)} but accepts a coordinates in the form of a String.
	 * Checks if moving a {@link core.Card} from a place to another respects the rules of Shape Up.
	 * @param coordinates String containing the coordinates 
	 * @return
	 */
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
	/**
	 * Same as {@link #moveTile(int, int, int, int)}.
	 * If {@link #testMovingTile(String)} wasn't consulted before, it stills checks if it follows the rules.
	 * @param coordinates String containing the coordinates
	 * @return if the Tile was actually moved. Returns false if it didn't respect the rules and so the tile was not moved.
	 */
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
	/**
	 * Mother function where the recursive method called is {@link #checkForDiconnectingGraphByMovingCard(int, int, AtomicInteger, HashSet)}.
	 * Checks if moving a card respects the Shape Up rule that the cards must form one and only one cluster of cards.
	 * @param xSrc the row where the card will be taken of
	 * @param ySrc the column where the card will be taken of
	 * @param xDest the row where the card will be put on
	 * @param yDest the column where the card will be put on
	 * @return if the card can be moved while maintainging one and only one cluster of cards.
	 */
	public boolean checkForDiconnectingGraphByMovingCard(int xSrc, int ySrc, int xDest, int yDest) {
		AtomicInteger countedCards = new AtomicInteger(0);

		Grid computer = this.clone();
		computer.setTile(xDest, yDest, computer.gridTiles.get(xSrc).get(ySrc).getAndRemoveCard());
		if(xDest==-1) xDest=0;
		if(yDest==-1) yDest=0;
		computer.checkForDiconnectingGraphByMovingCard(xDest, yDest, countedCards, new HashSet<Tile>());
		
		return (countedCards.get() == this.getNumberOfPlacedCards());
	}
	/**
	 * Recursive function that travels through the {@link #gridTiles} to see if it does encounter all the cards. Each function goes to its neighbor.
	 * If a tile was already visited or is to be removed then nothing else it counts up in the total number of tile counted cards. At the end of the execution
	 * if the number of visited tiles is the same as the total number of cards then it's ok, else it means it forms another cluster of cards that
	 * weren't counted.
	 * @param x The row where the function is at
	 * @param y The column where the function is at
	 * @param countedCards Number of cards counted up to now
	 * @param tilesSeen Hashset of the cards already seen to avoid recounting
	 */
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
	/**
	 * Same as {@link #checkForDiconnectingGraphByAddingDeadTile(int, int)} but checks for adding a dead tile. Works on the same principle :
	 * Visit neighbor by neighbor each tile and if the number of counted tiles is not the same as the total number of tiles then it means
	 * adding a dead tile at the given coordinates splits the play area in two.
	 * @param xAdd Row of the tile to kill
	 * @param yAdd column of the tile to kill
	 * @return if it doesn't disconnects the tiles.
	 */
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
	/**
	 * Function called recursively, firstly by {@link #checkForDiconnectingGraphByAddingDeadTile(int, int)}.
	 * @param x row where the function is
	 * @param y column where the function is
	 * @param xAdd row where the tile is to kill
	 * @param yAdd column where the tile is to kill
	 * @param countedCards Number of cards counted up to now
	 * @param tilesSeen {@link java.util.HashSet} of the cards already seen to avoid recounting
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
	/**
	 * Provides a DEEP COPY of the Grid.
	 * @return the deep copied grid
	 */
	public Grid clone(){
		Grid clonedGrid = new Grid(this.height, this.width, this.shiftable, this.isAdvancedGame);
		
		for(int x=0;x<this.getHeight();x++){
			if(this.shiftable) clonedGrid.gridTiles.add(new ArrayList<Tile>());
			for(int y=0;y<this.getWidthOnSpecificLine(x);y++){
				if(this.shiftable) clonedGrid.gridTiles.get(x).add(this.gridTiles.get(x).get(y).clone());
				else clonedGrid.gridTiles.get(x).set(y, this.gridTiles.get(x).get(y).clone());
			}
		}
		
		return clonedGrid;
	}
	/**
	 * Calculates the score for a given victory card.
	 * @param victoryCard The victory card to calculate the score from.
	 * @return the calculated score.
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
	 * Provides a formatted String for displaying a {@link core.Tile} and the {@link core.Card} it contains.
	 * @param x the row of the tile to format
	 * @param y the column of the tile to format
	 * @return A String containing a formatted output a {@link core.Tile}.
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
	 * Provides a formatted String for displaying the {@link #gridTiles}, hence offering a visual of the grid.
	 * @return a formatted String
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
	/**
	 * Prints in the System.in console the output of {@link #toString()}.
	 */
	public void display() {
		System.out.println(this.toString());
	}
}
