import java.util.Random;

public class Asteroid {
    Random random = new Random();
    int positionX, positionY;
    int size;
    int velocityX, velocityY;
    int health;


    public Asteroid(){
        positionX = random.nextInt(400);
        positionY = random.nextInt(400);
        size = random.nextInt(35)+10;
        health = size/2 + 1;

        velocityX = random.nextInt(5)-2;
        velocityY = random.nextInt(5)-2;
    }
}
