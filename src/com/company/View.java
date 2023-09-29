package com.company;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class View {
    private Controller controller;
    private JLabel label;

    public void create(int width, int height) {
        JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                controller.handleMouseClick(e.getX(), e.getY(), e.getButton());
            }
        });

        label = new JLabel();
        label.setBounds(0, 0, width, height);
        frame.add(label);

        frame.setVisible(true);
    }

    public void setImage(BufferedImage image) {
        label.setIcon(new ImageIcon(image));
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
