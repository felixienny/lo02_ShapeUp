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
		boolean turnFinished=false;
		
		String choice;

		do {
<<<<<<< HEAD
			if(choice1done)
			{
				do {
					this.actualGrid.display();
					System.out.println("Que veux-tu faire :");
					System.out.println("2 - DÈplacer une carte dÈj‡† sur le jeu");
					System.out.println("3 - Finir mon tour");
					choice = ShapUp.scanner.nextInt();
				}while(!(choice==2 || choice==3));
				if(choice==3) turnFinished=true;
			}
			
			if(!choice1done)
				do {
					System.out.println("Que veux-tu faire :");
					System.out.println("1 - Jouer une de tes cartes victoires");
					System.out.println("2 - DÈplacer une carte dÈj‡† sur le jeu");
					choice = ShapUp.scanner.nextInt();
				}while(!(choice==1 || choice==2));
			
			if(choice==1 && !choice1done)
			{
				do {
					System.out.println("Quelle carte veux tu jouer et o√π ?");
					String choicePlace = ShapUp.scanner.next().trim();
					this.WhereToSetCard.delete(0, this.WhereToSetCard.length());
					while (!choicePlace.matches("^[0-2]:(-1|[0-9]),(-1|[0-9])$")) {
						System.out.println("Mauvaise saisie, indique le num√©ro de ta carte (0-2) et l'endroit auquel tu veux la poser.");
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
					System.out.println("\nQuelle carte veux-tu dÈplacer et o˘ ?");
					String choiceMove = ShapUp.scanner.next().trim();
					this.whereToWhereMove.delete(0, this.whereToWhereMove.length());
					while (!choiceMove.matches("^(-1|[0-9]):(-1|[0-9])$")) {
						System.out.println("Mauvaise saisie, quel carte veux-tu d√©placer ?");
						System.out.println("Exemple : 1,3:(vers)6,7");
						choiceMove = ShapUp.scanner.next().trim();
					}
					this.whereToWhereMove.append(choiceMove);
				} while(!this.actualGrid.testMovingTile(this.whereToWhereMove.toString()));
				
				choice2done=true;
=======
			if (!choice1done) {
				System.out.println("\t0,1 - Poser une de tes cartes victoires sur le jeu sur le jeu");
			}
			else {
				this.actualGrid.display();
				System.out.println("\t1,2:2,1 - D√©placer une carte d√©j√† sur le jeu");
				System.out.println("\t3 - Finir ton tour");
			}
			
			choice = ShapUp.scanner.next().trim();

			if (choice.matches("^3$")) turnFinished = true;

			else if (choice.matches("^[0-2]:(-1|[0-9]),(-1|[0-9])$")) {
				if (this.actualGrid.testSettingTile(choice.split(":")[1])) {
					this.actualGrid.setTile(choice.split(":")[1], victoryCards.get(Integer.valueOf(choice.split(":")[0])));
					choice1done = true;
				}
			}

			else if (choice.matches("^(-1|[0-9]),(-1|[0-9]):(-1|[0-9]),(-1|[0-9])$")) {
				if (this.actualGrid.moveTile(choice)) {
					this.actualGrid.moveTile(choice);
					turnFinished = true;
				}
>>>>>>> felixienny
			}

		} while(!(choice1done && turnFinished));
	}

	public void computeBestMove(Card victoryCard, Card cardToPlace) {//classic
		System.out.println("Ta carte victoire est "+victoryCard+".");
		
		this.actualGrid.display();
		System.out.print("\nTa carte pioch√©e est : ");
		System.out.println(cardToPlace.toString());
		System.out.println();
		
		boolean choice1done=false;
		boolean turnFinished=false;
		
		String choice;

		do {
<<<<<<< HEAD
			if(choice1done)
			{
				do {
					this.actualGrid.display();
					System.out.println("Que veux-tu faire :");
					System.out.println("2 - DÈplacer une carte dÈj‡† sur le jeu");
					System.out.println("3 - Finir mon tour");
					choice = ShapUp.scanner.nextInt();
				}while(!(choice==2 || choice==3));
				if(choice==3) turnFinished=true;
			}
			
			if(!choice1done)
				do {
					System.out.println("Que veux-tu faire :");
					System.out.println("1 - Jouer la carte que tu as piochÈ");
					System.out.println("2 - DÈplacer une carte dÈj‡† sur le jeu");
					choice = ShapUp.scanner.nextInt();
				}while(!(choice==1 || choice==2));
			
			if(choice==1 && !choice1done)
			{
				do {
					System.out.println("O√π veux-tu la jouer ?");
					String choicePlace = ShapUp.scanner.next().trim();
					this.WhereToSetCard.delete(0, this.WhereToSetCard.length());
					while (!choicePlace.matches("^(-1|[0-9]),(-1|[0-9])$")) {
						System.out.println("Mauvaise saisie, o˘ veux-tu la jouer ?");
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
					System.out.println("\nQuelle carte veux-tu d√©placer ?");
					String choiceMove = ShapUp.scanner.next().trim();
					this.whereToWhereMove.delete(0, this.whereToWhereMove.length());
					while (!choiceMove.matches("^(-1|[0-9]),(-1|[0-9]):(-1|[0-9]),(-1|[0-9])$")) {
						System.out.println("Mauvaise saisie, quel carte veux-tu dÈplacer ?");
						System.out.println("Exemple : -1,3:(vers)6,7");
						choiceMove = ShapUp.scanner.next().trim();
					}
					this.whereToWhereMove.append(choiceMove);
				} while(!this.actualGrid.testMovingTile(this.whereToWhereMove.toString()));
				
				choice2done=true;
			}
		}while(!(choice1done && turnFinished));
		
=======
			if (!choice1done) {
				System.out.println("\t0,1 - Poser ta carte sur le jeu");
			}
			else {
				this.actualGrid.display();
				System.out.println("\t1,2:2,1 - D√©placer une carte d√©j√† sur le jeu");
				System.out.println("\tq - Finir ton tour");
			}
			
			choice = ShapUp.scanner.next().trim();

			if (choice.matches("^q$") && choice1done) turnFinished = true;

			else if (choice.matches("^(-1|[0-9]),(-1|[0-9])$")) {
				if (this.actualGrid.testSettingTile(choice)) {
					this.actualGrid.setTile(choice, cardToPlace);
					choice1done = true;
				}
			}

			else if (choice.matches("^(-1|[0-9]),(-1|[0-9]):(-1|[0-9]),(-1|[0-9])$")) {
				if (this.actualGrid.moveTile(choice)) {
					this.actualGrid.moveTile(choice);
					turnFinished = true;
				}
			}
		} while(!(choice1done && turnFinished));
>>>>>>> felixienny
	}
	
}
