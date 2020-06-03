package logic;

import logic.dominoes.Domino;
import logic.dominoes.ValueComparator;
import logic.enums.DominoStatus;
import logic.enums.GameState;
import logic.enums.Place;
import logic.enums.Side;
import logic.players.Player;

import java.util.*;

public final class Game {
    private static Game game;
    private LinkedList<Player> players;
    private Map<Player, LinkedList<Domino>> playerDominos;
    private LinkedList<Domino> dominoChain;
    private LinkedList<Domino> dominoStore;
    private GameState gameState;

    private Game(){
        players = new LinkedList<>();
        playerDominos = new HashMap<>();
        dominoChain = new LinkedList<>();
        dominoStore = new LinkedList<>();
        gameState = GameState.NOT_PLAYING;
    }

    public static Game getGame(){
        if (game == null){
            game = new Game();
        }
        return game;
    }

    public LinkedList<Domino> getDominoChain(){
        return dominoChain;
    }

    public Map<Player, LinkedList<Domino>> getPlayerDominos(){
        return playerDominos;
    }

    public LinkedList<Domino> getDominoStore(){
        return dominoStore;
    }

    public GameState getGameState(){
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int newGame(LinkedList<Player> players){
        if (players == null)
            throw new NullPointerException("players = null, expected LinkedList<Player> value");
        else{
            createDominoes();
            this.players = players;
            Collections.shuffle(this.players);
            giveDominoesToPlayers();
            gameState = GameState.PLAYING;
            Map.Entry<Player, Domino> playerDominoEntry = whoGoesFirst();
            playerDominoEntry.getValue().setStatus(DominoStatus.VALID);
            return players.indexOf(playerDominoEntry.getKey());
        }
    }

    private void createDominoes(){
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                Domino domino = new Domino(i, j);
                if (!dominoStore.contains(domino))
                    dominoStore.add(domino);
            }
        }
        Collections.shuffle(dominoStore);
    }

    private void giveDominoesToPlayers(){
        int playerCount = players.size();
        int dominoCountPerPerson = 0;
        if (playerCount == 2){
            dominoCountPerPerson = 7;
        }
        else if ((playerCount > 2) && (playerCount < 5)){
            dominoCountPerPerson = 5;
        }
        for (Player player : players){
            LinkedList<Domino> dominoes = new LinkedList<>();
            for(int i = 0; i < dominoCountPerPerson; i++){
                dominoes.push(dominoStore.pop());
            }
            playerDominos.put(player, dominoes);
        }
    }

    public void addDomino(Domino domino, Place place, Player player){
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else if (place == null)
            throw new NullPointerException("place = null, expected Place value");
        else if (player == null)
            throw new NullPointerException("player = null, expected Player value");
        else{
            if (isChainEmpty()){
                putToTheTop(domino);
            }
            else{
                if (place == Place.NO_MATTER){
                    Random rnd = new Random();
                    if (rnd.nextBoolean())
                        place = Place.TOP;
                    else
                        place = Place.BOTTOM;
                }
                Side side = whichSideMatched(domino, place);
                switch (side){
                    case HEAD:
                        if (place == Place.TOP){
                            domino.reverse();
                            putToTheTop(domino);
                        }
                        else if (place == Place.BOTTOM)
                            putToTheBottom(domino);
                        break;
                    case TAIL:
                        if (place == Place.BOTTOM){
                            domino.reverse();
                            putToTheBottom(domino);
                        }
                        else if (place == Place.TOP)
                            putToTheTop(domino);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + place);
                }
            }
            domino.setStatus(DominoStatus.NOT_VALID);
            if (playerDominos.get(player).size() == 0)
                gameState = GameState.OVER;
        }
    }

    private Map.Entry<Player, Domino> whoGoesFirst(){
        Map.Entry<Player, LinkedList<Domino>> min = Collections.min(playerDominos.entrySet(), new ValueComparator());
        return Map.entry(min.getKey(), Collections.min(min.getValue()));
    }

    public boolean isChainEmpty(){
        return dominoChain.isEmpty();
    }


    public Place determinePlace(Domino domino) {
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            Place place = Place.UNDEFINED;
            if (dominoChain.isEmpty() || isTopEqualsBottom()) {
                place = Place.NO_MATTER;
            } else {
                if ((isDominoMatchedTop(domino))
                        && (!isDominoMatchedBottom(domino))) {
                    place = Place.TOP;
                } else if ((isDominoMatchedBottom(domino))
                        && (!isDominoMatchedTop(domino))) {
                    place = Place.BOTTOM;
                }
            }
            return place;
        }
    }

    public boolean needToGo(Player player){
        if (player == null)
            throw new NullPointerException("player = null, expected Player value");
        else{
            boolean isOk = true;
            if (!isChainEmpty()){
                boolean isEveryDominoInvalid = setDominoStatus(player);
                if (isEveryDominoInvalid){
                    isOk = hitPlayer(player);
                }
            }
            return isOk;
        }
    }

    private boolean setDominoStatus(Player player){
        if (player == null)
            throw new NullPointerException("player = null, expected Player value");
        else{
            LinkedList<Domino> currentDominos = playerDominos.get(player);
            boolean isEveryDominoInvalid = true;
            for (Domino domino : currentDominos){
                if (isDominoMatched(domino))
                {
                    domino.setStatus(DominoStatus.VALID);
                    isEveryDominoInvalid = false;
                }
                else
                    domino.setStatus(DominoStatus.NOT_VALID);
            }
            return isEveryDominoInvalid;
        }
    }

    public boolean hitPlayer(Player player){
        if (player == null)
            throw new NullPointerException("player = null, expected Player value");
        else{
            boolean isOk = false;
            Domino dominoFromStore = new Domino();
            while (dominoFromStore.getStatus() == DominoStatus.NOT_VALID){
                if (!dominoStore.isEmpty()){
                    dominoFromStore = dominoStore.pop();
                    playerDominos.get(player).add(dominoFromStore);
                    if (isDominoMatched(dominoFromStore)){
                        dominoFromStore.setStatus(DominoStatus.VALID);
                        isOk = true;
                    }
                }
                else
                    break;
            }
            return isOk;
        }
    }

    public void putToTheTop(Domino domino){
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            dominoChain.addFirst(domino);
        }
    }

    public void putToTheBottom(Domino domino){
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            dominoChain.addLast(domino);
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
                    if (domino.getHead() == dominoChain.getFirst().getHead())
                        side = Side.HEAD;
                    else
                        side = Side.TAIL;
                    break;
                case BOTTOM:
                    if (domino.getHead() == dominoChain.getLast().getTail())
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
            if (!dominoChain.isEmpty()){
                boolean a = isDominoMatchedTop(domino);
                boolean b = isDominoMatchedBottom(domino);
                return a || b;
            }
            return false;
        }
    }

    public boolean isDominoMatchedTop(Domino domino){
        return isDominoMatchedPlace(domino, dominoChain.getFirst().getHead());
    }

    public boolean isDominoMatchedBottom(Domino domino){
        return isDominoMatchedPlace(domino, dominoChain.getLast().getTail());
    }

    private boolean isDominoMatchedPlace(Domino domino, int place) {
        if (domino == null)
            throw new NullPointerException("domino = null, expected Domino value");
        else{
            if (!dominoChain.isEmpty()){
                boolean a = domino.getHead() == place;
                boolean b = domino.getTail() == place;
                return a || b;
            }
            return false;
        }
    }


    public boolean isTopEqualsBottom(){
        return dominoChain.getFirst().getHead() == dominoChain.getLast().getTail();
    }

    @Override
    public String toString() {
        return dominoChain.toString();
    }
}
