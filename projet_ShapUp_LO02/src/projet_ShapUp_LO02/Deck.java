package projet_ShapUp_LO02;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    public Deck(int numberOfCards)
    {
    	int indexToAddFrom=0;
    	Card[] standardDeck = standardDeck();
    	shuffle(standardDeck);
    	
    	while(cards.size()<numberOfCards)
    	{
    		cards.add(standardDeck[indexToAddFrom]);
    		if(indexToAddFrom>=standardDeck.length) indexToAddFrom=0;
    	}
    	//! generated Deck with each Card having the same probability of appearing
    	standardDeck=null; //delete by letting gc() take care of it
    }
	
	public List<Card> cards = new ArrayList<Card> ();
    
	private void shuffle(Card[] cards)
	{
		//Fisher-Yates shuffle algorithm
		Random random = new Random();
		for(int i=0;i<cards.length;i++)
		{
			int randomIndex = i+random.nextInt(cards.length-1);
			exchangeCard(cards[i],cards[randomIndex]);
		}
	}
	private Card[] standardDeck()
	{
		Card cards[] = new Card[17];
		int i=0;
		for(Color colorSetter : Color.values())
		{
			for(Shape shapeSetter : Shape.values())
			{
				cards[i]=new Card(colorSetter,shapeSetter,false);
				i++;
				cards[i]=new Card(colorSetter,shapeSetter,true);
				i++;
			}
		}
		return cards;
	}
	private void exchangeCard(Card a, Card b)
	{
		Card temp=a;
		a=b;
		b=temp;
	}

	public Card pickNextCard() {return cards.get(0);};
	
	
	//partie Félix
	
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
        			Boolean[] Hollow = {true,false} ;
        			for (int j=0; j<Hollow.length; j++) {
        				cards.add(new Card(color, shape, Hollow[j]));
        			}
        		}
        	}
    	}
    }
    
    public void shuffle() {
    	Collections.shuffle(this.cards);
    }
    
    public static void main (String args []) {
    	Deck deck = new Deck(18);
    	deck.shuffle();
    	System.out.println(deck.cards);
    }
	
}
