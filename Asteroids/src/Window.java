import javax.swing.*;

public class Window extends JFrame{
    Graphic gr = new Graphic();
    
    public Window(){
        super("Asteroids");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        add(gr);

        pack();
    }
}
