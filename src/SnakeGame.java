import javax.swing.*;
import java.awt.*;

public class SnakeGame extends JFrame {
    Board board;
    SnakeGame(){
        board = new Board();
        add(board);
        pack();

        Toolkit toolkit = getToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation(screenSize.width/2 - getWidth()/2, screenSize.height/2 - getHeight()/2);
        setResizable(false);
        setVisible(true);
    }
    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame();
        snakeGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}