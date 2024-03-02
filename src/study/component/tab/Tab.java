package study.component.tab;

import study.component.Frame;
import study.component.tab.account.Account;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 选项卡
 * @email 1538520381@qq.com
 * @date 2024/3/1 15:55
 */
public class Tab extends JTabbedPane {
    private final Frame frame;

    public Tab(Frame frame) {
        this.frame = frame;

        addComponent();
        addListener();
    }

    private void addComponent() {
        addTab("账本", new Account(frame));
    }

    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                frame.mouseEntered();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.mouseExited(e.getX() + getLocationOnScreen().x - frame.getX(), e.getX() + getLocationOnScreen().y - frame.getY());
            }
        });
    }
}
