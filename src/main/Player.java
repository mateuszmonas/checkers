package main;

import java.util.*;

/**
 * Created by mateusz on 1/17/17.
 */
public class Player {

    //true == red turn
    //false == blue turn
    static boolean turn = true;

    //list of players in game
    //player 0 is red
    //player 1 is blue
    static ArrayList<Player> players = new ArrayList<>();

    //list of each player's figures
    ArrayList<Figure> figures;

    //true == red men
    //false == blue men
    private boolean color;

    //adds figures to the figures array list when the object is initialized
    Player(boolean color){
        players.add(this);
        this.color = color;
        figures=new ArrayList<>();
        int i = 0;
        int horizontal = 0;
        int vertical = (color) ? 0 : Settings.boardSize-1;


        while (i<Settings.numberOfFigures){
            if(horizontal==Settings.boardSize){
                horizontal = 0;
                vertical = (color) ? vertical+1 : vertical-1;
            }
            if(!Tile.tiles[horizontal][vertical].isColor()){
                figures.add(new Man(this, Tile.tiles[horizontal][vertical], color));
                i++;
            }
            horizontal++;
        }
    }

    boolean areJumpsPossible(){
        for (Figure f : figures) {
            if(!f.checkValidJumps().isEmpty()) return true;
        }
        return false;
    }
}
