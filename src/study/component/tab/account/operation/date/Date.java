package study.component.tab.account.operation.date;

import study.component.Frame;
import study.component.tab.account.operation.date.day.Day;
import study.component.tab.account.operation.date.dayLabel.DayLabel;
import study.component.tab.account.operation.date.month.Month;
import study.component.tab.account.operation.date.monthLabel.MonthLabel;
import study.component.tab.account.operation.date.year.Year;
import study.component.tab.account.operation.date.yearLabel.YearLabel;
import study.component.tab.account.operation.father.ItemFather;

/**
 * @author Persolute
 * @version 1.0
 * @description 时间
 * @email 1538520381@qq.com
 * @date 2024/3/1 16:55
 */
public class Date extends ItemFather {
    public Date(Frame frame) {
        super(frame, "日期");

        addComponent();
        addListener();
    }

    private void addComponent() {
        add(new Year(frame));
        add(new YearLabel(frame));
        add(new Month(frame));
        add(new MonthLabel(frame));
        add(new Day(frame));
        add(new DayLabel(frame));
    }

    private void addListener() {

    }
}
