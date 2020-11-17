package projet_ShapUp_LO02;
import java.lang.Cloneable;

//yghsd
public class Card implements Cloneable{  
    public Card(Color newColor, Shape newShape, boolean newHollow)
	{
		color=newColor;
		shape=newShape;
		hollow=newHollow;
	}
	
    private Color color;
    private Shape shape;
    private boolean hollow;
    
    public Color getColor() {return color;};
    public Shape getShape() {return shape;};
    public boolean getHollow() {return hollow;};
    public String toString() {
    	String str = new String();
    	str = this.shape + " " + this.color;
    	if(this.hollow) {
    		str += " HOLLOW\n";
    	}
    	else {
    		str += " FILLED\n";
    	}
    	return str;
    }
    public Card clone() {return new Card(color,shape,hollow);}
	//? implements interface equals
    public boolean equals(Card c) {
    	if (this.color == c.color && this.shape == c.shape && this.hollow == c.hollow) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
}
