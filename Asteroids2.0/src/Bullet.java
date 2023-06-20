import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Bullet {
    GamePanel gP;

    Random rand = new Random();
    
    double positionX, positionY;
    double velocityX, velocityY;

    Color randomColor;

    boolean valid = true;

    public Bullet(double angle, GamePanel gP){
        this.gP = gP;
        
        //SET POSITION RELATIVE TO THE ANGLE OF THE SPACESHIP
        positionX = (30 * Math.cos(Math.toRadians(angle-90))) + gP.screenWidth/2;
        positionY = (30 * Math.sin(Math.toRadians(angle-90))) + gP.screenHeight/2;

        //SET VELOCITY TO TRAVEL AWAY FROM THE SPACESHIP WITH AN ADDED RANDOM ELEMENT
        velocityX = (positionX - gP.screenWidth/2) / 5 + (rand.nextDouble()-0.5)/1.5;
        velocityY = (positionY - gP.screenHeight/2) / 5 + (rand.nextDouble()-0.5)/1.5;

        //CREATE A RANDOM RGB VALUE FOR THE COLOR OF THE BULLET
        randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    //UPDATE POSITION OF BULLET
    public void update(){

        //ADD X AND Y COMPONENT OF VELOCITY TO X AND Y COMPONENT OF POSITION
        positionX += (velocityX);
        positionY += (velocityY);

        //IF BULLET IS OUTSIDE OF THE SCREEN IT IS SET TO NOT VALID
        if(positionX > gP.screenWidth || positionX < 0 || positionY > gP.screenHeight || positionY < 0){
            valid = false;
        }
    }

    //DRAW BULLET
    public void draw(Graphics2D g2d){
        g2d.setColor(randomColor);
        g2d.drawRect((int)Math.round(positionX), (int)Math.round(positionY), 1, 1);
    }
}