package core;
/**
 * Provides an implementation for the visitor pattern. This class implement the interface {@link core.Visitor}.
 * When the function {@link core.Grid#calculateScore(Card)} is called, it creates an object to go along the map
 * and count points for one victory card attributes.
 */

class ScoreCounter implements Visitor{
	int score;

	final Shape vCardShape;
	final Color vCardColor;
	final boolean vCardHollow;
	
	int shapeCombo;
	int colorCombo;
    int hollowCombo;
    
	/** 
     * @param victoryCard
     * Constructor of the class, needs a victory card to compare cards placed on tiles with the victory card of a player.
     * It initialises the scores combos for shape, color and hollow at 0.
     */
	public ScoreCounter(Card victoryCard) {
		this.vCardShape=victoryCard.getShape();
		this.vCardColor=victoryCard.getColor();
        this.vCardHollow=victoryCard.getHollow();

		this.shapeCombo=0;
		this.colorCombo=0;
		this.hollowCombo=0;
	}
	
    
    /** 
     * @param currentTile
     * This function visit one tile and if there is a card compare this card to the player's victory card.
     * Based on the game rules, the combos are increase or not if there are no reasons.
     */
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
    
    
    
    /** 
     * @return int
     * This function is called by {@link core.Grid#calculateScore(Card)} to return at the end of the row or column processed,
     * the total score of the player with its specific victoryCard.
     */
    public int getScore() {
        if(colorCombo>=3) score+=(colorCombo+1); 
        if(shapeCombo>=2) score+=(shapeCombo-1); 
        if(hollowCombo>=3) score+=hollowCombo; 
        return score; 
    }

    
    /** 
     * In order to limit the number of objects created, this function is used to reset the scores to their initials values.
     */
    public void reset() {
        this.score=0;
        this.colorCombo=0;
        this.shapeCombo=0;
        this.hollowCombo=0;
    }
}