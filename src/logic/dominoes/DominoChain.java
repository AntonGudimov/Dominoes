package logic.dominoes;

import logic.enums.Place;
import logic.enums.Side;

import java.util.LinkedList;

public final class DominoChain {
    private static DominoChain dominoChain;
    private LinkedList<Domino> chain;
    private int top;
    private int bottom;

    private DominoChain(){
        chain = new LinkedList<>();
    }

    public static DominoChain getDominoChain(){
        if (dominoChain == null){
            dominoChain = new DominoChain();
        }
        return dominoChain;
    }

    public void putToTheTop(Domino domino){
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            chain.addFirst(domino);
            top = domino.getHead();
        }
    }

    public void putToTheBottom(Domino domino){
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            chain.addLast(domino);
            bottom = domino.getTail();
        }
    }

    public void putFirstDomino(Domino domino){
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            chain.addFirst(domino);
            top = domino.getHead();
            bottom = domino.getTail();
        }
    }


    public Side whichSideMatched(Domino domino, Place place){
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else if (place == null)
            throw new NullPointerException("place = null, expected Place value");
        else{
            Side side;
            switch (place){
                case TOP:
                    if (domino.getHead() == top)
                        side = Side.HEAD;
                    else
                        side = Side.TAIL;
                    break;
                case BOTTOM:
                    if (domino.getHead() == bottom)
                        side = Side.HEAD;
                    else
                        side = Side.TAIL;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + place);
            }
            return side;
        }
    }

    public boolean isDominoMatched(Domino domino){
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            if (!isEmpty()){
                boolean a = isDominoMatchedTop(domino);
                boolean b = isDominoMatchedBottom(domino);
                return a || b;
            }
            return false;
        }
    }

    public boolean isDominoMatchedTop(Domino domino){
        return isDominoMatchedPlace(domino, top);
    }

    public boolean isDominoMatchedBottom(Domino domino){
        return isDominoMatchedPlace(domino, bottom);
    }

    private boolean isDominoMatchedPlace(Domino domino, int place) {
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            if (!isEmpty()){
                boolean a = domino.getHead() == place;
                boolean b = domino.getTail() == place;
                return a || b;
            }
            return false;
        }
    }

    public boolean isEmpty(){
        return chain.isEmpty();
    }

    public boolean isTopEqualsBottom(){
        return top == bottom;
    }

    @Override
    public String toString() {
        return chain.toString();
    }
}
