package core;

import java.util.Iterator;

/**
 * Class created to be able to iterate over a {@link core.Grid}.
 * There are three types of iterator availables : all, row or column iterator.
 * The aim of this class is to make easy all the grid courses makes in Grid class for example.
 */
public class GridIterator implements Iterator<Tile> {
	/**
	 * Boolean to iterate over all the rows of the grid.
	 */
	boolean iterateOverEverything;
	/**
	 * Boolean to iterate on a specific row.
	 */
    boolean iterateOverX;
	/**
	 * Boolean to iterate on a specific column.
	 */
    boolean iterateOverY;

    
	int x,y;
	Grid gridAdress;

	
	/**
	 * Contructor that just takes in argument the current grid, and set iterate over all as default value.
	 */
	public GridIterator(Grid gridAdress) {
		this.iterateOverEverything=true;
		this.iterateOverX=false;
		this.iterateOverY=false;
		this.x=0;
        this.y=0;
		this.gridAdress=gridAdress;
    }
    
	/**
	 * Contructor that takes in arguments the current grid, two booleans and one int which is the index of the row or column to iterate over.
	 */
	public GridIterator(Grid gridAdress, boolean iterateOverX, boolean iterateOverY, int line) {
        this.iterateOverEverything = false;
        this.iterateOverX = iterateOverX;
        this.iterateOverY = iterateOverY;
        this.gridAdress=gridAdress;
        
		if (iterateOverX) {
			this.x=0;
            this.y=line;
		}
		else if (iterateOverY) {
			this.x=line;
            this.y=0;
		}
	}
	
	
	/** 
	 * Returns true if the actual tile observed has a neighbor tile in row or column.
	 * @return boolean
	 */
	public boolean hasNext() {
		boolean answer=false;
		
		if(iterateOverEverything) {
			if(x<this.gridAdress.getHeight() && y+1<this.gridAdress.getWidthOnSpecificLine(x)) {
				answer = true;
            }
            else if (x+1<this.gridAdress.getHeight()) {
                answer = true;
            }
            else if (x==this.gridAdress.getHeight()-1 && y==this.gridAdress.getWidthOnSpecificLine(x)-1) 
                answer = true;
        }
        else if(iterateOverX) {
            if (x<this.gridAdress.getHeight()) answer=true;
        }
        else if(iterateOverY) {
            if (y<this.gridAdress.getWidthOnSpecificLine(x)) answer=true;
        }

		return answer;
	}
	
	/** 
	 * Returns the neighbor tile in row or column of the actual tile observed.
	 * @return Tile
	 */
	public Tile next() {
        Tile nextTile = this.gridAdress.getTile(x, y);
		
		if(iterateOverEverything) {
			if (y+1>=this.gridAdress.getWidthOnSpecificLine(x)) {
				this.x++;
				this.y=0;
			}
			else {
				this.y++;
			}
        }
        else if(iterateOverX) {
            this.x++;
        }
        else if(iterateOverY) {
            this.y++;
        }

		return nextTile;
	}
}