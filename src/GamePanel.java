import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int ScreenWIDTH = 600;
    static final int ScreenHEIGHT = 600;
    static final int unitSize = 25;
    static final int gameUnits = (ScreenWIDTH * ScreenHEIGHT) / unitSize;
    static final int delay = 75;
    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean isRunning = false;
    Timer timer;
    Random rand;

    GamePanel(){
        rand = new Random();
        this.setPreferredSize(new Dimension(ScreenWIDTH, ScreenHEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        isRunning = true;
        timer = new Timer(delay, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if (isRunning) {
            for (int i = 0; i < ScreenHEIGHT / unitSize; i++) {
                g.drawLine(i * unitSize, 0, i * unitSize, ScreenHEIGHT);
                g.drawLine(0, i * unitSize, ScreenWIDTH, i * unitSize);

            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);


            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }

            g.setColor(Color.ORANGE);
            g.setFont(new Font("Times New Roman", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+ applesEaten, (ScreenWIDTH - metrics.stringWidth("Score: "+ applesEaten))/ 2, g.getFont().getSize());

        }
        else {
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = rand.nextInt((int)(ScreenWIDTH / unitSize)) * unitSize;
        appleY = rand.nextInt((int)(ScreenHEIGHT / unitSize)) * unitSize;
    }
    public void move(){
        for (int i = bodyParts; i > 0 ; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'R':
                x[0] = x[0] + unitSize;
                break;
            case 'L':
                x[0] = x[0] - unitSize;
                break;
            case 'U':
                y[0] = y[0] - unitSize;
                break;
            case 'D':
                y[0] = y[0] + unitSize;
                break;
        }
    }
    public void checkApple(){

        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollision(){
        //checks if head collides with body
        for (int i = bodyParts; i > 0 ; i--){
            if ((x[0] == x[i]) && (y[0] == y[i]) ){
                isRunning = false;
            }
        }
        //checks if head touches left border
        if(x[0] < 0){
            isRunning = false;
        }
        //checks if head touches right border
        if(x[0] > ScreenWIDTH){
            isRunning = false;
        }
        //checks if head touches top border
        if(y[0] < 0){
            isRunning = false;
        }
        //checks if head touches bottom border
        if(y[0] > ScreenHEIGHT){
            isRunning = false;
        }

        if(!isRunning){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //score
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ applesEaten, (ScreenWIDTH - metrics2.stringWidth("Score: "+ applesEaten))/ 2, g.getFont().getSize());

        //game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman", Font.BOLD, 80));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER!!!", (ScreenWIDTH - metrics.stringWidth("GAME OVER!!!"))/ 2, ScreenHEIGHT/2);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;

            }
        }
    }
}
