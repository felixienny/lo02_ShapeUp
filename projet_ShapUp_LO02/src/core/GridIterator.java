package core;

import java.util.Iterator;

public class GridIterator implements Iterator<Grid.Tile> {
	boolean iterateOverEverything;
    boolean iterateOverX;
    boolean iterateOverY;

    
	int x,y;	
	Grid gridAdress;
	
	public GridIterator(Grid gridAdress) {
		this.iterateOverEverything=true;
		this.iterateOverX=false;
		this.iterateOverY=false;
		this.x=0;
        this.y=0;
		this.gridAdress=gridAdress;
        // if (!this.gridAdress.containsACard(x,y)) {
        //     do {
        //         y++;
        //     } while (!this.gridAdress.containsACard(x,y) || y>=this.gridAdress.getWidthOnSpecificLine(x));
        //     if (!this.gridAdress.containsACard(x,y)) y=0;
        // }
    }
    
	public GridIterator(Grid gridAdress, boolean iterateOverX, boolean iterateOverY, int line) {
        this.iterateOverEverything = false;
        this.iterateOverX = iterateOverX;
        this.iterateOverY = iterateOverY;
        this.gridAdress=gridAdress;
        
		if(iterateOverX) {
			this.x=0;
            this.y=line;
            // if (!this.gridAdress.containsACard(x,y)) {
            //     do {
            //         x++;
            //     } while (!this.gridAdress.containsACard(x,y) || x>=this.gridAdress.getHeight());
            //     if (!this.gridAdress.containsACard(x,y)) x=0;
            // }
		}
		else if (iterateOverY) {
			this.x=line;
            this.y=0;
            // if (!this.gridAdress.containsACard(x,y)) {
            //     do {
            //         y++;
            //     } while (!this.gridAdress.containsACard(x,y) || y>=this.gridAdress.getWidthOnSpecificLine(x));
            //     if (!this.gridAdress.containsACard(x,y)) y=0;
            // }
		}
	}
	
	public boolean hasNext() {
		boolean answer=false;
		
		if(iterateOverEverything) {
			if(x<this.gridAdress.getHeight() && y+1<this.gridAdress.getWidthOnSpecificLine(x)) {
				answer = true;//this.gridAdress.containsACard(x, y+1);
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
	public Grid.Tile next() {
		//! getCard private
        Grid.Tile nextTile = this.gridAdress.getTile(x, y);
		
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