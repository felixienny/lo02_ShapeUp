package projet_ShapUp_LO02;

public abstract class Player {
	public Player(String name)
	{
		this.name=name;
	}
	
	protected Grid playingGridAdress;
	protected String name;
	protected int score;
	protected Card victoryCard;
	
	public String getName() {return name;};
	public abstract void askMove(Card pickedCard);
	public void setPlayingGridAdress(Grid newPlayingGridAdress) {this.playingGridAdress=newPlayingGridAdress;}
}
