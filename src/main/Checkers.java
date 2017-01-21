package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

//Checkers controller, responsible for drawing everything
class Drawing {
    GraphicsContext gc;

    Drawing(GraphicsContext gc){
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
                        switch(Checkers.isGameFinished()){
                            case 0:{
                                Platform.exit();
                                return;
                            }
                            case 1:{
                                Checkers.reset(gc);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    //highlights tiles that are valid moves
    void highlightValidMoves(ArrayList<Tile> validMoves){
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

    //creates a two dimensional array with 64 tiles
    void fillTiles(){
        int x = 0;
        int y = 0;
        boolean color = true;
        while(y<Settings.boardSize) {
            while(x<Settings.boardSize) {
                Tile.tiles[x][y] = new Tile(x, y, color);
                //sets tile's position in an array as its id
                //tiles[0][0].id=="00"
                //tiles[1][0].id=="01"
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
        if(tile.isColor())gc.setFill(Color.WHITE);
        else gc.setFill(Color.GRAY);
        gc.fillRect(tile.getX(),tile.getY(),tile.getTileDimension(),tile.getTileDimension());
    }

    private void highlightFigures(){
        gc.setStroke(Color.AQUA);
        if(Player.turn) Player.players.get(0).figures.forEach(f -> {
                if (!f.checkValidMoves().isEmpty()) {
                    gc.strokeRect(f.position.getX(), f.position.getY(), f.position.getTileDimension(), f.position.getTileDimension());
                }
            });
        else Player.players.get(1).figures.forEach(f -> {
            if (!f.checkValidMoves().isEmpty()) {
                gc.strokeRect(f.position.getX(), f.position.getY(), f.position.getTileDimension(), f.position.getTileDimension());
            }
        });
    }

}

public class Checkers extends Application
{

    //checks if game is finished
    //returns 0 if players want to play again
    //returns 1 if players don't want to play again
    //returns -1 if game is not finished
    static int isGameFinished(){
        int whoWon=-1;
        ArrayList<Tile> validMoves = new ArrayList<>();
        if(!Player.turn){
            //checks if player blue has any figures left
            if(Player.players.get(1).figures.isEmpty()){
                whoWon=0;
            }
            //checks if red player can make any moves
            for (Figure f : Player.players.get(0).figures) {
                validMoves.addAll(f.checkValidMoves());
            }
            if(validMoves.isEmpty()){
               whoWon=1;
            }
        }
        else {
            //checks if player red has any figures left
            if (Player.players.get(0).figures.isEmpty()){
                whoWon=1;
            }
            //checks if blue player can make any moves
            for (Figure f : Player.players.get(1).figures) {
                validMoves.addAll(f.checkValidMoves());
            }
            if (validMoves.isEmpty()) {
                whoWon=0;
            }
        }
        if(whoWon!=-1){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Game is finished");
            alert.setHeaderText((whoWon==0)?"Red player has won":"Blue player has won");
            alert.setContentText("Would you like to play again?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                return 1;
            } else {
                return 0;
            }
        }
        return -1;
    }

    //sets all variables to their initial values
    static Drawing reset(GraphicsContext gc){

        Player.players.clear();
        Player.turn=false;

        Drawing drawing = new Drawing(gc);

        //player red
        new Player(true);
        //player blue
        new Player(false);

        for (Player p : Player.players){
            p.figures.forEach(f -> f.drawFigure(gc));
        }
        return drawing;
    }

    public void start(Stage stage)
    {
        stage.setTitle( "checkers" );
        stage.setResizable(false);
        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene( scene );


        Canvas canvas = new Canvas( 840, 840 );
        root.getChildren().add( canvas );

        final GraphicsContext gc = canvas.getGraphicsContext2D();

        Settings.set(12, 30, true, true, false, true);

        Drawing drawing = reset(gc);

        scene.setOnMousePressed(drawing::getClickedTile);

        stage.show();
    }


    //starts the app
    public static void main(String[] args)
    {
        launch(args);
    }
}