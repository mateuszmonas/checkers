package main;

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
                    boolean manPromotedInstantly, boolean canCaptureBackwards){
        Settings.boardSize = boardSize;
        Settings.numberOfFigures = numberOfFigures;
        Settings.flyingKings = flyingKings;
        Settings.forcedCapture = forcedCapture;
        Settings.manPromotedInstantly = manPromotedInstantly;
        Settings.canCaptureBackwards = canCaptureBackwards;
    }
}
