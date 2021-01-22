package core;
import java.lang.Cloneable;

/**
 * Provides a simple class implementation to represent a card of the ShapUp game.
 */
class Card implements Cloneable{
	/**
	 * Constructor for this class. Given parameters are finals.
	 * @param newColor
	 * @param newShape
	 * @param newHollow
	 */
    public Card(Color newColor, Shape newShape, boolean newHollow)
	{
		color=newColor;
		shape=newShape;
		hollow=newHollow;
	}
    /**
     * 
     * @return The color of the card.
     */
    public Color getColor() {return color;}
    /**
     * 
     * @return The shape of the card.
     */
    public Shape getShape() {return shape;}
    /**
     * 
     * @return The hollowness of the card.
     */
    public boolean getHollow() {return hollow;}
    
    /**
     * 
     * @return A String formatted to display the card on 3 characters.
     */
    public String toString() {
    	String returnString = new String();

		if (hollow) returnString+="H";
		else returnString+="F";

		switch (color) {
			case RED: returnString+="R"; break;
			case GREEN: returnString+="G"; break;
			case BLUE: returnString+="B"; break;
			default: break;
		}

		switch (shape) {
			case CIRCLE: returnString+="C"; break;
			case TRIANGLE: returnString+="T"; break;
			case SQUARE: returnString+="S"; break;
			default: break;
		}
		return returnString;
    }
    /**
     * @return A deep copy of the card.
     */
	public Card clone() { return new Card(color,shape,hollow); }
    
	/**
	 * The color of this card.
	 */
    private Color color;
    /**
     * The shape of this card.
     */
    private Shape shape;
    /**
     * The hollowness of this card.
     */
    private boolean hollow;
}
