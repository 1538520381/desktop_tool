package study.component.tab.account.operation.date.day;

import study.Parameter;
import study.component.Frame;
import study.component.tab.account.operation.date.Date;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

/**
 * @author Persolute
 * @version 1.0
 * @description 天输入框
 * @email 1538520381@qq.com
 * @date 2024/3/2 16:53
 */
public class Day extends JTextField {
    private final Frame frame;

    public Day(Frame frame) {
        this.frame = frame;

        setText(String.valueOf(LocalDate.now().getDayOfMonth()));
        setPreferredSize(new Dimension(25, Parameter.ACCOUNT_OPERATION_ITEM_HEIGHT * 3 / 4));

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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();

                if (ch == KeyEvent.VK_BACK_SPACE || ch == KeyEvent.VK_DELETE) {
                    return;
                }

                if (!Character.isDigit(ch) && !Character.isISOControl(ch)) {
                    e.consume();
                }
            }
        });
    }
}
