package checkers;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by mateusz on 2/14/17.
 */
//checkers controller, responsible for controlling everything
class Controller {
    private GraphicsContext gc;
    
    Controller(GraphicsContext gc){
        this.gc=gc;
        gc.setLineWidth(2);
        fillTiles();
        fillBoard();
    }
    
    //gets tile that was clicked based on cursor position
    void getClickedTile(MouseEvent event){
        for (Tile ti[] : Tile.tiles) {
            for (Tile t: ti) {
                //check if mouse click was inside a Tile
                if (event.getX() > t.getX() && event.getX() < t.getX() + t.getTileDimension() &&
                        event.getY() > t.getY() && event.getY() < t.getY() + t.getTileDimension()) {
                    //checks if there is figure that must make a jump
                    //if there is it highlights possible jumps
                    if(Figure.isSelected!=null && !Figure.isSelected.checkValidJumps().isEmpty() && Figure.isSelected.mustJump){
                        highlightValidMoves(Figure.isSelected.checkValidJumps());
                    }
                    //check if clicked Tile contains a Figure
                    //if it does, the figure is selected and possible moves are highlighted
                    else if(t.isOccupied()!=null && !t.isOccupied().equals(Figure.isSelected)){
                        if(((Player.turn && t.isOccupied().color) || (!Player.turn && !t.isOccupied().color)) && !t.isOccupied().checkValidMoves().isEmpty()) {
                            t.isOccupied().selectFigure(gc);
                            highlightValidMoves(Figure.isSelected.checkValidMoves());
                        }
                    }
                    //if a figure is selected and clicked tile is empty it attempts to move selected figure
                    //then it redraws the board, and if there is a jump possibility it highlights it
                    if(Figure.isSelected!=null && t.isOccupied()==null){
                        Figure.isSelected.move(t);
                        fillBoard();
                        if(Figure.isSelected!=null){
                            highlightValidMoves(Figure.isSelected.checkValidMoves());
                        }
                        switch(main.isGameFinished()){
                            case 0:{
                                Platform.exit();
                                return;
                            }
                            case 1:{
                                main.reset(gc);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    
    //highlights tiles that are valid moves
    private void highlightValidMoves(ArrayList<Tile> validMoves){
        //I have to copy content of validMoves to new array list because for some reason validMoves becomes empty after i call fillBoard()
        ArrayList<Tile> a = new ArrayList<>();
        a.addAll(validMoves);
        fillBoard();
        gc.setStroke(Color.BROWN);
        gc.strokeRect(Figure.isSelected.position.getX(), Figure.isSelected.position.getY(),
                Figure.isSelected.position.getTileDimension(), Figure.isSelected.position.getTileDimension());
        for (Tile t : a) {
            gc.strokeRect(t.getX(), t.getY(), t.getTileDimension(), t.getTileDimension());
        }
    }
    
    //creates a two dimensional array with all tiles
    private void fillTiles(){
        int x = 0;
        int y = 0;
        boolean color = true;
        while(y<Settings.boardSize) {
            while(x<Settings.boardSize) {
                Tile.tiles[x][y] = new Tile(x, y, color);
                //sets tile's position in an array as its id
                //tiles[0][0].id=="00"
                //tiles[1][0].id=="10"
                Tile.tiles[x][y].setId(Integer.toString(x) + Integer.toString(y));
                color= !color;
                x++;
            }
            y++;
            x=0;
            color=!color;
        }
    }
    
    //draws all tiles and figures
    private void fillBoard(){
        int x = 0;
        int y = 0;
        while(y<Settings.boardSize){
            while(x<Settings.boardSize){
                colorTile(Tile.tiles[x][y]);
                x++;
            }
            y++;
            x=0;
        }
        for (Player p : Player.players){
            p.figures.forEach(f -> f.drawFigure(gc));
        }
    }
    
    //colors tile based on whether its color variable is true or false
    //true is white
    //false is black
    private void colorTile(Tile tile){
        if(tile.isColor())gc.setFill(Color.web("0xffeebb"));
        else gc.setFill(Color.web("0x558822"));
        gc.fillRect(tile.getX(),tile.getY(),tile.getTileDimension(),tile.getTileDimension());
    }
    
}