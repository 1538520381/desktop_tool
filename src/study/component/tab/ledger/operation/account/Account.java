package study.component.tab.ledger.operation.account;

import study.component.Frame;
import study.component.tab.ledger.operation.father.ItemFather;
import study.entity.ledger.operation.OperationEntity;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Persolute
 * @version 1.0
 * @description 账号
 * @email 1538520381@qq.com
 * @date 2024/3/2 20:35
 */
public class Account extends ItemFather {
    private JRadioButton wechat;
    private JRadioButton alipay;
    private JRadioButton ABC;

    public Account(Frame frame, OperationEntity operationEntity) {
        super(frame, operationEntity, "账号");

        addComponent();
        addListener();
    }

    private void addComponent() {
        wechat = new JRadioButton("微信", true);
        alipay = new JRadioButton("支付宝");
        ABC = new JRadioButton("农业银行");

        wechat.setActionCommand("0");
        alipay.setActionCommand("1");
        ABC.setActionCommand("2");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(wechat);
        buttonGroup.add(alipay);
        buttonGroup.add(ABC);

        add(wechat);
        add(alipay);
        add(ABC);
    }

    private void addListener() {
        ActionListener actionListener = e -> operationEntity.setType(Integer.parseInt(e.getActionCommand()));
        wechat.addActionListener(actionListener);
        alipay.addActionListener(actionListener);
        ABC.addActionListener(actionListener);
    }
}
