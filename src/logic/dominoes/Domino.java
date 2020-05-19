package logic.dominoes;

import logic.enums.DominoStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Domino implements Comparable<Domino>{
    private int head;
    private int tail;
    private DominoStatus status;

    public Domino(int value1, int value2){
        this.head = value1;
        this.tail = value2;
        status = DominoStatus.NOT_VALID;
    }

    public Domino()
    {
        this.head = -1;
        this.tail = -1;
        status = DominoStatus.NOT_VALID;
    }

    public int getHead() {
        return head;
    }

    public int getTail() {
        return tail;
    }

    public DominoStatus getStatus(){
        return status;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public void setTail(int tail) {
        this.tail = tail;
    }

    public void setStatus(DominoStatus status) {
        this.status = status;
    }

    public void reverse(){
        int temp = tail;
        tail = head;
        head = temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domino domino = (Domino) o;
        boolean a = head == domino.head && tail == domino.tail;
        boolean b = head == domino.tail && tail == domino.head;
        return a || b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail) + Objects.hash(tail, head);
    }

    @Override
    public String toString() {
        if (status == DominoStatus.NOT_VALID)
            return "(" + head + " : " + tail + ")";
        else
            return "[" + head + ": " + tail + "]";
    }

    @Override
    public int compareTo(@NotNull Domino domino) {
        if (head == tail){
            if (domino.head == domino.tail){
                if (domino.head + domino.tail == 0){
                    return -1;
                }
                else if (head + tail == 0){
                    return 1;
                }
                else{
                    return (head + tail) - (domino.head + domino.tail);
                }
            }
            else{
                return -1;
            }
        }
        else{
            if ((domino.head == domino.tail) && (domino.head + domino.tail != 0)){
                return 1;
            }
            else if ((domino.head == domino.tail) && (domino.head + domino.tail == 0)){
                return -1;
            }
            else {
                return (head + tail) - (domino.head + domino.tail);
            }
        }
    }
}
