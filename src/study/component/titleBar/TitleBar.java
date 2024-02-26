package study.component.titleBar;

import study.Parameter;
import study.component.Frame;
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

        setPreferredSize(new Dimension(Parameter.FRAME_WIDTH, 40));
        setBackground(Color.CYAN);

        addComponent();
        addListener();
    }

    private void addComponent() {
        setLayout(new BorderLayout());

        add(new Title(frame), BorderLayout.WEST);
    }

    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                frame.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.mouseExited(e);
            }
        });
    }
}
