package logic.dominoes;

import java.util.Stack;

public class CommandHistory {
    private Stack<DominoCommand> history;

    public CommandHistory(){
        history = new Stack<>();
    }

    public Stack<DominoCommand> getHistory(){
        return history;
    }

    public void push(DominoCommand command){
        history.push(command);
    }

    public boolean isEmpty(){
        return history.isEmpty();
    }

    public DominoCommand pop(){
        return history.pop();
    }
}
