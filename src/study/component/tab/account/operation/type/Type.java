package study.component.tab.account.operation.type;

import study.component.Frame;
import study.component.tab.account.operation.father.ItemFather;

import javax.swing.*;

/**
 * @author Persolute
 * @version 1.0
 * @description 收支
 * @email 1538520381@qq.com
 * @date 2024/3/2 19:56
 */
public class Type extends ItemFather {
    public Type(Frame frame) {
        super(frame, "收支");

        addComponent();
        addListener();
    }

    private void addComponent() {
        JRadioButton income = new JRadioButton("收入");
        JRadioButton outcome = new JRadioButton("支出");
        JRadioButton correction = new JRadioButton("校正");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(income);
        buttonGroup.add(outcome);
        buttonGroup.add(correction);
        add(income);
        add(outcome);
        add(correction);
        income.setSelected(true);
    }

    private void addListener() {

    }
}
