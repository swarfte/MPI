/*
 * @Author: Chau Lap Tou
 * @Date: 2021-08-25 22:54:39
 * @LastEditors: Chau Lap Tou
 * @LastEditTime: 2021-08-29 15:14:54
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

public class VocabularyCard{
    public static void main(String[] args) throws Exception {
        VocabularyCardGUI VC = new VocabularyCardGUI();
        VC.startGUI();

    }
}

class VocabularyCardGUI{
    public int width = 1200;//寬度
    public int height = 400;//高度
    public Font textFont = new Font ("Dialog",1,130);//顯示文本的字體
    private JFrame mainFrame = new JFrame("英文生字卡");//主框架
    private JPanel mainPanel = new JPanel();//主畫面
    private JLabel wordText = new JLabel();//顯示生字
    public JButton changeButton = new JButton("change");//切換中文英按鈕
    public JButton nextButton = new JButton("next");//下一個生字按鈕
    public JButton lastButton = new JButton("last");//上一個生字按鈕
    public JButton randomButton = new JButton("random");//隨機生字按鈕
    private ArrayList < JButton > buttonList = new ArrayList<>();//存放按鈕的列表
    public Dimension buttonSize = new Dimension(150,50);//設置按鈕大小
    public Font buttonFont = new Font("Dialog",1,30);//按鈕字體的大小
    private JPanel buttonPanel = new JPanel();//放按鈕用的界面
    public  String fileName = "word.properties";//*存放單詞的檔案
    private Properties wordInput = new Properties();//讀取單詞用
    private ArrayList < String > chineseWord = new ArrayList < >();//存放單詞的中文
    private ArrayList< String > englishWord = new ArrayList < >();//存放單詞的英文
    public boolean isChinese = true;//檢測目前的生字卡狀態
    private int currentNumber = 0;//目前的生字位置
    private int wordLength ;//檢測有多少個生字

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
            wordLength = englishWord.size();
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
        buttonList.add(lastButton);
        buttonList.add(changeButton);
        buttonList.add(nextButton);
        buttonList.add(randomButton);

        for (JButton x : buttonList){
            x.setPreferredSize(buttonSize);
            x.setFont(buttonFont);
            buttonPanel.add(x);
        }

        lastButton.addActionListener(new lastListener());
        changeButton.addActionListener(new changeListener());
        nextButton.addActionListener(new nextListener());
        randomButton.addActionListener(new randomListener());

        buttonPanel.setBackground(Color.darkGray);
        mainFrame.getContentPane().add(BorderLayout.SOUTH,buttonPanel);
    }

    public void textSet() {
        wordText.setFont(textFont);
        wordText.setText(chineseWord.get(currentNumber));
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
        public void actionPerformed(ActionEvent ae){
            if (isChinese){
                wordText.setText(englishWord.get(currentNumber));
                isChinese = false;
            }else {
                wordText.setText(chineseWord.get(currentNumber));
                isChinese = true;
            }
        }
    }
    
    class nextListener implements ActionListener {
        public void actionPerformed(ActionEvent ae){
            if (currentNumber < wordLength - 1 ){//不在最後一個單詞的位置
                if (isChinese){
                    wordText.setText(chineseWord.get(currentNumber + 1));
                }else{
                    wordText.setText(englishWord.get(currentNumber + 1));
                }
                currentNumber++;
            }else{
                currentNumber = 0 ;
                if (isChinese){
                    wordText.setText(chineseWord.get(currentNumber));
                }else {
                    wordText.setText(englishWord.get(currentNumber));
                }
            }
        }
    }

    class lastListener implements ActionListener {
        public void actionPerformed(ActionEvent ae){
            if (currentNumber > 0){//不在第一個單詞的位置
                if (isChinese){
                    wordText.setText(chineseWord.get(currentNumber - 1));
                }else {
                    wordText.setText(englishWord.get(currentNumber - 1));
                }
                currentNumber--;
            }else {
                currentNumber = wordLength - 1;
                if (isChinese){
                    wordText.setText(chineseWord.get(currentNumber));
                }else {
                    wordText.setText(englishWord.get(currentNumber));
                }
            }
        }
    }

    class randomListener implements ActionListener{
        public void actionPerformed(ActionEvent ae){
            currentNumber = (int) (Math.random() * (wordLength - 1));//在可選範圍內隨機找一個生字
            if (isChinese){
                wordText.setText(chineseWord.get(currentNumber));
            }else {
                wordText.setText(englishWord.get(currentNumber));
            }
        }
    }
}