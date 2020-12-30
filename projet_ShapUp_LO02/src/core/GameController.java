package core;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/**
 * Controller in the MVC design pattern to handle the interaction with the classes Graphical, Console and GameMaster.
 * 
 */
public class GameController {
    private Graphical graphicalView;
    private Console consoleView;
    private GameMaster game;
    /**
     * Enters the addresses of the objects necessary for operation of the MVC.
     * @param gameMaster
     * @param console
     * @param graphical
     */
    public GameController(GameMaster gameMaster, Console console, Graphical graphical) {
        this.game = gameMaster;
        this.consoleView = console;
        this.graphicalView = graphical;
    }
    /**
     * 
     */
    private class TurnOfPlayerHuman implements Runnable, ActionListener {
        private int xTemp;
        private int yTemp;
        private boolean moveDone;
        private boolean setDone;
        private boolean turnFinished;
        private boolean wantToMove;
        private boolean oneClickAlreadyDone;
        private ArrayList<Card> playerHand;
        private int vCardToUse;

        private TurnOfPlayerHuman(ArrayList<Card> playerHand) {
            this.setDone = false;
            this.moveDone = false;
            this.turnFinished = false;
            this.wantToMove = false;
            this.oneClickAlreadyDone = false;
            this.playerHand = playerHand;
            this.vCardToUse = -1;
            graphicalView.turnFinished.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    turnFinished = true;
                }
            });
            graphicalView.wantToMove.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    wantToMove = !wantToMove;
                }
            });
        }


        public void addListenersOnTiles() {
            if (game.getGridAddress().isAdvancedGame()) {
                for (int i = 0; i < game.getCurrentPlayer().getPlayerHand().size(); i++) {
                    JButton button = (JButton) graphicalView.player.getComponent(i);
                    button.addActionListener(this);
                }
            }
            for (int i = 0; i < graphicalView.table.getComponentCount(); i++) {
                JButton button = (JButton) graphicalView.table.getComponent(i);
                button.addActionListener(this);
            }
        }

        public void actionPerformed(ActionEvent event) {
            if (game.getGridAddress().isAdvancedGame()) {
                for (int i = 0; i < game.getCurrentPlayer().getPlayerHand().size(); i++)
                    if (event.getSource() == graphicalView.player.getComponent(i))
                        vCardToUse = i;
            }

            JButton button = (JButton) event.getSource();
            int x = button.getY() / button.getHeight();
            int y = button.getX() / button.getWidth();
            if (this.wantToMove && !this.moveDone) {
                if (oneClickAlreadyDone) {
                    if (game.getGridAddress().moveTile(xTemp - 1, yTemp - 1, x - 1, y - 1)) {
                        this.moveDone = true;
                        game.notifyObservers(Update.GRID);
                        game.notifyObservers(Update.PLAYER);
                        this.addListenersOnTiles();
                    }
                } else {
                    xTemp = x;
                    yTemp = y;
                    oneClickAlreadyDone = true;
                }
            } else if (!this.setDone) {
                if (game.getGridAddress().isAdvancedGame()) {
                    if (this.vCardToUse != -1 && game.getGridAddress().testSettingTile(x - 1, y - 1)) {
                        game.getGridAddress().setTile(x - 1, y - 1, playerHand.remove(vCardToUse));
                        this.setDone = true;
                        game.notifyObservers(Update.GRID);
                        game.notifyObservers(Update.PLAYER);
                        this.addListenersOnTiles();
                    }
                } else {
                    if (game.getGridAddress().testSettingTile(x - 1, y - 1)) {
                        game.getGridAddress().setTile(x - 1, y - 1, playerHand.remove(1));
                        this.setDone = true;
                        game.notifyObservers(Update.GRID);
                        game.notifyObservers(Update.PLAYER);
                        this.addListenersOnTiles();
                    }
                }
            }
        }

        public void consoleTyping() {
            if (this.turnFinished && this.setDone) return;
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String text = scanner.nextLine();
                if (!setDone) {
                    if (game.getGridAddress().isAdvancedGame()) {
                        if (text.matches("^[0-2]$")) vCardToUse = (int) Integer.valueOf(text);
                        if (this.vCardToUse != -1 && game.getGridAddress().testSettingTile(text)) {
                            game.getGridAddress().setTile(text, playerHand.remove(vCardToUse));
                            this.setDone = true;
                            game.notifyObservers(Update.GRID);
                            game.notifyObservers(Update.PLAYER);
                            this.addListenersOnTiles();
                        }
                    }
                    else {
                        if (game.getGridAddress().testSettingTile(text)) {
                            game.getGridAddress().setTile(text, playerHand.remove(1));
                            this.setDone = true;
                            game.notifyObservers(Update.GRID);
                            game.notifyObservers(Update.PLAYER);
                            this.addListenersOnTiles();
                        }
                    }
                }
                if (!this.moveDone && game.getGridAddress().moveTile(text)) {
                    this.moveDone = true;
                    game.notifyObservers(Update.GRID);
                    this.addListenersOnTiles();
                }
                if (text.matches("^q$")) this.turnFinished = true;
            }
            else {
                scanner.close();
            }
        }

        public void run() {
            this.addListenersOnTiles();
            while (!(this.turnFinished && this.setDone)) {
                if (this.setDone && this.moveDone) this.turnFinished = true;
                this.consoleTyping();
                System.out.print("");
            }
            System.out.println(turnFinished+" "+setDone);
        }
    }

    /**
     * Used to handle the two potential inputs (Console & Graphical) for a human player. 
     * @param playerHand Cards of the player to play his turn.
     */
    public void makeTurnOfPlayer(ArrayList<Card> playerHand) {
        Thread turn = new Thread(new TurnOfPlayerHuman(playerHand));
        turn.start();
        try { turn.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        turn = null;
    }
}