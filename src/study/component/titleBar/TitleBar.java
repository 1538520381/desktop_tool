package study.component.titleBar;

import study.Parameter;
import study.component.Frame;
import study.component.titleBar.controlBar.ControlBar;
import study.component.titleBar.title.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 标题栏
 * @email 1538520381@qq.com
 * @date 2024/2/15 14:42
 */
public class TitleBar extends JPanel {
    private final Frame frame;

    public TitleBar(Frame frame) {
        this.frame = frame;

        setPreferredSize(new Dimension(Parameter.FRAME_WIDTH, Parameter.TITLE_BAR_HEIGHT));
        setBackground(Color.CYAN);

        addComponent();
        addListener();
    }

    private void addComponent() {
        setLayout(new BorderLayout());
        add(new Panel(), BorderLayout.EAST);
        add(new Title(frame), BorderLayout.WEST);
        add(new ControlBar(frame), BorderLayout.EAST);
    }

    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                frame.mouseEntered();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.mouseExited(e.getX() + getLocationOnScreen().x - frame.getLocationOnScreen().x, e.getY() + getLocationOnScreen().y - frame.getLocationOnScreen().y);
            }
        });
    }
}
