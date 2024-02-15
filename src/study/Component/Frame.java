package study.Component;

import study.Parameter;

import javax.swing.*;
import java.awt.*;

/**
 * @author Persolute
 * @version 1.0
 * @description 窗体
 * @email 1538520381@qq.com
 * @date 2024/2/15 14:16
 */
public class Frame extends JFrame {
    private final Dimension screenSize = getToolkit().getScreenSize(); // 屏幕尺寸
    private final Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration()); // 工作区边距


    public Frame() {
        setSize(Parameter.FRAMEWIDTH, screenSize.height - screenInsets.top - screenInsets.bottom);
        setLocation(screenSize.width - Parameter.FRAMEWIDTH - screenInsets.left - screenInsets.right, 0);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        setLayout(new BorderLayout());
        addComponent();
        addListener();

        setVisible(true);
    }

    private void addComponent() {
        Container contentPane = getContentPane();
    }

    private void addListener() {
        Container contentPane = getContentPane();
    }
}
