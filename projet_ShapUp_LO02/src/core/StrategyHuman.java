package core;

import java.util.ArrayList;

class StrategyHuman extends Strategy {

	public void computeBestMove(ArrayList<Card> victoryCards)//advanced
	{
		System.out.print("Tes cartes sont : ");
		victoryCards.forEach(card -> System.out.print(" "+card));
		System.out.println("");

		this.actualGrid.display();
		
		boolean choice1done=false;
		boolean choice2done=false;
		boolean turnFinished=false;
		
		int choice=-1;
		do {
			if(choice1done)
			{
				do {
					this.actualGrid.display();
					System.out.println("Que veux-tu faire :");
					System.out.println("2 - Déplacer une carte déjà  sur le jeu");
					System.out.println("3 - Finir mon tour");
					choice = ShapUp.scanner.nextInt();
				}while(!(choice==2 || choice==3));
				if(choice==3) turnFinished=true;
			}
			
			if(!choice1done)
				do {
					System.out.println("Que veux-tu faire :");
					System.out.println("1 - Jouer une de tes cartes victoires");
					System.out.println("2 - Déplacer une carte déjà  sur le jeu");
					choice = ShapUp.scanner.nextInt();
				}while(!(choice==1 || choice==2));
			
			if(choice==1 && !choice1done)
			{
				do {
					System.out.println("Quelle carte veux tu jouer et oÃ¹ ?");
					String choicePlace = ShapUp.scanner.next().trim();
					this.WhereToSetCard.delete(0, this.WhereToSetCard.length());
					while (!choicePlace.matches("^[0-2]:(-1|[0-9]),(-1|[0-9])$")) {
						System.out.println("Mauvaise saisie, indique le numÃ©ro de ta carte (0-2) et l'endroit auquel tu veux la poser.");
						System.out.println("Exemple : 0-2:1,7");
						choicePlace = ShapUp.scanner.next().trim();
					}
					this.WhereToSetCard.append(choicePlace.split(":")[1]);
					this.bestVCardToUse = Integer.valueOf(choicePlace.split(":")[0]);
				} while (!this.actualGrid.testSettingTile(this.WhereToSetCard.toString()));
				
				choice1done=true;
			}
			
			if(choice==2 && !choice2done)
			{
				System.out.println(this.actualGrid.toString());
				do {
					System.out.println("\nQuelle carte veux-tu déplacer et où ?");
					String choiceMove = ShapUp.scanner.next().trim();
					this.whereToWhereMove.delete(0, this.whereToWhereMove.length());
					while (!choiceMove.matches("^(-1|[0-9]):(-1|[0-9])$")) {
						System.out.println("Mauvaise saisie, quel carte veux-tu dÃ©placer ?");
						System.out.println("Exemple : 1,3:(vers)6,7");
						choiceMove = ShapUp.scanner.next().trim();
					}
					this.whereToWhereMove.append(choiceMove);
				} while(!this.actualGrid.testMovingTile(this.whereToWhereMove.toString()));
				
				choice2done=true;
			}
		}while(!(choice1done && turnFinished));
	}

	public void computeBestMove(Card victoryCard, Card cardToPlace)//classic
	{
		System.out.println("Ta carte victoire est "+victoryCard+".");
		
		this.actualGrid.display();
		System.out.print("\nTa carte piochÃ©e est : ");
		System.out.println(cardToPlace.toString());
		System.out.println();
		
		boolean choice1done=false;
		boolean choice2done=false;
		boolean turnFinished=false;
		
		int choice=-1;
		do {
			if(choice1done)
			{
				do {
					this.actualGrid.display();
					System.out.println("Que veux-tu faire :");
					System.out.println("2 - Déplacer une carte déjà  sur le jeu");
					System.out.println("3 - Finir mon tour");
					choice = ShapUp.scanner.nextInt();
				}while(!(choice==2 || choice==3));
				if(choice==3) turnFinished=true;
			}
			
			if(!choice1done)
				do {
					System.out.println("Que veux-tu faire :");
					System.out.println("1 - Jouer la carte que tu as pioché");
					System.out.println("2 - Déplacer une carte déjà  sur le jeu");
					choice = ShapUp.scanner.nextInt();
				}while(!(choice==1 || choice==2));
			
			if(choice==1 && !choice1done)
			{
				do {
					System.out.println("OÃ¹ veux-tu la jouer ?");
					String choicePlace = ShapUp.scanner.next().trim();
					this.WhereToSetCard.delete(0, this.WhereToSetCard.length());
					while (!choicePlace.matches("^(-1|[0-9]),(-1|[0-9])$")) {
						System.out.println("Mauvaise saisie, où veux-tu la jouer ?");
						System.out.println("Exemple : 1,7");
						choicePlace = ShapUp.scanner.next().trim();
					}
					this.WhereToSetCard.append(choicePlace);
				} while (!this.actualGrid.testSettingTile(this.WhereToSetCard.toString()));
				
				choice1done=true;
			}
			
			if(choice==2 && !choice2done)
			{
				System.out.println(this.actualGrid.toString());
				do {
					System.out.println("\nQuelle carte veux-tu dÃ©placer ?");
					String choiceMove = ShapUp.scanner.next().trim();
					this.whereToWhereMove.delete(0, this.whereToWhereMove.length());
					while (!choiceMove.matches("^(-1|[0-9]),(-1|[0-9]):(-1|[0-9]),(-1|[0-9])$")) {
						System.out.println("Mauvaise saisie, quel carte veux-tu déplacer ?");
						System.out.println("Exemple : -1,3:(vers)6,7");
						choiceMove = ShapUp.scanner.next().trim();
					}
					this.whereToWhereMove.append(choiceMove);
				} while(!this.actualGrid.testMovingTile(this.whereToWhereMove.toString()));
				
				choice2done=true;
			}
		}while(!(choice1done && turnFinished));
		
	}
	
}
