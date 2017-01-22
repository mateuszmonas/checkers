package checkers;

/**
 * Created by mateusz on 1/17/17.
 */
class Tile{
    //array of all tiles
    static Tile tiles[][];

    //width and height of every tile
    static int tileDimension;

    //position of the top left corner of the tile
    private int x, y;

    //color of the tile
    //true is white
    //false is black
    private boolean color;

    //position of a tile in the tiles array
    //tiles[0][0].id=="00"
    //tiles[1][0].id=="01"
    private String id;

    //reference to the Figure that is positioned on the tile
    private Figure isOccupied;

    public Tile(int x, int y, boolean color){
        this.x = x*tileDimension;
        this.y = y*tileDimension;
        this.color = color;
        this.isOccupied = null;
    }

    public Figure isOccupied() {
        return isOccupied;
    }

    public void setOccupied(Figure occupied) {
        isOccupied = occupied;
    }

    public int getTileDimension() {
        return tileDimension;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    static void setTiles(int boardSize){
        tiles = new Tile[boardSize][boardSize];
        tileDimension = 840/boardSize;
    }
}