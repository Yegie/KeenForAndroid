package org.yegie.keenforandroid;

/**
 * Created by Sergey on 5/19/2016.
 */
public class KeenModel {

    private static final int MAX_SIZE = 9;

    /**
     * Stores any data that is linked to a square in the grid
     */
    public static class GridCell
    {
        boolean[] guesses;
        int finalGuessValue;
        int expectedValue;
        Zone zone;

        public GridCell(int size, int eV, Zone zone)
        {
            this.finalGuessValue = -1;
            this.guesses = new boolean[MAX_SIZE];
            this.expectedValue = eV;
            this.zone = zone;
        }
    }

    static public class Zone
    {
        enum Type
        {
            ADD,MINUS,TIMES,DIVIDE
        };

        Type zoneType;
        int expectedValue;

        public Zone(Type type, int eV)
        {
            this.zoneType = type;
            this.expectedValue = eV;
        }
        public String toString()
        {
            String a = ""+expectedValue;
            switch (zoneType)
            {
                case ADD:
                    a+=" +";
                    break;
                case MINUS:
                    a+=" -";
                    break;
                case TIMES:
                    a+=" x";
                    break;
                case DIVIDE:
                    a+=" /";
                    break;
            }

            return a;
        }
    }

    private final GridCell[][] gameGrid;

    private final Zone[] gameZones;


    private boolean finalGuess;
    private int activeX;
    private int activeY;
    private int size;

    /**
     * constructor that initializes everything
     */
    public KeenModel(int size, Zone[] zones, GridCell[][] grid)
    {
        this.size = size;
        this.gameGrid = grid;
        this.gameZones = zones;
        this.finalGuess = true;
        activeX = -1;
        activeY = -1;
    }

    /**
     * getter for the xy cords of the currently selected spot.
     */
    public int getActiveY(){return activeY;}
    public int getActiveX(){return activeX;}

    public void setActiveX(int activeX) {
        this.activeX = activeX;
    }
    public void setActiveY(int activeY) {
        this.activeY = activeY;
    }

    public void clearGuesses(int x, int y){
        gameGrid[x][y].guesses = new boolean[MAX_SIZE];
    }
    public void clearFinal(int x, int y){
        gameGrid[x][y].finalGuessValue = -1;
    }
    public void setCellFinalGuess(int x, int y, int guess){
        if(gameGrid[x][y].finalGuessValue == guess)
        {
            gameGrid[x][y].finalGuessValue = -1;
        }else {
            gameGrid[x][y].finalGuessValue = guess;
        }
    }
    public void addToCellGuesses(int x, int y, int guess){
        gameGrid[x][y].guesses[guess-1] = !gameGrid[x][y].guesses[guess-1];
    }

    public GridCell getCell(int x, int y)
    {
        return gameGrid[x][y];
    }

    public Zone[] getGameZones() {
        return gameZones;
    }

    public int getSize()
    {
        return size;
    }

    public boolean getFinalGuess() {return finalGuess; }
    public void toggleFinalGuess() {finalGuess = !finalGuess;}

}
