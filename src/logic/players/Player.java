package logic.players;

public abstract class Player {
    protected String name;

    public Player(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    @Override
    public String toString() {
        return name;
    }
}
