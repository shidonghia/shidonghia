
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;

public class menuGame extends JPanel{

    public menuGame() {

    }

    public void paint(Graphics g)	{
        g.setColor(Color.blue);
        g.drawRect(0, 0, 50, 50);
        //g.fillRect(200, 200, 50, 50);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
