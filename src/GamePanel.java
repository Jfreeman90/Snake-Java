import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.Color;

public class GamePanel extends JPanel implements ActionListener {
    //attributes
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT=700;
    static final int UNIT_SIZE=35;      //size of each invisible grid.
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; //total size of array squares available
    int DELAY=100;      //game speed (HIGHER = SLOWER)
    int[] x = new int[GAME_UNITS];    //will hold the x-value of the snakes body
    int[] y = new int[GAME_UNITS];    //will hold the y-value of the snakes body
    int bodyParts=5;       //initial size of the snake
    int foodEaten;          //players score
    int foodX;              //X location of the food
    int foodY;              //Y location of the food
    char direction = 'R';   //initial start the snake will move to the right
    boolean running=false;  //game is running true or false
    Timer timer;            //timer started
    Random random;          //random object initialised
    boolean initLoad=true;  //will be true on initial load up and display start screen
    int hiScore=0;          //A given hiscore for each load up - starts at 0 everytime
    boolean memeMode=false; //activate or deactivate meme mode
    boolean hardMode=false; //Activate hard mode
    File path= new File("C:\\Users\\JackF\\OneDrive\\Documents\\PythonScripts\\TSI\\Java\\SnakeJava\\");
    //meme mode images loaded in
    BufferedImage  foodImage = ImageIO.read(new File(path, "borisHead.png"));
    BufferedImage  snakeBody = ImageIO.read(new File(path, "snakeBody2.PNG"));
    BufferedImage  snakeHead = ImageIO.read(new File(path, "shrekHead.jpg"));
    BufferedImage  backgroundCat = ImageIO.read(new File(path, "catBackground.jpg"));
    //space theme images loaded in.;
    BufferedImage  wallyBody = ImageIO.read(new File(path, "snakeBodyWally.PNG"));
    BufferedImage  wallyHead = ImageIO.read(new File(path, "snakeHeadWally.PNG"));
    BufferedImage  wally = ImageIO.read(new File(path, "wally.png"));
    BufferedImage  wallyBackground = ImageIO.read(new File(path, "wallyBackGround.PNG"));
    //startscreen and end game images
    BufferedImage  startScreen = ImageIO.read(new File(path, "startscreen.png"));
    BufferedImage  gameoverBackground = ImageIO.read(new File(path, "gameoverscreen.PNG"));

    //Constructor
    GamePanel() throws IOException {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(160, 200, 20));
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
    }

    //starts a new timer, generates a new food and turns running to true.
    public void startGame(){
        newFood();
        //game is now running
        this.running=true;
        this.timer = new Timer(this.DELAY, this);
        this.timer.start();
        this.initLoad=false;

    }

    //initiate the paint component
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    //main function that will be called on re paint to display the snake
    public void draw(Graphics g){
        //if running
        if (this.running){
            //draw game grid.
            for (int i=0; i< SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }

            if (!this.memeMode && !this.hardMode) {
                //draw the food
                g.setColor(Color.darkGray);
                g.fillOval(this.foodX, this.foodY, UNIT_SIZE / 2, UNIT_SIZE / 2);

                //draw the snake
                g.setColor(Color.darkGray);
                for (int i = 0; i < this.bodyParts; i++) {
                    if (i == 0) {//snake head
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                           g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            } else if (this.hardMode) {
                //space MODE
                //draw out the same stuff but using the loaded meme images
                g.drawImage(this.wallyBackground, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                //draw the food
                g.drawImage(this.wally, this.foodX- UNIT_SIZE/4, this.foodY- UNIT_SIZE/4, UNIT_SIZE+15, UNIT_SIZE+15, null);

                //draw the snake
                for (int i = 0; i < this.bodyParts; i++) {
                    if (i == 0) {//snake head
                        g.drawImage(this.wallyHead, this.x[i], this.y[i], UNIT_SIZE, UNIT_SIZE, null);
                    } else {
                        g.drawImage(this.wallyBody, this.x[i], this.y[i], UNIT_SIZE, UNIT_SIZE, null);
                    }
                }
            }
            else{
                //draw out the same stuff but using the loaded meme images
                g.drawImage(this.backgroundCat, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                //draw the food
                g.drawImage(this.foodImage, this.foodX - UNIT_SIZE/4, this.foodY - UNIT_SIZE/4, UNIT_SIZE+10, UNIT_SIZE+10, null);

                //draw the snake
                for (int i = 0; i < this.bodyParts; i++) {
                    if (i == 0) {//snake head
                        g.drawImage(this.snakeHead, this.x[i]-5, this.y[i]-5, UNIT_SIZE+10, UNIT_SIZE+10, null);
                    } else {
                        g.drawImage(this.snakeBody, this.x[i], this.y[i], UNIT_SIZE, UNIT_SIZE, null);
                    }
                }
            }

            //draw the current score, if game score beats current hiscore change to a new text.
            if(this.foodEaten > this.hiScore){
                g.setColor(Color.BLACK);
                g.setFont(new Font("Ink Free", Font.BOLD, 30));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("HISCORE: " + this.hiScore, 3 * (SCREEN_WIDTH - metrics.stringWidth("HISCORE: " + this.hiScore)) / 4, g.getFont().getSize());
                g.setColor(new Color(3, 45, 210));
                g.drawString("SCORE: " + this.foodEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + this.foodEaten)) / 4, g.getFont().getSize());
            }
            else {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Ink Free", Font.BOLD, 30));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("SCORE: " + this.foodEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + this.foodEaten)) / 4, g.getFont().getSize());
                g.drawString("HISCORE: " + this.hiScore, 3 * (SCREEN_WIDTH - metrics.stringWidth("HISCORE: " + this.hiScore)) / 4, g.getFont().getSize());
            }

        } else if (!this.running && this.initLoad) {
                //load up screen
                startScreen(g);
        }
        else {
            //game is over
            gameOver(g);
        }

    }

    //create the x and y co-ord of a new food and redraw it
    public void newFood(){
        this.foodX=random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE + UNIT_SIZE/4;
        this.foodY=random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE +UNIT_SIZE/4;
    }

    //convert snake head location to just a grid location
    private int snakeLocationToInteger(int snakePosition){
        return snakePosition/UNIT_SIZE;
    }

    //convert food location to just a grid location intger
    private int foodLocationToInteger(int foodPosition){
        return (foodPosition-UNIT_SIZE/4)/UNIT_SIZE;
    }

    //return distance between snakehead and food in the x direction.
    private int XColsAway(int xSnakeHead, int xFoodLocation){
        return Math.abs(xFoodLocation-xSnakeHead);
    }

    //return distance between snakehead and food in the y direction.
    private int yRowsAway(int ySnakeHead, int yFoodLocation){
        return Math.abs(yFoodLocation-ySnakeHead);
    }

    //return integer of +/-1 in if food location is in front of or behind current head location in the x direction (horizontal)
    private int relativeX(int xSnakeHead, int xFoodLocation){
        if (xSnakeHead==xFoodLocation){
            return 0;
        }else if (xSnakeHead>xFoodLocation){
            return -1;
        } else {
            return 1;
        }
    }

    //return integer of +/-1 in if food location is in front of or behind current head location in the y direction (vertical)
    private int relativeY(int ySnakeHead, int yFoodLocation){
        if (ySnakeHead==yFoodLocation){
            return 0;
        }else if (ySnakeHead>yFoodLocation){
            return -1;
        } else {
            return 1;
        }
    }

    //check is food is in a location one step away and return that direction
    private char foodOneSquareAway(int xSnakeHead, int ySnakeHead, int xFoodLocation, int yFoodLocation){
    if (xFoodLocation==xSnakeHead+1 && yFoodLocation==ySnakeHead){
        return 'R';
    }
    if (xFoodLocation==xSnakeHead-1 && yFoodLocation==ySnakeHead){
        return 'L';
    }
    if (yFoodLocation==ySnakeHead+1 && xFoodLocation==xSnakeHead){
        return 'U';
    }
    if (yFoodLocation==ySnakeHead-1 && xFoodLocation==xSnakeHead) {
        return 'D';
    }
    return 'O';
    }

    //function to check is next step will collide with the body given direction, snake head and snake body.
    //return true if there is a collision
    private boolean checkCollideWithSelf(char currentDirection, int[] xSnakeLocations, int[] ySnakeLocations){
        int xCurrentHead=snakeLocationToInteger(xSnakeLocations[0]);
        int yCurrentHead=snakeLocationToInteger(ySnakeLocations[0]);
        //check for the current direction if the next step forward will collide with the snake body
        switch (currentDirection) {
            case 'R':
                for (int i = 1; i < this.bodyParts; i++) {
                    if (xCurrentHead + 1 == snakeLocationToInteger(x[i])) {
                        return true;
                    }
                }
                break;
            case 'L':
                for (int i = 1; i < this.bodyParts; i++) {
                    if (xCurrentHead - 1 == snakeLocationToInteger(x[i])) {
                        return true;
                    }
                }
                break;
            case 'U':
                for (int i = 1; i < this.bodyParts; i++) {
                    if (yCurrentHead - 1 == snakeLocationToInteger(y[i])) {
                        return true;
                    }
                }
                break;
            case 'D':
                for (int i = 1; i < this.bodyParts; i++) {
                    if (yCurrentHead + 1 == snakeLocationToInteger(y[i])) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    //function that will auto move the snake head towards the food
    public void autoPlay(){
        /*System.out.println("----------------------------------");
        System.out.println("SnakeHead: x " +this.x[0] + " y "+ this.y[0]);
        System.out.println("SnakeHead: x " + snakeLocationToInteger(this.x[0]) + " y "+ snakeLocationToInteger(this.y[0]));
        System.out.println("Food location: x " + foodLocationToInteger(this.foodX) + " y "+ foodLocationToInteger(this.foodY));
        System.out.println("----------------------------------");

        //find the relative location of the food in relation to the snakes current head location.

         */
        int xRelativeLocation=relativeX(snakeLocationToInteger(this.x[0]), foodLocationToInteger(this.foodX));
        int yRelativeLocation=relativeY(snakeLocationToInteger(this.y[0]), foodLocationToInteger(this.foodY));
        System.out.println("Current direction " + this.direction);
        System.out.println("X relative food location : " + xRelativeLocation);
        System.out.println("Y relative food location : " + yRelativeLocation);
        System.out.println("----------------------------------");


        //make decisions depending on the location of the food in relation to the snake head.
        //if the snake is currently in the right row or column only change the perpendicular direction
        if (xRelativeLocation==0 || yRelativeLocation==0) {
            if (xRelativeLocation==0) {
                if (yRelativeLocation==1){
                    //if food is in the same row and is below the snake head
                    if (this.direction=='U'){
                        //don't allow a 180 turn and change to right or left first
                        this.direction='R';
                    } else {
                        this.direction = 'D';
                    }
                } else {
                    //if food is in the same row and is above the snake head
                    if (this.direction=='D'){
                        //don't allow a 180 turn and change to right or left first
                        this.direction='R';
                    } else {
                        this.direction = 'U';
                    }
                }
            } else {
                if (xRelativeLocation==1){
                    //if food is in the same column as the snake head and to the right
                    if (this.direction=='L'){
                        //don't allow a 180 turn and change to right or left first
                        this.direction='U';
                    } else {
                        this.direction = 'R';
                    }
                } else {
                    //if food is in the same column as the snake head and to the left
                    if (this.direction=='R'){
                        //don't allow a 180 turn and change to right or left first
                        this.direction='D';
                    } else {
                        this.direction = 'L';
                    }
                }
            }
        } else if (xRelativeLocation==1){
            //if food is to the right of the snake head move right
            if (this.direction=='L'){
                //don't allow a 180 turn and change to right or left first
                this.direction='U';
            } else {
                this.direction = 'R';
            }
        } else if (xRelativeLocation==-1){
            //if food is to the left of the snake head move right
            if (this.direction=='R'){
                //don't allow a 180 turn and change to right or left first
                this.direction='D';
            } else {
                this.direction = 'L';
            }
        }

        //check for collisions and if returns true change direction depending on current location to try to prevent collisions
        /*
        if (checkCollideWithSelf(this.direction, this.x, this.y)){
            switch (this.direction){
                case 'U':
                    //going up will collide, and you can't go back on yourself so chose a right/left
                    if(Math.random() >0.5){
                        this.direction='R';
                    } else {
                        this.direction='L';
                    }
                    break;
                case 'D':
                    //going down will collide, and you can't go back on yourself so chose a right/left
                    if(Math.random()>0.5){
                        this.direction='L';
                    } else {
                        this.direction='D';
                    }
                    break;
                case 'R':
                    //going right will collide, and you can't go back on yourself so chose an up/down
                    if(Math.random() >0.5){
                        this.direction='U';
                    } else {
                        this.direction='D';
                    }
                    break;
                case 'L':
                    //going left will collide, and you can't go back on yourself so chose an up/down
                    if(Math.random() >0.5){
                        this.direction='D';
                    } else {
                        this.direction='U';
                    }
                    break;
            }
        }
        */
    }

    //method to move the snake on the screen. Draw the heads new position while drawing each of the other
    //positions in the next position.
    public void move(){
        for (int i=this.bodyParts; i >0; i--){
            //iterate through entire snake updating its position previous value is now new value
            x[i]=x[i-1];
            y[i]=y[i-1];
        }

        // switch case for all the different directions the snake can move.
        switch (this.direction) {
            case 'U' -> {
                //System.out.println("UP movement");
                y[0] = y[0] - UNIT_SIZE;    //y value moves up by removing unit size
            }
            case 'D' -> {
                //System.out.println("DOWN movement");
                y[0] = y[0] + UNIT_SIZE;    //y value moves down by removing unit size
            }
            case 'R' -> {
                //System.out.println("RIGHT movement");
                x[0] = x[0] + UNIT_SIZE;    //y value moves up by removing unit size
            }
            case 'L' -> {
                //System.out.println("LEFT movement");
                x[0] = x[0] - UNIT_SIZE;    //y value moves up by removing unit size
            }
        }

    }

    //function to increase score and body size if head collides with the food
    public void checkFood(){
        if (x[0]==this.foodX - UNIT_SIZE/4  && y[0]==this.foodY- UNIT_SIZE/4 ){
            //System.out.println("FOOD EATEN");
            this.bodyParts++;
            this.foodEaten++;
            newFood();
        }
    }

    //checks for snake collinding with edge of the screen as well as its own body
    public void checkCollisions(){
        //check if head collides with the body
        for (int i=bodyParts; i>0; i--){
            if (x[0]==x[i] && y[0]==y[i]){
                //System.out.println("Collision detected");
                this.running=false;
            }
        }

        //check if head collides with the side of the screen
        if (x[0] < 0){
            //System.out.println("Collision detected");
            this.running=false;
        }
        if (x[0] > SCREEN_WIDTH){
            //System.out.println("Collision detected");
            this.running=false;
        }

        //check if head collides with the top/bottom of the screen
        if (y[0] < 0){
            //System.out.println("Collision detected");
            this.running=false;
        }
        if (y[0] > SCREEN_HEIGHT){
            //System.out.println("Collision detected");
            this.running=false;
        }

        //stop timer if collision detected and end game.
        if(!this.running){
            this.timer.stop();
        }

    }

    //draw a game over screen with score
    public void gameOver(Graphics g){
        //game over background image
        g.drawImage(this.gameoverBackground, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        //display game over information.
        if (this.foodEaten > this.hiScore) {
            g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT/2-(g.getFont().getSize()));
            g.setFont(new Font("Ink Free", Font.BOLD, 55));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("New HISCORE", (SCREEN_WIDTH - metrics2.stringWidth("New HISCORE"))/2, SCREEN_HEIGHT/2);
            g.drawString("Score: " + this.foodEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + this.foodEaten))/2, SCREEN_HEIGHT/2 + (g.getFont().getSize()));
            g.drawString("Press space to restart", (SCREEN_WIDTH - metrics2.stringWidth("Press space to restart"))/2, SCREEN_HEIGHT-g.getFont().getSize());
        } else {
            g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT/2-(g.getFont().getSize()));
            g.setFont(new Font("Ink Free", Font.BOLD, 55));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Score: " + this.foodEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + this.foodEaten)) / 2, SCREEN_HEIGHT /2 + g.getFont().getSize());
            g.drawString("Press space to restart", (SCREEN_WIDTH - metrics2.stringWidth("Press space to restart"))/2, SCREEN_HEIGHT-g.getFont().getSize());
        }
    }

    //draw a screen on load up
    public void startScreen(Graphics g){
        //display start screen
        g.drawImage(this.startScreen, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }

    //reset all variables needed to begin a new game
    public void resetGame(){
        //System.out.println("Reset GAME");
        //reset game variables
        this.timer.stop();
        this.x = new int[GAME_UNITS];    //will hold the x-value of the snakes body
        this.y = new int[GAME_UNITS];    //will hold the y-value of the snakes body
        this.bodyParts=5;       //initial size of the snake
        if (this.foodEaten > this.hiScore) {
            this.hiScore = this.foodEaten;  //set new hiscore if the food eaten is bigger than last hiscore
        }

        this.foodEaten=0;          //players score
        this.direction = 'R';   //initial start the snake will move to the right]
        this.running=false;     //game is running true or false

        startGame();
    }

    public void enterMemeMode(){
        //change visuals for a meme mode.
        if (!this.memeMode) {
            this.memeMode = true;
        } else {
            this.memeMode=false;
            this.hardMode=false;
        }
    }

    public void enterHardMode(){
        //change visuals for a meme mode.
        if (!this.hardMode) {
            this.hardMode = true;
        } else {
            this.hardMode=false;
            this.memeMode=false;
        }
    }

    public void increaseSnakeSpeed(){
        if(this.DELAY>20) {
            this.timer.stop();
            this.DELAY -= 10;
            this.timer = new Timer(this.DELAY, this);
            this.timer.start();
        }
    }

    public void decreaseSnakeSpeed(){
        if(this.DELAY<300) {
            this.timer.stop();
            this.DELAY += 10;
            this.timer = new Timer(this.DELAY, this);
            this.timer.start();
        }
    }

    //main method that will contain the game loop.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.running){
            move();
            checkFood();
            checkCollisions();
            autoPlay();
        }
        repaint();

    }

    public class myKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                    //to prevent 180 direction changes here check if they aren't already going right before changing.
                    if (direction != 'R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                    //to prevent 180 direction changes here check if they aren't already going left before changing.
                    if (direction != 'L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP, KeyEvent.VK_W:
                    //to prevent 180 direction changes here check if they aren't already going down before changing.
                    if (direction != 'D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                    //to prevent 180 direction changes here check if they aren't already going up before changing.
                    if (direction != 'U'){
                        direction='D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (initLoad) {
                        startGame();
                    } else {
                        resetGame();
                    }
                    break;
                case KeyEvent.VK_M:
                    //enter meme mode
                    enterMemeMode();
                    break;
                case KeyEvent.VK_H:
                    enterHardMode();
                    break;

                case KeyEvent.VK_P:
                    //increase speed of snake
                    //System.out.println("P pressed");
                    increaseSnakeSpeed();
                    break;
                case KeyEvent.VK_L:
                    //decrease speed of snake
                    //System.out.println("L pressed");
                    decreaseSnakeSpeed();
                    break;
            }

        }
    }
}

