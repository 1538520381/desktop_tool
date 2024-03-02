package study.component.tab.ledger.operation.amount;

import study.component.Frame;
import study.component.tab.ledger.operation.amount.amountInput.AmountInput;
import study.component.tab.ledger.operation.amount.amountLabel.AmountLabel;
import study.component.tab.ledger.operation.father.ItemFather;

/**
 * @author Persolute
 * @version 1.0
 * @description 金额
 * @email 1538520381@qq.com
 * @date 2024/3/2 20:43
 */
public class Amount extends ItemFather {
    public Amount(Frame frame) {
        super(frame, "金额");

        addComponent();
        addListener();
    }

    private void addComponent() {
        add(new AmountInput(frame));
        add(new AmountLabel(frame));
    }

    private void addListener() {

    }
}
