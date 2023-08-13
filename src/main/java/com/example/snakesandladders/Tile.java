package com.example.snakesandladders;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * Tahle class slouzi k implementaci jednoho policka - dedi ze tridy Rectangle
 * This class is used to implement a tile - Inherits from the class Rectangle.
 * @author Erik Praunsperger
 */
public class Tile extends Rectangle {
    public Tile(int x, int y){
        setWidth(BoardGame.TILE_SIZE);
        setHeight(BoardGame.TILE_SIZE);


        //Tohle je stejne schovane za backgroundImage - slouzilo k debuggovani
        //This is hidden behind backgroundImage - it was used for debugging
        setFill(Color.YELLOW);
        setStroke(Color.BLACK);
    }
}
