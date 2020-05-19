package logic.players;


public class HumanCreator extends PlayerCreator {

    public HumanCreator(){
        super();
    }

    @Override
    public Player CreatePlayer(String name) {
        if (count < limit){
            count++;
            return new Human(name);
        }
        return null;
    }
}
