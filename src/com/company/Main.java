package com.company;

import logic.*;
import logic.dominoes.Domino;
import logic.dominoes.DominoComand;
import logic.dominoes.DominoInvoker;
import logic.dominoes.PutDomino;
import logic.enums.DominoStatus;
import logic.enums.GameState;
import logic.enums.Place;
import logic.players.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int choice = 1;
        do{
            System.out.println("0 - exit\n1 - new game");
            try{
                choice = Integer.parseInt(bufferedReader.readLine());
                switch (choice){
                    case 0:
                        break;
                    case 1:
                        playGame(getPlayers());
                    default:
                        System.out.println("Unknown option");
                }
            }
            catch (IOException | NumberFormatException ex){
                System.err.println(ex.getMessage());
            }
        }
        while (choice != 0);
    }

    public static LinkedList<Player> getPlayers(){
        PlayerCreator humanCreator = new HumanCreator();
        PlayerCreator computerCreator = new ComputerCreator();

        LinkedList<Player> players = new LinkedList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int playersCount = 1;
        do{
            System.out.println("Enter number of players\nCount of players must be from 2 to 4");
            try{
                playersCount = Integer.parseInt(bufferedReader.readLine());
                if (playersCount >= 2 && playersCount <= 4){
                    for (int i = 0; i < playersCount; i++){
                        System.out.printf("Name of the %d player: ", i + 1);
                        String name = bufferedReader.readLine();
                        System.out.printf("Enter type of the %d player\nh - human\nc - computer\n", i + 1);
                        Player player;
                        String type = "";
                        do{
                            type = bufferedReader.readLine().toLowerCase();
                            if (type.equals("h"))
                            {
                                player = humanCreator.CreatePlayer(name);
                                break;
                            }
                            else if (type.equals("c"))
                            {
                                player = computerCreator.CreatePlayer(name);
                                break;
                            }
                            else
                                System.out.println("Unknown option");
                        }
                        while (true);
                        players.add(player);
                    }
                }
                else{
                    System.out.println("Count of players must be from 2 to 4\nTry again");
                }
            }
            catch (IOException | NumberFormatException ex){
                System.err.println(ex.getMessage());
            }
        }
        while ((playersCount < 2) || (playersCount > 4));
        return players;
    }

    public static void playGame(LinkedList<Player> players){

        Game game = Game.getGame();

        DominoInvoker dominoInvoker = new DominoInvoker();
        DominoComand dominoComand = new PutDomino(game);
        dominoInvoker.setDominoCommand(dominoComand);

        int indexOfPlayerWhoGoesFirst = game.newGame(players);

        ListIterator<Player> itr = players.listIterator(indexOfPlayerWhoGoesFirst);

        int count = -1;

        boolean isThereAPossibilityOfFish = false;

        while (game.getGameState() == GameState.PLAYING){
            while (itr.hasNext()){
                Player currentPlayer = itr.next();

                boolean needToGo = game.needToGo(currentPlayer);

                if (needToGo){
                    isThereAPossibilityOfFish = false;
                    int id = 0;
                    Map.Entry<Domino, Place> dominoPlace;
                    if (currentPlayer.getClass() == Human.class)
                        dominoPlace = HumanChoosingDominoAndPlace(game, currentPlayer);
                    else
                        dominoPlace = ComputerChoosingDominoAndPlace(game, currentPlayer);

                    dominoComand.setDomino(dominoPlace.getKey());
                    dominoComand.setPlace(dominoPlace.getValue());
                    dominoComand.setPlayer(currentPlayer);
                    dominoInvoker.executeCommand();

                    if (game.getGameState() == GameState.OVER){
                        System.out.println("Size of store = " + game.getDominoStore().size());
                        System.out.println("Chain: "+ game.getDominoChain().toString());
                        System.out.println("Winner is " + currentPlayer.getName());
                        System.out.println("Congratulations!!!");
                        break;
                    }
                }
                else if (!isThereAPossibilityOfFish){
                    count = players.size();
                    isThereAPossibilityOfFish = true;
                }
                else{
                    count--;
                }

                if (count == 0){
                    System.out.println("Size of store = " + game.getDominoStore().size());
                    System.out.println("Chain: "+ game.getDominoChain().toString());
                    game.setGameState(GameState.FISH);
                    System.out.println("Fish");
                    System.out.println(game.getPlayerDominos().toString());
                    break;
                }
            }
            itr = players.listIterator(0);
        }
    }

    public static Map.Entry<Domino, Place> HumanChoosingDominoAndPlace(Game game, Player currentPlayer){
        if (game == null)
            throw new NullPointerException("game = null, expected Game value");
        else if (currentPlayer == null)
            throw new NullPointerException("currentPlayer = null, expected Player value");
        else{
            int id = 0;
            Place place;
            Domino domino = new Domino();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            Map<Player, LinkedList<Domino>> playerDominoes = game.getPlayerDominos();
            LinkedList<Domino> currentDominoes = playerDominoes.get(currentPlayer);
            while(domino.getStatus() == DominoStatus.NOT_VALID){
                    do {
                        try{
                            System.out.println("Size of store = " + game.getDominoStore().size());
                            System.out.println("Chain: "+ game.getDominoChain().toString());
                            System.out.println(currentPlayer.getName() + ": " + currentDominoes.toString());
                            System.out.println("Enter number of the domino");
                            id = Integer.parseInt(bufferedReader.readLine());
                            if (id >= playerDominoes.get(currentPlayer).size() || id < 0){
                                System.out.println("Index is out of range");
                            }
                        }
                        catch (IOException | NumberFormatException ex){
                            System.err.println(ex.getMessage());
                            id = -1;
                        }
                    }while (id < 0 || id >= playerDominoes.get(currentPlayer).size());

                Domino chosenDomino = currentDominoes.get(id);

                if (chosenDomino.getStatus() == DominoStatus.NOT_VALID)
                    System.out.println("This domino is not valid");
                else{
                    domino.setStatus(DominoStatus.VALID);
                    domino.setHead(chosenDomino.getHead());
                    domino.setTail(chosenDomino.getTail());
                }
            }
            currentDominoes.remove(id);

            place = game.determinePlace(domino);
            while(place == Place.UNDEFINED){
                try{
                    System.out.println("0 - top\n1 - bottom");
                    int choice = Integer.parseInt(bufferedReader.readLine());
                    switch (choice){
                        case 0:
                            place = Place.TOP;
                            break;
                        case 1:
                            place = Place.BOTTOM;
                            break;
                        default:
                            System.out.println("Unknown option");
                            System.out.println("Size of store = " + game.getDominoStore().size());
                            System.out.println("Chain: "+ game.getDominoChain().toString());
                            System.out.println(currentPlayer.getName() + ": " + currentDominoes.toString());
                            System.out.println(domino.toString());
                            break;
                    }
                }
                catch (IOException | NumberFormatException ex){
                    System.err.println(ex.getMessage());
                    System.out.println("Size of store = " + game.getDominoStore().size());
                    System.out.println("Chain: "+ game.getDominoChain().toString());
                    System.out.println(currentPlayer.getName() + ": " + currentDominoes.toString());
                    System.out.println(domino.toString());
                }
            }
            return Map.entry(domino, place);
        }
    }

    public static Map.Entry<Domino, Place> ComputerChoosingDominoAndPlace(Game game, Player currentPlayer){
        if (game == null)
            throw new NullPointerException("game = null, expected Game value");
        else if (currentPlayer == null)
            throw new NullPointerException("currentPlayer = null, expected Player value");
        else{
            Domino domino = new Domino();
            Random rnd = new Random();
            Map<Player, LinkedList<Domino>> playerDominoes = game.getPlayerDominos();
            LinkedList<Domino> currentDominoes = playerDominoes.get(currentPlayer);
            LinkedList<Integer> validIndexes = new LinkedList<>();
            for (int i = 0; i < currentDominoes.size(); i++){
                if(currentDominoes.get(i).getStatus() == DominoStatus.VALID)
                    validIndexes.add(i);
            }
            int id = rnd.nextInt(validIndexes.size());

            System.out.println("Size of store = " + game.getDominoStore().size());
            System.out.println("Chain: "+ game.getDominoChain().toString());
            System.out.println(currentPlayer.getName() + ": " + currentDominoes.toString() + "\n");

            Domino chosenDomino = currentDominoes.get(validIndexes.get(id));

            domino.setStatus(DominoStatus.VALID);
            domino.setHead(chosenDomino.getHead());
            domino.setTail(chosenDomino.getTail());
            currentDominoes.remove(validIndexes.get(id).intValue());

            Place place = game.determinePlace(domino);
            if (place == Place.UNDEFINED){
                boolean choice = rnd.nextBoolean();
                if (choice)
                    place = Place.TOP;
                else
                    place = Place.BOTTOM;
            }
            return Map.entry(domino, place);
        }
    }
}
