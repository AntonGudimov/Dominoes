package logic.dominoes;

import logic.Game;
import logic.players.Player;

public abstract class DominoComand{
    protected Game game;
    protected Player player;

    public DominoComand(Game game){
        this.game = game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public abstract void execute();
}
