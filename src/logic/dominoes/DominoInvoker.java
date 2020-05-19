package logic.dominoes;

import logic.dominoes.DominoComand;

public class DominoInvoker {
    private DominoComand dominoComand;

    public void setDominoCommand(DominoComand dominoComand) {
        this.dominoComand = dominoComand;
    }

    public void executeCommand(){
        dominoComand.execute();
    }
}
