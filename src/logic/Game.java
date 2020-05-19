package logic;

import logic.dominoes.Domino;
import logic.dominoes.DominoChain;
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
    private DominoChain dominoChain;
    private LinkedList<Domino> dominoStore;
    private GameState gameState;

    private Game(){
        players = new LinkedList<>();
        playerDominos = new HashMap<>();
        dominoChain = DominoChain.getDominoChain();
        dominoStore = new LinkedList<>();
        gameState = GameState.NOT_PLAYING;
    }

    public static Game getGame(){
        if (game == null){
            game = new Game();
        }
        return game;
    }

    public DominoChain getDominoChain(){
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
                dominoChain.putFirstDomino(domino);
            }
            else{
                if (place == Place.NO_MATTER){
                    Random rnd = new Random();
                    if (rnd.nextBoolean())
                        place = Place.TOP;
                    else
                        place = Place.BOTTOM;
                }
                Side side = dominoChain.whichSideMatched(domino, place);
                switch (side){
                    case HEAD:
                        if (place == Place.TOP){
                            domino.reverse();
                            dominoChain.putToTheTop(domino);
                        }
                        else if (place == Place.BOTTOM)
                            dominoChain.putToTheBottom(domino);
                        break;
                    case TAIL:
                        if (place == Place.BOTTOM){
                            domino.reverse();
                            dominoChain.putToTheBottom(domino);
                        }
                        else if (place == Place.TOP)
                            dominoChain.putToTheTop(domino);
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
            if (dominoChain.isTopEqualsBottom()) {
                place = Place.NO_MATTER;
            } else {
                if ((dominoChain.isDominoMatchedTop(domino))
                        && (!dominoChain.isDominoMatchedBottom(domino))) {
                    place = Place.TOP;
                } else if ((dominoChain.isDominoMatchedBottom(domino))
                        && (!dominoChain.isDominoMatchedTop(domino))) {
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
                if (dominoChain.isDominoMatched(domino))
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

    private boolean hitPlayer(Player player){
        if (player == null)
            throw new NullPointerException("player = null, expected Player value");
        else{
            boolean isOk = false;
            Domino dominoFromStore = new Domino();
            while (dominoFromStore.getStatus() == DominoStatus.NOT_VALID){
                if (!dominoStore.isEmpty()){
                    dominoFromStore = dominoStore.pop();
                    playerDominos.get(player).add(dominoFromStore);
                    if (dominoChain.isDominoMatched(dominoFromStore)){
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
}
