package checkers;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.Alert.AlertType;



public class main extends Application
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

    //opens new window where you can change game settings
    private static void changeSettings(Stage owner, GraphicsContext gc){
        ArrayList<String> lines = new ArrayList<>();
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setTitle("Settings");
        GridPane root = new GridPane();
        root.setHgap(0);
        root.setVgap(10);
        root.setPadding(new Insets(10, 25, 10, 25));
        Scene scene = new Scene(root, 300, 200);
        dialog.setScene(scene);


        try {
            Files.lines(Paths.get("Settings.txt")).forEach(lines::add);
        }catch (Exception e){
            lines.add("0");
            lines.add("false");
            lines.add("false");
            lines.add("false");
            lines.add("false");
        }

        ChoiceBox choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(
                "8x8 | 12 figures", "10x10 | 20 figures", "12x12 | 30 figures")
        );
        choiceBox.setValue(choiceBox.getItems().get(Integer.valueOf(lines.get(0))));
        root.add(choiceBox, 0, 0);

        CheckBox flyingKings = new CheckBox("Flying kings");
        flyingKings.setSelected(lines.get(1).equals("true"));
        root.add(flyingKings, 0, 1);

        CheckBox forcedCapture = new CheckBox("Forced capture");
        forcedCapture.setSelected(lines.get(2).equals("true"));
        root.add(forcedCapture, 0, 2);

        CheckBox manPromotedInstantly = new CheckBox("Man promoted instantly");
        manPromotedInstantly.setSelected(lines.get(3).equals("true"));
        root.add(manPromotedInstantly, 0, 3);

        CheckBox canCaptureBackwards = new CheckBox("Can capture backwards");
        canCaptureBackwards.setSelected(lines.get(4).equals("true"));
        root.add(canCaptureBackwards, 0, 4);

        Button save = new Button("Save");
        save.setOnAction(event -> {
            lines.clear();
            lines.add(Integer.toString(choiceBox.getItems().indexOf(choiceBox.getValue().toString())));
            lines.add(flyingKings.isSelected()?"true":"false");
            lines.add(forcedCapture.isSelected()?"true":"false");
            lines.add(manPromotedInstantly.isSelected()?"true":"false");
            lines.add(canCaptureBackwards.isSelected()?"true":"false");
            lines.add(flyingKings.isSelected()?"true":"false");
            try {
                Files.write(Paths.get("Settings.txt"), lines, Charset.forName("UTF-8"));
            }catch (Exception e){}
            Settings.set(choiceBox.getItems().indexOf(choiceBox.getValue().toString()), flyingKings.isSelected(), forcedCapture.isSelected(),
                    manPromotedInstantly.isSelected(), canCaptureBackwards.isSelected());
            reset(gc);
            dialog.close();
            });
        root.add(save, 0, 5);

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> dialog.close());
        root.add(cancel, 1, 5);



        dialog.show();
    }

    //sets all variables to their initial values
    static Controller reset(GraphicsContext gc){

        Tile.setTiles(Settings.boardSize);
        Player.players.clear();
        Player.turn=false;

        Controller controller = new Controller(gc);

        //player red
        new Player(true);
        //player blue
        new Player(false);

        for (Player p : Player.players){
            p.figures.forEach(f -> f.drawFigure(gc));
        }
        return controller;
    }

    public void start(Stage stage)
    {
        stage.setTitle( "checkers" );
        stage.setResizable(false);
        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene( scene );

        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        int dimensions = (int)(rectangle2D.getMaxY()-100 - ((rectangle2D.getMaxY()-100)%120));

        Canvas canvas = new Canvas( dimensions, dimensions );
        root.getChildren().add( canvas );

        final GraphicsContext gc = canvas.getGraphicsContext2D();

        //contains list of all settings
        ArrayList<String> lines = new ArrayList<>();

        //if Settings.txt exists it will save its content to array list lines
        //else it will fill lines with preset settings and create file Settings.txt
        try {
            Files.lines(Paths.get("Settings.txt")).forEach(lines::add);
        }catch (Exception e){
                lines.add("0");
                lines.add("false");
                lines.add("false");
                lines.add("false");
                lines.add("false");
            try {
                Files.write(Paths.get("Settings.txt"), lines, Charset.forName("UTF-8"));
            }catch (Exception ex){}
            }

        Settings.set(Integer.valueOf(lines.get(0)), lines.get(1).equals("true"), lines.get(2).equals("true"), lines.get(3).equals("true"), lines.get(4).equals("true"));

        Controller controller = reset(gc);

        scene.setOnMousePressed(controller::getClickedTile);
        scene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ESCAPE)) changeSettings(stage, gc);
        });

        stage.show();
    }


    //starts the app
    public static void main(String[] args)
    {
        launch(args);
    }
}