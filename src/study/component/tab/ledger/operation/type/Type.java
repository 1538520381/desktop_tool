package study.component.tab.ledger.operation.type;

import study.component.Frame;
import study.component.tab.ledger.operation.father.ItemFather;
import study.entity.ledger.operation.OperationEntity;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Persolute
 * @version 1.0
 * @description 收支
 * @email 1538520381@qq.com
 * @date 2024/3/2 19:56
 */
public class Type extends ItemFather {
    private JRadioButton income;
    private JRadioButton outcome;
    private JRadioButton correction;

    public Type(Frame frame, OperationEntity operationEntity) {
        super(frame, operationEntity, "收支");

        addComponent();
        addListener();
    }

    private void addComponent() {
        income = new JRadioButton("收入", true);
        outcome = new JRadioButton("支出");
        correction = new JRadioButton("校正");

        income.setActionCommand("0");
        outcome.setActionCommand("1");
        correction.setActionCommand("2");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(income);
        buttonGroup.add(outcome);
        buttonGroup.add(correction);

        add(income);
        add(outcome);
        add(correction);
    }

    private void addListener() {
        ActionListener actionListener = e -> operationEntity.setType(Integer.parseInt(e.getActionCommand()));
        income.addActionListener(actionListener);
        outcome.addActionListener(actionListener);
        correction.addActionListener(actionListener);
    }
}
