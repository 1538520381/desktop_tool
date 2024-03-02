package study.component.tab.ledger.operation.amount.amountInput;

import study.Parameter;
import study.component.Frame;
import study.entity.ledger.operation.OperationEntity;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 金额输入框
 * @email 1538520381@qq.com
 * @date 2024/3/2 20:51
 */
public class AmountInput extends JTextField {
    private final Frame frame;
    private final OperationEntity operationEntity;

    public AmountInput(Frame frame, OperationEntity operationEntity) {
        this.frame = frame;
        this.operationEntity = operationEntity;

        setPreferredSize(new Dimension(60, Parameter.ACCOUNT_OPERATION_ITEM_HEIGHT * 3 / 4));

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

                if (!Character.isDigit(ch) && !Character.isISOControl(ch) && ch != '.') {
                    e.consume();
                }
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                operationEntity.setAmount(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                operationEntity.setAmount(getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                operationEntity.setAmount(getText());
            }
        });
    }
}
