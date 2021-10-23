import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;

public class Music{//啟動的類
    public static void main(String[] args) {//啟動程式
        BeatBox MusicBox = new BeatBox();//生成主程式
        MusicBox.buildGUI();//啟動連鎖反應
    }
}
class BeatBox{//音樂盒

    JPanel mainPanel;//主畫面
    ArrayList<JCheckBox> checkboxList;//檢測用的列表
    Sequencer sequencer;//音樂的隊列(CD播放機)
    Sequence sequence;//讀取track的對像(CD碟)
    Track track;//加入音色用的(CD碟的內容(聲音))
    JFrame theFrame;//主框架
    int BaseBPM = 40;//設定初始的BPM值

    String[] instrumentName = {
        "大鼓",
        "閉擊腳踏鈸",
        "開擊腳踏鈸",
        "軍鼓",
        "碎音鈸",
        "拍手",
        "高音鼓",
        "高音小鼓",
        "砂槌",
        "吹口哨",
        "低音手鼓",
        "牛铃",
        "振盪器",
        "低樂鼓",
        "高音撞鈴",
        "開音高音手鼓"
    };//顯示的選項

    int instrumentNumber = instrumentName.length;//統計有多少種樂器

    int[] instruments = {35,45,46,38,49,39,50,60,70,72,64,56,78,47,67,63};//對應樂器的關鍵字
    
    public void buildGUI(){//創建畫面

        //主框架的部份
        theFrame = new JFrame("簡易音樂盒");//設置標題並創建一個框架
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//設定關閉畫面同時關閉進程


        //主畫面的部份
        BorderLayout layout = new BorderLayout();//設置布局管理器
        JPanel background = new JPanel(layout);//初如化框架並傳入選用的布局管理器
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//設定主畫面上的組件間的空白邊緣,傳入的參數分別對應上下左右

        //檢測按鈕的部份
        checkboxList = new ArrayList<JCheckBox>();//生成一個存放檢測按鈕的列表

        //控制按鈕的佈局器部份
        Box buttonBox = new Box(BoxLayout.Y_AXIS);//設定按鈕的佈局方式,用垂直的方式佈局

        //播放按鈕的部份
        JButton start = new JButton("開始");//創建播放按鈕並顯示為開始
        start.addActionListener(new MyStartListener());//把播放按鈕加入監聽中,用作檢測動作
        buttonBox.add(start);//把播放按鈕放入buttonBox中(用垂直的形式排序)
        buttonBox.add(new JLabel(" "));//用作排版

        //結束按鈕的部份
        JButton stop = new JButton("結束");//創建結束按鈕並顯示為結束
        stop.addActionListener(new MyStopListener());//把結束按鈕加入監聽中,用作檢測動作
        buttonBox.add(stop);//把結束按鈕放入buttonBox中(用垂直的形式排序)
        buttonBox.add(new JLabel(" "));//用作排版

        //加快節奏的按鈕的部份
        JButton upTemPo = new JButton("節奏加快");//創建加快節奏的按鈕並顯示為節奏加快
        upTemPo.addActionListener(new MyUpTempoListener());//把加快節奏的按鈕加入監聽中,用作檢測動作
        buttonBox.add(upTemPo);//把加快節奏的按鈕放入buttonBox中(用垂直的形式排序)
        buttonBox.add(new JLabel(" "));//用作排版

        //減慢節奏的按鈕的部份
        JButton downTemPo = new JButton("節奏減慢");//創建減慢節奏的按鈕並顯示為節奏加減慢
        downTemPo.addActionListener(new MyDownTempoListener());//把減慢節奏的按鈕加入監聽中,用作檢測動作
        buttonBox.add(downTemPo);//把減慢節奏的按鈕放入buttonBox中(用垂直的形式排序)

        //顯示樂器名稱的佈局器部份
        Box nameBox = new Box(BoxLayout.Y_AXIS);//設定樂器名稱的佈局方式,用垂直的方式佈局
        for (int i = 0 ; i < instrumentNumber ; i++){//對每件樂器循環一次
            nameBox.add(new JLabel(instrumentName[i]));//把樂器列表中的元素都加入佈局器中(用垂直的方式排序)
            nameBox.add(new JLabel(" "));//重要,加入空白行調整樂器名稱的間距,實現排版的作用
        }

        //背景佈局器的部份
        background.add(BorderLayout.EAST,buttonBox);//在主畫面的右方加入功能按鈕的列表
        background.add(BorderLayout.WEST,nameBox);//在主畫面的左方加入樂器名稱的列表

        //把主畫面填入主框架
        theFrame.getContentPane().add(background);//用預設的佈局器放入主畫面

        //樂器時間軸按鈕佈的佈局器部份
        GridLayout grid = new GridLayout(instrumentNumber,instrumentNumber);//根據樂器的多少創建出不同大小的時間軸,預設是16*16
        grid.setVgap(1);//設置不同元素的垂直間隔的距離,用像素作為單位
        grid.setHgap(2);//設置不同元素的水平間隔的距離,用像素作為單位
        mainPanel = new JPanel(grid);//用樂器時間軸的佈局方式初始化一個圖像顯示界面
        background.add(BorderLayout.CENTER,mainPanel);//在主畫面的中間部份插入顯示界面
        
        //樂器時間軸按鈕的部份
        for (int i = 0 ; i < instrumentNumber * instrumentNumber ; i++){//有N樣樂器則實現N^2個按鈕
            JCheckBox c = new JCheckBox();//創建一個檢測按鈕
            c.setSelected(false);//設置按鈕默認是未選取的狀態
            checkboxList.add(c);//把生成的按鈕加入到檢測按鈕的列表中
            mainPanel.add(c);//把生成的按鈕加入到時間軸的佈局器中
        }

        //關於midi的初始化
        setUpMidi();//執行初始化的代碼,共打開隊列和track

        //啟動畫面並設為可見的
        theFrame.setBounds(50,30,300,300);//設置畫面的大小
        theFrame.pack();//自動排序組件的位置
        theFrame.setVisible(true);//把主畫面設置為可見的

    }

    public void setUpMidi(){//用作midi的初始化類
        try{//嘗試運行這段有風險的代碼

            //初始化部份的播放設定
            sequencer = MidiSystem.getSequencer();//創建一個隊列,用來存放sequence對像
            sequencer.open();//打開隊列
            sequence = new Sequence(Sequence.PPQ,4);//新增一個讀取track內容的對像
            track = sequence.createTrack();//創話track來儲存midi中的音色
            sequencer.setTempoInBPM(BaseBPM);//設置默認的播放速度
            
        }catch(Exception e){//運行時捕捉到異常的話
            e.printStackTrace();//翻回異動的情況
        }
        
    }

    public void buildTrackAndStart(){//用作創建track和播放所選聲音
        //創建一個數組用來儲存一項樂器的值
        int[] trackList = null;//如果某一節需要輸出聲音,那數組中該節對應的索引值便會是關鍵字值,否則值為)
        
        //用作更新track
        sequence.deleteTrack(track);//把原先的track刪除
        track = sequence.createTrack();//創造一個全新的track

        //用作檢測樂器是否被啟用的部份
        for(int i = 0 ; i < instrumentNumber; i++){//對每一個樂器都執行一次
            trackList = new int[instrumentNumber];//有多少種樂器就生成多少格
            int key = instruments[i];//設定不同樂器的關鍵字

            for (int j = 0 ; j < instrumentNumber ; j++){//對每一個節拍執行一次
                JCheckBox jc = (JCheckBox) checkboxList.get(j+(16*i));//獲取每個時間軸上的按鈕的狀態
                if(jc.isSelected()){//如果該按鈕被勾選了
                    trackList[j] = key;//傳入樂器的關鍵字放到數組對應的位置上,表示第j拍中第i樣樂器輸出了聲音
                }else{
                    trackList[j] = 0;//設置該值為0,表示第j拍上第i樣樂器沒有輸出聲音
                }
            }
            //把剛才記錄節拍的trackList加到tracks上
            makeTracks(trackList);//調用函式生成音軌
            track.add(makeEvent(176,1,127,0,instrumentNumber));//把監聽事件用的"音色"加入音軌中,每個樂器都有加入
        }

        //用於檢測是否播放結束,並實現重覆播放的部份
        track.add(makeEvent(192,9,1,0,instrumentNumber - 1));//用於確保第i - 1拍有事情,否則beabox不會重覆播放
        try {//嘗試執行無限次數重覆播放
            sequencer.setSequence(sequence);//"把唱片放入CD機"
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);//設定為無窮的重覆次數
            sequencer.start();//啟動"CD機"進行播放音樂的動作
        }catch(Exception e){//用於捕捉運行時的異常
            e.printStackTrace();//翻回出現的異常
        }

    }

    public class MyStartListener implements ActionListener {//監聽播放按鈕的動作的類
        public void actionPerformed(ActionEvent a){//當按鈕有動作時調動的方法
            buildTrackAndStart();//把音符放入track中並啟動音樂
        }
    }

    public class MyStopListener implements ActionListener {//監聽結束按鈕的動作的類
        public void actionPerformed (ActionEvent a){//當按鈕有動作時調動的方法
            sequencer.stop();//停止"CD機"的播放
        }
    }

    public class MyUpTempoListener implements ActionListener {//監聽節奏加快按鈕的動作的類
        public void actionPerformed(ActionEvent a){//當按鈕有動作時調動的方法
            float tempoFactor = sequencer.getTempoFactor();//獲取目前的播放速度
            sequencer.setTempoFactor((float) (tempoFactor * 1.03));//設定新的播放速度為原本的1.03倍
            System.out.println(BaseBPM * tempoFactor * 1.03);//顯示當前的BPM
        }
    }

    public class MyDownTempoListener implements ActionListener {//監聽節奏減慢按鈕的動作的類
        public void actionPerformed(ActionEvent a){//當按鈕有動作時調動的方法
            float tempoFactor = sequencer.getTempoFactor();//獲取目前的播放速度
            sequencer.setTempoFactor((float) (tempoFactor * 0.97));//設定新的播放速度為原本的0.97倍
            System.out.println(BaseBPM * tempoFactor * 0.97);//顯示當前的BPM
        }
    }

    public void makeTracks(int[] list){//創建某樣樂器的所有事件
        for (int i = 0 ; i < instrumentNumber ; i++ ){//對每一個節拍執行一次
            int key = list[i];//獲取當前節拍的值
            if (key != 0){//如果key不為0的話,便是每種樂器的關鍵字
                track.add(makeEvent(144,9,key,100,i));//創建note on 事件(即開始演奏某樣樂器)
                track.add(makeEvent(128,9,key,100,i+1));//創建note off事件(即離開彈奏某樣樂器)
            }
        }
    }

    public MidiEvent makeEvent (int comd , int chan , int one , int two ,int tick){//用作生成事件的類
        MidiEvent event = null;//初始化一個事件變量
        try {
            ShortMessage a = new ShortMessage();//創建一個信息收集器
            a.setMessage(comd, chan, one, two);//把傳入的參數設置成信息
            event = new MidiEvent(a,tick);//在對應的節拍中加入事件
        }catch(Exception e){//當發生異常的時候
            e.printStackTrace();//反回發現的異常
        }
    return event;
    }
}