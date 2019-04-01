/*
Jose Toscano
A game based on the Simon memory game.
3/20/18
*/

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Random;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Game {
    private JPanel startPanel, mainPanel, gameOverPanel, panels;
    private JButton readyButton, enterButton, upButton, leftButton, rightButton, downButton, yesButton, noButton;
    private JTextArea gameIntroLabel, gameOverLabel;
    private JLabel highScoreLabel, currentScoreLabel;
    private CardLayout cl;
    private int highScore, currentScore;
    private LinkedList<Integer> buttonsPressedList;
    private LinkedList<Integer> moveSetList;
    private volatile boolean keepGoing = true;
    
    Game() {
        JFrame mainFrame = new JFrame("Simon");
        final int mainFrameSize = 500;
        
        mainFrame.setSize(mainFrameSize, mainFrameSize);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveScore();
                System.exit(0);
            }
        });

        this.startPanel = createStartPanel();
        this.highScore = getHighScore();
        this.mainPanel = createMainPanel();
        this.gameOverPanel = createGameOverPanel();
        this.panels = new JPanel(new CardLayout());

        this.cl = (CardLayout)panels.getLayout();

        
        // Storing each panel inside of panels
        this.panels.add(this.startPanel, "introToGame");
        this.panels.add(this.mainPanel, "startGame");
        this.panels.add(this.gameOverPanel, "gameOver");

        // Adding Panels to JFrame jfrm
        mainFrame.add(panels);
        
        // Cant decrease or increase window size.
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        
        // timer = new Timer(500, e -> cl.show(panels, "B")); 
        
        this.buttonsPressedList = new LinkedList<Integer>();
        this.moveSetList = new LinkedList<Integer>();

        if(this.highScore != 0) {
            this.cl.show(this.panels, "startGame");
            runGame();
        }
    }

    private JPanel createStartPanel() {
        final int gameIntroLabelHeight = 275;
        final int gameIntroLabelWidth = 300;
        final int readyButtonHeight = 100;
        final int readyButtonWidth = 100;

        startPanel = new JPanel(null); //must set layout to null to be able to position buttons as i want

        this.gameIntroLabel = new JTextArea();
        this.gameIntroLabel.setLineWrap(true); // wraps the words
        this.gameIntroLabel.setWrapStyleWord(true); // wraps by word instead of character
        this.gameIntroLabel.setEditable(false);
        this.gameIntroLabel.setFont(new Font("Times", 1, 14));
        this.gameIntroLabel.setText("Hello this is game like Simon."
        + "If you have never played Simon this is how the game works."
        + "When you click ready the game will start. One button " 
        + " will flash. Click that button and hit enter." 
        + " Now two buttons will flash. Click them in the order"
        + " that they  appeared in and click enter when done. "
        + " Each time you get it right, the"
        + " number of buttons flashing will increase by 1. "
        + " continue clicking the buttons in the order that they appear"
        + " and hitting enter when done. The game will end when you get"
        + " you get it wrong.");
        this.gameIntroLabel.setBounds(100, 0, gameIntroLabelWidth, gameIntroLabelHeight);
        startPanel.add(gameIntroLabel);
        
        this.readyButton = new JButton("Ready");
        this.readyButton.setBounds(200, 300, readyButtonWidth, readyButtonHeight);
        this.readyButton.addActionListener(e -> {
            cl.show(panels, "startGame");
            runGame();
        });
        startPanel.add(readyButton);

        return startPanel;
    }

    private JPanel createMainPanel() { // need to finish
        final int buttonsWidth = 100;
        final int buttonsHeight = 100;
        mainPanel = new JPanel(null);
        
        this.upButton = new JButton();
        this.upButton.setBounds(200, 0, buttonsWidth, buttonsHeight);
        this.upButton.addActionListener(e -> buttonsPressedList.add(0));
        mainPanel.add(upButton);

        this.leftButton = new JButton();
        this.leftButton.setBounds(0, 190, buttonsWidth, buttonsHeight);
        this.leftButton.addActionListener(e -> buttonsPressedList.add(1));
        mainPanel.add(leftButton);

        this.rightButton = new JButton();
        this.rightButton.setBounds(400, 190, buttonsWidth, buttonsHeight);
        this.rightButton.addActionListener(e -> buttonsPressedList.add(2));
        mainPanel.add(rightButton);

        this.downButton = new JButton();
        this.downButton.setBounds(200, 375, buttonsWidth, buttonsHeight);
        this.downButton.addActionListener(e -> buttonsPressedList.add(3));
        mainPanel.add(downButton);

        this.enterButton = new JButton("Enter");
        this.enterButton.setBounds(200, 190, buttonsWidth, buttonsHeight);
        this.enterButton.addActionListener(e -> {
        keepGoing = checkInput();
            if(keepGoing) {
                updateScore();
                runGame();
            } else {
                this.gameOverLabel.setText("   Game Over!\n" +
        "   Your last score is " + currentScore + "\n   The highest score is " + highScore +
        "\n   Would you like to play again?");
                cl.show(this.panels, "gameOver");
            }
        });
        mainPanel.add(enterButton);

        currentScore = 0;
        this.currentScoreLabel = new JLabel();
        this.currentScoreLabel.setText("Current Score: " + currentScore); 
        this.currentScoreLabel.setBounds(0,0,200,25);
        mainPanel.add(currentScoreLabel);

        this.highScoreLabel = new JLabel();
        this.highScoreLabel.setText("High Score: " + highScore);
        this.highScoreLabel.setBounds(0,50,200,25);
        mainPanel.add(highScoreLabel);

        return mainPanel;
    }

    private JPanel createGameOverPanel() { // need to finish
        final int gameOverLabelHeight = 200;
        final int gameOverLabelWidth = 300;
        final int yesButtonHeight = 100;
        final int yesButtonWidth = 100;
        final int noButtonHeight = 100;
        final int noButtonWidth = 100;

        gameOverPanel = new JPanel(null);

        this.gameOverLabel = new JTextArea();
        this.gameOverLabel.setLineWrap(true);
        this.gameOverLabel.setWrapStyleWord(true);
        this.gameOverLabel.setEditable(false);
        this.gameOverLabel.setFont(new Font("Times", 1, 14));
        
        this.gameOverLabel.setBounds(100, 0, gameOverLabelWidth, gameOverLabelHeight);
        gameOverPanel.add(gameOverLabel);

        this.yesButton = new JButton("Yes");
        this.yesButton.setBounds(100, 300, yesButtonWidth, yesButtonHeight);
        this.yesButton.addActionListener(e -> {
            resetGame();
            cl.show(panels, "startGame");
            runGame();
        });
        gameOverPanel.add(yesButton);
        this.noButton = new JButton("No");
        this.noButton.setBounds(300, 300, noButtonWidth, noButtonHeight);
        this.noButton.addActionListener(e -> {
            saveScore();
            gameOverLabel.setText("Data saved. You may now exit the window.");
            this.yesButton.setVisible(false);
            this.noButton.setVisible(false);
        });
        gameOverPanel.add(noButton);
        return gameOverPanel;
    }

    private void resetGame() {
        currentScore = 0;
        this.currentScoreLabel.setText("Current Score: " + currentScore);
        moveSetList.clear();
    }

    private void saveScore() {
        BufferedWriter br = null;
        String highscore = "" + this.highScore;

        try {
            br = new BufferedWriter(new FileWriter("highscore.txt"));
            br.write(highscore);
        } catch (Exception e) {
            System.out.println("file not found");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch(Exception e) {
                System.out.println("file not found");
            }
        }
    }

    private int getHighScore() {
        Integer highScore = 0;
        String stringNum;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("highscore.txt"));
            if(br != null) {
                stringNum = br.readLine();
                if(stringNum != null) {
                    highScore = Integer.parseInt(stringNum);
                }
            }
        } catch (Exception e) {
            System.out.println("file not found");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch(Exception e) {
                System.out.println("file not found");
            }
        }

        return highScore;
    }

    private void runGame() {
        JButton[] buttons = {upButton, downButton, leftButton, rightButton, enterButton};
        disableButtons(buttons);
        flashButtons();          
    }
    
    private boolean checkInput() {
        boolean flag = true;
        if(this.moveSetList.size() != this.buttonsPressedList.size()) {
            flag = false;
        } else {
            for(int i = 0; i < this.moveSetList.size(); i++) {
                if(this.moveSetList.get(i) != this.buttonsPressedList.get(i)) {
                    flag = false;
                }
            }
        }
        this.buttonsPressedList.clear();
        
        return flag;
    }
    
    private void disableButtons(JButton[] buttons) {
        for(JButton button: buttons) {
            button.setEnabled(false);
        }
    }

    private void flashButtons() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(4);
        moveSetList.add(randomNumber);
        FlashButtonsThread flashingButtonsThread = new FlashButtonsThread(upButton, downButton, rightButton, leftButton, enterButton, moveSetList);
        flashingButtonsThread.getThread().start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game());
    }

    private void updateScore() {
        currentScore++;
        if(currentScore > highScore) {
            highScore = currentScore;
        }
        currentScoreLabel.setText("Current Score: " + currentScore);
        highScoreLabel.setText("High Score: " + highScore);
    }
}


