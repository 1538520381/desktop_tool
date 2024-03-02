package study.component.tab.ledger.operation.date;

import study.component.Frame;
import study.component.tab.ledger.operation.date.day.Day;
import study.component.tab.ledger.operation.date.dayLabel.DayLabel;
import study.component.tab.ledger.operation.date.month.Month;
import study.component.tab.ledger.operation.date.monthLabel.MonthLabel;
import study.component.tab.ledger.operation.date.year.Year;
import study.component.tab.ledger.operation.date.yearLabel.YearLabel;
import study.component.tab.ledger.operation.father.ItemFather;
import study.entity.ledger.operation.OperationEntity;

/**
 * @author Persolute
 * @version 1.0
 * @description 时间
 * @email 1538520381@qq.com
 * @date 2024/3/1 16:55
 */
public class Date extends ItemFather {
    public Date(Frame frame, OperationEntity operationEntity) {
        super(frame, operationEntity, "日期");

        addComponent();
        addListener();
    }

    private void addComponent() {
        add(new Year(frame, operationEntity));
        add(new YearLabel(frame));
        add(new Month(frame, operationEntity));
        add(new MonthLabel(frame));
        add(new Day(frame, operationEntity));
        add(new DayLabel(frame));
    }

    private void addListener() {

    }
}
