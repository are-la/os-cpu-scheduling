package utils;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.Main;

import java.awt.*;
import java.util.*;

public class Function extends Main {
    public static int strcount = 0;
    public static int index = Main.size;
//    public static int begin=50;
//    public static int end=50;

    //一键清空的清空函数
    public static void clear() {
        readylist.clear();
        behindlist.clear();
        hanglist.clear();
        finishlist.clear();
        areaBehind.setText("");
        areaRun.setText("");
        areaReady.setText("");
        areaHang.setText("");
        areaFinish.setText("");
        areaStatus.setText("");
        readyStr = "";
        runStr = "";
        finishStr = "";
        behindStr = "";
        hangStr = "";
        count = 0;
        processCount = 5;
        LabelProcess.setText("当前道数:5");
        isRun = false;
        label.setText("当前状态：准备");
        AllC = 0;
        useRecode = new int[1000];
        observableList.clear();
        Table table = new Table(0, 50, "系统占用", "操作系统");
        observableList.add(table);
        memShengyu = 50;
        memInf.setText("内存剩余:50 ");
    }

    //随机生成进程时的初始化函数
    public static void Init() {
        for (int i = 0 + AllC; i < count + AllC; i++) {
            String name = "p" + i;

            //随机生成进程信息
            int runtime = (int) (Math.random() * 5 + 2);
            int priority = (int) (Math.random() * 10 + 1);
            int memory = (int) (Math.random() * 10 + 1);
            String condition = "run";
            PCB p = new PCB();
            p.setName(name);
            p.setRuntime(runtime);
            p.setPriority(priority);
            p.setCondition(condition);
            p.setSizeOfMem(memory);
            //添加到后备队列中
            behindStr = behindStr + "进程名:" + name + " " + "运行时间:" + runtime + " " + "优先级:" + priority + "所需内存大小:" + memory + '\n';
            behindlist.add(p);
        }

        AllC += count;
        areaBehind.setText(behindStr);
        if (mole == 1)
            Collections.sort(behindlist, new myCollection());
    }


    public static void changStates() { //主要用于：1.将进程从后备队列放入就绪队列 2.将进程从就绪队列放入运行状态

        if (behindlist.size() == 0) areaBehind.setText("");

        //如果有进程位于后备队列满足条件就调度到就绪
        if (readylist.size() <= processCount && behindlist.size() > 0) { //当就绪队列进程数小于规定道数时执行
            int existPosition = findSpace(behindlist.get(0).getSizeOfMem());

            //表示找到了合适的大小
            if (existPosition != -1) {
                memShengyu = memShengyu - behindlist.get(0).getSizeOfMem();
                Labelwarning.setVisible(false);

                //将对于内存区变为占有
                for (int i = existPosition; i < existPosition + behindlist.get(0).getSizeOfMem(); i++) {
                    useRecode[i] = 1;
                }

                memInf.setText("内存剩余:" + memShengyu);

                Table table = new Table(existPosition, behindlist.get(0).getSizeOfMem(), behindlist.get(0).getName() + "占用", behindlist.get(0).getName());
                observableList.add(table);


                //在未分分区表中显示出来
                Pane pane1 = new Pane();
                pane1.setPrefSize(220, behindlist.get(0).getSizeOfMem()*perpixel);
                pane1.setLayoutX(0);
                pane1.setLayoutY(existPosition*perpixel);
                pane1.setBackground(new Background(new BackgroundFill(new Color(0.99, 0.4, 0.4, 1), null, null)));
                Label labelPane = new Label(" 起始地址："+existPosition+"  "+"长度："+behindlist.get(0).getSizeOfMem()+"  "+"状态："+behindlist.get(0).getName()+"占用");
                labelPane.setPrefSize(220, behindlist.get(0).getSizeOfMem()*perpixel);
                labelPane.setFont(new Font("Arial",perpixel*2*(perpixel*behindlist.get(0).getSizeOfMem()/17.0)));
                labelPane.setLayoutX(0);
                labelPane.setLayoutY(0);
                pane1.getChildren().add(labelPane);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //更新JavaFX的主线程的代码放在此处
                        Main.areaMe.getChildren().add(pane1);
                    }
                });

                Main.areaFree.setText("");
                freeMem();

                readylist.add(behindlist.get(0));
                strcount++;
                if(strcount==10){
                    strcount=0;
                    statuStr="";
                }
                statuStr = statuStr+ behindlist.get(0).getName()+"进程从后备队列调度进入就绪队列";
                areaStatus.setText(statuStr);
                if (mole == 1)
                    Collections.sort(readylist, new myCollection());
                behindlist.remove(0);
                displayBehind();
            } else {
                Labelwarning.setVisible(true);
            }
        }

        //处理运行队列状态
        if (readylist.size() > 0) {
            runningPcb = readylist.get(0);
            readylist.remove(0);
            displayReady(); //重新渲染就绪队列
            runStr = "正在执行的进程名:" + runningPcb.getName() + " " + "剩余运行时间:" + (runningPcb.getRuntime()) + " " + "优先级:" + runningPcb.getPriority() + "所需内存:" + runningPcb.getSizeOfMem();
            areaRun.setText(runStr);
            strcount++;
            if(strcount==10){
                strcount=0;
                statuStr="";
            }
            statuStr = statuStr+ runningPcb.getName()+"进程进入CPU运行"+'\n';
            areaStatus.setText(statuStr);

            if (mole == 1)
                runningPcb.setPriority(runningPcb.getPriority() + 1); //动态优先级
            runningPcb.setRuntime(runningPcb.getRuntime() - 1);

            if (runningPcb.getRuntime() > 0 && runningPcb.getCondition().equals("run")) {
                readylist.add(runningPcb);
                if (mole == 1)
                    Collections.sort(readylist, new myCollection());
            } else if (runningPcb.getRuntime() == 0) {
                if(strcount==10){
                    strcount=0;
                    statuStr= runningPcb.getName()+"进程已完成运行"+'\n';
                }
                statuStr = statuStr+ runningPcb.getName()+"进程已完成运行"+'\n';
                areaStatus.setText(statuStr);
                //完成进程移除语句
                areaRun.setText("");
                //从table中移除
                int index = 0;
                for (int i = 0; i < observableList.size(); i++) {
                    if (observableList.get(i).getName().equals(runningPcb.getName()))
                        index = i;
                }

                //内存区变空闲
                for (int i = observableList.get(index).getStartAddress(); i < observableList.get(index).getStartAddress() + observableList.get(index).getLength(); i++){
                    useRecode[i] = 0;}


                //在未分分区表中清理内存
                Pane pane2 = new Pane();
                pane2.setPrefSize(220, observableList.get(index).getLength()*perpixel);
                pane2.setLayoutX(0);
                pane2.setLayoutY(observableList.get(index).getStartAddress()*perpixel);
                pane2.setBackground(new Background(new BackgroundFill(new Color(0.4, 0.8, 0.5, 1), null, null)));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //更新JavaFX的主线程的代码放在此处
                        Main.areaMe.getChildren().add(pane2);
                    }
                });

                Main.areaFree.setText(" ");
                freeMem();

                observableList.remove(index);

                memShengyu = memShengyu + runningPcb.getSizeOfMem();
                memInf.setText("内存剩余:" + memShengyu);

                finishStr += runningPcb.getName() + "进程已经完成" + '\n';
                areaFinish.setText(finishStr);
                finishlist.add(runningPcb);
            }
        }

    }


    //找是否还有空闲空间函数
    public static int findSpace(int sizeOfMem) {
        for (int i = arrivePosition; i < size; i++) {
            int countMem = 0;

            //找空闲大小够不够
            while (i + countMem < size && useRecode[i + countMem] == 0) { //保证内存不溢出
                countMem++;
            }

            if (countMem >= sizeOfMem) {
                return i; //返回目前空闲的首地址
            }
            i = i + countMem;

        }
        return -1;
    }

    //寻找空闲内存区域并显示
    public static void freeMem(){
        useRecode[size] = 1;
        int begin = 50;
        int end = 50;
        String str = "";
        for(int i = 50; i<= size; i++){
            if(useRecode[i] == 0){
                ++end;
            }
            if(useRecode[i] == 1 &&(begin!=end)){
                str= str +"起始地址："+begin+"  长度："+(end-begin)+"  未分配(空闲)"+'\n';
                begin= i ;
                end= i ;
            }
            if(useRecode[i]==1 &&(begin == end)){
                begin = i;
                end = i;
            }
        }
        System.out.println("++="+"50处的空间为："+useRecode[50]);
        Main.areaFree.setText(str);
    }


    //将后备队列信息显示在textArea中
    public static void displayBehind() {
        behindStr = "";
        for (int i = 0; i < behindlist.size(); i++) {
            behindStr = behindStr + "进程名:" + behindlist.get(i).getName() + " " + "运行时间：" + behindlist.get(i).getRuntime() + " " + "优先级:" + behindlist.get(i).getPriority()
                    + "所需内存" + behindlist.get(i).getSizeOfMem() + '\n';
        }
        areaBehind.setText(behindStr);
    }


    public static void displayReady() {
        readyStr = "";

        for (int i = 0; i < readylist.size(); i++) {
            readyStr = readyStr + "进程名:" + readylist.get(i).getName() + " " + "运行时间：" + readylist.get(i).getRuntime() + " " + "优先级 " + readylist.get(i).getPriority()
                    + "所需内存" + readylist.get(i).getSizeOfMem() + '\n';
        }
        areaReady.setText(readyStr);
    }

}
