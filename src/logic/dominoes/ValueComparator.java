package logic.dominoes;

import logic.dominoes.Domino;
import logic.players.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;

public class ValueComparator implements Comparator<Map.Entry<Player, LinkedList<Domino>>> {

    @Override
    public int compare(Map.Entry<Player, LinkedList<Domino>> playerDominoes1, Map.Entry<Player, LinkedList<Domino>> playerDominoes2) {
        if (playerDominoes1 == null)
            throw new NullPointerException("playerDominoes1 = null, expected Map.Entry<Player, LinkedList<Domino>>");
        else if (playerDominoes2 == null)
            throw new NullPointerException("playerDominoes2 = null, expected Map.Entry<Player, LinkedList<Domino>>");
        else{
            return Collections.min(playerDominoes1.getValue()).compareTo(Collections.min(playerDominoes2.getValue()));
        }
    }
}
