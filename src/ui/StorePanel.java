package ui;

import main.GameMain;
import model.User;
import database.DatabaseManager;
import utils.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class StorePanel extends JPanel {

    private GameMain gameMain;
    private JLabel statusLabel;

    private static final int[] PLANE_NUMBERS = {1, 2, 3, 4, 5, 6, 7};

    // esm ha sakhtegi :)

    private static final String[] PLANE_NAMES = {
            "Default Plane",
            "Fast Plane",
            "Heavy Plane",
            "Sniper Plane",
            "Fancy Plane",
            "Beast Plane",
            "Alien Plane"
    };

    public StorePanel(GameMain gameMain) {

        this.gameMain = gameMain;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 50));

        // Header

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(20, 20, 50));

        // Title

        JLabel title = new JLabel("SELECT YOUR PLANE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.YELLOW);
        headerPanel.add(title, BorderLayout.NORTH);

        // Mini title under it

        statusLabel = new JLabel("Choose a plane to equip", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusLabel.setForeground(Color.LIGHT_GRAY);
        headerPanel.add(statusLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Planes grid

        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(new Color(20, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 15, 15, 15);

        for (int i = 0; i < PLANE_NUMBERS.length; i++) {
            JPanel planePanel = createPlanePanel(i);
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            gridPanel.add(planePanel, gbc);
        }

        // Add scroll panel

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBackground(new Color(20, 20, 50));
        scrollPane.getViewport().setBackground(new Color(20, 20, 50));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(20, 20, 50));

        // Add back button

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setBackground(new Color(60, 60, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        backBtn.addActionListener(e -> gameMain.showPanel("MainMenu"));

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Creates each planes panel

    private JPanel createPlanePanel(int index) {

        int planeNumber = PLANE_NUMBERS[index];
        String name = PLANE_NAMES[index];

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 80));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        panel.setPreferredSize(new Dimension(200, 160));
        panel.setMaximumSize(new Dimension(200, 160));

        // loads plane image

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(40, 40, 80));
        imagePanel.setPreferredSize(new Dimension(180, 80));

        BufferedImage planeImage = ImageLoader.loadImage("airplan/" + planeNumber + ".png");
        JLabel imageLabel;

        if (planeImage != null) {
            Image scaledImage = planeImage.getScaledInstance(120, 70, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
        }
        else {
            imageLabel = new JLabel("Plane " + planeNumber);
            imageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            imageLabel.setForeground(Color.WHITE);
        }

        // Adds the plane to the panel

        imagePanel.add(imageLabel);
        panel.add(imagePanel, BorderLayout.CENTER);

        // Name of the plane

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setForeground(Color.YELLOW);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setPreferredSize(new Dimension(200, 25));
        panel.add(nameLabel, BorderLayout.SOUTH);

        return panel;
    }
}