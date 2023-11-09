/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dogjump;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 *
 * @author Nonny
 */

class PlatformPosition{
    int x,y;
}

public class DogJump extends JPanel implements Runnable,KeyListener{
    final int WIDTH = 500;
    final int HEIGHT = 800;
    
    boolean isRunning;
    boolean gameover;
    Thread thread;
    BufferedImage view,background,platform,dog;
    
    PlatformPosition[] platformsPosition;
    int x = 100, y = 100, h = 500;
    float dy = 0;
    boolean right,left;
    
    public DogJump(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
    }
    public static void main(String[] args) {
        JFrame w = new JFrame("Ninja Dog Jump");
        w.setResizable(false);
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.add(new DogJump());
        w.pack();
        w.setLocationRelativeTo(null);
        w.setVisible(true);
    }
    
    @Override
    public void addNotify(){
        super.addNotify();
        if(thread == null){
            thread = new Thread(this);
            isRunning = true;
            thread.start();
        }
    }
    public void start(){
        try{
            view = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
            
            background = ImageIO.read(getClass().getResource("background.png"));
            platform = ImageIO.read(getClass().getResource("platform.png"));
            dog = ImageIO.read(getClass().getResource("dog.png"));
            
            platformsPosition = new PlatformPosition[10];
            for(int i=0;i<10;i++){
                platformsPosition[i] = new PlatformPosition();
                platformsPosition[i].x = new Random().nextInt(450);
                platformsPosition[i].y = new Random().nextInt(800);
            }
        }catch (IOException e){
        }
    }
    public void update(){
        if(right){
            x += 3;
        }else if(left){
            x -= 3;
        }
        dy += 0.2;
        y += dy;
        if(y >= 800){ // เมื่อ y มากกว่าหรือเท่ากับ 800
            gameover = true;
            isRunning = false; // หยุดการทำงาน
        }
        /*if(y>800){
            dy = -10;
        }*/
        if(y<h){
            for(int i=0;i<10;i++){
                y = h;
                platformsPosition[i].y = platformsPosition[i].y - (int) dy;
                if(platformsPosition[i].y>800){
                    platformsPosition[i].y = 0;
                    platformsPosition[i].x = new Random().nextInt(450);
                }
            }
        }
        for(int i=0;i<10;i++){
            if ((x+50 > platformsPosition[i].x)&&
                    (x+20 < platformsPosition[i].x+40) &&
                    (y+70 > platformsPosition[i].y)&&
                    (y+70 < platformsPosition[i].y+50) &&
                    (dy>0)){
                dy = -10;
            }
        }
    }
    public void draw(){
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        g2.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        g2.drawImage(dog,x,y,dog.getWidth(),dog.getHeight(),null);
        for(int i=0;i<10;i++){
            g2.drawImage(
                    platform,
                    platformsPosition[i].x,
                    platformsPosition[i].y,
                    platform.getWidth(),
                    platform.getHeight(),
                    null
            );
        }
        if (gameover) {
            Font font = new Font("Arial", Font.BOLD, 36);
            g2.setFont(font);
            g2.setColor(Color.RED);
            String message = "Game Over";
            int messageWidth = g2.getFontMetrics().stringWidth(message);
            int messageX = (WIDTH - messageWidth) / 2;
            int messageY = HEIGHT / 2;
            g2.drawString(message, messageX, messageY);
        }
        Graphics g = getGraphics();
        g.drawImage(view,0,0,WIDTH,HEIGHT,null);
        g.dispose();
    }
    @Override
    public void run(){
        try{
            requestFocus();
            start();
            while(isRunning){
                update();
                draw();
                Thread.sleep(1000/60);
            }
        }catch (InterruptedException e){
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            right = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            left = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            right = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            left = false;
        }
    }
}
