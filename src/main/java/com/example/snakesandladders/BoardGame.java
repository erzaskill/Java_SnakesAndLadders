package com.example.snakesandladders;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Tahle classa obsahuje vlastne celou implementaci hry, od mechanickych pohybu hracu, az po celou logiku hry.
 *
 * This class has the whole implementation of the game - the mechanics of the players, the whole logic of the game...
 * @author Erik Praunsperger
 */

public class BoardGame extends Application {

    //Zde setneme aby postavicka byla ve stredu prvnihoTilu, a aby byla ve prostred.
    //Tile ma velikost 70 px, Tedy aby byl uprostred prvniho tilu, tak musime setnout sirku na 35px.
    //A musime setnou vysku, aby zacal v na prvnim radku dole, tedy (Tile_size*10-35) = 665 v nasem pripade (rozliseni obrazku je 700x700)

    //Here we set a the players position, so they are centered in the middle of the first tile.
    //The Tile's size is 70 px - so if we want our player to be in the center of the first Tile, we need to set the x position to half of the tile (35px)
    //We also need to set the height position (y coordinates), so the player starts from the bottom of the board (Tile_size*10-35)
    // = 665 (the resolution of the board is 700x700)

    public static final int TILE_SIZE = 70;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    public int random;
    public Label randomResult;

    //debugging
    public int cirPos[][] = new int[10][10];
    public int ladderSnakePosition[][] = new int[7][8];
    //end of debugging

    public Circle player1;
    public Circle player2;

    public boolean player1Turn = true;
    public boolean player2Turn = true;

    public int playerPosition1 = 1;
    public int playerPosition2 = 1;

    public static int player1XPosition = 35;
    public static int player1YPosition = 665;

    public static int player2XPosition = 35;
    public static int player2YPosition = 665;


    public int positionCircle1 = 1;
    public int positionCircle2 = 1;

    public boolean gameStart = false; //The game starts after clicking the start button
    public Button gameButton;


    private Group tileGroup = new Group();

    /**
     * Tato metoda nam pripravi celou hru, vytvori nam cele hraci pole: Startovaci a cilove pozice hracu, hady a zebriky, tlacitka. Je zde ukryta i samotna logika hry.
     *
     * This method will prepare the whole game, it will create the game tile with all the objects:
     * Start and finish positions of the players, snakes and ladders, buttons.
     * Here you can also find the game logic.
     */
    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, (HEIGHT * TILE_SIZE) + 70);
        //U vysky jsem pridal jeste velikost 70 px, kvuli menu tlacitkum, ktere budou pod hraci plochou
        //I also added another 70 px to the height (y coordinates), so I can place buttons, which are placed under the game board
        root.getChildren().addAll(tileGroup);

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Tile tile = new Tile(TILE_SIZE, TILE_SIZE);
                tile.setTranslateX(j * TILE_SIZE);
                tile.setTranslateY(i * TILE_SIZE);
                tileGroup.getChildren().add(tile);

                cirPos[i][j] = j*(TILE_SIZE - 35);
            }
        }

        //Vytvareni hadu a zebriku -> lepsi udelat ARRAY hadu a zebriku (zatim si netroufam)
        //Creating Snakes and ladders -> it's better to make an array of all the snakes and ladders (will improve it later) - TODO
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(cirPos[i][j]+ " ");
            }
            System.out.println();
        }


        player1 = new Circle(35);
        player1.setId("player1");

        //prozatim moznost - chci si tam naimportovat moje figurky az vyresim problem s cestou style.css
        //Temporary option - I want to import my own player model (picture) instead - need to fix the problem with the file path of style.css - TODO
        player1.setFill(Color.RED);
        player1.getStyleClass().add("style.css"); //todo - file path to style.css - to import my own player model (picture)
        player1.setTranslateY(player1YPosition);

        player2 = new Circle(35);
        player2.setId("player2");

        //prozatim moznost - chci si tam naimportovat moje figurky az vyresim problem s cestou style.css
        //Temporary option - I want to import my own player model (picture) instead - need to fix the problem with the file path of style.css - TODO
        player2.setFill(Color.DARKBLUE);
        player2.getStyleClass().add("style.css"); //todo - file path to style.css - to import my own player model (picture)
        player2.setTranslateX(player2XPosition);
        player2.setTranslateY(player2YPosition);


        //First player
        Button button1 = new Button("Player 1");
        button1.setTranslateX(70);
        button1.setTranslateY(715);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(gameStart == true){
                    if(player1Turn == true){
                        getDiceValue();
                        randomResult.setText(String.valueOf(random));
                        move1Player();
                        translatePlayer(player1XPosition,player1YPosition,player1);
                        player1Turn = false;
                        player2Turn = true;
                        playerPosition1+= random;

                        //ZDE JE CHYBA - po stoupnuti na hada/zebrik ->postavicka nevi kterym smerem jit, jestli do leva nebo do prava
                        //BUG - If I land on snake or ladder -> The player doesn't know what way he should go (left/right) - TODO

                        //LADDERS POSITIONS AND TRANSLATIONS
                        if(player1XPosition == 245 && player1YPosition == 665){ //first row, 4th tile is ladder.
                            translatePlayer(player1XPosition = 315, player1YPosition = 525, player1);
                        }
                        if(player1XPosition == 525 && player1YPosition == 595){ //second row, 13th tile is ladder.
                            translatePlayer(player1XPosition = 385, player1YPosition = 385, player1);
                        }
                        if(player1XPosition == 525 && player1YPosition == 455){ //fourth row, 33th tile is ladder.
                            translatePlayer(player1XPosition = 595, player1YPosition = 385, player1);
                        }
                        if(player1XPosition == 105 && player1YPosition == 385){ //fifth row, 42nd tile is ladder.
                            translatePlayer(player1XPosition = 175, player1YPosition = 245, player1);
                        }
                        if(player1XPosition == 665 && player1YPosition == 385){ //fifth row, 50th tile is ladder.
                            translatePlayer(player1XPosition = 595, player1YPosition = 245, player1);
                        }
                        if(player1XPosition == 105 && player1YPosition == 245){ //seventh row, 62nd tile is ladder.
                            translatePlayer(player1XPosition = 35, player1YPosition = 105, player1);
                        }
                        if(player1XPosition == 455 && player1YPosition == 175){ //eighth row, 74th tile is ladder.
                            translatePlayer(player1XPosition = 595, player1YPosition = 35, player1);
                        }

                        //SNAKES POSITIONS AND TRANSLATIONS
                        if(player1XPosition == 455 && player1YPosition == 525){ //third row, 27th tile is snake.
                            translatePlayer(player1XPosition = 315, player1YPosition = 665, player1);
                        }
                        if(player1XPosition == 35 && player1YPosition == 455){ //fourth row, 40th tile is snake.
                            translatePlayer(player1XPosition = 175, player1YPosition = 665, player1);
                        }
                        if(player1XPosition == 175 && player1YPosition == 385){ //fifth row, 43th tile is snake.
                            translatePlayer(player1XPosition = 175, player1YPosition = 595, player1);
                        }
                        if(player1XPosition == 455 && player1YPosition == 315){ //sixth row, 57th tile is snake.
                            translatePlayer(player1XPosition = 665, player1YPosition = 455, player1);
                        }
                        if(player1XPosition == 385 && player1YPosition == 245){ //seventh row, 66th tile is snake.
                            translatePlayer(player1XPosition = 315, player1YPosition = 385, player1);
                        }
                        if(player1XPosition == 315 && player1YPosition == 175){ //eighth row, 76th tile is snake.
                            translatePlayer(player1XPosition = 175, player1YPosition = 315, player1);
                        }
                        if(player1XPosition == 595 && player1YPosition == 105){ //ninth row, 89th tile is snake.
                            translatePlayer(player1XPosition = 525, player1YPosition = 315, player1);
                        }
                        if(player1XPosition == 105 && player1YPosition == 35){ //last row, 99th tile is snake.
                            translatePlayer(player1XPosition = 35, player1YPosition = 385, player1);
                        }
                    }
                }
            }
        });


        //Second player
        Button button2 = new Button("Player 2");
        button2.setTranslateX(560);
        button2.setTranslateY(715);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(gameStart == true){
                    if(player2Turn){
                        getDiceValue();
                        randomResult.setText(String.valueOf(random));
                        move2Player();
                        translatePlayer(player2XPosition,player2YPosition,player2);
                        player2Turn = false;
                        player1Turn = true;
                        playerPosition2+= random;

                        //ZDE JE CHYBA - po stoupnuti na hada/zebrik ->postavicka nevi kterym smerem jit, jestli do leva nebo do prava
                        //BUG - If I land on snake or ladder -> The player doesn't know what way he should go (left/right) - TODO

                        //LADDERS POSITIONS AND TRANSLATIONS
                        if(player2XPosition == 245 && player2YPosition == 665){ //first row, 4th tile is ladder.
                            translatePlayer(player2XPosition = 315, player2YPosition = 525, player2);
                        }
                        if(player2XPosition == 525 && player2YPosition == 595){ //second row, 13th tile is ladder.
                            translatePlayer(player2XPosition = 385, player2YPosition = 385, player2);
                        }
                        if(player2XPosition == 525 && player2YPosition == 455){ //fourth row, 33th tile is ladder.
                            translatePlayer(player2XPosition = 595, player2YPosition = 385, player2);
                        }
                        if(player2XPosition == 105 && player2YPosition == 385){ //fifth row, 42nd tile is ladder.
                            translatePlayer(player2XPosition = 175, player2YPosition = 245, player2);
                        }
                        if(player2XPosition == 665 && player2YPosition == 385){ //fifth row, 50th tile is ladder.
                            translatePlayer(player2XPosition = 595, player2YPosition = 245, player2);
                        }
                        if(player2XPosition == 105 && player2YPosition == 245){ //seventh row, 62nd tile is ladder.
                            translatePlayer(player2XPosition = 35, player2YPosition = 105, player2);
                        }
                        if(player2XPosition == 455 && player2YPosition == 175){ //eighth row, 74th tile is ladder.
                            translatePlayer(player2XPosition = 595, player2YPosition = 35, player2);
                        }

                        //SNAKES POSITIONS AND TRANSLATIONS
                        if(player2XPosition == 455 && player2YPosition == 525){ //third row, 27th tile is snake.
                            translatePlayer(player2XPosition = 315, player2YPosition = 665, player2);
                        }
                        if(player2XPosition == 35 && player2YPosition == 455){ //fourth row, 40th tile is snake.
                            translatePlayer(player2XPosition = 175, player2YPosition = 665, player2);
                        }
                        if(player2XPosition == 175 && player2YPosition == 385){ //fifth row, 43th tile is snake.
                            translatePlayer(player2XPosition = 175, player2YPosition = 595, player2);
                        }
                        if(player2XPosition == 455 && player2YPosition == 315){ //sixth row, 57th tile is snake.
                            translatePlayer(player2XPosition = 665, player2YPosition = 455, player2);
                        }
                        if(player2XPosition == 385 && player2YPosition == 245){ //seventh row, 66th tile is snake.
                            translatePlayer(player2XPosition = 315, player2YPosition = 385, player2);
                        }
                        if(player2XPosition == 315 && player2YPosition == 175){ //eighth row, 76th tile is snake.
                            translatePlayer(player2XPosition = 175, player2YPosition = 315, player2);
                        }
                        if(player2XPosition == 595 && player2YPosition == 105){ //ninth row, 89th tile is snake.
                            translatePlayer(player2XPosition = 525, player2YPosition = 315, player2);
                        }
                        if(player2XPosition == 105 && player2YPosition == 35){ //last row, 99th tile is snake.
                            translatePlayer(player2XPosition = 35, player2YPosition = 385, player2);
                        }
                    }
                }
            }
        });

        gameButton = new Button("Start Game");
        gameButton.setTranslateX(315);
        gameButton.setTranslateY(715);
        gameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameButton.setText("The game just started!");
                player1XPosition = 35;
                player1YPosition = 665;
                player2XPosition = 35;
                player2YPosition = 665;

                player1.setTranslateX(player1XPosition);
                player1.setTranslateY(player1YPosition);
                player2.setTranslateX(player2XPosition);
                player2.setTranslateY(player2YPosition);
                gameStart = true;
            }
        });

        randomResult = new Label("0"); //TODO - No text but dice animation instead - dice .img files
        randomResult.setTranslateX(350);
        randomResult.setTranslateY(750);

        Image image = new Image("C:\\Users\\skajl\\Desktop\\Vzdelani\\PEF\\Vyuka\\Semestr4\\Java\\PJJ_HowToPass\\Projekt\\SnakesAndLadders\\src\\board.jpg");
        //Musi se zmenit cesta k board.jpg souboru, jinak se vyhodi chyba a hra se nespusti
        //Najit lepsi zpusob, jak hodit cestu toho obrazku, tak aby to fungovalo i na jinacich pocitacich(jinaci ceste k souboru)
        //TODO - find a better way - the file path of the board picture, so it works on different computers(different file paths)
        ImageView backGroundImage = new ImageView();
        backGroundImage.setImage(image);
        backGroundImage.setFitHeight(700);
        backGroundImage.setFitWidth(700); // It's not dymanic, so please don't change the resolution - tnx :D

        tileGroup.getChildren().addAll(backGroundImage, player1, player2, button1, button2, gameButton, randomResult);
        return root;
    }

    /**
     * metoda hodu kostkou - vygeneruje nahodne cislo od 1-6
     *
     * The method - throwing the dice - Generates random number 1-6
     */
    private void getDiceValue(){
        random = (int)(Math.random()*6+1); //pouzil jsem metodu z class Math
    }

    /**
     * Tato metoda se nam stara o zajisteni mechaniky pohybu prvniho hrace po spravnych pixel (Tilech = polickach)
     *
     * This method is for first Player moving mechanics
     */
    private void move1Player(){
        for (int i = 0; i < random; i++) {
            if(positionCircle1 % 2 == 1){
                player1XPosition+=70;
            }
            if(positionCircle1 % 2 == 0) {
                player1XPosition-=70;
            }

            if(player1XPosition > 665){
                player1YPosition-=70;
                player1XPosition-=70;
                positionCircle1++;
            }

            if(player1XPosition < 35){
                player1YPosition-=70;
                player1XPosition+=70;
                positionCircle1++;
            }


            //Nejsem si jisty jestli tady nema byt && instead (zatim uplne idealne nefunguje ani jedno)
            //Not sure about the condition - so right now it's temporary - TODO
            if(player1XPosition < 25 || player1YPosition < 25){
                player1XPosition = 35;
                player1YPosition = 35;
                randomResult.setText("Player 1 won!");
                gameButton.setText("Start new game");
            }

        }
    }

    /**
     * Tato metoda se nam stara o zajisteni mechaniky pohybu druheho hrace po spravnych pixel (Tilech = polickach)
     *
     * This method is for second Player moving mechanics
     */
    private void move2Player(){
        for (int i = 0; i < random; i++) {
            if(positionCircle2 % 2 == 1){
                player2XPosition+=70;
            }
            if(positionCircle2 % 2 == 0) {
                player2XPosition-=70;
            }

            if(player2XPosition > 665){
                player2YPosition-=70;
                player2XPosition-=70;
                positionCircle2++;
            }

            if(player2XPosition < 35){
                player2YPosition-=70;
                player2XPosition+=70;
                positionCircle2++;
            }

            if(player2XPosition < 25 || player2YPosition < 25){ //Nejsem si jisty jestli tady nema byt && instead TODO
                player2XPosition = 35;
                player2YPosition = 35;
                randomResult.setText("Player 2 won!");
                gameButton.setText("Start new game");
            }

        }
    }

    /**
     * Tato metoda nam zajistuje, aby jsme videli jak se nam hraci posunou na dane misto. (animaci)
     * @param x jedna se o souradnici sirky v pixelech (width)
     * @param y jedna se o souradnici vysky v pixelech (height)
     * @param player tenhle paramatr slouzi k urceni, ktereho hrace se translate tyka
     *
     * This method is for the animation when player gets translated to the right tile.
     */
    private void translatePlayer(int x, int y, Circle player){
        TranslateTransition animate = new TranslateTransition(Duration.millis(1000),player);
        animate.setToX(x);
        animate.setToY(y);
        animate.setAutoReverse(false);
        animate.play();
    }

    /**
     * Automaticky vygenerovana metoda - zde jsem pouze nastavil scenu, aby byla vygenerovana pri startu metodou {@link #createContent() createContent}. A taky jsem zmenil nazev na hru SnakesAndLadders
     * @param stage
     * @throws IOException
     *
     *
     * Automatically generated - the Scene is created by my createContent() method
     */

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent());
        //scene.getStylesheets().add("[url na style.css]");
        stage.setTitle("SnakesAndLadders ");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}