package core;
import java.lang.Cloneable;


class Card implements Cloneable{  
    public Card(Color newColor, Shape newShape, boolean newHollow)
	{
		color=newColor;
		shape=newShape;
		hollow=newHollow;
	}
//methods
    
    //setter
    
    //getter
    public Color getColor() {return color;}
    public Shape getShape() {return shape;}
    public boolean getHollow() {return hollow;}
    
    //job specific
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
    public void display()  {System.out.print(this.toString());}
    public Card clone() {return new Card(color,shape,hollow);}
    
//attributes
    //private
    private Color color;
    private Shape shape;
    private boolean hollow;
}
