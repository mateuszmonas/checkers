package checkers;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 * Created by mateusz on 1/17/17.
 */
public class King extends Figure {

    King(Man man){
        validMoves = new ArrayList<>();
        mustJump = man.mustJump;
        color = man.color;
        position = man.position;
        player = man.player;
        position.setOccupied(this);
    }

    King(Player player, Tile position, boolean color){
        this.player = player;
        validMoves = new ArrayList<>();
        this.position = position;
        this.color = color;
        position.setOccupied(this);
    }

    //draws a figure in its position
    //player1 figures are red
    //player2 figures are blue
    //Kings have smaller brown circle inside
    void drawFigure(GraphicsContext gc){
        if(color) gc.setFill(Color.RED);
        else gc.setFill(Color.BLUE);
        gc.fillOval(position.getX(), position.getY(),Tile.tileDimension,Tile.tileDimension);
        gc.setFill((color) ? Color.BLUE : Color.RED);
        gc.fillOval(position.getX()+Tile.tileDimension/4, position.getY()+Tile.tileDimension/4,Tile.tileDimension/2,Tile.tileDimension/2);
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

        if(Settings.flyingKings){
            while(x + 1 < Settings.boardSize && y + 1 < Settings.boardSize){
                if (x + 2 < Settings.boardSize && y + 2 < Settings.boardSize && Tile.tiles[x + 2][y + 2].isOccupied() == null && Tile.tiles[x + 1][y + 1].isOccupied() != null &&
                        Tile.tiles[x + 1][y + 1].isOccupied().color != color) {
                    validMoves.add(Tile.tiles[x + 2][y + 2]);
                    break;
                }
                x++;
                y++;
            }
            x = position.getX()/Tile.tileDimension;
            y = position.getY()/Tile.tileDimension;
            while(x - 1 >= 0 && y + 1 < Settings.boardSize) {
                if (x - 2 >= 0 && y + 2 < Settings.boardSize && Tile.tiles[x - 2][y + 2].isOccupied() == null && Tile.tiles[x - 1][y + 1].isOccupied() != null &&
                        Tile.tiles[x - 1][y + 1].isOccupied().color != color) {
                    validMoves.add(Tile.tiles[x - 2][y + 2]);
                    break;
                }
                x--;
                y++;
            }
            x = position.getX()/Tile.tileDimension;
            y = position.getY()/Tile.tileDimension;
            while (x + 1 < Settings.boardSize && y - 1 >= 0 ) {
                if (x + 2 < Settings.boardSize && y - 2 >= 0 && Tile.tiles[x + 2][y - 2].isOccupied() == null && Tile.tiles[x + 1][y - 1].isOccupied() != null &&
                        Tile.tiles[x + 1][y - 1].isOccupied().color != color) {
                    validMoves.add(Tile.tiles[x + 2][y - 2]);
                    break;
                }
                x++;
                y--;
            }
            x = position.getX()/Tile.tileDimension;
            y = position.getY()/Tile.tileDimension;
            while (x - 1 >= 0 && y - 1 >= 0 ) {
                if (x - 2 >= 0 && y - 2 >= 0 && Tile.tiles[x - 2][y - 2].isOccupied() == null && Tile.tiles[x - 1][y - 1].isOccupied() != null &&
                        Tile.tiles[x - 1][y - 1].isOccupied().color != color) {
                    validMoves.add(Tile.tiles[x - 2][y - 2]);
                    break;
                }
                x--;
                y--;
            }
        }
        else {
            if (x + 2 < Settings.boardSize && y + 2 < Settings.boardSize && Tile.tiles[x + 1][y + 1].isOccupied() != null
                    && Tile.tiles[x + 2][y + 2].isOccupied() == null && Tile.tiles[x + 1][y + 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x + 2][y + 2]);
            }
            if (x - 2 >= 0 && y + 2 < Settings.boardSize && Tile.tiles[x - 1][y + 1].isOccupied() != null
                    && Tile.tiles[x - 2][y + 2].isOccupied() == null && Tile.tiles[x - 1][y + 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x - 2][y + 2]);
            }
            if (x + 2 < Settings.boardSize && y - 2 >= 0  && Tile.tiles[x + 1][y - 1].isOccupied() != null
                    && Tile.tiles[x + 2][y - 2].isOccupied() == null && Tile.tiles[x + 1][y - 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x + 2][y - 2]);
            }
            if (x - 2 >= 0 && y - 2 >= 0  && Tile.tiles[x - 1][y - 1].isOccupied() != null
                    && Tile.tiles[x - 2][y - 2].isOccupied() == null && Tile.tiles[x - 1][y - 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x - 2][y - 2]);
            }
        }
        return validMoves;
    }

    //returns array list of all possible moves
    ArrayList<Tile> checkValidMoves(){
        validMoves.clear();
        //if Settings.forcedCapture is true it checks if any of players pieces can capture something
        if(Settings.forcedCapture) {
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

        if(Settings.flyingKings){
            while(x + 1 < Settings.boardSize && y + 1 < Settings.boardSize){
                if( Tile.tiles[x + 1][y + 1].isOccupied() == null){
                    validMoves.add(Tile.tiles[x + 1][y + 1]);
                } else if (x + 2 < Settings.boardSize && y + 2 < Settings.boardSize && Tile.tiles[x + 2][y + 2].isOccupied() == null &&
                        Tile.tiles[x + 1][y + 1].isOccupied().color != color) {
                    validMoves.add(Tile.tiles[x + 2][y + 2]);
                    break;
                }
                x++;
                y++;
            }
            x = position.getX()/Tile.tileDimension;
            y = position.getY()/Tile.tileDimension;
            while(x - 1 >= 0 && y + 1 < Settings.boardSize) {
                if (Tile.tiles[x - 1][y + 1].isOccupied() == null) {
                    validMoves.add(Tile.tiles[x - 1][y + 1]);
                } else if (x - 2 >= 0 && y + 2 < Settings.boardSize && Tile.tiles[x - 2][y + 2].isOccupied() == null &&
                        Tile.tiles[x - 1][y + 1].isOccupied().color != color) {
                    validMoves.add(Tile.tiles[x - 2][y + 2]);
                    break;
                }
                x--;
                y++;
            }
            x = position.getX()/Tile.tileDimension;
            y = position.getY()/Tile.tileDimension;
            while (x + 1 < Settings.boardSize && y - 1 >= 0 ) {
                if (Tile.tiles[x + 1][y - 1].isOccupied() == null){
                    validMoves.add(Tile.tiles[x + 1][y - 1]);
                } else if (x + 2 < Settings.boardSize && y - 2 >= 0 && Tile.tiles[x + 2][y - 2].isOccupied() == null &&
                        Tile.tiles[x + 1][y - 1].isOccupied().color != color) {
                    validMoves.add(Tile.tiles[x + 2][y - 2]);
                    break;
                }
                x++;
                y--;
            }
            x = position.getX()/Tile.tileDimension;
            y = position.getY()/Tile.tileDimension;
            while (x - 1 >= 0 && y - 1 >= 0 ) {
                if (Tile.tiles[x - 1][y - 1].isOccupied() == null){
                    validMoves.add(Tile.tiles[x - 1][y - 1]);
                } else if (x - 2 >= 0 && y - 2 >= 0 && Tile.tiles[x - 2][y - 2].isOccupied() == null &&
                        Tile.tiles[x - 1][y - 1].isOccupied().color != color) {
                    validMoves.add(Tile.tiles[x - 2][y - 2]);
                    break;
                }
                x--;
                y--;
            }
        }
        else {
            if (x + 1 < Settings.boardSize && y + 1 < Settings.boardSize && Tile.tiles[x + 1][y + 1].isOccupied() == null) {
                validMoves.add(Tile.tiles[x + 1][y + 1]);
            } else if (x + 2 < Settings.boardSize && y + 2 < Settings.boardSize && Tile.tiles[x + 2][y + 2].isOccupied() == null &&
                    Tile.tiles[x + 1][y + 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x + 2][y + 2]);
            }
            if (x - 1 >= 0 && y + 1 < Settings.boardSize && Tile.tiles[x - 1][y + 1].isOccupied() == null) {
                validMoves.add(Tile.tiles[x - 1][y + 1]);
            } else if (x - 2 >= 0 && y + 2 < Settings.boardSize && Tile.tiles[x - 2][y + 2].isOccupied() == null &&
                    Tile.tiles[x - 1][y + 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x - 2][y + 2]);
            }
            if (x + 1 < Settings.boardSize && y - 1 >= 0 && Tile.tiles[x + 1][y - 1].isOccupied() == null) {
                validMoves.add(Tile.tiles[x + 1][y - 1]);
            } else if (x + 2 < Settings.boardSize && y - 2 >= 0 && Tile.tiles[x + 2][y - 2].isOccupied() == null &&
                    Tile.tiles[x + 1][y - 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x + 2][y - 2]);
            }
            if (x - 1 >= 0 && y - 1 >= 0 && Tile.tiles[x - 1][y - 1].isOccupied() == null) {
                validMoves.add(Tile.tiles[x - 1][y - 1]);
            } else if (x - 2 >= 0 && y - 2 >= 0 && Tile.tiles[x - 2][y - 2].isOccupied() == null &&
                    Tile.tiles[x - 1][y - 1].isOccupied().color != color) {
                validMoves.add(Tile.tiles[x - 2][y - 2]);
            }
        }

        return validMoves;
    }

    boolean move(Tile target){
        if((checkValidMoves().contains(target) && !mustJump) || (checkValidJumps().contains(target) && mustJump)) {
            int x = position.getX()/Tile.tileDimension;
            int y = position.getY()/Tile.tileDimension;
            int tarX = target.getX()/Tile.tileDimension;
            int tarY = target.getY()/Tile.tileDimension;
            position.setOccupied(null);
            this.position=target;
            position.setOccupied(this);

            //left up
            if(x-tarX>0 && y-tarY>0 && Tile.tiles[tarX + 1][tarY + 1].isOccupied() != null){
                Tile.tiles[tarX + 1][tarY + 1].isOccupied().capture();
                if (!checkValidJumps().isEmpty()) {
                    mustJump = true;
                    return false;
                }
            }
            //right down
            if(x-tarX<0 && y-tarY<0 && Tile.tiles[tarX - 1][tarY - 1].isOccupied() != null){
                Tile.tiles[tarX - 1][tarY - 1].isOccupied().capture();
                if (!checkValidJumps().isEmpty()) {
                    mustJump = true;
                    return false;
                }
            }
            //left down
            if(x-tarX>0 && y-tarY<0 && Tile.tiles[tarX + 1][tarY - 1].isOccupied() != null){
                Tile.tiles[tarX + 1][tarY - 1].isOccupied().capture();
                if (!checkValidJumps().isEmpty()) {
                    mustJump = true;
                    return false;
                }
            }
            //right up
            if(x-tarX<0 && y-tarY>0 && Tile.tiles[tarX - 1][tarY + 1].isOccupied() != null){
                Tile.tiles[tarX - 1][tarY + 1].isOccupied().capture();
                if (!checkValidJumps().isEmpty()) {
                    mustJump = true;
                    return false;
                }
            }
            mustJump=false;
            Player.turn = !Player.turn;
            isSelected = null;
            return true;

        }
        else {
            return false;
        }
    }
}
