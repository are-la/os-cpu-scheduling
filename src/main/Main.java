package main;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
//import java.awt.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import utils.*;
import view.*;


import javax.swing.*;
import java.util.*;
import java.util.Timer;

import static javafx.scene.paint.Color.*;


public class Main extends Application {

    public static int size = 100;
    public static int memShengyu = 50;
    public static int AllC = 0; //全局进程计数器
    public static int count = 0; //这是产生的进程数
    public static int processCount = 5;
    public static int time = 0;
    public static int arrivePosition = 50;
    public static int refreashtime = 1;
    public static double perpixel = 6;

    //四个list存储pcb类信息
    public static List<PCB> readylist; //就绪队列
    public static List<PCB> behindlist; //后备队列
    public static List<PCB> hanglist; //挂起队列
    public static List<PCB> finishlist; //终止队列
    public static boolean isRemoveHang = false; //是否要被解挂
    public static boolean hangBool = true; //是否被挂起

    //TextArea用来显示进程状态
    public static TextArea areaReady; //显示就绪的进程
    public static TextArea areaRun; //显示正在运行的进程
    public static TextArea areaFinish; //显示已经运行结束的进程
    public static TextArea areaBehind; //显示后备队列的进程
    public static TextArea areaHang; //显示被挂起的进程
    public static TextArea areaStatus; //显示进程的状态
    public static TextArea areaFree;//显示未分分区表

    public static Pane areaMe;//显示内存的占用情况
    public static Pane areaOs;//显示操作系统占用的空间

    //用来存储进程信息的字符串
    public static String readyStr = "";
    public static String runStr = "";
    public static String finishStr = "";
    public static String behindStr = "";
    public static String hangStr = "";
    public static String statuStr = "";

    public static PCB runningPcb = null;//正在运行的进程
    public static Label LabelProcess; //显示当前cpu道数的标签
    public static Label Labelwarning; //分配cpu时的警示标签

    public static Label label;
    public static TextField memInf;
    public static Button memSizeBtn;
    public static boolean isRun = false; //程序是否开始运行的标志
    public static int skinNum = 0; //设置背景所用的参数
    public static int mole = 1;

    //这个数组表示内存，0表示空闲，1表示被占用
    public static int[] useRecode = new int[1000];

    //这是TableView类与之相关的list,用来存储Table类
    public static ObservableList<Table> observableList = FXCollections.observableArrayList();

    //设置背景渐变色的函数
    public LinearGradient setLinear(int i) {
        //对应色表
        int[][] linearList = {
                {158, 144, 151, 190, 169, 173},
                {0, 90, 167, 255, 253, 228},
                {198, 255, 221, 251, 215, 134},
                {219, 212, 180, 122, 161, 210},
                {43, 192, 228, 234, 236, 198},
                {239, 239, 187, 212, 211, 221},
                {190, 147, 197, 123, 198, 188},
                {48, 232, 191, 255, 130, 53},
                {229, 93, 135, 95, 195, 228},
                {237, 66, 100, 255, 237, 188},
                {254, 172, 94, 199, 121, 208},
                {199, 121, 208, 75, 192, 200}};
        LinearGradient linear = new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new
                Stop[]{new Stop(0, rgb(linearList[i][0], linearList[i][1], linearList[i][2])),
                new Stop(0.5f, rgb(linearList[i][3], linearList[i][4], linearList[i][5]))});
        return linear;
    }

    //启动初始界面
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();

        //矩形来填充渐变色
        Rectangle rec = new Rectangle(0, 0, 1300, 800);
        rec.setFill(setLinear(skinNum));
        pane.getChildren().add(rec);

        //显示CPU状态的label
        label = new Label("当前状态:准备");
        label.setLayoutY(720);
        label.setLayoutX(25);
        label.setFont(Font.font(20));
        pane.getChildren().add(label);

        //显示文字后备队列
        Label labelBehind = new Label("后备队列：");
        labelBehind.setFont(Font.font(20));
        labelBehind.setLayoutX(20);
        labelBehind.setLayoutY(20);
        pane.getChildren().add(labelBehind);

        //后备队列展示框
        areaBehind = new TextArea();
//        area.setMaxHeight(800); // 设置多行输入框的最大高度
        areaBehind.setPrefSize(220, 340); // 设置多行输入框的推荐宽高
        areaBehind.setEditable(false); // 设置多行输入框能否编辑
        areaBehind.setWrapText(true); // 设置多行输入框是否支持自动换行。true表示支持，false表示不支持。
        areaBehind.setLayoutX(20);
        areaBehind.setLayoutY(60);
        pane.getChildren().add(areaBehind);

        //展示文字就绪队列
        Label labelRea = new Label("就绪队列：");
        labelRea.setFont(Font.font(20));
        labelRea.setLayoutX(20);
        labelRea.setLayoutY(410);
        pane.getChildren().add(labelRea);

        //就绪队列展示框
        areaReady = new TextArea();
        areaReady.setPrefSize(220, 210); // 设置多行输入框的推荐宽高
        areaReady.setEditable(false); // 设置多行输入框能否编辑
        areaReady.setWrapText(true); // 设置多行输入框是否支持自动换行。true表示支持，false表示不支持。
        areaReady.setLayoutX(20);
        areaReady.setLayoutY(450);
        pane.getChildren().add(areaReady);

        //设置刷新时间
        Label labelRefreash = new Label("设置刷新时间(秒)：");
        labelRefreash.setLayoutX(1020);
        labelRefreash.setLayoutY(450);
        labelRefreash.setFont(Font.font(17));
        pane.getChildren().add(labelRefreash);

        ObservableList<Integer> items = FXCollections.observableArrayList();
        items.addAll(1, 2, 3, 4, 5);
        Spinner<Integer> spinner = new Spinner<>(items);
        spinner.setLayoutX(1020);
        spinner.setLayoutY(480);
        pane.getChildren().add(spinner);

        Button submit = new Button("提交");
        submit.setId("submit");
        submit.setLayoutX(1185);
        submit.setLayoutY(480);
        pane.getChildren().add(submit);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int count = spinner.getValue();
                refreashtime = count;
            }
        });


        //显示文字未分分区表
        Label labelTable = new Label("未分分区表(图像化展示)：");
        labelTable.setFont(Font.font(20));
        labelTable.setLayoutX(270);
        labelTable.setLayoutY(20);
        pane.getChildren().add(labelTable);

        //内存占用展示框
        areaMe = new Pane();
        areaMe.setPrefSize(220, 600);
        areaMe.setLayoutX(270);
        areaMe.setLayoutY(60);
        areaMe.setBackground(new Background(new BackgroundFill(new Color(0.4, 0.8, 0.5, 1), null, null)));

        //显示OS占用空间
        areaOs = new Pane();
        areaOs.setPrefSize(220, 50*perpixel);
        areaOs.setLayoutX(0);
        areaOs.setLayoutY(0);
        areaOs.setBackground(new Background(new BackgroundFill(new Color(0.99, 0.4, 0.4, 1), null, null)));

        Label labelOs = new Label(" 起始地址：0  长度：50  状态：系统占用");
        labelOs.setFont(new Font("Arial",perpixel*2));
        labelOs.setLayoutX(0);
        labelOs.setLayoutY(0);
        areaOs.getChildren().add(labelOs);
        areaMe.getChildren().add(areaOs);

        pane.getChildren().add(areaMe);

        //设置内存大小的开关
        memSizeBtn = new Button("设置当前内存大小:100");
        memSizeBtn.setId("memSizeBtn");
        memSizeBtn.setLayoutX(1020);
        memSizeBtn.setLayoutY(520);
        memSizeBtn.setPrefSize(170,20);
        pane.getChildren().add(memSizeBtn);

        //设置内存大小开关的行为事件
        memSizeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (isRun) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("错误提示");
                    alert.setHeaderText("请等待程序运行完！");
                    alert.showAndWait();
                    return;
                }

                //跳转到新的界面进行选择
                updateMemPane pane = new updateMemPane();

                //异常处理
                try {
                    pane.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //显示文字未分分区表
        Label labelFree = new Label("未分分区表：");
        labelFree.setFont(Font.font(20));
        labelFree.setLayoutX(520);
        labelFree.setLayoutY(20);
        pane.getChildren().add(labelFree);

        areaFree = new TextArea();
        areaFree.setPrefSize(220, 280); // 设置多行输入框的推荐宽高
        areaFree.setEditable(false); // 设置多行输入框能否编辑
        areaFree.setWrapText(true); // 设置多行输入框是否支持自动换行。true表示支持，false表示不支持。
        areaFree.setLayoutX(520);
        areaFree.setLayoutY(60);
        pane.getChildren().add(areaFree);

        //显示文字内存占用表
        Label labelMem = new Label("内存占用表：");
        labelMem.setFont(Font.font(20));
        labelMem.setLayoutX(520);
        labelMem.setLayoutY(350);
        pane.getChildren().add(labelMem);

        //显示内存的表
        TableView<Table> tableCPU = new TableView<Table>(observableList);

        //表的三个列
        TableColumn<Table, String> tc_start = new TableColumn<>("起始地址");
        TableColumn<Table, String> tc_length = new TableColumn<>("长度");
        TableColumn<Table, String> tc_status = new TableColumn<>("状态");

        tc_status.setPrefWidth(58);

        //让列不可以改变大小
        tc_start.setResizable(false);
        tc_length.setResizable(false);
        tc_status.setResizable(false);

        //让列与Table类中变量进行绑定
        tc_start.setCellValueFactory(new PropertyValueFactory<>("startAddress"));
        tc_length.setCellValueFactory(new PropertyValueFactory<>("length"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        //添加到表中
        tableCPU.getColumns().add(tc_start);
        tableCPU.getColumns().add(tc_length);
        tableCPU.getColumns().add(tc_status);
        tableCPU.setPrefHeight(280);
        tableCPU.setPrefWidth(220);
        tableCPU.setLayoutX(520);
        tableCPU.setLayoutY(390);


        //操作系统占用情况
        Table table = new Table(0, 50, "系统占用", "操作系统"); //初始化默认数据
        observableList.add(table);
        pane.getChildren().add(tableCPU);

        Labelwarning = new Label("内存不足,无法添加进程！");
        Labelwarning.setId("warning");
        Labelwarning.setFont(Font.font(20));
        Labelwarning.setLayoutX(270);
        Labelwarning.setLayoutY(670);
        pane.getChildren().add(Labelwarning);
        Labelwarning.setVisible(false);

        //实例化list
        readylist = new ArrayList<>();
        behindlist = new ArrayList<>();
        hanglist = new ArrayList<>();
        finishlist = new ArrayList<>();

        //显示文字运行队列
        Label labelRun = new Label("CPU运行进程：");
        labelRun.setFont(Font.font(20));
        labelRun.setLayoutX(770);
        labelRun.setLayoutY(20);
        pane.getChildren().add(labelRun);

        //运行队列textArea设置
        areaRun = new TextArea();
        areaRun.setPrefSize(220, 100); // 设置多行输入框的推荐宽高
        areaRun.setEditable(false); // 设置多行输入框能否编辑
        areaRun.setWrapText(true); // 设置多行输入框是否支持自动换行。true表示支持，false表示不支持。
        areaRun.setLayoutX(770);
        areaRun.setLayoutY(60);

        //显示文字已经完成的进程
        Label labelFinish = new Label("已经完成的进程:");
        labelFinish.setFont(Font.font(20));
        labelFinish.setLayoutX(770);
        labelFinish.setLayoutY(360);
        pane.getChildren().add(labelFinish);

        //完成队列textArea设置
        areaFinish = new TextArea();
//        area.setMaxHeight(800); // 设置多行输入框的最大高度
        areaFinish.setPrefSize(220, 270); // 设置多行输入框的推荐宽高
        areaFinish.setEditable(false); // 设置多行输入框能否编辑
        areaFinish.setWrapText(true); // 设置多行输入框是否支持自动换行。true表示支持，false表示不支持。
        areaFinish.setLayoutX(770);
        areaFinish.setLayoutY(400);
        pane.getChildren().add(areaFinish);

        //显示文字挂起队列
        Label labelHang = new Label("挂起队列:");
        labelHang.setFont(Font.font(20));
        labelHang.setLayoutX(770);
        labelHang.setLayoutY(170);
        pane.getChildren().add(labelHang);

        //挂起队列textArea设置
        areaHang = new TextArea();
        areaHang.setPrefSize(220, 130); // 设置多行输入框的推荐宽高
        areaHang.setEditable(false); // 设置多行输入框能否编辑
        areaHang.setWrapText(true); // 设置多行输入框是否支持自动换行。true表示支持，false表示不支持。
        areaHang.setLayoutX(770);
        areaHang.setLayoutY(210);
        pane.getChildren().add(areaHang);

        //进程状态设置
        Label labelStatu = new Label("进程状态：");
        labelStatu.setFont(Font.font(20));
        labelStatu.setLayoutX(1020);
        labelStatu.setLayoutY(20);
        pane.getChildren().add(labelStatu);
        //进程状态textarea设置
        areaStatus = new TextArea();
        areaStatus.setPrefSize(220, 300); // 设置多行输入框的推荐宽高
        areaStatus.setEditable(false); // 设置多行输入框能否编辑
        areaStatus.setWrapText(true); // 设置多行输入框是否支持自动换行。true表示支持，false表示不支持。
        areaStatus.setLayoutX(1020);
        areaStatus.setLayoutY(60);
        pane.getChildren().add( areaStatus);


        //显示文字label请输入随机产生的进程个数设置
        Label labelCount = new Label("请输入随机产生的进程个数：");
        labelCount.setPrefHeight(40);
        labelCount.setFont(Font.font(17));
        labelCount.setLayoutX(1020);
        labelCount.setLayoutY(360);

        //输入想产生多少进程的框
        TextField textField = new TextField();
        textField.setEditable(true);
        textField.setLayoutX(1020);
        textField.setLayoutY(400);
        textField.setPrefSize(150,40);


        //提交随机生成进程按钮的行为事件
        Button submitCount = new Button("提交");
        submitCount.setLayoutX(1180);
        submitCount.setLayoutY(410);
        submitCount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //文本为空时候的异常处理
                if (textField.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("错误提示");
                    alert.setHeaderText("请输入进程数！");
                    alert.showAndWait();
                    return;
                }
                if(textField.getText().equals("0")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("错误提示");
                    alert.setHeaderText("进程数不能为0！   ");
                    alert.showAndWait();
                    return;
                }
                //将文本框中的字符串转化为数字
                count = Integer.parseInt(textField.getText());

                //调用初始化函数
                Function.Init();
            }
        });

        //显示信息: 内存剩余
        memInf = new TextField("内存剩余:  70");
        memInf.setLayoutX(220);
        memInf.setLayoutY(720);
        memInf.setEditable(false);
        memInf.setFont(Font.font(15));
        pane.getChildren().add(memInf);

        //显示文字:当前道数
        LabelProcess = new Label("当前道数: 5");
        LabelProcess.setPrefHeight(40);
        LabelProcess.setFont(Font.font(20));
        LabelProcess.setLayoutY(713);
        LabelProcess.setLayoutX(470);
        pane.getChildren().add(LabelProcess);

        //设置道数开关 行为事件
        Button setprocessBtn = new Button("设置道数");
        setprocessBtn.setLayoutX(600);
        setprocessBtn.setLayoutY(720);
        pane.getChildren().add(setprocessBtn);

        setprocessBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //调用设置界面
                ProcessPane pane = new ProcessPane();
                try {
                    pane.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //手动创建新进程按钮
        Button createBtn = new Button("创建一个新进程");
        createBtn.setLayoutY(600);
        createBtn.setLayoutX(1020);
        createBtn.setPrefWidth(170);
        createBtn.setId("create");
        pane.getChildren().add(createBtn);

        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isRun = false;
                NewPane createPane = new NewPane();
                try {
                    createPane.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //事件函数，驱使队列自动运行完不可或缺的一个函数
        //定时调度所拥有的Timer Tasks
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() { //放每次需要执行的代码
                time += 100;

                //这个变量记录是否被按下了解挂
                if (isRemoveHang) {
                    //这一段处理解挂，在解挂时候应该保证不被CPU调度影响
                    if ((runningPcb != null && runningPcb.getRuntime() == 0 && readylist.size() >= processCount + 1) ||
                            (runningPcb != null && runningPcb.getRuntime() != 0 && readylist.size() >= processCount)) {
                        if (hangBool) {
                            hangBool = false;
                        }
                    } else if (hanglist.size() == 0) {
                        isRemoveHang = false;
                    } else {

                        //设置状态
                        hanglist.get(0).setCondition("run"); //改变挂起队列第一个进程的状态
                        readylist.add(hanglist.get(0)); //把挂起的进程放进就绪队列
                        hanglist.remove(0); //从挂起队列中移除

                        //进程排序，按照优先级
                        Collections.sort(readylist, new myCollection());

                        if (!isRun)
                            Function.displayReady();

                        //重新显示信息
                        hangStr = "";
                        for (int i = 0; i < hanglist.size(); i++) {
                            hangStr += hanglist.get(i).getName() + "已经挂起" + " " + "优先级:" + hanglist.get(i).getPriority() + "\n";
                        }
                        areaHang.setText(hangStr);
                        isRemoveHang = false;

                    }
                }//处理解挂结束

                //isRun记录了是否按下了暂停，同时每隔一秒触发一次这个事件
                if (isRun && time >= refreashtime*1000) { //设置进程状态切换时间

                    time = 0;

                    //是否可以结束运行
                    if (finishlist.size() == AllC) {
                        isRun = false;
                        return;
                    }

                    //若仍需要运行就调用函数
                    Function.changStates(); //1.将进程从后备队列放入就绪队列 2.将进程从就绪队列放入运行状态
                }
            }
        };
        timer.schedule(task, 0, 100); //从这句代码开始每隔100毫秒执行一次task代码——即解挂和挂起的刷新时间是100毫秒
        // firstTime为Date类型,period为long
//      //firstTime时刻开始，每隔period毫秒执行一次

        //开始运行按钮
        Button btnRun = new Button("开始运行");
        btnRun.setLayoutX(1020);
        btnRun.setLayoutY(640);
        btnRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //设置flag isRun为真
                isRun = true;
                label.setText("当前状态：运行");
            }
        });

        //暂停按钮
        Button btnStop = new Button("暂停");
        btnStop.setLayoutX(1150);
        btnStop.setLayoutY(640);
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isRun = false;
                label.setText("当前状态：暂停");
            }
        });

        //挂起按钮
        Button hangBtn = new Button("挂起运行进程");
        hangBtn.setLayoutX(820);
        hangBtn.setLayoutY(720);
        hangBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                //异常处理
                if (areaRun.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("错误提示");
                    alert.setHeaderText(null);
                    alert.setContentText("当前没有正在运行的进程！");
                    alert.showAndWait(); //显示警示窗
                    return;
                }

                if (runningPcb.getRuntime() == 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("错误提示");
                    alert.setHeaderText("当前进程已经运行完！");
                    alert.showAndWait();
                    return;
                }
                areaRun.setText("");
                hangStr = "";
                //避免重复添加
                if (!(hanglist.contains(runningPcb)))
                    hanglist.add(runningPcb);
                runningPcb.setCondition("hanging");
                readylist.remove(runningPcb);
                //在挂起队列刷新显示进程
                for (int i = 0; i < hanglist.size(); i++) {
                    hangStr += hanglist.get(i).getName() + "已经挂起" + "优先级" + hanglist.get(i).getPriority() + "\n";
                }
                areaHang.setText(hangStr);
            }
        });
        pane.getChildren().add(hangBtn);

        //解挂按钮
        Button removeHangBtn = new Button("解挂");
        removeHangBtn.setLayoutX(720);
        removeHangBtn.setLayoutY(720);
        removeHangBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //异常处理
                isRemoveHang = true;
                if ((runningPcb != null && runningPcb.getRuntime() == 0 && readylist.size() >= processCount + 1) ||
                        (runningPcb != null && runningPcb.getRuntime() != 0 && readylist.size() >= processCount)) {
                    //解挂后的进程无处可放时：
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText("请等待当前进程执行结束！");
                    alert.showAndWait();
                } else if (hanglist.size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText("当前没有进程可以解挂！");
                    alert.showAndWait();
                }
            }
        });

        //一键清空按钮
        Button clearBtn = new Button("一键清空");
        clearBtn.setLayoutY(720);
        clearBtn.setLayoutX(980);
        pane.getChildren().add(clearBtn);
        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Function.clear();
            }
        });

        //切换调度算法
        Button swapBtn = new Button("切换到时间片调度");
        swapBtn.setLayoutY(560);
        swapBtn.setLayoutX(1020);
        swapBtn.setPrefWidth(170);

        swapBtn.setId("swapbtn");
        swapBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (mole == 1) {
                    mole = 2;
                    Function.clear();
                    swapBtn.setText("切换到优先级调度");
                    return;
                }
                if (mole == 2) {
                    mole = 1;
                    Function.clear();
                    swapBtn.setText("切换到时间片调度");
                }
            }
        });
        pane.getChildren().add(swapBtn);

        pane.getChildren().add(removeHangBtn);
        pane.getChildren().addAll(labelCount, textField, submitCount, btnRun, btnStop);
        pane.getChildren().add(areaRun);

        primaryStage.setTitle("CPU调度模拟");
        Scene scene = new Scene(pane);
        //设置css语句，程序样式
//        scene.getStylesheets().add(getClass().getResource("main/modify.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setHeight(800);
        primaryStage.setWidth(1300);
        primaryStage.show();

        //关掉窗口也关闭后台处理
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}

