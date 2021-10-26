package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.*;
import main.Main;


import java.util.Collections;


//创新新进程界面
public class NewPane extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        Label labelName = new Label("进程名    ");
        labelName.setFont(Font.font(15));
        TextField textFieldName = new TextField();
        textFieldName.setLayoutX(70);
        textFieldName.setText("p" + Main.AllC);
        textFieldName.setEditable(false);
        pane.getChildren().addAll(labelName, textFieldName);

        ObservableList<Integer> items = FXCollections.observableArrayList();
        items.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Spinner<Integer> spinner = new Spinner<>(items);
        spinner.setLayoutX(70);
        spinner.setLayoutY(30);
        Label labelRun = new Label("运行时间");
        labelRun.setFont(Font.font(15));
        labelRun.setLayoutY(30);
        pane.getChildren().addAll(labelRun, spinner);

        //保证从不同的线程实时更新UI
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                spinner.requestFocus();
            }
        });

        ObservableList<Integer> items2 = FXCollections.observableArrayList();
        items2.addAll(1, 2, 3, 4, 5);
        Spinner<Integer> spinner2 = new Spinner<>(items2);
        spinner2.setLayoutX(70);
        spinner2.setLayoutY(60);
        Label labelPri = new Label("优先级   ");
        labelPri.setFont(Font.font(15));
        labelPri.setLayoutY(60);
        pane.getChildren().addAll(labelPri, spinner2);

        Button submit = new Button("提交");
        submit.setId("submit");
        submit.setLayoutX(95);
        submit.setLayoutY(90);
        pane.getChildren().add(submit);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //new一个新的进程的PCB
                PCB pcb = new PCB();
                String name = textFieldName.getText();
                int runtime = spinner.getValue();
                int priority = spinner2.getValue();
                pcb.setRuntime(runtime);
                pcb.setPriority(priority);
                pcb.setName(name);
                Main.behindlist.add(pcb); //加入后备队列

                if (Main.mole == 1)
                    Collections.sort(Main.behindlist, new myCollection());
                Main.areaBehind.setText(Main.areaBehind.getText() + "新建进程名:" + pcb.getName() + " " + "运行时间：" + pcb.getRuntime() + " " + "优先级 " + pcb.getPriority() + '\n');
                Main.AllC++;
                stage.close();
            }
        });

        Scene scene = new Scene(pane);
//        scene.getStylesheets().add(getClass().getResource("main.modify.css").toExternalForm());
        stage.setTitle("创建进程");
        stage.setScene(scene);
        stage.show();
    }
}