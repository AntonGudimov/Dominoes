package logic.dominoes;


public class DominoInvoker {
    private DominoCommand dominoCommand;

    public void setDominoCommand(DominoCommand dominoCommand) {
        this.dominoCommand = dominoCommand;
    }

    public void executeCommand(){
        dominoCommand.execute();
    }
}
