/** Monsters.java -- answer file for exam 2ip91 -- 24 January 2017
 *   @author
 *   @id
 */

/* Don't make your lines longer than this one *********************************/

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;

class Monsters {
    MonsterPanel monsterPanel = new MonsterPanel();
    Timer timer;
    
    void createGUI() {
        // create the GUI on the event thread.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame("Monster Mania");
                frame.add(monsterPanel, BorderLayout.CENTER);
                frame.setSize( 600, 400 );
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                
                monsterPanel.setBackground( Color.DARK_GRAY );
                monsterPanel.addMonsters();
                
                timer = new Timer( 50, monsterPanel);
                timer.start();
            }
        });
    }
    
    public static void main( String[] a ) {
        new Monsters().createGUI();
    }
}

class MonsterPanel extends JPanel implements ActionListener {
    ArrayList<Monster> monsters = 
        new ArrayList<Monster>();  // the list of monsters on the screen
    
    void addMonsters() {
        monsters.add( new Monster( 50, 40 ));
        monsters.add( new Monster( 550, 40 ));
        monsters.add( new SeeingMonster( 300, 200, Color.RED));
        monsters.add( new MovingMonster( 200, 100, 50));
        monsters.add( new BouncingMonster( 200, 100, 50, 50));
        
    }
    
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        for (Monster monster : monsters) {
            monster.draw( g );
        }
    }
    
    public void actionPerformed( ActionEvent e ) {
        for (Monster monster : monsters) {
            monster.step( this );
        }
        repaint();
    }  
}

class Monster {
    int size = 50;
    int arcSize = 10;  // defines roundedness of rounded rectangle
    int locx = 0;     // x coordinate center (pixel coordinates)
    int locy = 0;    // y coordinate center (pixel coordinates)
    Color fill = Color.YELLOW;   // inner color
    Color line = Color.BLACK;   // color of border
    int phase = 0;  // phase in the animation, a counter of time steps
    
    Monster() {
        // location extremely top left
        locx = 0;
        locy = 0;
    }
            
    Monster( int x, int y ) {
        locx = x;
        locy = y;
    }
    
    // update the monster because a time step has passed
    // the parameter monsterPanel can be used for getting 
    // information about the panel, e.g., the size
    void step( MonsterPanel monsterPanel ) {
        phase++;
    }
        
    void draw( Graphics g ) {
        // draw body
        g.setColor( fill );
        g.fillRoundRect( locx - size/2, locy - size/2, size, size, arcSize, arcSize );
        g.setColor( line );
        g.drawRoundRect( locx - size/2, locy -size/2, size, size, arcSize, arcSize );
        // draw mouth
        // every 5 time steps, mouth is changed
        if ( phase % 20 < 10 ) {
            int s = size/5; // size of closed mouth
            g.drawOval( locx - s/2, locy + size/6, s, s );
        } else {
            int s = size/3; // size of open mouth
            g.fillOval( locx - s/2, locy + size/6, size/3, size/3 );
        }
    }
}

class SeeingMonster extends Monster {
    Color eyeColor;
    
    SeeingMonster(int x, int y, Color eyeColor) {
        super(x, y);
        this.eyeColor = eyeColor;        
    }
    
    void draw(Graphics g) {
        super.draw(g);
        g.setColor(eyeColor);
        int s = size/3;
        g.fillOval( locx + s/2, locy - size/4, size/4, size/4 );
        g.fillOval( locx - s/2, locy - size/4, size/4, size/4 );
    }
}

class MovingMonster extends Monster{
    Timer timerMon;
    int timeSpeed;
    
    MovingMonster(int locx, int locy, int timeSpeed) {
        super(locx, locy);
        this.timeSpeed = timeSpeed;
        timerMon = new Timer(timeSpeed, (ActionEvent e) -> {
            this.locx += 5;
        });
        timerMon.start();
    }
    
    void draw(Graphics g) {
        super.draw(g);
    }   
}

class BouncingMonster extends Monster {
    int speedX;
    int speedY;
    Timer timerBounce;
    
    BouncingMonster(int locx, int locy, int speedX, int speedY) {
        super(locx, locy);
        this.speedX = speedX;
        this.speedY = speedY;
        timerBounce = new Timer(50, (ActionEvent e) -> {
            setSpeed();
        });
        timerBounce.start();
    }
    
    void setSpeed() {
        if (locx >= 555) {
            speedX = -5;
        } else if (locx <= 25) {
            speedX = 5;
        } else if (locy >= 340) {
            speedY = -5;
        } else if (locy <= 25) {
            speedY = 5;
        }
        
        locx += speedX;
        locy += speedY;
    }
    
    void draw(Graphics g) {
        super.draw(g);
    }   
}
                    
