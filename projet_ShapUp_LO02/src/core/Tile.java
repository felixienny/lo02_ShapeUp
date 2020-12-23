package core;

public class Tile implements Cloneable {
    private Card card;
    private boolean alive;

    public Tile(Card card, boolean alive) {
        this.card = card;
        this.alive = alive;
    }
    
    /** 
     * @return Card
     */
    public Card getAndRemoveCard(){
        Card cardToRemove;
        if (this.getCard() == null) cardToRemove = null;
        else cardToRemove = this.card.clone();
        this.card = null;
        return cardToRemove; 
    }
    
    
    /**
     * @return Card
     */
    public Card getCard() { return this.card; }
    
    
    /** 
     * @return boolean
     */
    public boolean getAlive() { return this.alive; }

    
    
    /** 
     * @param card
     */
    public void setCard(Card card) { this.card = card; }

    
    /** 
     * @param card
     */
    public void setAlive(boolean alive) { this.alive = alive; }

    
    
    /** 
     * @return Tile
     */
    public Tile clone(){ 
        if (this.card != null) return new Tile(this.card.clone(), this.alive); 
        else return new Tile(null, this.alive); 
    }
}