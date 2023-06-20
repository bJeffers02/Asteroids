import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Main {
    public static void main(String[] args) throws Exception {
        
        //INITIALIZE FRAME
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Asteroids");

        //INITIALIZE GAME PANEL
        GamePanel gP = new GamePanel();
        frame.add(gP, BorderLayout.CENTER);

        //DEBUG LABEL, CAN BE USED TO SHOW VARIABLE STATUS
        frame.add(gP.debugLabel, BorderLayout.NORTH);

        //SIZE FRAME TO FIT GAME PANEL
        frame.pack();
        frame.setResizable(false);
        
        frame.setLocation(200, 100);
        frame.setVisible(true);
        
        //START GAME
        gP.startGameThread();
    }
}