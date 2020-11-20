package projet_ShapUp_LO02;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

public class Tournoi {
    private ArrayList<HashMap<Player,Integer>> scores = new ArrayList<HashMap<Player,Integer>>();

    public Tournoi(List<Player> players, int nMatches) {
        for(int m=0; m<nMatches; m++){
            HashMap<Player, Integer> playersScores = new HashMap<Player, Integer>();
            for(Player player : players){
                playersScores.put(player, 0);
            }
            scores.add(m, (HashMap<Player, Integer>) playersScores.clone());
            playersScores.clear();
        }
    }

    public void setScoreOnPlayer(int match, Player player, int earnedPoints){ 
        this.scores.get(match).put( player, (this.scores.get(match).get(player)+earnedPoints) ) ; 
    }

    public HashMap<Player, Integer> getScoresOnMatch(int match){ 
        return this.scores.get(match); 
    }

    public int getScoreOnPlayer(Player player){ 
        int scoreOnPlayer = 0;
        Iterator<HashMap<Player,Integer>> m = this.scores.iterator();
        while(m.hasNext()){
            HashMap<Player, Integer> score = (HashMap<Player, Integer>) m.next();
            scoreOnPlayer += score.get(player);
        }
        return scoreOnPlayer;
    }




    public static void main(String[] args) {
        List<Player> players = new ArrayList<Player>();
        Player a = new PlayerH("a");
        players.add(a); 
        Tournoi t = new Tournoi(players, 2);
        
        System.out.println(t.getScoresOnMatch(0));

        System.out.println(t.getScoreOnPlayer(a));
        t.setScoreOnPlayer(0, a, 536);
        System.out.println(t.getScoreOnPlayer(a));

    }
}
