/*
 * @Author: Swarfte
 * @Date: 2021-08-25 22:54:39
 * @LastEditors: Swarfte
 * @LastEditTime: 2021-08-29 00:53:59
 * @python_exe: pyinstaller -F -w file_name.py -p C:/python/lib/site-packages 
 * @java_class: javac -encoding utf-8 file_name.java
 * @java_jar: jar -cvmf manifest.txt name.jar *.class
 * @GithubName: Swarfte
 */

import java.awt.*;
import javax.swing.*;
import java.nio.charset.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Method;

public class VocabularyCard{
    public static void main(String[] args) throws Exception {
        VocabularyCardGUI VC = new VocabularyCardGUI();
        VC.startGUI();

        // //!測試用
        // Method VCTest =  VC.getClass().getDeclaredMethod("test");
        // VCTest.setAccessible(true);
        // VCTest.invoke(VC);
    }
}

class VocabularyCardGUI{
    int width = 700;
    int height = 300;
    JFrame mainFrame = new JFrame("英文生字卡");//主框架
    JPanel mainPanel = new JPanel();//主畫面
    JLabel wordText = new JLabel();//顯示生字
    JButton changeButton = new JButton("change");//切換中文英按鈕
    JButton nextButton = new JButton("next");//下一個生字按鈕
    JButton lastButton = new JButton("last");//上一個生字按鈕
    JPanel buttonPanel = new JPanel();//放按鈕用的布局
    String fileName = "word.properties";//*存放單詞的檔案
    Properties wordInput = new Properties();//讀取單詞用
    ArrayList < String > chineseWord = new ArrayList < >();//存放單詞的中文
    ArrayList< String > englishWord = new ArrayList < >();//存放單詞的英文
    boolean isChinese = true;//檢測目前的生字卡狀態
    int CurrentNumber = 0;//目前的生字位置

    private void test(){
        loadDictionary();
        System.out.println(chineseWord.get(1));
        System.out.println(englishWord.get(1));
    }

    public void loadDictionary() {
        try {
            wordInput.load(new FileReader(fileName, StandardCharsets.UTF_8));//!用utf-8編碼讀取文件
            Iterator<String> it = wordInput.stringPropertyNames().iterator();//&用迭代器讀取設定檔的名稱
            while(it.hasNext()){
                String key = it.next();//讀取字典中的key值
                String value = wordInput.getProperty(key); //讀取key對應的值
                englishWord.add(key);
                chineseWord.add(value);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildGUI(){
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width,height);//設置畫面大小
        mainFrame.setVisible(true);//把主畫面設置為可見的
    }

    public void buttonSet(){
        buttonPanel.add(lastButton);

        changeButton.addActionListener(new changeListener());
        buttonPanel.add(changeButton);
        
        buttonPanel.add(nextButton);
        buttonPanel.setBackground(Color.darkGray);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        mainFrame.getContentPane().add(BorderLayout.SOUTH,buttonPanel);
    }

    public void textSet() {
        wordText.setFont(new Font("Dialog",1,100));
        wordText.setText(chineseWord.get(CurrentNumber));
        mainPanel.add(wordText);
        mainFrame.getContentPane().add(BorderLayout.CENTER,mainPanel);
    }

    public void startGUI(){
        loadDictionary();
        buttonSet();
        textSet();
        buildGUI();
    }

    class changeListener implements ActionListener {
        boolean isChinese = true;//檢測目前的生字卡狀態
        public void actionPerformed(ActionEvent ae){
            if (isChinese){
                wordText.setText(englishWord.get(CurrentNumber));
                isChinese = false;
            }else {
                wordText.setText(chineseWord.get(CurrentNumber));
                isChinese = true;
            }
        }
    }

    class nextListener implements ActionListener {
        public void actionPerformed(ActionEvent ae){
    
        }
    }

    class lastListener implements ActionListener {
        public void actionPerformed(ActionEvent ae){
            
        }
    }
}