package logic.players;

import logic.players.Computer;
import logic.players.Player;
import logic.players.PlayerCreator;

public class ComputerCreator extends PlayerCreator {

    public ComputerCreator(){
        super();
    }

    @Override
    public Player CreatePlayer(String name) {
        if (count < limit){
            count++;
            return new Computer(name);
        }
        return null;
    }
}
