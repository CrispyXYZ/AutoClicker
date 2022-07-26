package me.crispyxyz.autoclicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

public class App implements ActionListener {
    private boolean flag;
    private final JCheckBox rightClickCheckBox;
    private final JLabel statusLabel;
    private final MainForm mainForm;

    public App() {
        //init form
        JFrame frame = new JFrame("AutoClicker");
        this.mainForm = new MainForm();
        frame.setContentPane(mainForm.getMainPanel());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        //bind views
        this.rightClickCheckBox = mainForm.getRightClickCheckBox();
        this.statusLabel = mainForm.getStatusLabel();
        statusLabel.setText(String.valueOf(flag));

        //add listener
        JButton runButton = mainForm.getRunButton();
        runButton.addActionListener(this);
    }

    public static void main(String[] args) {
        new App();
    }

    private static void showSimpleErrorDialog(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void changeFlagAndUpdateView() {
        flag = !flag;
        statusLabel.setText(String.valueOf(flag));
    }

    private void run() throws InterruptedException {
        int firstDelayTime;
        int delayTime;
        int clickTimes;

        try {
            firstDelayTime = Integer.parseInt(mainForm.getFirstDelayTimeTextField().getText());
            delayTime = Integer.parseInt(mainForm.getDelayTimeTextField().getText());
            clickTimes = Integer.parseInt(mainForm.getClickTimesTextField().getText());
        } catch (NumberFormatException e) {
            return;
        }

        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            return;
        }

        int mouseButton;
        if (rightClickCheckBox.isSelected()) {
            mouseButton = InputEvent.BUTTON3_DOWN_MASK;
        } else {
            mouseButton = InputEvent.BUTTON1_DOWN_MASK;
        }

        changeFlagAndUpdateView();
        Thread.sleep(firstDelayTime);
        for (int i = 0; i < clickTimes; i++) {
            robot.mousePress(mouseButton);
            Thread.sleep(delayTime);
            robot.mouseRelease(mouseButton);
            Thread.sleep(delayTime);
        }
        changeFlagAndUpdateView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(() -> {
            try {
                run();
            } catch (InterruptedException ex) {
                showSimpleErrorDialog(ex.toString());
            }
        }).start();
    }
}
