import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;


public class TestResponse {

	public static void main(String[] args) {
        // TODO 自动生成的方法存根
        new TestResponseGUI().startGUI();
	}

}

class TestResponseGUI{
	public int width = 400;//寬度
    public int height = 400;//高度
    public Font textFont = new Font ("Dialog", Font.BOLD,40);
    private JFrame mainFrame = new JFrame("測試反應");//主框架
    private JPanel mainPanel = new JPanel();//主畫面
    private JPanel buttonJPanel = new JPanel();//放按鈕用的界面
    public JButton clickButton = new JButton("Start");//測試反應的按鈕
    private JLabel useTime = new JLabel("");//顯示反應時間
    private boolean canTouch = false;//判斷是否能點擊
    private boolean isStart = false;//判斷是否啟動測試程式
    long startTime ;//計算啟動點擊間隔
    double time;//隨機時間
    double interval;//判斷兩次點擊的間隔

    public void startGUI(){//啟動主畫面
        buttonSet();
        BuildGUI();
    }

    public void startTest(){//點擊按鈕時啟動測試
            isStart = true;
            time = Math.random() * 5 * 1000;//生成0~7秒的數字
    }

    public void checkTouch(){//檢測第2次點擊是否符合時間
        double currentTime = System.nanoTime();//用作比較當前的時候
        interval = currentTime - startTime;//計算相差的時間
        if (interval - time > 0){
            canTouch = true;
        }
    }

    public void changeBackgroundColor (){
        try {
            TimeUnit.MILLISECONDS.sleep((long) time);
            mainPanel.setBackground(Color.red);

            useTime.setText("");
            clickButton.setText("Click");
            clickButton.setBackground(Color.CYAN);
            clickButton.setForeground(Color.RED);
            startTime = System.nanoTime();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void textShowTime(){//顯示兩次間佢的時間
        String showTime = interval / 1000000 + "ms";//轉換單位
        useTime.setText(showTime);//顯示時間
    }


    public void BuildGUI(){//設置主畫面

        mainPanel.add(useTime);
        useTime.setFont(textFont);
        useTime.setForeground(Color.CYAN);

        mainPanel.setBackground(Color.white);
        mainFrame.getContentPane().add(BorderLayout.CENTER,mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width,height);//設置畫面大小
        mainFrame.setVisible(true);//把主畫面設置為可見的
    }

    public void buttonSet(){//設置按鈕的類
        clickButton.setFont(textFont);
        clickButton.addActionListener(new clickListener());
        buttonJPanel.add(clickButton);
        buttonJPanel.setBackground(Color.BLACK);
        mainFrame.getContentPane().add(BorderLayout.SOUTH,buttonJPanel);
    }

    public void reStart(){//重新測試
        clickButton.setBackground(Color.white);
        clickButton.setForeground(Color.BLACK);
        clickButton.setText("Start");

        isStart = false;
        canTouch = false;
        mainPanel.setBackground(Color.white);
    }

    class clickListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!isStart) {//初始啟動時
                startTest();
                changeBackgroundColor();
            }else {//再次點抨(測試反應)
                checkTouch();
            }
            if (isStart && canTouch ) {//切換畫面前點擊
                textShowTime();
                reStart();
            }
        }
    }
}