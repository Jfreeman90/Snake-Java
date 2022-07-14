import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    //Constructor
    GameFrame(){
        this.add(new GamePanel());
        this.setTitle("Snake - Java");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable((false));
        this.setVisible(true);
        //this.setLocationRelativeTo(null);
        ImageIcon snakeLogo = new ImageIcon("download.jpg");
        this.setIconImage(snakeLogo.getImage());
        this.pack();
    }
}
