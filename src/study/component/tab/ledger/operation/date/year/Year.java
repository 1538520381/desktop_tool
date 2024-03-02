package study.component.tab.ledger.operation.date.year;

import study.Parameter;
import study.component.Frame;

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
 * @description 年输入框
 * @email 1538520381@qq.com
 * @date 2024/3/2 16:32
 */
public class Year extends JTextField {
    private final Frame frame;

    public Year(Frame frame) {
        this.frame = frame;

        setText(String.valueOf(LocalDate.now().getYear()));
        setPreferredSize(new Dimension(40, Parameter.ACCOUNT_OPERATION_ITEM_HEIGHT * 3 / 4));

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
