package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class PomodoroTimer extends JFrame implements ActionListener {
    private final int width;
    private final int height;

    private JButton start;
    private JButton reset;
    private JButton plus5;
    private JButton minus5;
    private JLabel timeLabel;
    private JLabel startTimeLabel;
    private final int defaultSessionTime = 1500;
    private int sessionTime;
    private int elapsedTime;
    boolean startClicked = false;

    private String timeText(int elapsedTime) {
        return String.format("%02d:%02d:%02d",
                elapsedTime / 3600000,
                (elapsedTime / 60000) % 60,
                (elapsedTime / 1000) % 60);
    }


    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (elapsedTime != 0) {
                elapsedTime -= 1000;
                timeLabel.setText(timeText(elapsedTime));
            } else {
                stopTimer();
            }
        }
    });

    private void stopTimer() {
        timer.stop();
        elapsedTime = sessionTime * 1000;
        timeLabel.setText(timeText(elapsedTime));
        start.setText("Start");
        startClicked = false;
    }

    public PomodoroTimer() {
        sessionTime = defaultSessionTime;
        ImageIcon image;
        try {
            image = new ImageIcon(ImageIO.read(new File(this.getClass().getClassLoader().getResource("bg.jpeg").toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.setContentPane(new JLabel(image));

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
        this.timeLabel = createLabel(timeText(elapsedTime), 275, 100, 200, 100, 15);

        this.add(this.timeLabel);
        timeLabel.setVisible(false);
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
        Object source = e.getSource();
        if (start.equals(source)) {
            startTimeLabel.setVisible(false);
            if (!startClicked) {
                startClicked = true;
                if ((sessionTime - elapsedTime) == sessionTime) {
                    elapsedTime = sessionTime * 1000;
                }
                timeLabel.setVisible(true);
                start.setText("STOP");
                timer.start();
                setPlusMinusVisibility(false);
            } else {
                startClicked = false;
                start.setText("Start");
                timer.stop();
            }
        } else if (reset.equals(source)) {
            if (!startClicked) {
                sessionTime = defaultSessionTime;
                startTimeLabel.setText(String.valueOf(sessionTime / 60));
            }
            timeLabel.setVisible(false);
            startTimeLabel.setVisible(true);
            stopTimer();
            setPlusMinusVisibility(true);
        } else if (plus5.equals(source)) {
            sessionTime = sessionTime + 5 * 60;
            this.startTimeLabel.setText(String.valueOf(sessionTime / 60));
        } else if (minus5.equals(source)) {
            sessionTime = sessionTime - 5 * 60;
            this.startTimeLabel.setText(String.valueOf(sessionTime / 60));
            if (sessionTime == 600) minus5.setVisible(false);
        }
    }

    private void setPlusMinusVisibility(boolean aFlag) {
        plus5.setVisible(aFlag);
        minus5.setVisible(aFlag);
    }

}
