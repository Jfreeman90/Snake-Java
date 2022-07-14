import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.Color;

public class GamePanel extends JPanel implements ActionListener {
    //attributes
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;      //size of each invisible grid.
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; //total size of array squares available
    static final int DELAY =125;     //game speed
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
    int hiScore=0;        //A given highscore for each load up

    //Constructor
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        //startGame(); //change to starting on a space bar press

    }

    public void startGame(){
        newFood();
        //game is now running
        this.running=true;
        this.timer = new Timer(DELAY, this);
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
            //draw game grid.
            /*
            for (int i=0; i< SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }*/

            //draw the food
            g.setColor(Color.YELLOW);
            g.fillOval(this.foodX, this. foodY, UNIT_SIZE/2, UNIT_SIZE/2);

            //draw the snake
            for(int i=0; i< this.bodyParts; i++){
                if(i==0){//snake head
                    g.setColor(Color.PINK);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor((Color.MAGENTA));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //draw the current score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: "+this.foodEaten, (SCREEN_WIDTH - metrics.stringWidth( "SCORE: "+ this.foodEaten) ) /4, g.getFont().getSize());
            g.drawString("HISCORE: "+this.hiScore, 3*(SCREEN_WIDTH - metrics.stringWidth( "HISCORE: "+ this.hiScore) ) /4, g.getFont().getSize());

            //draw the timer.



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
            timer.stop();
        }

    }

    public void gameOver(Graphics g){
        //display game over information.
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        g.drawString("Score: "+this.foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+this.foodEaten))/2, SCREEN_HEIGHT/2+g.getFont().getSize());
    }

    public void startScreen(Graphics g){
        //display game over information.
        g.setColor(Color.GREEN);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to begin!", (SCREEN_WIDTH - metrics.stringWidth("Press SPACE to begin!"))/2, SCREEN_HEIGHT/2);
    }

    public void resetGame(){
        System.out.println("Reset GAME");
        //reset game variables
        this.x = new int[GAME_UNITS];    //will hold the x-value of the snakes body
        this.y = new int[GAME_UNITS];    //will hold the y-value of the snakes body
        this.bodyParts=5;       //initial size of the snake
        if (this.foodEaten > this.hiScore) {
            this.hiScore = this.foodEaten;  //set new hiscore if the food eaten is bigger than last hiscore
        }

        this.foodEaten=0;          //players score
        this.direction = 'R';   //initial start the snake will move to the right
        this.running=false;     //game is running true or false

        startGame();
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
                case KeyEvent.VK_LEFT:
                    //to prevent 180 direction changes here check if they aren't already going right before changing.
                    if (direction != 'R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    //to prevent 180 direction changes here check if they aren't already going left before changing.
                    if (direction != 'L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    //to prevent 180 direction changes here check if they aren't already going down before changing.
                    if (direction != 'D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
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
            }

        }
    }
}
