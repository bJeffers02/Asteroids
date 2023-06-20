import java.awt.Color;
import java.awt.Graphics2D;

public class SpaceShip {
    
    GamePanel gP;
    keyHandler eH;
    
    Double playerAngle = 0.0;
    Double rotateSpeed = 4.5;
    int playerHealth = 20;

    public SpaceShip(GamePanel gP, keyHandler eH){
        this.gP = gP;
        this.eH = eH;
    }

    //UPDATE ANGLE OF SPACESHIP
    public void update(){

        //IF LEFT ARROW KEY IS HELD DOWN, SPACESHIP ROTATES LEFT
        if(eH.rotateLeft == true){
            playerAngle -= rotateSpeed;
        }

        //IF RIGHT ARROW KEY IS HELD DOWN, SPACESHIP ROTATES RIGHT
        if(eH.rotateRight == true){
            playerAngle += rotateSpeed;
        }

        //IF SPACE BAR IS PRESSED, ONE BULLET IS CREATED
        if(eH.shoot == true){
            gP.bullets.add(new Bullet(this.playerAngle, gP));
            eH.shoot = false;
        }
    }

    //DRAW SPACESHIP
    public void draw(Graphics2D g2d){
        g2d.setColor(Color.white);
        
        g2d.rotate(Math.toRadians(playerAngle), (gP.screenWidth/2), (gP.screenHeight/2));
        g2d.drawPolygon(new int[] {(gP.screenWidth/2)-(24), (gP.screenWidth/2), (gP.screenWidth/2)+(24)}, new int[] {(gP.screenHeight/2)+(24)-7, (gP.screenHeight/2)-(24)-7, (gP.screenHeight/2)+(24)-7}, 3);
    }
}
