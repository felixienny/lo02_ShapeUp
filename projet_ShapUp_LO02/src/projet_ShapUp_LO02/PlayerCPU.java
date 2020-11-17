package projet_ShapUp_LO02;

public class PlayerCPU extends Player {
	public PlayerCPU(String name) {super(name);}
	
	public void askMove(Card pickedCard) {calculateBestMoveAndDo(pickedCard);}
	private void calculateBestMoveAndDo(Card pickedCard)
	{
		int width=this.playingGridAdress.getWidth();
		int height=this.playingGridAdress.getHeight();
		
		int scoreOfBestPossibility=0;
		int xOfBestPossibility=0;
		int yOfBestPossibility=0;
		
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				if(this.playingGridAdress.isFreeToPlaceACardOn(i, j))
				{
					Grid newPossibility=(Grid)this.playingGridAdress.clone();//clone
					newPossibility.setTile(i, j, pickedCard);//pose carte
					int scoreOfNewPossibility=newPossibility.calculateScore(this.victoryCard.clone());//c score
					if(scoreOfBestPossibility<scoreOfNewPossibility)
					{
						scoreOfBestPossibility=scoreOfNewPossibility;
						xOfBestPossibility=i;
						yOfBestPossibility=j;
					}
					
				}
			}
		}
		this.playingGridAdress.setTile(xOfBestPossibility, yOfBestPossibility, pickedCard);
		this.score=scoreOfBestPossibility;
	}
	
}
