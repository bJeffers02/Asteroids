import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;
import java.awt.Font;
import java.awt.FontMetrics;

public class GamePanel extends JPanel implements Runnable{
    
    //WINDOW SIZE SETTINGS
    final int screenWidth = 768; 
    final int screenHeight = 576;

    JLabel debugLabel;

    int frameCounter = 0;
    int FPS = 60;

    int spawnInterval = 300;
    int mininumSpawnInterval = 10;

    keyHandler eH = new keyHandler();
    
    Thread gameThread;

    Random rand = new Random();

    //INITIALIZE SPACESHIP OBJECTS
    SpaceShip ship = new SpaceShip(this, eH);

    //HOLDS ALL ASTEROIDS
    ArrayList<Asteroid> asteroids = new ArrayList<>();

    //HOLDS ALL BULLETS
    ArrayList<Bullet> bullets = new ArrayList<>();

    int score = 0;

    Font myFont1;

    //GAME PANEL CONSTRUCTOR
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(eH);
        this.setFocusable(true);
        debugLabel = new JLabel(""+score);
        debugLabel.setOpaque(true);
        debugLabel.setForeground(Color.white);
        debugLabel.setBackground(Color.black);
    }

    //STARTS GAME THREAD
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
        eH.pause = true;
    }

    //GAME LOOP
    //SOURCE https://www.youtube.com/watch?v=VpH33Uw-_0E
    @Override
    public void run() {

        //CALCULATE TIME BETWEEN REDRAWS AND TIME OF NEXT REDRAW
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){

            //UPDATE POSITOINS OF OBJECTS 
            update();

            //REDRAW OBJECTS WITH UPDATED POSITONS 
            repaint();
            
            if(ship.playerHealth <= 0){
                break;
            }
            
            try {
                //CALCULATE TIME UNTIL NEXT REDRAW
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                //IF TIME UNTIL NEXT REDRAW IS LESS THAN 0, IT IS SET TO 0
                if(remainingTime < 0){
                    remainingTime = 0;
                }

                //PAUSE THREAD UNTIL THE TIME OF NEXT REDRAW
                Thread.sleep((long)remainingTime);

                //SET TIME OF NEXT REDRAW
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            

        }
    }

    //UPDATE POSITIONS AND COLLISIONS OF OBJECTS DRAWN ONTO THE SCREEN
    public void update(){

        debugLabel.setText("" + spawnInterval + " " + (frameCounter % spawnInterval));
        //UPDATES ROTATION ANGLE OF SHIP
        ship.update();

        //IF THE GAME IS PAUSED, ASTEROIDS WILL NOT UPDATE
        if(eH.pause == false){
            //COUNTS THE NUMBER OF FRAMES IN THE GAME SO FAR
            ++frameCounter;

            //REDUCES THE SPAWN INTERVAL AS THE GAME GOES ON, AS LONG AS THE SPAWN INTERVAL IS HIGHER THAN THE MINIMUM 
            if(spawnInterval > mininumSpawnInterval && frameCounter % 120 == 0){
                --spawnInterval;
            }

            //SPAWNS NEW ASTEROIDS AT THE SPECIFIED RATE
            if(frameCounter % spawnInterval == 0){
                asteroids.add(new Asteroid(this));
            }

            //CHECKS IF EACH BULLET HAS HIT AN ASTEROID, IF IT HAS, BULLET IS DELETED AND ASTEROID HEALTH IS DECREASED BY 5
            for(Asteroid ast : asteroids){
                for(Bullet bull : bullets){
                    if(bull.positionX > ast.positionX - ast.size/2 && bull.positionX < ast.positionX + ast.size - ast.size/2 && bull.positionY > ast.positionY - ast.size/2 && bull.positionY < ast.positionY + ast.size - ast.size/2){
                        ast.health -= 5;
                        bull.valid = false;
                    }
                }
            }

            //ADDS BETWEEN 1 AND 4 ASTEROIDS OF SMALLER SIZE WHEN A LARGE ASTEROID IS DESTROYED
            ListIterator<Asteroid> aste1 = asteroids.listIterator();
            while(aste1.hasNext()){
                Asteroid a = aste1.next();
                if(a.health <= 0 && a.size > 35){
                    int rn = rand.nextInt(4);
                    if(rn >= 0){
                        aste1.add(new Asteroid(this, a.size, a.positionX-2, a.positionY-2, a.velocityX, a.velocityY));
                    }
                    if(rn >= 1){
                        aste1.add(new Asteroid(this, a.size, a.positionX - 2 , a.positionY + a.size/2 + 2, a.velocityX, a.velocityY));
                    }
                    if(rn >= 2){
                        aste1.add(new Asteroid(this, a.size, a.positionX + a.size/2 + 2, a.positionY-2, a.velocityX, a.velocityY));
                    }
                    if(rn >= 3){
                        aste1.add(new Asteroid(this, a.size, a.positionX + a.size/2 + 2, a.positionY + a.size/2 + 2, a.velocityX, a.velocityY));
                    }
                }
            }
        
            //CHECKS IF EACH ASTEROID HAS HIT THE SPACESHIP, IF SO, ASTEROID IS DELETED AND SHIP HEALTH IS DECREASED BY ASTEROID SIZE DIVIDED BY 10
            //todo simplify equations
            Iterator<Asteroid> aste2 = asteroids.iterator();
            while(aste2.hasNext()){
                Asteroid a = aste2.next();
                if((a.positionX - a.size/2 < screenWidth/2 + 25 && a.positionX - a.size/2 > screenWidth/2-25 && a.positionY - a.size/2< screenHeight/2 + 25 && a.positionY  - a.size/2> screenHeight/2 - 25)
                    ||(a.positionX + a.size - a.size/2< screenWidth/2 +25 && a.positionX + a.size - a.size/2 > screenWidth/2-25 && a.positionY + a.size - a.size/2 < screenHeight/2 + 25 && a.positionY + a.size - a.size/2 > screenHeight/2 - 25)
                    ||(a.positionX + a.size - a.size/2< screenWidth/2 + 25 && a.positionX + a.size - a.size/2> screenWidth/2-25 && a.positionY - a.size/2 < screenHeight/2 + 25 && a.positionY - a.size/2 > screenHeight/2 - 25)
                    ||(a.positionX - a.size/2 < screenWidth/2 +25 && a.positionX - a.size/2 > screenWidth/2-25 && a.positionY  + a.size - a.size/2 < screenHeight/2 + 25 && a.positionY  + a.size - a.size/2 > screenHeight/2 - 25)){
                    ship.playerHealth -= Math.ceil(a.size/10); 
                    aste2.remove();
                    
                }
            }

            //IF ASTEROID HEALTH IS BELOW OR EQUAL TO 0, ASTEROID IS REMOVED
            Iterator<Asteroid> aste3 = asteroids.iterator();
            while(aste3.hasNext()){
                Asteroid a = aste3.next();
                if(a.health <= 0){
                    aste3.remove();
                    score += a.size;
                }
            }

            //UPDATE POSITON OF ASTEROIDS
            for(Asteroid ast : asteroids){
                ast.update();
            }
        }
        
        //IF BULLET HAS HIT AN ASTEROID OR FLIES OFFSCREEN, BULLET IS DELETED
        Iterator<Bullet> bul = bullets.iterator();
        while(bul.hasNext()){
            Bullet b = bul.next();
            if(b.valid == false){
                bul.remove();
            }
        }

        //UPDATES POSITON OF BULLETS
        for(Bullet bull : bullets){
            bull.update();
        }
    }

    //DRAWS OBJECTS AT UPDATED POSITIONS
    public void paintComponent(Graphics g){
        
        //PAINT OVER LAST FRAME 
        super.paintComponent(g);

        //CREATE NEW GRAPHICS2D OBJECT TO DRAW SHIP AT CORRECT ANGLE
        Graphics2D g2d = (Graphics2D)g.create();

        //DRAW SHIP
        ship.draw(g2d);
        
        //RESET ROTATION TO DRAW ASTEROIDS AND BULLETS
        g2d.dispose();
        g2d = (Graphics2D)g.create();
        
        //DRAW BULLETS
        for(Bullet bull : bullets){
            bull.draw(g2d);
        }
        
        //IF THE GAME IS PAUSED, ASTEROIDS WILL NOT BE DRAWN
        if(eH.pause == false){

            //DRAWS ASTEROIDS
            for(Asteroid ast : asteroids){
                ast.draw(g2d);
            }

            //DRAWS HEALTH AND SCORE
            myFont1 = new Font("Default", Font.PLAIN, 17);
            g2d.setFont(myFont1);
            g2d.setColor(Color.white);
            g2d.drawString("Health: " + ship.playerHealth, 5, 20);
            
            g2d.drawString("Score: " + score, screenWidth - 100, 20);
        }
        
        //DRAWS INSTRUCTIONS ON HOW TO PLAY WHEN GAME IS PAUSED
        else{
            String text = "Press Q to Play";
            
            //CREATES NEW FONT
            myFont1 = new Font("Default", Font.PLAIN, 22);
            FontMetrics metrics = g2d.getFontMetrics(myFont1);
            
            //DRAW INSTRUCTION ON HOW TO START THE GAME ON LOWER CENTER OF PANEL
            g2d.setFont(myFont1);
            g2d.setColor(Color.white);
            g2d.drawString(text, (screenWidth - metrics.stringWidth(text)) / 2, ((screenHeight - metrics.getHeight()) / 2) + metrics.getAscent() + 100);
            
            //todo simplify equations
            //DRAW SPACE BAR   
            g2d.drawRect(screenWidth/4 - 150/2, screenHeight/4 - 25, 150, 35);
            g2d.drawPolygon(new int[]{screenWidth/4 - 150/2 - 15, screenWidth/4 - 150/2, screenWidth/4 + 150/2, screenWidth/4 + 150/2 + 15},new int[]{screenHeight/4 + 25, screenHeight/4 + 10, screenHeight/4 + 10, screenHeight/4 + 25,}, 4);
            g2d.drawPolygon(new int[]{screenWidth/4 - 150/2 - 15, screenWidth/4 - 150/2 - 15, screenWidth/4 - 150/2, screenWidth/4 - 150/2}, new int[]{screenHeight/4 + 25, screenHeight/4 - 10, screenHeight/4 - 25, screenHeight/4 + 10}, 4);
            g2d.drawPolygon(new int[]{screenWidth/4 - 150/2 - 15 + 165, screenWidth/4 - 150/2 - 15 + 165, screenWidth/4 - 150/2 + 165, screenWidth/4 - 150/2 + 165}, new int[]{screenHeight/4 + 10, screenHeight/4 - 25,screenHeight/4 - 10, screenHeight/4 + 25}, 4);
            g2d.drawString("space", screenWidth/4 - 150/2 + 10, screenHeight/4);
            
            //DRAWS INSTRUCTION ON HOW TO SHOOT BULLETS
            g2d.drawString("SHOOT", screenWidth/4 - 150/2 + (150 - metrics.stringWidth("SHOOT")) / 2, screenHeight/4 + 65);
            

            //todo simplify
            //DRAW ARROW KEYS
            g2d.drawRect(screenWidth*3/4 - 150/2 + 20, screenHeight/4 - 25, 35, 35);
            g2d.drawRect(screenWidth*3/4 - 150/2 + 95, screenHeight/4 - 25, 35, 35);
            g2d.drawPolygon(new int[]{screenWidth*3/4 - 150/2 - 15 + 20, screenWidth*3/4 - 150/2 - 15 +20, screenWidth*3/4 - 150/2 +20, screenWidth*3/4 - 150/2 +20}, new int[]{screenHeight/4 + 25, screenHeight/4 - 10, screenHeight/4 - 25, screenHeight/4 + 10}, 4);
            g2d.drawPolygon(new int[]{screenWidth*3/4 - 150/2 - 15 + 95, screenWidth*3/4 - 150/2 - 15 +95, screenWidth*3/4 - 150/2 +95, screenWidth*3/4 - 150/2 +95}, new int[]{screenHeight/4 + 25, screenHeight/4 - 10, screenHeight/4 - 25, screenHeight/4 + 10}, 4);
            g2d.drawPolygon(new int[]{screenWidth*3/4 - 150/2 - 15 + 145, screenWidth*3/4 - 150/2 - 15 + 145, screenWidth*3/4 - 150/2 + 145, screenWidth*3/4 - 150/2 + 145}, new int[]{screenHeight/4 + 10, screenHeight/4 - 25,screenHeight/4 - 10, screenHeight/4 + 25}, 4);
            g2d.drawPolygon(new int[]{screenWidth*3/4 - 150/2 - 15 + 70, screenWidth*3/4 - 150/2 - 15 + 70, screenWidth*3/4 - 150/2 + 70, screenWidth*3/4 - 150/2 + 70}, new int[]{screenHeight/4 + 10, screenHeight/4 - 25,screenHeight/4 - 10, screenHeight/4 + 25}, 4);
            g2d.drawPolygon(new int[]{screenWidth*3/4 - 150/2 - 15 + 20, screenWidth*3/4 - 150/2 + 20, screenWidth*3/4 -20, screenWidth*3/4 +15-20},new int[]{screenHeight/4 + 25, screenHeight/4 + 10, screenHeight/4 + 10, screenHeight/4 + 25,}, 4);
            g2d.drawPolygon(new int[]{screenWidth*3/4 - 150/2 - 15 + 20 + 75, screenWidth*3/4 - 150/2 + 20 + 75, screenWidth*3/4 -20+ 75, screenWidth*3/4 +15-20+ 75},new int[]{screenHeight/4 + 25, screenHeight/4 + 10, screenHeight/4 + 10, screenHeight/4 + 25,}, 4);
            g2d.drawString("<-", screenWidth*3/4 - 150/2 + 28, screenHeight/4);
            g2d.drawString("->", screenWidth*3/4 - 150/2 + 28 + 75, screenHeight/4);
            
            //DRAWS INSTRUCTION ON HOW TO ROTATE SHIP
            g2d.drawString("ROTATE", screenWidth/4 * 3 - 150/2 + (150 - metrics.stringWidth("ROTATE")) / 2, screenHeight/4 + 65);
        }
        
        g2d.dispose();

        //WHEN SHIP HEALTH IS 0 OR LESS, DEATH SCREEN IS DRAWN WITH FINAL SCORE
        if(ship.playerHealth <= 0){
            super.paintComponent(g);
            g2d = (Graphics2D)g.create();
            g2d.setColor(Color.white);
            Font myFont = new Font("Default", 1, 50);
            g2d.setFont(myFont);
            g2d.drawString("Game Over", screenWidth/2 - 125, screenHeight/2);
            g2d.drawString("Score: " + score, screenWidth/2 - 120, screenHeight/2 + 50);
        }
    }
}   