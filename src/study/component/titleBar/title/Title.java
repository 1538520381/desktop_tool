package study.component.titleBar.title;

import study.Parameter;
import study.component.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author Persolute
 * @version 1.0
 * @description 标题
 * @email 1538520381@qq.com
 * @date 2024/2/15 14:50
 */
public class Title extends JLabel {
    private final Frame frame;

    private int x;
    private int y;

    public Title(Frame frame) {
        this.frame = frame;

        setText("   " + Parameter.TITLE + "   ");
        setOpaque(true);
        setBackground(Color.ORANGE);

        addComponent();
        addListener();
    }

    private void addComponent() {
    }

    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                frame.mouseEntered();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.mouseExited(e.getX() + getLocationOnScreen().x - frame.getLocationOnScreen().x, e.getY() + getLocationOnScreen().y - frame.getLocationOnScreen().y);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(frame.getX() + e.getX() - x, frame.getY() + e.getY() - y);
                frame.repaint();
            }
        });
    }
}
