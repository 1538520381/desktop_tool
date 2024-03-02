package study.component.tab.ledger.operation.description;

import study.component.Frame;
import study.component.tab.ledger.operation.description.descriptionInput.DescriptionInput;
import study.component.tab.ledger.operation.father.ItemFather;
import study.entity.ledger.operation.OperationEntity;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 描述
 * @email 1538520381@qq.com
 * @date 2024/3/2 21:15
 */
public class Description extends ItemFather {
    public Description(Frame frame, OperationEntity operationEntity) {
        super(frame, operationEntity, operationEntity.getType() == 0 ? "来源" : operationEntity.getType() == 1 ? "用途" : "原因");

        addComponent();
        addListener();
    }

    private void addComponent() {
        add(new DescriptionInput(frame, operationEntity));
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
