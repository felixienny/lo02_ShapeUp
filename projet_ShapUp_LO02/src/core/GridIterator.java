package core;

import java.util.Iterator;

public class GridIterator implements Iterator<Card> {
	boolean iterateOverEverything;
	boolean iterateOverRows;
	
	int x;//rows
	int y;//columns
	
	Grid gridAdress;
	
	public GridIterator(Grid gridAdress)
	{
		this.iterateOverEverything=true;
		this.iterateOverRows=false;
		this.x=0;
		this.y=0;
		this.gridAdress=gridAdress;
	}
	public GridIterator(Grid gridAdress, String a, int origin)
	{
		this.iterateOverEverything=false;
		this.gridAdress=gridAdress;
		if(a.equals("row"))
		{
			this.iterateOverRows=true;
			this.x=origin;
			this.y=0;
		}
		else
		{
			this.iterateOverRows=false;
			this.x=0;
			this.y=origin;
		}
	}
	
	public boolean hasNext()
	{
		boolean answer;
		
		if(iterateOverEverything)
		{
			if(x+1>=this.gridAdress.getWidth())
			{
				answer=this.gridAdress.containsACard(0, y+1);
			}
			else
			{
				answer=this.gridAdress.containsACard(x, y);
			}
		}
		else
		{
			if(iterateOverRows)
			{
				if(this.gridAdress.containsACard(x+1, y)) answer=true;
				else answer=false;
			}
			else
			{
				if(this.gridAdress.containsACard(x, y+1)) answer=true;
				else answer=false;
			}
		}
		
		return answer;
	}
	public Card next()
	{
		//! getCard private
		Card answer = this.gridAdress.getCard(x, y);
		
		if(iterateOverEverything)
		{
			if(x+1>=this.gridAdress.getWidth())
			{
				this.x=0;
				this.y++;
			}
			else
			{
				this.x++;
			}
		}
		else
		{
			if(iterateOverRows)
			{
				this.x++;
			}
			else
			{
				this.y++;
			}
		}
		return answer;
	}
}
