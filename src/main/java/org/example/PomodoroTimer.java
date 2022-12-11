package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PomodoroTimer extends JFrame implements ActionListener {
    private final int width;
    private final int height;

    private JButton start;
    private JButton reset;
    private JButton plus5;
    private JButton minus5;
    private JLabel timeLabel;
    private JLabel startTimeLabel;
    private int sessionTime = 1200;
    private int elapsedTime;

    private String createTimeText(int elapsedTime) {
        int h = elapsedTime / 3600000;
        int m = (elapsedTime / 60000) % 60;
        int s = (elapsedTime / 1000) % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    boolean startClicked = false;

    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (elapsedTime != 0) {
                elapsedTime -= 1000;
                timeLabel.setText(createTimeText(elapsedTime));
            } else {
                stopTimer();
            }
        }
    });

    private void stopTimer() {
        timer.stop();
        elapsedTime = sessionTime * 1000;
        timeLabel.setText(createTimeText(elapsedTime));
        start.setText("Start");
        startClicked = false;
        int choice = JOptionPane.showConfirmDialog(null, "Nice Session", "Session End", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            showMessage("Keep going");
        } else if (choice == JOptionPane.NO_OPTION) {
            showMessage("Don't give up");
        }

    }

    private static void showMessage(String Keep_going) {
        JOptionPane.showMessageDialog(null, Keep_going);
    }


    public PomodoroTimer() {
        ImageIcon image;
        try {
            image = new ImageIcon(ImageIO.read(new File("D:\\IdeaProjects\\pomadoro\\src\\main\\resources\\bg.jpeg")));
            this.setContentPane(new JLabel(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.width = image.getIconWidth();
        this.height = image.getIconHeight();
        this.setSize(width, height);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        fillWindow();
    }

    private void fillWindow() {


        JLabel title = createLabel("POMODORO TIMER", 210, 50, 250, 100, 18);
        startTimeLabel = createLabel(String.valueOf(sessionTime / 60), 300, 125, 50, 50, 18);

        this.start = createButton("Start", true, 5, 200, 100, 50);
        this.reset = createButton("Reset", false, 5, 200, 100, 50);
        this.plus5 = createButton("+", true, 50, 125, 50, 50);
        this.minus5 = createButton("-", false, 50, 125, 50, 50);

        this.add(startTimeLabel);
        this.add(title);
        this.add(this.start);
        this.add(this.reset);
        this.add(plus5);
        this.add(minus5);
        this.pack();

    }

    private JButton createButton(String name,
                                 boolean position, int fromCenter, int y, int width, int height) {
        JButton button = new JButton(name);
        int x = this.width / 2;
        if (position) {
            x = x - width - fromCenter;
        } else x += fromCenter;
        button.setBounds(x, y, width, height);
        button.addActionListener(this);
        return button;
    }

    private JLabel createLabel(String title,
                               int x, int y, int width, int height, int size) {
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setBounds((this.width - width) / 2, y, width, height);
        label.setFont(new Font("Sans-serif", Font.BOLD, size));
        label.setForeground(Color.WHITE);
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            startTimeLabel.setVisible(false);
            if (!startClicked) {
                startClicked = true;
                elapsedTime = sessionTime * 1000;
                this.timeLabel = createLabel(createTimeText(elapsedTime), 275, 100, 100, 100, 15);
                this.add(this.timeLabel);
                start.setText("STOP");
                timer.start();
            } else {
                startClicked = false;
                start.setText("Start");
                timer.stop();
            }
        } else if (e.getSource() == reset) {
            stopTimer();
        } else if (e.getSource() == plus5) {
            sessionTime = sessionTime + 5 * 60;
            this.startTimeLabel.setText(String.valueOf(sessionTime / 60));
        } else if (e.getSource() == minus5 && sessionTime > 300) {
            sessionTime = sessionTime - 5 * 60;
            this.startTimeLabel.setText(String.valueOf(sessionTime / 60));
        }
    }
}
