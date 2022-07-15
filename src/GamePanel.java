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
    int DELAY=75;      //game speed (HIGHER = SLOWER)
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

    public void startGame(){
        newFood();
        //game is now running
        this.running=true;
        this.timer = new Timer(this.DELAY, this);
        this.timer.start();
        this.initLoad=false;

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //if running
        if (this.running){
            /*draw game grid.
            for (int i=0; i< SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
             */

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

    public void newFood(){
        //create the x and y co-ord of a new food and redraw it
        //some checks for snake body and so on
        this.foodX=random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE + UNIT_SIZE/4;
        this.foodY=random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE +UNIT_SIZE/4;
    }

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

    public void checkFood(){
        if (x[0]==this.foodX - UNIT_SIZE/4  && y[0]==this.foodY- UNIT_SIZE/4 ){
            //System.out.println("FOOD EATEN");
            this.bodyParts++;
            this.foodEaten++;
            newFood();
        }
    }

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

    public void startScreen(Graphics g){
        //display start screen
        g.drawImage(this.startScreen, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }

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
        this.DELAY=100;
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
            //System.out.println(this.foodX + "," + this.foodY);
            //System.out.println(x[0] + "," + y[0]);
            //System.out.println(timer);
            checkFood();
            checkCollisions();
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

