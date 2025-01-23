import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class TrafficSignalProject extends JFrame {

    // Signal Panels
    private JPanel redSignalPanel, yellowSignalPanel, greenSignalPanel;
    // Car Panel
    private JPanel carPanel;
    // Buttons
    private JButton startButton, slowButton, stopButton, reverseButton, uTurnButton;
    // Timer for car movement
    private Timer carMoveTimer;
    private int carSpeed = 1; // Speed of car (initially slow)
    private int carXPosition = 20; // Initial position of the car
    private int direction = 1; // 1 for forward, -1 for reverse
    private JLabel carLabel; // Car label to display the car image
    private ImageIcon carIcon; // Original car icon
    private boolean flipped = false; // Tracks if the car is flipped

    public TrafficSignalProject() {
        // Double the dimensions of the game
        int width = 1200;
        int height = 800;

        setSize(width, height);
        setTitle("Traffic Signal Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(100, 100);
        setLayout(null);

        // Custom JPanel for the background image
        ImageIcon back = new ImageIcon(getClass().getResource("images/back (1).png"));
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(back.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, width, height);
        add(backgroundPanel);

        // Red Signal Panel
        redSignalPanel = new JPanel();
        redSignalPanel.setBounds(748, 80, 80, 80); // Adjusted to fit new size
        redSignalPanel.setBackground(Color.BLACK);  // Initially black
        backgroundPanel.add(redSignalPanel);

        // Yellow Signal Panel
        yellowSignalPanel = new JPanel();
        yellowSignalPanel.setBounds(848, 80, 80, 80); // Adjusted to fit new size
        yellowSignalPanel.setBackground(Color.BLACK);  // Initially black
        backgroundPanel.add(yellowSignalPanel);

        // Green Signal Panel
        greenSignalPanel = new JPanel();
        greenSignalPanel.setBounds(948, 80, 80, 80); // Adjusted to fit new size
        greenSignalPanel.setBackground(Color.BLACK);  // Initially black
        backgroundPanel.add(greenSignalPanel);

        // Car Panel
        carPanel = new JPanel();
        carPanel.setBounds(20, 620, 160, 80); // Adjusted to fit new size
        carPanel.setBackground(new Color(115, 115, 115));
        backgroundPanel.add(carPanel);

        carIcon = new ImageIcon(getClass().getResource("images/car.png"));
        carLabel = new JLabel(carIcon);
        carLabel.setBounds(0, 0, 160, 80);
        carPanel.add(carLabel);

        // Buttons
        startButton = createButton("Start", 20, 20, new Color(0, 133, 11), new Color(0, 100, 0), e -> {
            greenSignalPanel.setBackground(Color.GREEN);
            yellowSignalPanel.setBackground(Color.BLACK);
            redSignalPanel.setBackground(Color.BLACK);
            carSpeed = 6;  // Fast speed
            direction = 1; // Move forward
            startCarMovement();
        });
        backgroundPanel.add(startButton);

        slowButton = createButton("Slow", 20, 70, new Color(255, 255, 0), new Color(200, 200, 0), e -> {
            greenSignalPanel.setBackground(Color.BLACK);
            yellowSignalPanel.setBackground(Color.YELLOW);
            redSignalPanel.setBackground(Color.BLACK);
            carSpeed = 2;  // Slow speed
            direction = 1; // Move forward
            startCarMovement();
        });
        backgroundPanel.add(slowButton);

        stopButton = createButton("Stop", 20, 120, new Color(255, 0, 0), new Color(200, 0, 0), e -> {
            greenSignalPanel.setBackground(Color.BLACK);
            yellowSignalPanel.setBackground(Color.BLACK);
            redSignalPanel.setBackground(Color.RED);
            carSpeed = 0;  // Stop car
            direction = 0;
            startCarMovement();
        });
        backgroundPanel.add(stopButton);

        reverseButton = createButton("Reverse", 20, 170, new Color(0, 0, 255), new Color(0, 0, 200), e -> {
            greenSignalPanel.setBackground(Color.BLACK);
            yellowSignalPanel.setBackground(Color.BLACK);
            redSignalPanel.setBackground(Color.BLACK);
            direction = -1; // Set direction to reverse
            carSpeed = 2;  // Reverse at slow speed
            startCarMovement();
        });
        backgroundPanel.add(reverseButton);

        // U-Turn Button
        uTurnButton = createButton("U-Turn", 20, 220, new Color(128, 0, 128), new Color(102, 0, 102), e -> {
            direction *= -1; // Flip the direction
            flipped = !flipped; // Toggle flipped state
            carLabel.setIcon(CarFlip.flipCarIcon(carIcon, flipped)); // Flip the car image
        });
        backgroundPanel.add(uTurnButton);

        // Timer to move the car
        carMoveTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (carSpeed > 0) {
                    carXPosition += direction * carSpeed; // Move the car based on direction

                    // Check if the car hits the invisible wall on the right side
                    int invisibleWallX = width - 160; // Invisible wall position (subtract car width)
                    if (carXPosition >= invisibleWallX) {
                        carXPosition = invisibleWallX; // Stop the car at the wall
                        carMoveTimer.stop();
                    }

                    // Check if the car hits the left edge
                    if (carXPosition <= 0) {
                        carXPosition = 0; // Prevent car from going out of bounds
                        carMoveTimer.stop();
                    }

                    // Update the car panel position
                    carPanel.setBounds(carXPosition, 620, 160, 80);
                }
            }
        });

        setVisible(true);
    }

    // Start car movement
    private void startCarMovement() {
        carMoveTimer.start();  // Start or resume the car movement based on the speed and direction
    }

    // Create a button with hover effects
    private JButton createButton(String text, int x, int y, Color defaultColor, Color hoverColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 160, 60); // Adjusted for larger UI
        button.setForeground(Color.WHITE);
        button.setBackground(defaultColor);
        button.setOpaque(true); // Ensures the background color is visible
        button.setBorderPainted(false); // Removes default border for cleaner look
        button.setFont(new Font("Railway", Font.BOLD, 18)); // Larger font for larger UI
        button.addActionListener(action);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(defaultColor);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        new TrafficSignalProject();
    }
}

// CarFlip Utility Class
class CarFlip {

    public static Icon flipCarIcon(ImageIcon originalIcon, boolean flipped) {
        if (originalIcon == null) {
            return null; // No icon to flip
        }

        Image originalImage = originalIcon.getImage();
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);

        // Create a new image with flipped horizontal orientation
        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flippedImage.createGraphics();
        if (flipped) {
            g2d.drawImage(originalImage, width, 0, -width, height, null);
        } else {
            g2d.drawImage(originalImage, 0, 0, width, height, null);
        }
        g2d.dispose();

        return new ImageIcon(flippedImage);
    }
}
