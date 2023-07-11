import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    final int B_WIDTH = 400;
    final int B_HEIGHT = 400;
    final int CELL_SIZE = 10;
    final int MAX_CELLS = B_HEIGHT* B_WIDTH /(2*CELL_SIZE);
    final int DELAY = 150;
    final int initSnake = 3;
    int[] snakeX = new int[MAX_CELLS];
    int[] snakeY = new int[MAX_CELLS];
    int appleX;
    int appleY;
    int snake;
    Image head, body, apple;
    Timer timer;
    boolean left = true;
    boolean right = false;
    boolean up = false;
    boolean down = false;
    boolean inGame;

    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);
        initGame();
        loadImage();
    }

    // create a method to initialize the game.
    public void initGame(){
        inGame = true;
        snake = initSnake;
        locateSnake();
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    // check if the apple is eaten or not
    public void eatApple(){
        if(appleX==snakeX[0] && appleY==snakeY[0]){
            snake++;
            locateApple();
        }
    }

    // It gives random location of Apple every time.
    public void locateApple(){
        appleX = (int)(Math.random()*(B_WIDTH/CELL_SIZE)-1)*CELL_SIZE;
        appleY = (int)(Math.random()*(B_HEIGHT/CELL_SIZE)-1)*CELL_SIZE;
    }

    // Drawing the Snake and Apple.
    public void locateSnake(){
        snakeX[0] = (int)(Math.random()*(B_WIDTH/CELL_SIZE)-1)*CELL_SIZE;
        snakeY[0] = (int)(Math.random()*(B_HEIGHT/CELL_SIZE)-1)*CELL_SIZE;

        for(int i = 1; i < snake; i++){
            snakeX[i] = snakeX[0]+CELL_SIZE*i;
            snakeY[i] = snakeY[0];
        }
    }

    // moving snake in one direction.
    public void move(){
        for(int i = snake-1; i > 0; i--){
            snakeX[i] = snakeX[i-1];
            snakeY[i] = snakeY[i-1];
        }

        if(left){
            if(snakeX[0] == 0) snakeX[0] = B_WIDTH-CELL_SIZE;
            else snakeX[0] -= CELL_SIZE;
        } else if(right){
            if(snakeX[0] == B_WIDTH-CELL_SIZE) snakeX[0] -= snakeX[0];
            else snakeX[0] += CELL_SIZE;
        } else if(up){
            if(snakeY[0] == 0) snakeY[0] = B_HEIGHT-CELL_SIZE;
            else snakeY[0] -= CELL_SIZE;
        }
        else {
            if(snakeY[0] == B_HEIGHT-CELL_SIZE) snakeY[0] -= snakeY[0];
            else snakeY[0] += CELL_SIZE;
        }
    }

    // Checking if the collision is happened or not;
    public void checkCollision(){
        for(int i = 1; i < snake; i++){
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                inGame = false;
                break;
            }
        }
    }

    // Loading all the required images.
    public void loadImage(){
        head = new ImageIcon("src/resources/head.png").getImage();
        body = new ImageIcon("src/resources/body.png").getImage();
        apple = new ImageIcon("src/resources/apple.png").getImage();
    }

    public void gameOver(Graphics g){
        String msg = "Game Over";
        String reStart = "Press Enter to Restart";
        String score = Integer.toString((snake-initSnake)*10);
        Font font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (B_WIDTH-fontMetrics.stringWidth(msg))/2, B_HEIGHT/3);
        g.drawString(score, (B_WIDTH-fontMetrics.stringWidth(score))/2, B_HEIGHT/2);
        g.drawString(reStart, (B_WIDTH-fontMetrics.stringWidth(reStart))/2, 2*B_HEIGHT/3);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }

    public void doDrawing(Graphics g){
        if(inGame){
            for(int i = 1; i < snake; i++){
                g.drawImage(body, snakeX[i], snakeY[i], this);
            }
            g.drawImage(apple, appleX, appleY, this);
            g.drawImage(head, snakeX[0], snakeY[0], this);
        } else {
            timer.stop();
            gameOver(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        eatApple();
        move();
        repaint();
        checkCollision();
    }

    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
//            System.out.println(inGame+" "+key);
            if(key == KeyEvent.VK_SPACE){
                if(timer.isRunning()) timer.stop();
                else timer.start();
            }
            if(!inGame && key == KeyEvent.VK_ENTER){
                initGame();
            }
            if(!timer.isRunning()) return;
            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            } else if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            } else if(key == KeyEvent.VK_UP && !down){
                up = true;
                left = false;
                right = false;
            } else if(key == KeyEvent.VK_DOWN && !up){
                down = true;
                left = false;
                right = false;
            }
        }
    }
}
