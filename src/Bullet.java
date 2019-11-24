import java.awt.*;

public class Bullet extends Rectangle {
    public int positionTargetX;
    public int positionTargetY;

    public int speedBullet;

    public int idBullet;

    public Bullet(int airID, int idEnemy, int positionBulletX, int positionBulletY,int bulletSize,int speedBullet){
        super(positionBulletX,positionBulletY,bulletSize,bulletSize);
        this.speedBullet = speedBullet;
        this.idBullet = idEnemy;
    }

    public void update(){
        try {

        } catch(Exception e){}
    }
}
