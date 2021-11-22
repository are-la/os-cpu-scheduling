package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.Main;


//更新内存大小界面
public class updateMemPane extends Application {
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();

        Label label = new Label("选择内存");
        label.setFont(Font.font(20));
        label.setLayoutX(200);
        pane.getChildren().add(label);

        Button button1 = new Button("200");
        button1.setLayoutX(100);
        button1.setLayoutY(30);
        pane.getChildren().add(button1);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Main.memSizeBtn.setText("设置内存大小:200");
                int change = 200 - Main.size;
                Main.perpixel = 600.0/200;
                Main.areaOs.setPrefSize(220, 50*Main.perpixel);
                Main.size = 200;
                Main.memShengyu = Main.memShengyu + change;
                Main.memInf.setText("剩余内存:" + Main.memShengyu);
                stage.close();
            }
        });

        Button button2 = new Button("300");
        button2.setLayoutX(200);
        button2.setLayoutY(30);
        pane.getChildren().add(button2);

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Main.memSizeBtn.setText("设置内存大小:300");
                int change = 300 - Main.size;
                Main.size = 300;
                Main.perpixel = 600.0/300;
                Main.areaOs.setPrefSize(220, 50*Main.perpixel);
                Main.memShengyu = Main.memShengyu + change;
                Main.memInf.setText("剩余内存:" + Main.memShengyu);
                stage.close();
            }
        });

        Button button3 = new Button("400");
        button3.setLayoutX(300);
        button3.setLayoutY(30);
        pane.getChildren().add(button3);

        button3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Main.memSizeBtn.setText("设置内存大小:400");
                int change = 400 - Main.size;
                Main.size = 400;
                Main.perpixel = 600.0/400;
                Main.areaOs.setPrefSize(220, 50*Main.perpixel);
                Main.memShengyu = Main.memShengyu + change;
                Main.memInf.setText("剩余内存:" + Main.memShengyu);
                stage.close();
            }
        });

        Button button4 = new Button("500");
        button4.setLayoutX(400);
        button4.setLayoutY(30);
        pane.getChildren().add(button4);

        button4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Main.memSizeBtn.setText("设置内存大小:500");
                int change = 500 - Main.size;
                Main.size = 500;
                Main.perpixel = 600.0/500;
                Main.areaOs.setPrefSize(220, 50*Main.perpixel);
                Main.memShengyu = Main.memShengyu + change;
                Main.memInf.setText("剩余内存:" + Main.memShengyu);
                stage.close();
            }
        });

        Button button5 = new Button("100");
        button5.setLayoutX(0);
        button5.setLayoutY(30);
        pane.getChildren().add(button5);

        button5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Main.memSizeBtn.setText("设置内存大小:100");
                int change = 100 - Main.size;
                Main.size = 100;
                Main.perpixel = 600.0/100;
                Pane pane1 = new Pane();
                pane1.setPrefSize(220, 50*Main.perpixel);
                pane1.setLayoutX(0);
                pane1.setLayoutY(0);
                pane1.setBackground(new Background(new BackgroundFill(new Color(0.99, 0.4, 0.4, 1), null, null)));
                Label labelOs = new Label(" 起始地址：0  长度：50  状态：系统占用");
                labelOs.setFont(new Font("Arial",Main.perpixel*2));
                labelOs.setLayoutX(0);
                labelOs.setLayoutY(0);
                pane1.getChildren().add(labelOs);
                Main.areaMe.getChildren().add(pane1);
                Main.memShengyu = Main.memShengyu + change;
                Main.memInf.setText("剩余内存:" + Main.memShengyu);
                stage.close();
            }
        });


        Scene scene = new Scene(pane);
//        scene.getStylesheets().add(getClass().getResource("modify.css").toExternalForm());
        stage.setTitle("设置内存");
        stage.setScene(scene);
        stage.show();
    }
}

