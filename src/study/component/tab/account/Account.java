package study.component.tab.account;

import study.Parameter;
import study.component.Frame;
import study.component.tab.account.operation.Operation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 账本
 * @email 1538520381@qq.com
 * @date 2024/3/1 16:10
 */
public class Account extends JPanel {
    private final Frame frame;

    public Account(Frame frame) {
        this.frame = frame;

        addComponent();
        addListener();
    }

    private void addComponent() {
        setLayout(new BorderLayout());

        add(new Operation(frame), BorderLayout.WEST);
    }

    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                frame.mouseEntered();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.mouseExited(e.getX() + getLocationOnScreen().x - frame.getX(), e.getY() + getLocationOnScreen().y - frame.getY());
            }
        });
    }
}
