import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JFrame {

   
    private static final int SCREEN_WIDTH = 640; 
    private static final int SCREEN_HEIGHT = 480; 
    private static final int UNIT_SIZE = 20; 
    private static final int DELAY = 150; 

    
    private final List<Point> snake = new ArrayList<>(); 
    private Point food; 
    private char direction = 'R'; 
    private boolean running = false; 
    private Timer timer; 
    private int score = 0; 
    private final Random random = new Random(); 

    
    public SnakeGame() {
        
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLocationRelativeTo(null); 
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                changeDirection(e.getKeyCode());
            }
        });

      
        initGame();
        timer = new Timer(DELAY, new GameLoop());
        timer.start();
    }

   
    private void initGame() {
        snake.clear();

       
        int startX = SCREEN_WIDTH / 2;
        int startY = SCREEN_HEIGHT / 2;
        for (int i = 0; i < 10; i++) { 
            snake.add(new Point(startX - (i * UNIT_SIZE), startY));
        }

        spawnFood();
        running = true; 
    }

    private void spawnFood() {
        while (true) {
            int x = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            int y = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

            food = new Point(x, y);

            if (!snake.contains(food)) {
                break;
            }
        }
    }

   
    private void changeDirection(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
        }
    }

  
    private void move() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case 'U': head.y -= UNIT_SIZE; break;
            case 'D': head.y += UNIT_SIZE; break;
            case 'L': head.x -= UNIT_SIZE; break;
            case 'R': head.x += UNIT_SIZE; break;
        }
        snake.add(0, head); 

        if (head.equals(food)) { 
            spawnFood(); 
            score++;
        } else {
            snake.remove(snake.size() - 1); 
        }
    }

    
    private boolean checkCollision() {
        Point head = snake.get(0);

       
        if (head.x < 0 || head.x >= SCREEN_WIDTH || head.y < 0 || head.y >= SCREEN_HEIGHT) {
            return true;
        }

      
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                return true;
            }
        }
        return false;
    }

   
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (running) {
           
            g.setColor(Color.RED);
            g.fillOval(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

       
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); 
                } else {
                    g.setColor(new Color(45, 180, 0)); 
                }
                g.fillRect(snake.get(i).x, snake.get(i).y, UNIT_SIZE, UNIT_SIZE);
            }

            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g);
        }
    }

    
    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, SCREEN_HEIGHT / 2 + 40);
    }

    
    private class GameLoop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                move();
                if (checkCollision()) {
                    running = false;
                    timer.stop();
                }
            }
            repaint(); 
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
