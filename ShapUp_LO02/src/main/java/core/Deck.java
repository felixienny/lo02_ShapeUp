package projet_ShapUp_LO02;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
	public List<Card> cards = new ArrayList<Card> ();
    
    public Deck (float nbcartes) {
    	int nbloops;
    	if (nbcartes % 18 != 0) { 
    		if (nbcartes % 18 < 9) { nbloops = Math.round(nbcartes/18) + 1; }
    		else { nbloops = Math.round(nbcartes/18); }
    	}
    	else { nbloops = Math.round(nbcartes/18); }

    	for (int i=1; i<=nbloops; i++) {
    		for (Color color : Color.values()) { 
        		for (Shape shape : Shape.values()) {
					cards.add(new Card(color, shape, true));
					cards.add(new Card(color, shape, false));
        		}
        	}
    	}
    }
	public Card pickNextCard() {return cards.get(0);};
    public void shuffle() {
    	Collections.shuffle(this.cards);
    }
}
