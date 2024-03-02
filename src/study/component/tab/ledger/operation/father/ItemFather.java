package study.component.tab.ledger.operation.father;

import study.component.Frame;
import study.component.tab.ledger.operation.father.label.Label;
import study.entity.ledger.operation.OperationEntity;

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
    protected final OperationEntity operationEntity;

    public ItemFather(Frame frame, OperationEntity operationEntity, String text) {
        this.frame = frame;
        this.operationEntity = operationEntity;

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
