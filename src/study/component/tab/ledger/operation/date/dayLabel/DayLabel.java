package study.component.tab.ledger.operation.date.dayLabel;

import study.component.Frame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 日标签
 * @email 1538520381@qq.com
 * @date 2024/3/2 16:54
 */
public class DayLabel extends JLabel {
    private final Frame frame;

    public DayLabel(Frame frame) {
        this.frame = frame;

        setText("日");

        addComponent();
        addListener();
    }

    private void addComponent() {

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
