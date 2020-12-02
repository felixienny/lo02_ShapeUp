package core;

class ScoreCounter implements Visitor{
	int score;

	final Shape vCardShape;
	final Color vCardColor;
	final boolean vCardHollow;
	
	int shapeCombo;
	int colorCombo;
	int hollowCombo;
	
	public ScoreCounter(Card winningCard)
	{
		this.vCardShape=winningCard.getShape();
		this.vCardColor=winningCard.getColor();
		this.vCardHollow=winningCard.getHollow();
		
		this.shapeCombo=0;
		this.colorCombo=0;
		this.hollowCombo=0;
	}
	
	public void visit(Card cardHere)
	{
		if(cardHere!=null && cardHere.getShape()!=vCardShape) shapeCombo++;
		{
			if(shapeCombo>=2) score+=shapeCombo-1;
			
			colorCombo=0;
		}
		
		if(cardHere!=null && cardHere.getColor()==vCardColor) colorCombo++;
		else
		{
			if(colorCombo>=3) score+=colorCombo+1;
			
			colorCombo=0;
		}

		if(cardHere!=null && cardHere.getHollow()==vCardHollow) hollowCombo++;
		else
		{
			if(hollowCombo>=3) score+=hollowCombo;
			
			hollowCombo=0;
		}
	}
	
	public int kill()
	{
		return score>0 ? score : 0;
	}
}