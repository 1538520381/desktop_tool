package study.component.tab.ledger.operation.amount;

import study.component.Frame;
import study.component.tab.ledger.operation.amount.amountInput.AmountInput;
import study.component.tab.ledger.operation.amount.amountLabel.AmountLabel;
import study.component.tab.ledger.operation.father.ItemFather;
import study.entity.ledger.operation.OperationEntity;

/**
 * @author Persolute
 * @version 1.0
 * @description 金额
 * @email 1538520381@qq.com
 * @date 2024/3/2 20:43
 */
public class Amount extends ItemFather {
    public Amount(Frame frame, OperationEntity operationEntity) {
        super(frame, operationEntity, "金额");

        addComponent();
        addListener();
    }

    private void addComponent() {
        add(new AmountInput(frame, operationEntity));
        add(new AmountLabel(frame));
    }

    private void addListener() {

    }
}
