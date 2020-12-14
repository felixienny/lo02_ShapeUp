package core;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Deck {
	public Deck() { this.generate(); }
	
	public Card pickNextCard() {
		if(cards.size()==0) this.generate();
		return cards.remove(0);
	}

	private void generate() {
		for (Color color : Color.values()) { 
    		for (Shape shape : Shape.values()) {
				cards.add(new Card(color, shape, true));
				cards.add(new Card(color, shape, false));
    		}
		}
    	Collections.shuffle(this.cards, new Random(System.currentTimeMillis()));
	}
	
	private List<Card> cards = new ArrayList<Card> ();
}
