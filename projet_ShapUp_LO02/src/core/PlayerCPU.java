package core;

class PlayerCPU extends Player {
	public PlayerCPU(String name) {super(name);}
	
	public void askMove(Card pickedCard) {calculateBestMoveAndDo(pickedCard);}
	private void calculateBestMoveAndDo(Card pickedCard)
	{
		int width=this.playingGridAdress.getWidth();
		int height=this.playingGridAdress.getHeight();
		
		int scoreOfBestPossibility=-1;
		int xOfBestPossibility=0;
		int yOfBestPossibility=0;
		
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				if(this.playingGridAdress.isPlayable(i, j))
				{
					Grid newPossibility=this.playingGridAdress.clone();//clone
					
					newPossibility.setTile(i, j, pickedCard);//pose carte
					int scoreOfNewPossibility=newPossibility.calculateScore(this.victoryCard.clone());//calc score
					
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
		this.currentScore=scoreOfBestPossibility;
	}
}
