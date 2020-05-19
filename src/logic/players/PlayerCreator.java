package logic.players;

import logic.players.Player;

public abstract class PlayerCreator {

    public static int limit;
    public static int count;

    public PlayerCreator(){
        count = 0;
        limit = 4;
    }
    public abstract Player CreatePlayer(String name);
}
