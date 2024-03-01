package study.component.titleBar.controlBar.closeButton;

import study.Parameter;
import study.component.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 关闭按钮
 * @email 1538520381@qq.com
 * @date 2024/2/26 15:14
 */
public class CloseButton extends JButton {
    private final Frame frame;

    public CloseButton(Frame frame) {
        this.frame = frame;

        setText("x");
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
                System.exit(0);
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
