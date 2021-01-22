package core;
/**
 * A smaller object to contain the informations of a part of a {@link core.Grid} : dead, empty, occupied tile, cloning.
 */
public class Tile implements Cloneable {
    private Card card;
    private boolean alive;
    /**
     * Instantiate and gives basic informations for the Tile.
     * @param card null or not null Card.
     * @param alive indicates if a card can be set on the Tile.
     */
    public Tile(Card card, boolean alive) {
        this.card = card;
        this.alive = alive;
    }
    /** 
     * Gives the card that is on the Tile and removes it from the Tile. {@link core.Tile#getCard()}
     * @return Card Card that's no longer on the Tile.
     */
    public Card getAndRemoveCard(){
        Card cardToRemove;
        if (this.getCard() == null) cardToRemove = null;
        else cardToRemove = this.card.clone();
        this.card = null;
        return cardToRemove; 
    }
    /**
     * Getter to peak the card that's on the Tile. Doesn't remove it. {@link core.Tile#getAndRemoveCard()}
     * @return Card That is peaked on.
     */
    public Card getCard() { return this.card; }
    /**
     * Getter to know if the Tile can receive a card or not.
     * @return boolean
     */
    public boolean getAlive() { return this.alive; }
    /** 
     * Puts down the Card on the Tile.
     * @param card
     */
    public void setCard(Card card) { this.card = card; }
    /** 
     * Set if the Tile can receive a Card or not.
     * @return Tile
     */
    public void setAlive(boolean alive) { this.alive = alive; }
    /**
     * Returns a deep copy of the Card. {@link java.Cloneable}
     * @return the clone of the Card
     */
    public Tile clone(){ 
        if (this.card != null) return new Tile(this.card.clone(), this.alive); 
        else return new Tile(null, this.alive); 
    }
}