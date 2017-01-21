package main;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Created by mateusz on 1/17/17.
 */
//superclass for Man and King
//we can use polymorphism thanks to it
abstract class Figure {
    //if after a move there is a jump possible this is set to true
    boolean mustJump = false;
    //depending on who owns the figure it is set to true for red od false for blue
    boolean color;
    //reference to the Tile on which the figure is located
    Tile position;
    //list of all the valid moves that the figure can make
    ArrayList<Tile> validMoves;
    //reference to the selected figure
    static Figure isSelected;
    //reference to player owning the figure
    Player player;

    //removes captured tile from the array list of player's figures
    //and sets its position to unoccupied
    void capture(){
        this.position.setOccupied(null);
        player.figures.remove(this);
    }

    //sets isSelected as reference to the object that called this function
    void selectFigure(GraphicsContext gc){
        isSelected = this;
    }

    //checks if figure can jump after moving
    //each if checks if there is a figure of opposite color on the nearby tile
    //and if the next tile is empty
    //returns array list with references to tiles that can be jumped on
    abstract ArrayList<Tile> checkValidJumps();

    //checks if figure can make any moves
    //returns array list with references to tiles that can be moved on
    abstract ArrayList<Tile> checkValidMoves();

    //logic responsible for drawing each figure
    abstract void drawFigure(GraphicsContext gc);

    //logic responsible for moving the figure
    abstract boolean move(Tile target);


}
