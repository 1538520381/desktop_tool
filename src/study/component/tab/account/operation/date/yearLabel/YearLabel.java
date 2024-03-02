package study.component.tab.account.operation.date.yearLabel;

import study.component.Frame;


import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 年标签
 * @email 1538520381@qq.com
 * @date 2024/3/2 16:46
 */
public class YearLabel extends JLabel {
    private final Frame frame;

    public YearLabel(Frame frame) {
        this.frame = frame;

        setText("年");

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
