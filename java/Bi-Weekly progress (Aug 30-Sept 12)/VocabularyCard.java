/*
 * @Author: Swarfte
 * @Date: 2021-08-25 22:54:39
 * @LastEditors: Swarfte
 * @LastEditTime: 2021-08-29 13:19:51
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
    int width = 1000;//寬度
    int height = 400;//高度
    int fontSize = 125;//字體大小
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
    int wordLength ;//檢測有多少個生字

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
        lastButton.addActionListener(new lastListener());
        buttonPanel.add(lastButton);

        changeButton.addActionListener(new changeListener());
        buttonPanel.add(changeButton);
        
        nextButton.addActionListener(new nextListener());
        buttonPanel.add(nextButton);

        buttonPanel.setBackground(Color.darkGray);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        mainFrame.getContentPane().add(BorderLayout.SOUTH,buttonPanel);
    }

    public void textSet() {
        wordText.setFont(new Font("Dialog",1,fontSize));
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
            if (CurrentNumber < wordLength - 1 ){//不在最後一個單詞的位置
                if (isChinese){
                    wordText.setText(chineseWord.get(CurrentNumber + 1));
                }else{
                    wordText.setText(englishWord.get(CurrentNumber + 1));
                }
                CurrentNumber++;
            }else{
                CurrentNumber = 0 ;
                if (isChinese){
                    wordText.setText(chineseWord.get(CurrentNumber));
                }else {
                    wordText.setText(englishWord.get(CurrentNumber));
                }
            }
        }
    }

    class lastListener implements ActionListener {
        public void actionPerformed(ActionEvent ae){
            if (CurrentNumber > 0){//不在第一個單詞的位置
                if (isChinese){
                    wordText.setText(chineseWord.get(CurrentNumber - 1));
                }else {
                    wordText.setText(englishWord.get(CurrentNumber - 1));
                }
                CurrentNumber--;
            }else {
                CurrentNumber = wordLength - 1;
                if (isChinese){
                    wordText.setText(chineseWord.get(CurrentNumber));
                }else {
                    wordText.setText(englishWord.get(CurrentNumber));
                }
            }
        }
    }
}