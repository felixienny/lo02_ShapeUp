package core;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.Graphics;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.event.*;


public class Graphical implements Observer {
    private final String dir = new File("").getAbsolutePath();
    private final String[] cards = {
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FBC.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FBS.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FBT.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FGC.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FGS.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FGT.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FRC.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FRS.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\FRT.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HBC.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HBS.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HBT.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HGC.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HGS.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HGT.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HRC.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HRS.JPG",
        dir + "\\projet_ShapUp_LO02\\img\\cards\\HRT.JPG",
    };
    private final HashMap<String,Image> cardsImg = new HashMap<>();
    
    private GameMaster gameMaster;



    private JFrame window;
    private JPanel table;
    private JPanel player;
    private JPanel situation;
    private JPanel turn;
    private JPanel match;
    
    private JPanel top;


    public Graphical(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
        this.gameMaster.addObserver(this);

        this.window = new JFrame("ShapeUp");
        this.window.setSize(1500, 900);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.top = new JPanel();

        this.player = new JPanel();
        this.situation = new JPanel();
        this.turn = new JPanel();
        this.match = new JPanel();

        this.table = new JPanel();

        this.top.setPreferredSize(new Dimension(this.window.getSize().width, this.window.getSize().height*2/10));
                
        this.table.setSize(new Dimension(this.window.getSize().width*7/10, this.window.getSize().height*8/10));

        this.top.setLayout(new BoxLayout(this.top, BoxLayout.X_AXIS));

        this.player.setLayout(new FlowLayout());
        this.situation.setLayout(new FlowLayout());
        this.turn.setLayout(new BoxLayout(this.turn, BoxLayout.Y_AXIS));
        this.match.setLayout(new BoxLayout(this.match, BoxLayout.Y_AXIS));


        this.player.setSize(new Dimension(this.top.getSize().width*4/10, this.top.getSize().height));
        this.situation.setSize(new Dimension(this.top.getSize().width*1/10, this.top.getSize().height));
        this.turn.setSize(new Dimension(this.top.getSize().width*1/10, this.top.getSize().height));
        this.match.setSize(new Dimension(this.top.getSize().width*4/10, this.top.getSize().height));
        

        this.top.add(this.player);
        this.top.add(this.situation);
        this.top.add(this.turn);
        this.top.add(this.match);

        this.window.add(this.top, BorderLayout.NORTH);
        this.window.add(this.table, BorderLayout.CENTER);

        for (int c=0; c<cards.length; c++) cardsImg.put(cards[c].split("\\\\")[cards[c].split("\\\\").length-1].split("\\.")[0], new ImageIcon(cards[c]).getImage());

        this.window.setVisible(true);
    }

    public void update(GameMaster gameMaster, Update update) {
        switch (update) {
            case ALL:
                this.table.removeAll();
                this.player.removeAll();
                this.situation.removeAll();
                this.turn.removeAll();
                this.match.removeAll();
                this.displayGrid();
                this.displaySituation();
                this.displayTurn();
                this.displayMatch();
                this.table.validate();
                this.player.validate();
                this.situation.validate();
                this.turn.validate();
                this.match.validate();
                break;
            case GRID:
                this.table.removeAll();
                this.displayGrid();
                this.table.validate();
                break;
            case PLAYER:
                this.player.removeAll();
                this.displayPlayer();
                this.player.validate();
                this.situation.removeAll();
                this.displaySituation();
                this.situation.validate();
                break;
            case TURN:
                this.turn.removeAll();
                this.displayTurn();
                this.turn.validate();
                break;
            case MATCH:
                this.match.removeAll();
                this.displayMatch();
                this.match.validate();
                break;
        }
    }

    public void displayPlayer() {
        Iterator<Card> iterator = this.gameMaster.getCurrentPlayer().getPlayerHand().iterator();
        while (iterator.hasNext()) {
            JButton card = new JButton();
            card.setHorizontalAlignment(JLabel.CENTER);
            card.setIcon(new ImageIcon(cardsImg.get(iterator.next().toString()).getScaledInstance(-1, 140, java.awt.Image.SCALE_SMOOTH)));
            card.setSize(card.getIcon().getIconWidth(), card.getIcon().getIconHeight());
            card.setHorizontalTextPosition(JLabel.CENTER);
            card.setVerticalTextPosition(JLabel.BOTTOM);
            this.player.add(card);
        }
    }

    public void displaySituation() {
        this.situation.add(createJLabelHeader(this.gameMaster.getCurrentPlayer().getName()));
        this.situation.add(createJLabel("\nMap = "+(this.gameMaster.getGridAddress().getNumberOfPlacedCards()*100 / ((this.gameMaster.getGridAddress().getRealHeight()*(this.gameMaster.getGridAddress().getRealWidth())-this.gameMaster.getGridAddress().getNumberOfDeadTiles()))+"%")));
    }

    public void displayTurn() {
        this.turn.add(createJLabelHeader("TURN "+this.gameMaster.getCurrentTurn()));

        JPanel playerTurns = new JPanel();
        playerTurns.setLayout(new GridLayout(0,1));

        for(Player player : this.gameMaster.getPlayers()) 
            playerTurns.add(createJLabel(player.getName() + " : " + player.getCurrentScore()));

        this.turn.add(playerTurns);
    }

    public void displayMatch() {
        this.match.add(createJLabelHeader("MATCH "+this.gameMaster.getCurrentMatch()));
        
        JPanel playerScores = new JPanel();
        playerScores.setLayout(new GridLayout(this.gameMaster.getPlayers().size(),this.gameMaster.getCurrentMatch()+1));

        if (this.gameMaster.getCurrentMatch()>=1) {
            for(Player player : this.gameMaster.getPlayers()) {
                playerScores.add(createJLabel(player.getName()));
                player.getScores().forEach(score -> playerScores.add(createJLabel(String.valueOf(score))));
                playerScores.add(createJLabel(String.valueOf(player.getFinalScore())));
            }
        }
        
        this.match.add(playerScores);
    }

    public void displayGrid() {
        this.table.setLayout(new GridLayout(0, this.gameMaster.getGridAddress().getWidth() + 2));

        int height = this.table.getHeight()/(this.gameMaster.getGridAddress().getHeight()+2);
        int width = this.table.getWidth()/(this.gameMaster.getGridAddress().getWidth()+2);

        for (int y = 0; y < this.gameMaster.getGridAddress().getWidth()+2; y++)
            this.table.add(new JButton("---"));

        for (int x = 0; x < this.gameMaster.getGridAddress().getHeight(); x++) {
            this.table.add(new JButton("---"));

            for (int y = 0; y < this.gameMaster.getGridAddress().getWidth(); y++)
                this.table.add(createButtonWithScaledImage(height, width, this.gameMaster.getGridAddress().tileToString(x, y)));

            this.table.add(new JButton("---"));
        }

        for (int y=0; y<this.gameMaster.getGridAddress().getWidth()+2; y++) 
            this.table.add(new JButton("---"));
    }

    public JLabel createJLabel(String text) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><div style='text-align:center;display:flex;'>");
        sb.append(text.replaceAll("\\n", "<br>"));
        sb.append("</div></html>");
        
        JLabel jLabel = new JLabel(sb.toString());
        
        jLabel.setFont(new Font("Calibri", Font.PLAIN, 20));

        return jLabel;
    }

    public JLabel createJLabelHeader(String text) {
        JLabel jLabel = new JLabel(text);
        
        jLabel.setFont(new Font("Calibri", Font.PLAIN, 25));
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        jLabel.setOpaque(true);
        jLabel.setBackground(java.awt.Color.RED);

        return jLabel;
    }

    private JButton createButtonWithScaledImage(int height, int width, String tile) {
        JButton jbutton = new JButton();
        jbutton.setBackground(java.awt.Color.WHITE);

        Insets insets = jbutton.getInsets();
        height -= insets.top + insets.bottom;
        width -= insets.left + insets.right;

        if (width > height) width = -1;
        else height = -1;

        if (tile.matches("^[A-Z]{3}$")) jbutton.setIcon(new ImageIcon(cardsImg.get(tile).getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH)));
        else jbutton.setText(tile); 

        return jbutton;
    }



    public static void main(String[] args) {
    }

}
