package study.component.titleBar.controlBar;

import study.component.Frame;
import study.component.titleBar.controlBar.closeButton.CloseButton;
import study.component.titleBar.controlBar.minimizeButton.MinimizeButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Persolute
 * @version 1.0
 * @description 控制栏
 * @email 1538520381@qq.com
 * @date 2024/2/26 15:03
 */
public class ControlBar extends JPanel {
    private final Frame frame;

    public ControlBar(Frame frame) {
        this.frame = frame;

        addComponent();
        addListener();
    }

    private void addComponent() {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        add(new MinimizeButton(frame));
        add(new CloseButton(frame));
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
