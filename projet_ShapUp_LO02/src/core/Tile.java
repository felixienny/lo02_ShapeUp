package core;

public class Tile implements Cloneable {
    private Card card;
    private boolean alive;
    public Tile(Card card, boolean alive) {
        this.card = card;
        this.alive = alive;
    }
    public Card getAndRemoveCard(){ 
        Card cardToRemove = this.card.clone();
        this.card = null;
        return cardToRemove; 
    }
    public Card getCard() { return this.card; }
    public boolean getAlive() { return this.alive; }

    public void setCard(Card card) { this.card = card; }
    public void setAlive(boolean alive) { this.alive = alive; }

    public Tile clone(){
        return new Tile(this.card.clone(), this.alive);
    }
}