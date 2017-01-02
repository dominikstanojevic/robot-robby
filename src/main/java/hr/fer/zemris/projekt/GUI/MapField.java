package hr.fer.zemris.projekt.GUI;

import hr.fer.zemris.projekt.grid.Field;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class MapField extends JComponent {

    private static final long serialVersionUID = -8621030149139135400L;

    private Field field;
    private boolean editingEnabled = false;
    private boolean isCurrent = false;
    private boolean isPickup;
    private boolean isWallHit;

    private Image empty;
    private static final String emptyPath = "images/grass.png";
    private Image bottle;
    private static final String bottlePath = "images/bottle.png";
    private Image emptyRobby;
    private static final String emptyRobbyPath = "images/roby.png";
    private Image bottleRobby;
    private static final String bottleRobbyPath = "images/roby-bottle.png";
    private Image emptyPickup;
    private static final String emptyPickupPath = "images/empty-pickup.png";
    private Image pickup;
    private static final String pickupPath = "images/pickup.png";
    private Image hitWall;
    private static final String hitWallPath = "images/roby-wall.png";
    private Image bottleHitWall;
    private static final String bottleHitWallPath = "images/roby-bottle-wall.png";

    public MapField(Field field) {

        setSize(new Dimension(20, 20));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.field = field;

        loadImages();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (editingEnabled) {
                    toggleFieldValue();
                }

            }

        });
    }

    private void loadImages() {

        empty = loadImage(emptyPath);
        bottle = loadImage(bottlePath);
        emptyRobby = loadImage(emptyRobbyPath);
        bottleRobby = loadImage(bottleRobbyPath);
        pickup = loadImage(pickupPath);
        emptyPickup = loadImage(emptyPickupPath);
        hitWall = loadImage(hitWallPath);
        bottleHitWall = loadImage(bottleHitWallPath);
    }

    private Image loadImage(String path) {

        File file = new File(path);
        try {
            Image image = ImageIO.read(file);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MapField() {
        this(Field.EMPTY);
    }

    public void setField(Field field) {
        this.field = field;

        repaint();
        revalidate();
    }

    public Field getField() {
        return field;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;

        repaint();
        revalidate();
    }

    public void setPickup(boolean isPickup) {
        this.isPickup = isPickup;

        repaint();
        revalidate();
    }

    public void setWallHit(boolean isWallHit) {
        this.isWallHit = isWallHit;

        repaint();
        revalidate();
    }

    private void toggleFieldValue() {

        if (field == Field.BOTTLE)
            field = Field.EMPTY;
        else if (field == Field.EMPTY)
            field = Field.BOTTLE;

        repaint();
        revalidate();

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (field == Field.EMPTY) {

            if (isPickup) {

                g.drawImage(emptyPickup, 0, 0, getWidth(), getHeight(), this);

            } else if (isWallHit) {

                g.drawImage(hitWall, 0, 0, getWidth(), getHeight(), this);

            } else if (isCurrent) {

                g.drawImage(emptyRobby, 0, 0, getWidth(), getHeight(), this);

            } else {
                g.drawImage(empty, 0, 0, getWidth(), getHeight(), this);
            }

        } else if (field == Field.BOTTLE) {

            if (isPickup) {
                g.drawImage(pickup, 0, 0, getWidth(), getHeight(), this);

            } else if (isWallHit) {
                g.drawImage(bottleHitWall, 0, 0, getWidth(), getHeight(), this);

            } else if (isCurrent) {
                g.drawImage(bottleRobby, 0, 0, getWidth(), getHeight(), this);

            } else {
                g.drawImage(bottle, 0, 0, getWidth(), getHeight(), this);
            }
        }

    }

    public void setEditingEnabled(boolean b) {

        this.editingEnabled = b;

    }

}
