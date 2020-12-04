package core;

class ScoreCounter implements Visitor{
	int score;

	final Shape vCardShape;
	final Color vCardColor;
	final boolean vCardHollow;
	
	int shapeCombo;
	int colorCombo;
	int hollowCombo;
	
	public ScoreCounter(Card victoryCard) {
		this.vCardShape=victoryCard.getShape();
		this.vCardColor=victoryCard.getColor();
        this.vCardHollow=victoryCard.getHollow();

		this.shapeCombo=0;
		this.colorCombo=0;
		this.hollowCombo=0;
	}
	
	public void visit(Tile currentTile) {

        if(currentTile!=null && currentTile.getCard()!=null) {
            Card currentCard = currentTile.getCard();

            if (currentCard.getColor() == vCardColor) colorCombo++;
            else {
                if(colorCombo>=3) score+=(colorCombo+1); 
                colorCombo=0;
            }

            if (currentCard.getShape() == vCardShape) shapeCombo++;
            else {
                if(shapeCombo>=2) score+=(shapeCombo-1); 
                shapeCombo=0;
            }
            
            if (currentCard.getHollow() == vCardHollow) hollowCombo++;
            else {
                if(hollowCombo>=3) score+=hollowCombo; 
                hollowCombo=0;
            }
        }
        else {
            colorCombo=0;
            shapeCombo=0;
            hollowCombo=0;
        }
    }
    
    public int getScore() { 
        if(colorCombo>=3) score+=(colorCombo+1); 
        if(shapeCombo>=2) score+=(shapeCombo-1); 
        if(hollowCombo>=3) score+=hollowCombo; 
        return score; 
    }

    public void reset() {
        this.score=0;
        this.colorCombo=0;
        this.shapeCombo=0;
        this.hollowCombo=0;
    }
}