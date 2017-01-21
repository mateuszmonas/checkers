package main;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 * Created by mateusz on 1/17/17.
 */
public class Man extends Figure {

    Man(Player player, Tile position, boolean color){
        this.player = player;
        validMoves = new ArrayList<>();
        this.position = position;
        this.color = color;
        position.setOccupied(this);

    }

    //draws a figure in its position
    //player1 figures are red
    //player2 figures are blue
    void drawFigure(GraphicsContext gc){
        if(color) gc.setFill(Color.RED);
        else gc.setFill(Color.BLUE);
        gc.fillOval(position.getX(), position.getY(),Tile.tileDimension,Tile.tileDimension);
        if(Player.turn==color && !checkValidMoves().isEmpty()){
            if(!color) gc.setStroke(Color.RED);
            else gc.setStroke(Color.BLUE);
            gc.strokeOval(position.getX(), position.getY(), position.getTileDimension(), position.getTileDimension());
        }
    }

    ArrayList<Tile> checkValidJumps(){
        validMoves.clear();
        int x = position.getX()/Tile.tileDimension;
        int y = position.getY()/Tile.tileDimension;
        if(this.color || (Settings.canCaptureBackwards && mustJump)) {
            if (x + 2 < Settings.boardSize && y + 2 < Settings.boardSize && Tile.tiles[x + 1][y + 1].isOccupied() != null && Tile.tiles[x + 2][y + 2].isOccupied() == null &&
                    Tile.tiles[x + 1][y + 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x + 2][y + 2]);
            }
            if (x - 2 >= 0 && y + 2 < Settings.boardSize && Tile.tiles[x - 1][y + 1].isOccupied() != null && Tile.tiles[x - 2][y + 2].isOccupied() == null &&
                    Tile.tiles[x - 1][y + 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x - 2][y + 2]);
            }
        }
        if(!this.color || (Settings.canCaptureBackwards && mustJump)) {
            if (x + 2 < Settings.boardSize && y - 2 >= 0 && Tile.tiles[x + 1][y - 1].isOccupied() != null && Tile.tiles[x + 2][y - 2].isOccupied() == null &&
                    Tile.tiles[x + 1][y - 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x + 2][y - 2]);
            }
            if (x - 2 >= 0 && y - 2 >= 0 && Tile.tiles[x - 1][y - 1].isOccupied() != null && Tile.tiles[x - 2][y - 2].isOccupied() == null &&
                    Tile.tiles[x - 1][y - 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x - 2][y - 2]);
            }
        }
        return validMoves;
    }

    //returns an array list of all possible moves
    ArrayList<Tile> checkValidMoves(){
        validMoves.clear();
        //if Settings.forcedCapture is true it checks if any of players pieces can capture something
        if(Settings.forcedCapture || mustJump) {
            //if any of the players pieces can capture something, but this piece is not one of them it returns empty array list
            if (player.areJumpsPossible() && checkValidJumps().isEmpty()) {
                return validMoves;
            }
            if (!checkValidJumps().isEmpty()) {
                return checkValidJumps();
            }
        }
        int x = position.getX()/Tile.tileDimension;
        int y = position.getY()/Tile.tileDimension;
        //if the figure is red it checks only if it can move down
        if(color) {
            //checks if tile to the down right is occupied
            //if it is not it is added to the list
            if (x + 1 < Settings.boardSize && y + 1 < Settings.boardSize && Tile.tiles[x + 1][y + 1].isOccupied() == null) {
                validMoves.add(Tile.tiles[x + 1][y + 1]);
            }
            //if it is occupied it check if the one behind it is not
            //and then adds it to the list
            else if (x + 2 < Settings.boardSize && y + 2 < Settings.boardSize && Tile.tiles[x + 1][y + 1].isOccupied() != null && Tile.tiles[x + 2][y + 2].isOccupied() == null &&
                    Tile.tiles[x + 1][y + 1].isOccupied().color!=color) {
                validMoves.add(Tile.tiles[x + 2][y + 2]);
            }
            //checks if tile to the down left is occupied
            if (x - 1 >= 0 && y + 1 < Settings.boardSize && Tile.tiles[x - 1][y + 1].isOccupied() == null) {
                validMoves.add(Tile.tiles[x - 1][y + 1]);
            }
            //if it is occupied it check if the one behind it is not
            //and then adds it to the list
            else if (x - 2 >= 0 && y + 2 < Settings.boardSize && Tile.tiles[x - 1][y + 1].isOccupied() != null && Tile.tiles[x - 2][y + 2].isOccupied() == null &&
                    Tile.tiles[x - 1][y + 1].isOccupied().color!=color) {
                validMoves.add(Tile.tiles[x - 2][y + 2]);
            }
        }
        //if the figure is blue it checks only if it can move up
        else {
            //checks if tile to the up right is occupied
            if (x + 1 < Settings.boardSize && y - 1 >= 0 && Tile.tiles[x + 1][y - 1].isOccupied() == null) {
                validMoves.add(Tile.tiles[x + 1][y - 1]);
            }
            //if it is occupied it check if the one behind it is not
            //and then adds it to the list
            else if (x + 2 < Settings.boardSize && y - 2 >= 0 && Tile.tiles[x + 1][y - 1].isOccupied() != null && Tile.tiles[x + 2][y - 2].isOccupied() == null &&
                    Tile.tiles[x + 1][y - 1].isOccupied().color!=color) {
                validMoves.add(Tile.tiles[x + 2][y - 2]);
            }
            //checks if tile to the up left is occupied
            if (x - 1 >= 0 && y - 1 >= 0 && Tile.tiles[x - 1][y - 1].isOccupied() == null) {
                validMoves.add(Tile.tiles[x - 1][y - 1]);
            }
            //if it is occupied it check if the one behind it is not
            //and then adds it to the list
            else if (x - 2 >= 0 && y - 2 >= 0 && Tile.tiles[x - 1][y - 1].isOccupied() != null && Tile.tiles[x - 2][y - 2].isOccupied() == null &&
                    Tile.tiles[x - 1][y - 1].isOccupied().color!=color) {
                validMoves.add(Tile.tiles[x - 2][y - 2]);
            }
        }
        return validMoves;
    }

    //removes this object from list of player's figures and adds a new King to it
    private void promote(){
        if((position.getY()==0 && !color) || (position.getY()==840-Tile.tileDimension && color)) {
            player.figures.remove(this);
            player.figures.add(new King(player, this.position, this.color));
        }
    }

    //logic responsible for moving the figure
    //if a figure doesn't have to jump it checks if checkValidMoves returns an array list containing targeted tile
    //if a figure has to jump it checks if checkValidJumps returns an array list containing targeted tile
    //sets objects position to unoccupied and then changes it to target
    //if there is a figure between position and target it is captured
    //if after moving there is a possibility of a jump it must be taken and mustJump is set to true
    //else it sets it to false and sets Figure.isSelected to null and changes turn
    //if figure is on an end of the board it call promote() function to change a Man into a King
    boolean move(Tile target){
        if((checkValidMoves().contains(target) && !mustJump) || (checkValidJumps().contains(target) && mustJump)) {
            int x = position.getX()/Tile.tileDimension;
            int y = position.getY()/Tile.tileDimension;
            int tarX = target.getX()/Tile.tileDimension;
            int tarY = target.getY()/Tile.tileDimension;
            position.setOccupied(null);
            this.position=target;
            position.setOccupied(this);
            if(y-tarY!=1 && y-tarY!=-1){
                Tile.tiles[(x+tarX)/2][(y+tarY)/2].isOccupied().capture();
                //when figure has reached end of the board and there is a valid jump after jumping
                //it checks if instantaneous promotion is set on
                mustJump=true;
                if(!checkValidJumps().isEmpty() && !((position.getY() == 840-Tile.tileDimension || position.getY() == 0) && Settings.manPromotedInstantly)){
                    return false;
                }
            }
            mustJump=false;
            Player.turn = !Player.turn;
            isSelected = null;
            if (position.getY() == 840-Tile.tileDimension || position.getY() == 0) {
                promote();
            }
            return true;

        }
        else {
            return false;
        }
    }

}