import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;

public class Asteroid {
    Random random = new Random();
    double positionX, positionY;
    int size;
    double velocityX, velocityY;
    int health;

    double angle;

    GamePanel gP;

    //SHAPE OF ASTEROID
    int[] shapeX;
    int[] shapeY;

    //SHAPE OF ASTEROID PLUS POSITION
    int[] pointX;
    int[] pointY;

    //CONSTRUCTOR FOR SPAWNED ASTEROIDS
    public Asteroid(GamePanel gP){
        this.gP = gP;
        
        size = random.nextInt(40)+16;
        health = size/2 + 1;

        setPosition();
        setVelocity();
        randomShapeGenerator();
    }

    //CONSTRUCTOR FOR OFFSPRING ASTEROIDS THAT SPAWN WHEN LARGER ASTEROIDS DIE
    public Asteroid(GamePanel gP, int oldSize, double positionX, double positionY, double velocityX, double velocityY){
        this.gP = gP;

        size = oldSize/2 + random.nextInt(7)-3;
        health = size/2 + 1;

        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        randomShapeGenerator();
    }

    //SETS ASTEROID POSITION TO EDGES OF THE SCREEN
    public void setPosition(){
        switch(random.nextInt(4)){
            case 0:
                positionX = -size-10;
                positionY = random.nextInt(gP.screenHeight);
                break;
            case 1:
                positionX = gP.screenWidth + size;
                positionY = random.nextInt(gP.screenHeight);
                break;
            case 2:
                positionX = random.nextInt(gP.screenWidth);
                positionY = -size-10;
                break;
            case 3:
                positionX = random.nextInt(gP.screenWidth);
                positionY = gP.screenHeight + size;
                break;
        }
    }

    //SETS VELOCITY OF ASTEROID TO TRAVEL TO CENTER OF THE SCREEN
    public void setVelocity(){
        velocityX = (gP.screenWidth/2 - positionX) / 600;
        velocityY = (gP.screenHeight/2 - positionY) / 600;
    }

    //CREATES A RANDOM PSEUDO-CIRCULAR POLYGON
    //SOURCE: https://gamedev.stackexchange.com/questions/180096/how-do-you-draw-a-random-irregular-polygon-in-java-asteroids
    public void randomShapeGenerator(){
        double angleStep = Math.PI * 2 / (size);
        double targetAngle;
        double angle;
        double radius;
        double x, y;
        
        shapeX = new int[size];
        shapeY = new int[size];

        for(int i = 0; i < size; ++i) {
            targetAngle = angleStep * i; 
            angle = targetAngle + (random.nextDouble() - 0.5) * angleStep * 0.25; 
            radius = size/2 + random.nextDouble() * 7; 
            x = Math.cos(angle) * radius; 
            y = Math.sin(angle) * radius;
            shapeX[i] = (int)(x);
            shapeY[i] = (int)(y);
        }
    }

    //UPDATE POSITION OF ASTEROID
    public void update(){
        positionX += (velocityX);
        positionY += (velocityY);
        pointX = new int[size];
        pointY = new int[size];
        for(int i = 0; i < size; ++i) {
            pointX[i] = (int)Math.round(shapeX[i] + positionX);
            pointY[i] = (int)Math.round(shapeY[i] + positionY);
        }
    }

    //DRAW ASTEROID
    public void draw(Graphics2D g2d){
        g2d.setColor(Color.white);
        g2d.drawPolygon(pointX, pointY, size);
    }
}