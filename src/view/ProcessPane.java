package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.Main;


//选择道数界面
public class ProcessPane extends Application {
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        Label label = new Label("选择道数    ");
        label.setFont(Font.font(15));
        pane.getChildren().add(label);

        ObservableList<Integer> items = FXCollections.observableArrayList();
        items.addAll(5, 6, 7, 8, 9, 10);
        Spinner<Integer> spinner = new Spinner<>(items);
        spinner.setLayoutX(70);
        pane.getChildren().add(spinner);

        Button submit = new Button("提交");
        submit.setId("submit");
        submit.setLayoutY(30);
        pane.getChildren().add(submit);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int count = spinner.getValue();
                Main.processCount = count;
                Main.LabelProcess.setText("当前道数:" + count);
                stage.close();
            }
        });

        Scene scene = new Scene(pane);
//        scene.getStylesheets().add(getClass().getResource("modify.css").toExternalForm());
        stage.setTitle("设置道数");
        stage.setScene(scene);
        stage.show();

    }
}

