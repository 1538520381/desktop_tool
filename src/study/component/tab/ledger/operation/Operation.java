package study.component.tab.ledger.operation;

import study.Parameter;
import study.component.Frame;
import study.component.tab.ledger.operation.account.Account;
import study.component.tab.ledger.operation.amount.Amount;
import study.component.tab.ledger.operation.date.Date;
import study.component.tab.ledger.operation.description.Description;
import study.component.tab.ledger.operation.type.Type;
import study.entity.ledger.operation.OperationEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 操作区
 * @email 1538520381@qq.com
 * @date 2024/3/1 16:42
 */
public class Operation extends JPanel {
    private final Frame frame;
    private final OperationEntity operationEntity;

    public Operation(Frame frame) {
        this.frame = frame;
        this.operationEntity = new OperationEntity();

        setPreferredSize(new Dimension(Parameter.FRAME_WIDTH * 2 / 3, (frame.frameHeight - Parameter.TITLE_BAR_HEIGHT) / 3));

        addComponent();
        addListener();
    }

    private void addComponent() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(new Date(frame, operationEntity));
        add(new Type(frame, operationEntity));
        add(new Account(frame, operationEntity));
        add(new Amount(frame, operationEntity));
        add(new Description(frame, operationEntity));
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
