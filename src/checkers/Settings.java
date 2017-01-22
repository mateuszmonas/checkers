package checkers;

/**
 * Created by mateusz on 1/21/17.
 */
public class Settings {
    static int boardSize;
    static int numberOfFigures;
    static boolean flyingKings;
    static boolean forcedCapture;
    static boolean manPromotedInstantly;
    static boolean canCaptureBackwards;

    static void set(int boardSize, int numberOfFigures, boolean flyingKings, boolean forcedCapture,
                    boolean manPromotedInstantly, boolean canCaptureBackwards) {
        Settings.boardSize = boardSize;
        Settings.numberOfFigures = numberOfFigures;
        Settings.flyingKings = flyingKings;
        Settings.forcedCapture = forcedCapture;
        Settings.manPromotedInstantly = manPromotedInstantly;
        Settings.canCaptureBackwards = canCaptureBackwards;
    }

    //when called from checkers.changeSettings function it switches board size based on which option was selected in choiceBox
    static void set(int index, boolean flyingKings, boolean forcedCapture,
                    boolean manPromotedInstantly, boolean canCaptureBackwards) {
        switch (index){
            case 0:{
                boardSize = 8;
                numberOfFigures = 12;
                break;
            }
            case 1:{
                boardSize = 10;
                numberOfFigures = 20;
                break;
            }
            case 2:{
                boardSize = 12;
                numberOfFigures = 30;
                break;
            }

        }
        Settings.flyingKings = flyingKings;
        Settings.forcedCapture = forcedCapture;
        Settings.manPromotedInstantly = manPromotedInstantly;
        Settings.canCaptureBackwards = canCaptureBackwards;
    }
}
