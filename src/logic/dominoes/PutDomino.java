package logic.dominoes;

import logic.Game;

public class PutDomino extends DominoComand {
    private Game game;

    public PutDomino(Game game){
        this.game = game;
    }

    @Override
    public void execute() {
        game.addDomino(domino, place, player);
    }
}
