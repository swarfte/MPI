import java.awt.*;
import javax.swing.*;

import java.awt.*;
import javax.swing.*;
import java.nio.charset.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class TestResponse {

	public static void main(String[] args) {
        // TODO 自动生成的方法存根
        new TestResponseGUI().startGUI();
	}

}

class TestResponseGUI{
	public int width = 600;//寬度
    public int height = 600;//高度
    public Font textFont = new Font ("Dialog", Font.BOLD,20);
    private JFrame mainFrame = new JFrame("測試反應");//主框架
    private JPanel mainPanel = new JPanel();//主畫面
    private JPanel buttonJPanel = new JPanel();//放按鈕用的界面
    public JButton click = new JButton("點擊開始");//測試反應的按鈕
    private boolean canTouch = false;//判斷是否能點擊

    public void startGUI(){//啟動主畫面
        buttonSet();
        BuildGUI();
    }

    public void BuildGUI(){//設置主畫面
        mainFrame.getContentPane().add(BorderLayout.CENTER,mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width,height);//設置畫面大小
        mainFrame.setVisible(true);//把主畫面設置為可見的
    }

    public void buttonSet(){//設置按鈕的類
        click.addActionListener(new clickActionListener());
        buttonJPanel.add(click);
        buttonJPanel.setBackground(Color.BLACK);
        mainFrame.getContentPane().add(BorderLayout.SOUTH,buttonJPanel);
    }

    class clickActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}