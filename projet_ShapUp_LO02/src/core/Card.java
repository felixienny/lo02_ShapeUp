package core;
import java.lang.Cloneable;


class Card implements Cloneable{  
    public Card(Color newColor, Shape newShape, boolean newHollow)
	{
		color=newColor;
		shape=newShape;
		hollow=newHollow;
	}
	
    private Color color;
    private Shape shape;
    private boolean hollow;
    
    public Color getColor() {return color;}
    public Shape getShape() {return shape;}
    public boolean getHollow() {return hollow;}
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
    public void display()
    {
    	if(this.getHollow()) {
			System.out.print("F");
		}
		else {						
			System.out.print("H");
		}

		switch (this.getColor()) {
		case RED:
			System.out.print("R");
			break;
		case GREEN:
			System.out.print("G");
			break;
		case BLUE:
			System.out.print("B");
			break;
		default:
			break;
		}

		switch (this.getShape()) {
		case CIRCLE:
			System.out.print("C");
			break;
		case TRIANGLE:
			System.out.print("T");
			break;
		case SQUARE:
			System.out.print("S");
			break;
		default:
			break;
		}
    }
    public Card clone() {return new Card(color,shape,hollow);}
	
}
