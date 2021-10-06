package com.hhr.controller;

import com.hhr.thread.MyJavaFxThreadPool;
import com.hhr.util.SingletonFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author: Harry
 * @Date: 2021/10/5 22:29
 * @Version 1.0
 */
public class TaskInformationController implements Initializable {
    private static VBox informationVBox2;
    private static ScrollPane scrollPane2;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox informationVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        informationVBox2 = informationVBox;
        scrollPane2 = scrollPane;
    }

    public static void addVBoxInformation(final String information){
        SingletonFactory.getInstace(MyJavaFxThreadPool.class).javaFxExecute(new Runnable() {
            @Override
            public void run() {
                Text informationText = new Text(information);
                informationText.setWrappingWidth(666);
                informationVBox2.getChildren().add(informationText);
                scrollPane2.setVvalue(1.0);
            }
        });
    }

    public static void clearVBox(){
        informationVBox2.getChildren().clear();
    }
}
