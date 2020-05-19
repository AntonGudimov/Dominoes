package logic.players;


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
