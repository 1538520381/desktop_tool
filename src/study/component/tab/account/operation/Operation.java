package study.component.tab.account.operation;

import study.Parameter;
import study.component.Frame;
import study.component.tab.account.operation.date.Date;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 操作区
 * @email 1538520381@qq.com
 * @date 2024/3/1 16:42
 */
public class Operation extends JPanel {
    private final Frame frame;

    public Operation(Frame frame) {
        this.frame = frame;

        setPreferredSize(new Dimension(Parameter.FRAME_WIDTH / 2, (frame.frameHeight - Parameter.TITLE_BAR_HEIGHT) / 3));

        addComponent();
        addListener();
    }

    private void addComponent() {
        setLayout(new FlowLayout());

        add(new Date(frame));
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
