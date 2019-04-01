/*
    Jose Toscano
    3/20/18
*/

import javax.swing.JButton;
import java.util.LinkedList;
import java.awt.Color;

public class FlashButtonsThread implements Runnable {
    private Thread thread;
    private JButton upButton, downButton, rightButton, leftButton, enterButton;
    private LinkedList<Integer> moveSetList;
    private final int time = 500;
    private final Color red = Color.red;
    private final Color green = Color.green;
    private final Color blue = Color.blue;
    private final Color yellow = Color.yellow;
    private final Color neutral = new JButton().getBackground();


    FlashButtonsThread(JButton upButton, JButton downButton, JButton rightButton, JButton leftButton, JButton enterButton, LinkedList<Integer> moveSetList) {
        thread = new Thread(this);
        this.upButton = upButton;
        this.downButton = downButton;
        this.rightButton = rightButton;
        this.leftButton = leftButton;
        this.enterButton = enterButton;
        this.moveSetList = moveSetList;
    }

    public void run() {
        for(int num : moveSetList){
            switch(num) {
                case 0: 
                    upButton.setBackground(red);
                    pause(time);
                    upButton.setBackground(neutral);
                    // System.out.print("Up ");
                    break;
                case 1:
                    leftButton.setBackground(green);
                    pause(time);
                    leftButton.setBackground(neutral);
                    // System.out.print("Left ");
                    break;
                case 2:
                    rightButton.setBackground(yellow);
                    pause(time);
                    rightButton.setBackground(neutral);
                    // System.out.print("Right ");
                    break;
                case 3:
                    downButton.setBackground(blue);
                    pause(time);
                    downButton.setBackground(neutral);
                    // System.out.print("Down ");
                    break;
            }
            pause(time);
        }
        enableButtons(upButton, downButton, leftButton, rightButton, enterButton);
    }

    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException exc) {

        }
    }  
    
    private void enableButtons(JButton ... buttons) {
        for(JButton button: buttons) {
            button.setEnabled(true);
        }
    }

    public Thread getThread() {
        return thread;
    }
}
