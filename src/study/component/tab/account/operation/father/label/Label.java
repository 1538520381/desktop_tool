package study.component.tab.account.operation.father.label;

import study.Parameter;
import study.component.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 标签
 * @email 1538520381@qq.com
 * @date 2024/3/1 17:02
 */
public class Label extends JLabel {
    private final Frame frame;

    public Label(Frame frame, String text) {
        this.frame = frame;

        setText(text + "：");
        setPreferredSize(new Dimension(Parameter.ACCOUNT_OPERATION_LABEL_WIDTH, Parameter.ACCOUNT_OPERATION_ITEM_HEIGHT));
        setHorizontalAlignment(SwingConstants.RIGHT);

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
