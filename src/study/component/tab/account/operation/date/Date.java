package study.component.tab.account.operation.date;

import study.Parameter;
import study.component.Frame;
import study.component.tab.account.operation.date.day.Day;
import study.component.tab.account.operation.date.dayLabel.DayLabel;
import study.component.tab.account.operation.date.label.Label;
import study.component.tab.account.operation.date.month.Month;
import study.component.tab.account.operation.date.monthLabel.MonthLabel;
import study.component.tab.account.operation.date.year.Year;
import study.component.tab.account.operation.date.yearLabel.YearLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 时间
 * @email 1538520381@qq.com
 * @date 2024/3/1 16:55
 */
public class Date extends JPanel {
    private final Frame frame;

    public Date(Frame frame) {
        this.frame = frame;

        setPreferredSize(new Dimension(Parameter.FRAME_WIDTH / 2, Parameter.ACCOUNT_OPERATION_ITEM_HEIGHT));

        addComponent();
        addListener();
    }

    private void addComponent() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        add(new Label(frame));
        add(new Year(frame));
        add(new YearLabel(frame));
        add(new Month(frame));
        add(new MonthLabel(frame));
        add(new Day(frame));
        add(new DayLabel(frame));
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
    }
}
