import java.awt.event.*;

public class keyHandler implements KeyListener{

    public boolean rotateLeft, rotateRight, shoot, pause;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //SETS ROTATION BOOLEANS TO TRUE WHEN ARROW KEY IS PRESSED
        if(code == 37){
            rotateLeft = true;
        }
        else if(code == 39){
            rotateRight = true;
        }

        //SETS SHOOT BOOLEAN TO TRUE WHEN SPACEBAR IS PRESSED
        else if(code == 32){
            shoot = true;
        }

        //PAUSES AND UNPAUSES GAME WHEN Q IS PRESSED
        else if(code == 81){
            pause = !pause;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        //SETS ROTATION BOOLEANS TO FALSE WHEN KEY IS RELEASED
        if(code == 37){
            rotateLeft = false;
        }
        else if(code == 39){
            rotateRight = false;
        }
    }
    
}
