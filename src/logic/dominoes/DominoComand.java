package logic.dominoes;

import logic.enums.Place;
import logic.players.Player;

public abstract class DominoComand{
    protected Domino domino;
    protected Place place;
    protected Player player;

    public void setDomino(Domino domino) {
        this.domino = domino;
    }

    public void setPlace(Place place){
        this.place = place;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public abstract void execute();
}
