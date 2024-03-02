package study.component.tab.ledger.operation.description.descriptionInput;

import study.component.Frame;
import study.entity.ledger.operation.OperationEntity;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 描述输入框
 * @email 1538520381@qq.com
 * @date 2024/3/2 22:22
 */
public class DescriptionInput extends JScrollPane {
    private final Frame frame;
    private final OperationEntity operationEntity;
    private JTextArea jTextArea;

    public DescriptionInput(Frame frame, OperationEntity operationEntity) {
        this.frame = frame;
        this.operationEntity = operationEntity;

        setPreferredSize(new Dimension(175, 50));
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        addComponent();
        addListener();
    }

    private void addComponent() {
        jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        setViewportView(jTextArea);

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

        jTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                operationEntity.setDescription(jTextArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                operationEntity.setDescription(jTextArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                operationEntity.setDescription(jTextArea.getText());
            }
        });
    }
}
