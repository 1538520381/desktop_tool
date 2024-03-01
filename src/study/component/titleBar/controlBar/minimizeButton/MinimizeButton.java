package study.component.titleBar.controlBar.minimizeButton;

import study.Parameter;
import study.component.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 最小化按钮
 * @email 1538520381@qq.com
 * @date 2024/2/26 15:33
 */
public class MinimizeButton extends JButton {
    private final Frame frame;

    public MinimizeButton(Frame frame) {
        this.frame = frame;

        setText("-");
        setPreferredSize(new Dimension(Parameter.CONTROL_BAR_BUTTON_WIDTH, Parameter.TITLE_BAR_HEIGHT));
        setFocusable(false);

        addComponent();
        addListener();
    }

    private void addComponent() {

    }

    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.frameWidth = frame.getWidth();
                frame.frameHeight = frame.getHeight();
                frame.frameX = frame.getX();
                frame.frameY = frame.getY();

                frame.setState(JFrame.ICONIFIED);
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
    }
}
