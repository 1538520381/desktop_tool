package study.component.tab.ledger.operation.account;

import study.component.Frame;
import study.component.tab.ledger.operation.father.ItemFather;

import javax.swing.*;

/**
 * @author Persolute
 * @version 1.0
 * @description 账号
 * @email 1538520381@qq.com
 * @date 2024/3/2 20:35
 */
public class Account extends ItemFather {
    public Account(Frame frame) {
        super(frame, "账号");

        addComponent();
        addListener();
    }

    private void addComponent() {
        JRadioButton wechat = new JRadioButton("微信");
        JRadioButton alipay = new JRadioButton("支付宝");
        JRadioButton ABC = new JRadioButton("农业银行");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(wechat);
        buttonGroup.add(alipay);
        buttonGroup.add(ABC);
        add(wechat);
        add(alipay);
        add(ABC);
        wechat.setSelected(true);
    }

    private void addListener() {

    }
}
