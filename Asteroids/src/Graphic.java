import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;



public class Graphic extends Canvas implements MouseListener, MouseMotionListener, KeyListener{
    
    int positionX = 200, positionY = 200;
    double angle;
    int x, y;

    ArrayList<Asteroid> asteroids = new ArrayList<>();

    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            for(Asteroid ast : asteroids){
                ast.positionX += ast.velocityX;
                ast.positionY += ast.velocityY;
                if(asteroids.size() > 0){
                    paintComponent(getGraphics());
                }
            }
        }
    };
    Timer timer = new Timer(100, taskPerformer);
        
    public Graphic(){
        setBackground(Color.WHITE);
        setSize(400, 400);

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        asteroids.add(new Asteroid());
        asteroids.add(new Asteroid());
        asteroids.add(new Asteroid());
        asteroids.add(new Asteroid());

    }

    public void paintComponent(Graphics g) {
        
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(getBackground());
        g2d.fillOval(positionX-35, positionY-35, 70, 70);

        g2d.rotate(-angle , positionX, positionY);
        
        g2d.setColor(Color.BLUE);

        if(y < positionY){
            g2d.fillPolygon(new int[] {positionX-20, positionX, positionX+20}, new int[] {positionY+15, positionY-25, positionY+15}, 3);
        }
        if(y >= positionY){
            g2d.fillPolygon(new int[] {positionX-20, positionX, positionX+20}, new int[] {positionY-15, positionY+25, positionY-15}, 3);
        }

        g2d.dispose();


        for(Asteroid ast : asteroids){
            g2d = (Graphics2D)g.create();
            g2d.setColor(getBackground());
            g2d.fillRect(ast.positionX-(ast.velocityX+10), ast.positionY-(ast.velocityY+10), ast.size+15, ast.size+15);
            g2d.setColor(Color.BLUE);
            g2d.drawRect(ast.positionX, ast.positionY, ast.size, ast.size);
            g2d.clearRect(ast.positionX, ast.positionY-10 , 20, 10);
            g2d.drawString("" + ast.health, ast.positionX, ast.positionY);  
            g2d.dispose();  
        }

        Iterator<Asteroid> aste = asteroids.iterator();
        while(aste.hasNext()){
            Asteroid a = aste.next();
            if(a.health <= 0){
                g2d = (Graphics2D)g.create();
                g2d.setColor(getBackground());
                g2d.fillRect(a.positionX, a.positionY-10 , a.size + 10, a.size + 11 );
                g2d.dispose();
                aste.remove();
            }
        }

        if(asteroids.size() == 0){
            g2d = (Graphics2D)g.create();
            g2d.setColor(getBackground());
            g2d.fillRect(100, 100, 200, 200);
            g2d.setColor(Color.BLUE);
            g2d.drawString("Game Won!", 150, 200);
            g2d.dispose();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        angle = Math.atan((double)(x-positionX)/(y-positionY));
        //System.out.println(Math.toDegrees(-angle));
        if(asteroids.size() > 0){
            paintComponent(getGraphics());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 65){
            positionX-=5;
        }
        if(e.getKeyCode() == 68){
            positionX+=5;
        }
        if(e.getKeyCode() == 83){
            positionY+=5;
        }
        if(e.getKeyCode() == 87){
            positionY-=5;
        }
        //System.out.println(positionX + " " + positionY);
        if(asteroids.size() > 0){
            paintComponent(getGraphics());
        }
    }




    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x, y;
        x = e.getX();
        y = e.getY();
        for(Asteroid ast : asteroids){
            if((x >= ast.positionX && x <= ast.positionX + ast.size) && (y >= ast.positionY && y <= ast.positionY + ast.size)){
                ast.health -= 5;
            }
        }
        timer.start();
        if(asteroids.size() > 0){
            paintComponent(getGraphics());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
