package logic.dominoes;

import java.util.Stack;

public class CommandHistory {
    private Stack<DominoComand> history;

    public CommandHistory(){
        history = new Stack<>();
    }

    public Stack<DominoComand> getHistory(){
        return history;
    }

    public void push(DominoComand command){
        history.push(command);
    }

    public boolean isEmpty(){
        return history.isEmpty();
    }

    public DominoComand pop(){
        return history.pop();
    }
}
