package core;
import java.lang.Cloneable;

public class Tile implements Cloneable{
	public Tile()
	{
		canContainACard=true;
	}
	public Tile(Card newCard)
	{
		canContainACard=true;
		containedCard=newCard;
	}
	
	public boolean setTileDead()
	{
		boolean success=true;
		
		if(containedCard==null)
		{
			canContainACard=false;
		}
		else success=false;
		
		return success;
	}
	public boolean setCard(Card newCard)
	{
		boolean success=true;
		if(containedCard!=null || canContainACard==false) success=false;
		else
		{
			containedCard=newCard;
			return true;
		}
		return success;
	}

	public Card getAndRemoveCard()
	{
		Card tempCard=containedCard;
		this.containedCard=null;
		return tempCard;
	}
	public Card getCardReference() {return containedCard;}
	public boolean currentlyContainsACard() {return containedCard == null ? false : true;}
	public boolean isDead() {return canContainACard;}
	public Tile clone()
	{
		Tile clonedTile = new Tile();
		if(canContainACard==false) clonedTile.setTileDead();
		else
		{
			if(containedCard!=null)
			{
				clonedTile.setCard(containedCard.clone());
			}
		}
		return clonedTile;
	}
	
	private Card containedCard;
	private boolean canContainACard;
}
