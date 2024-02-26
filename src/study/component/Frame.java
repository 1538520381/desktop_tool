package study.component;

import study.Parameter;
import study.component.titleBar.TitleBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

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
    public Integer frameWidth = Parameter.FRAME_WIDTH;
    public Integer frameHeight = screenSize.height - screenInsets.top - screenInsets.bottom;
    public Integer frameX = screenSize.width - Parameter.FRAME_WIDTH - screenInsets.left - screenInsets.right;
    public Integer frameY = 0;

    private final java.util.Timer displayTimer = new Timer(); // 计时器
    private boolean mouseFlag = false; // 鼠标悬浮标识


    public Frame() {
        setSize(frameWidth, frameHeight);
        setLocation(frameX, frameY);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        addComponent();
        addListener();

        setVisible(true);

        if (!mouseFlag) {
            hidden();
        }
    }

    private void addComponent() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new TitleBar(this), BorderLayout.NORTH);
    }

    private void addListener() {
        Container contentPane = getContentPane();

        contentPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Frame.this.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Frame.this.mouseExited(e);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeiconified(WindowEvent e) {
                setSize(frameWidth, frameHeight);
                setLocation(frameX, frameY);
            }
        });
    }

    public void mouseEntered(MouseEvent e) {
        mouseFlag = true;
        if (getY() < 0) {
            display();
        }
    }

    public void mouseExited(MouseEvent e) {
        if (!getContentPane().contains(e.getPoint())) {
            mouseFlag = false;
            hidden();
        }
    }

    /*
     * @author Persolute
     * @version 1.0
     * @description 窗体显示
     * @email 1538520381@qq.com
     * @date 2024/2/13 14:34
     */
    private void display() {
        displayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getY() == 0) {
                    cancel();
                }
                int newX = getX();
                int newY = Math.min(getY() + Parameter.SPEED, 0);
                setLocation(newX, newY);
            }
        }, 0, 1);
    }

    /*
     * @author Persolute
     * @version 1.0
     * @description 窗体隐藏
     * @email 1538520381@qq.com
     * @date 2024/2/13 14:34
     */
    private void hidden() {
        displayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mouseFlag) {
                    int y = getY();
                    if (!mouseFlag && y == 0) {
                        displayTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (mouseFlag || getY() + getHeight() == 5) {
                                    cancel();
                                }
                                int newX = getX();
                                int newY = Math.max(getY() - Parameter.SPEED, 5 - getHeight());
                                setLocation(newX, newY);
                            }
                        }, 0, 1);
                    }
                }
            }
        }, 500);
    }
}
