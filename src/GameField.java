import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class GameField extends JPanel implements Runnable {
    public Thread thread = new Thread(this);
    public int indexImageEnemy = 1;
    public int level =1;


    menuGame button = new menuGame();

    public static Image[] tileset_ground = new Image[100];
    public static Image[] tileset_air = new Image[100];
    public static Image[] tileset_res = new Image[100];
    public static Image[] tileset_mob = new Image[100];

    public static int coinage = 5000;
    public static int health = 10000; // hp mình

    public static int killed =0,maxLevel=10;
    public static int winTime =2000,winFrame = 0;
    public static int killToWin = 61;

    public static boolean isFirst = true;
    public static boolean isDebug = false;
    public static boolean isWin = false;
    public boolean isCreate = true;

    public static Point mse = new Point(0,0);

    public static int myWidth, myHeight;

    public static Room room ;
    public static readMap readMap;
    public static Store store;
    public static Enemy[] mobs = new Enemy[61];
    public static List<Tower> theTower = new ArrayList<>();


    public GameField(GameStage frame){
        frame.addMouseListener(new KeyHandel());
        frame.addMouseMotionListener(new KeyHandel());
        thread.start();
    }

    //check win?
    public static void hasWon(){
        if(killed==killToWin){
            isWin = true;
            killed = 0;
            coinage=5000;
        }
    }


    // load ảnh
    public void define() {
        room = new Room();
        readMap = new readMap();
        store = new Store();


        for (int i = 0; i < tileset_ground.length; i++) {
            tileset_ground[i] = new ImageIcon("Images/co1.png").getImage();
            tileset_ground[i] = createImage(new FilteredImageSource(tileset_ground[i].getSource(), new CropImageFilter(0, 26 * i, 26, 26)));
        }

        for (int i = 0; i < tileset_air.length; i++) {
            tileset_air[i] = new ImageIcon("Images/thap3.png").getImage();// đây
            tileset_air[i] = createImage(new FilteredImageSource(tileset_air[i].getSource(), new CropImageFilter(0, 26 * i, 26, 26)));
        }
        tileset_res[0] = new ImageIcon("Images/cell.png").getImage();
        tileset_res[1] = new ImageIcon("Images/heart.png").getImage();
        tileset_res[2] = new ImageIcon("Images/coin.png").getImage();
        tileset_mob[0] = new ImageIcon("Images/quai0.png").getImage();

        readMap.loadSave(new File("save/mission1"));

        //tạo quái
        for (int j = 0; j < 20; j++) {
            mobs[j] = new NormalEnemy();
        }
        for (int j = 20; j < 40; j++) {
            mobs[j] = new SpeedEnemy();
        }
        for (int j = 40; j < 60; j++) {
            mobs[j] = new TankEnemy();
        }
        for (int j = 60; j < mobs.length; j++) {
            mobs[j] = new BossEnemy();
        }
    }

    public void paintComponent (Graphics g){

        if(isFirst){
            myWidth = getWidth();
            myHeight = getHeight();
            define();
            isFirst = false;
        }
        g.setColor(new Color(70,70,70));
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(new Color(0,0,0));
        g.drawLine(room.Block[0][0].x-1,0,room.Block[0][0].x-1,room.Block[room.worldHeight-1][0].y+room.blockSize); // Drawing the left line.
        g.drawLine(room.Block[0][room.worldWidth-1].x+room.blockSize,0,room.Block[0][room.worldWidth-1].x+room.blockSize,room.Block[room.worldHeight-1][0].y+room.blockSize); // Drawing the right line.
        g.drawLine(room.Block[0][0].x,room.Block[room.worldHeight-1][0].y+room.blockSize,room.Block[0][room.worldWidth-1].x+room.blockSize,room.Block[room.worldHeight-1][0].y+room.blockSize); // Drawing the bottom line.
        room.draw(g); // Drawing the room.
        g.setColor(Color.cyan);
        g.setFont(new Font("Courier New",Font.BOLD,30));
        g.drawString("Wave: " + level, 115, 20);
        //load quái
        for(int i=0;i<mobs.length;i++){
            if(mobs[i].inGame){
                mobs[i].draw(g);
            }
        }

        store.draw(g); // Drawing the store.


        // man hinh khi bi thua
        if(health<1) {
            Image lose = new ImageIcon("Images/lose.png").getImage();
            g.drawImage(lose,0,0,null);

        }
        // thang
        if(isWin) {

            if (level == maxLevel) {
                Image win = new ImageIcon("Images/win.png").getImage();
                g.drawImage(win,0,0,null);

            } else if (level < maxLevel) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Courier New", Font.BOLD, 24));
                g.drawString("You Won,Congratulation! Please wait for next level... ", 120, 20);
            }
        }
    }

    public int spawnTime = 1000,spawnFrame = 0; // thoi gian quai sinh ra
    public void mobSpawner(){
        if(isCreate) {
            if (spawnFrame >= spawnTime) {
                for (int i = 0; i < mobs.length; i++) {
                    if (!mobs[i].inGame) {
                        mobs[i].spawnMob(Value.NormalEnemy);
                        break;
                    }
                    if (mobs[mobs.length - 1].inGame) {
                        isCreate = false;
                    }
                }
                spawnFrame = 0;
            } else {
                spawnFrame += 1;
            }
        }
    }

    public void run(){

        while(true){
            if(!isFirst && health>0 && !isWin){
                room.physic();
                mobSpawner();
                for (int i = 0; i < mobs.length; i++) {
                    if (mobs[i].inGame) {
                        mobs[i].physic();
                    }
                }
            }
            else if(isWin){
                if(winFrame>=winTime){
                    if(level==maxLevel){
                        isWin = true;
                        System.exit(0);
                    }
                    else {
                        level += 1;

                        // delete quai khi chuyen map moi
                        /*for ( int i =0 ; i< mobs.length; i++)
                        {
                            mobs[i].deleteMob();
                        }*/

                        Random rd = new Random();
                        int n = rd.nextInt(4);

                        tileset_mob[0] = new ImageIcon("quai"+ n + ".png").getImage();

                        readMap.loadSave(new File("save/mission1")); // load map mới

                        isWin = false;
                        isCreate = true;

                    }
                    winFrame=0;
                }
                else {
                    winFrame+=1;
                }
            }
            repaint();
            try {
                thread.sleep(1);
            } catch (Exception e) { } ;
        }
    }
}