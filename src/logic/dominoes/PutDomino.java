package logic.dominoes;

import logic.Game;
import logic.enums.Place;

public class PutDomino extends DominoCommand {
    private Domino domino;
    private Place place;

    public PutDomino(Game game){
        super(game);
    }

    public void setDomino(Domino domino) {
        this.domino = domino;
    }

    public void setPlace(Place place){
        this.place = place;
    }

    @Override
    public void execute() {
        game.addDomino(domino, place, player);
    }
}
