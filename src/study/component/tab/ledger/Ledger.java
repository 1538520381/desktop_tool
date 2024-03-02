package study.component.tab.ledger;

import study.component.Frame;
import study.component.tab.ledger.operation.Operation;

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
public class Ledger extends JPanel {
    private final Frame frame;

    public Ledger(Frame frame) {
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
