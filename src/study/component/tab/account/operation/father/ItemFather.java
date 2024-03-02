package study.component.tab.account.operation.father;

import study.Parameter;
import study.component.Frame;
import study.component.tab.account.operation.father.label.Label;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 表单父类
 * @email 1538520381@qq.com
 * @date 2024/3/2 18:11
 */
public class ItemFather extends JPanel {
    protected final Frame frame;

    public ItemFather(Frame frame, String text) {
        this.frame = frame;

        setPreferredSize(new Dimension(Parameter.FRAME_WIDTH / 2, Parameter.ACCOUNT_OPERATION_ITEM_HEIGHT));

        addComponent(text);
        addListener();
    }

    private void addComponent(String text) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        add(new Label(frame, text));
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
